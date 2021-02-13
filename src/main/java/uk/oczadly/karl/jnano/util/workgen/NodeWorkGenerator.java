/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkCancel;
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkGenerate;
import uk.oczadly.karl.jnano.util.workgen.policy.NodeWorkDifficultyPolicy;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This {@code WorkGenerator} computes the work solution on the provided RPC endpoint, using whichever computation
 * method is configured on the node. Requests are sent using the {@link RequestWorkGenerate} query, and cancelled
 * using {@link RequestWorkCancel}.
 *
 * <p>Instances of this class should be re-used throughout your application, as each instance will spawn new
 * background threads. This practice also ensures that tasks are queued correctly in the order of request.</p>
 */
public class NodeWorkGenerator extends AbstractWorkGenerator {

    private final RpcQueryNode rpc;
    private final boolean usePeers;
    private final ExecutorService workExecutor = Executors.newSingleThreadExecutor();
    
    /**
     * Constructs a {@code NodeWorkGenerator} using the {@link NodeWorkDifficultyPolicy} difficulty policy, and
     * {@code usePeers} set to true.
     *
     * @param rpc the RPC endpoint
     *
     * @see NodeWorkDifficultyPolicy
     */
    public NodeWorkGenerator(RpcQueryNode rpc) {
        this(rpc, true);
    }
    
    /**
     * Constructs a {@code NodeWorkGenerator} using the {@link NodeWorkDifficultyPolicy} difficulty policy.
     *
     * @param rpc      the RPC endpoint
     * @param usePeers whether work peers should be used
     *
     * @see NodeWorkDifficultyPolicy
     */
    public NodeWorkGenerator(RpcQueryNode rpc, boolean usePeers) {
        this(rpc, usePeers, new NodeWorkDifficultyPolicy(rpc));
    }
    
    /**
     * Constructs a {@code NodeWorkGenerator} using the specified difficulty policy.
     *
     * @param rpc              the RPC endpoint
     * @param usePeers         whether work peers should be used
     * @param difficultyPolicy the difficulty policy to use
     */
    public NodeWorkGenerator(RpcQueryNode rpc, boolean usePeers, WorkDifficultyPolicy difficultyPolicy) {
        super(difficultyPolicy);
        if (rpc == null)
            throw new IllegalArgumentException("RPC node object cannot be null.");
        this.rpc = rpc;
        this.usePeers = usePeers;
    }
    
    
    /**
     * @return the {@code RpcQueryNode} instance
     */
    public final RpcQueryNode getRpcInstance() {
        return rpc;
    }
    
    /**
     * @return true if work peers should be used
     */
    public final boolean getUsePeers() {
        return usePeers;
    }
    
    @Override
    protected void cleanup() {
        try {
            workExecutor.shutdownNow();
        } finally {
            super.cleanup();
        }
    }
    
    @Override
    protected final WorkSolution generateWork(HexData root, WorkDifficulty difficulty, RequestContext context)
            throws Exception {
        // We submit to an executor so that we can receive interrupts and send out a cancellation request.
        Future<WorkSolution> work = workExecutor.submit(() -> sendWorkRequest(root, difficulty));
        
        try {
            // Wait for work (or cancellation interrupt)
            return work.get();
        } catch (InterruptedException e) {
            // Attempt to cancel work
            sendCancelRequest(root);
            throw e;
        } catch (ExecutionException e) {
            // Exception with computation or IO
            if (e.getCause() instanceof Exception)
                throw (Exception)e.getCause();
            throw new Error(e.getCause()); // Unexpected exception type
        }
    }
    
    
    protected WorkSolution sendWorkRequest(HexData root, WorkDifficulty difficulty) throws IOException, RpcException {
        RequestWorkGenerate req = new RequestWorkGenerate(root.toHexString(), usePeers, difficulty);
        return rpc.processRequest(req).getWorkSolution();
    }
    
    protected void sendCancelRequest(HexData root) {
        rpc.processRequestAsync(new RequestWorkCancel(root.toHexString()));
    }
    
}
