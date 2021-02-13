/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Combines multiple {@code WorkGenerator} instances together to generate work in parallel using multiple sources.
 *
 * <p>This can be used for better reliability when using external services, or to improve the speed of work
 * generation requests.</p>
 *
 * <p>It is important to be aware that <em>each generator may provide their own difficulty policy</em>. If this occurs,
 * then each generator may attempt to compute the work at different target difficulty thresholds. For a consistent
 * generation response, you should manually supply the same policy to each generator instance during construction.</p>
 */
public final class CombinedWorkGenerator implements WorkGenerator {
    
    private final Set<WorkGenerator> generators;
    private final ExecutorService executorService;
    
    /**
     * Creates a CombinedWorkGenerator using the provided generators.
     * @param generators an array of work generators to use (must have at least 1)
     */
    public CombinedWorkGenerator(WorkGenerator... generators) {
        if (generators == null)
            throw new IllegalArgumentException("Generators array cannot be null.");
        if (generators.length == 0)
            throw new IllegalArgumentException("At least 1 generator must be specified.");
        
        this.generators = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(generators)));
        this.executorService = Executors.newFixedThreadPool(generators.length);
    }
    
    
    public Set<WorkGenerator> getGenerators() {
        return generators;
    }
    
    
    @Override
    public FutureWork generate(Block block, WorkDifficulty baseDifficulty) {
        return doGenerate(g -> g.generate(block, baseDifficulty));
    }
    
    @Override
    public FutureWork generate(Block block, double diffMultiplier) {
        return doGenerate(g -> g.generate(block, diffMultiplier));
    }
    
    @Override
    public FutureWork generate(HexData root, WorkDifficulty baseDifficulty) {
        return doGenerate(g -> g.generate(root, baseDifficulty));
    }
    
    @Override
    public FutureWork generate(HexData root, double diffMultiplier) {
        return doGenerate(g -> g.generate(root, diffMultiplier));
    }
    
    
    @Override
    public synchronized boolean isShutdown() {
        return generators.stream().allMatch(WorkGenerator::isShutdown);
    }
    
    @Override
    public synchronized void shutdown() {
        executorService.shutdownNow();
        generators.forEach(WorkGenerator::shutdown);
    }
    
    
    private synchronized FutureWork doGenerate(Function<WorkGenerator, FutureWork> proxyFunc) {
        if (isShutdown())
            throw new IllegalStateException("All generators are shut down.");
        
        GenerationTaskCoordinator coordinator = new GenerationTaskCoordinator();
        coordinator.spawnThreads(proxyFunc);
        return new FutureWork(coordinator.resultFuture);
    }
    
    
    private class GenerationTaskCoordinator {
        private final Collection<FutureWork> allTasks = new ArrayList<>();
        private final FutureWorkWithNotification resultFuture;
        private volatile int tasksRemaining;
        
        public GenerationTaskCoordinator() {
            this.resultFuture = new FutureWorkWithNotification();
        }
        
        public synchronized void taskComplete(GeneratedWork result) {
            tasksRemaining--;
            resultFuture.complete(result);
            cancelAll();
        }
        
        public synchronized void taskFail() {
            if (!resultFuture.isDone() && --tasksRemaining <= 0) {
                resultFuture.completeExceptionally(new WorkGenerationException("All work generators failed."));
                cancelAll(); // Should be no more tasks remaining
            }
        }
        
        public synchronized void cancelAll() {
            resultFuture.completeExceptionally(new WorkGenerationException("Work cancelled."));
            allTasks.forEach(FutureWork::cancel);
        }
        
        public synchronized void spawnThreads(Function<WorkGenerator, FutureWork> proxyFunc) {
            for (WorkGenerator generator : generators) {
                try {
                    FutureWork futureWork = proxyFunc.apply(generator);
                    executorService.submit(new GeneratorWatcher(futureWork));
                    allTasks.add(futureWork);
                } catch (IllegalStateException ignored) {}
            }
            tasksRemaining = allTasks.size();
            if (tasksRemaining == 0)
                taskFail();
        }
        
        /** Waits for a generator Future to complete or fail, and then notifies the coordinator. */
        private class GeneratorWatcher implements Runnable {
            private final FutureWork generatorFuture;
            
            public GeneratorWatcher(FutureWork generatorFuture) {
                this.generatorFuture = generatorFuture;
            }
            
            @Override
            public void run() {
                try {
                    taskComplete(generatorFuture.get());
                } catch (ExecutionException | InterruptedException | CancellationException e) {
                    taskFail();
                } catch (Throwable t) {
                    t.printStackTrace();
                    taskFail();
                }
            }
        }
        
        /** Cancels all threads upon cancellation. */
        private class FutureWorkWithNotification extends CompletableFuture<GeneratedWork> {
            @Override
            public boolean cancel(boolean interrupt) {
                if (super.cancel(interrupt)) {
                    cancelAll();
                    return true;
                }
                return false;
            }
        }
    }
    
}
