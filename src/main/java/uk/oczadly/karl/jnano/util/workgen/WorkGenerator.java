/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.workgen.policy.NodeWorkDifficultyPolicy;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

/**
 * This interface is to be implemented by classes which can generate proof-of-work solutions.
 *
 * The following implementations are provided: {@link OpenCLWorkGenerator}, {@link CPUWorkGenerator},
 * {@link NodeWorkGenerator} and {@link DPOWWorkGenerator}.
 */
public interface WorkGenerator {
    
    /**
     * Generates a {@link WorkSolution} for the provided block, using the difficulty retrieved from the difficulty
     * policy, and applying the recommended multiplier.
     *
     * @param block the block to generate work for
     * @return the computed work solution, as a Future (not yet computed)
     *
     * @see WorkDifficultyPolicy#forBlock(Block)
     */
    default FutureWork generate(Block block) {
        return generate(block, 1);
    }
    
    /**
     * Generates a {@link WorkSolution} for the provided block, using the specified difficulty. The current multiplier
     * recommended by the policy will also be applied to the supplied base difficulty.
     *
     * @param block          the block to generate work for
     * @param baseDifficulty the minimum base difficulty threshold of the work
     * @return the computed work solution, as a Future (not yet computed)
     */
    FutureWork generate(Block block, WorkDifficulty baseDifficulty);
    
    /**
     * Generates a {@link WorkSolution} for the provided block, using the specified multiplier on top of the
     * specified difficulty policy and recommended multiplier.
     *
     * <p>The provided difficulty multiplier is stacked on top of the recommended multiplier value returned by the
     * {@link NodeWorkDifficultyPolicy}. For example, if the policy specifies a multiplier of {@code 3} and you
     * provide a multiplier of {@code 2}, the resulting work must be at least {@code 6} times the base difficulty.</p>
     *
     * @param block          the block to generate work for
     * @param diffMultiplier the difficulty multiplier
     * @return the computed work solution, as a Future (not yet computed)
     *
     * @see WorkDifficultyPolicy#forBlock(Block)
     */
    FutureWork generate(Block block, double diffMultiplier);
    
    /**
     * Generates a {@link WorkSolution} for the provided block root hash using the "any" difficulty provided by the
     * difficulty policy, and applying the recommended multiplier.
     *
     * @param root the root hash (note: <strong>not</strong> the block's hash)
     * @return the computed work solution, as a Future (not yet computed)
     *
     * @see WorkSolution#getRoot(Block)
     * @see WorkDifficultyPolicy#forAny()
     */
    default FutureWork generate(HexData root) {
        return generate(root, 1);
    }
    
    /**
     * Generates a {@link WorkSolution} for the provided block root hash, using the specified difficulty. The current
     * multiplier recommended by the policy will also be applied to the supplied base difficulty.
     *
     * @param root           the root hash (note: <strong>not</strong> the block's hash)
     * @param baseDifficulty the minimum base difficulty threshold of the work
     * @return the computed work solution, as a Future (not yet computed)
     *
     * @see WorkSolution#getRoot(Block)
     */
    FutureWork generate(HexData root, WorkDifficulty baseDifficulty);
    
    /**
     * Generates a {@link WorkSolution} for the provided block root hash, using the "any" difficulty provided by the
     * difficulty policy, and applying both the recommended multiplier and specified multiplier.
     *
     * @param root           the root hash (note: <strong>not</strong> the block's hash)
     * @param diffMultiplier the difficulty multiplier
     * @return the computed work solution, as a Future (not yet computed)
     *
     * @see WorkSolution#getRoot(Block)
     */
    FutureWork generate(HexData root, double diffMultiplier);
    
    
    /**
     * Returns whether this generator has been shut down by calling {@link #shutdown()}.
     * @return true if this generator has been shut down
     */
    boolean isShutdown();
    
    /**
     * Attempts to cancel all pending work generations, and terminates all background threads and connections. A
     * WorkGenerator object cannot be reused once shutdown.
     */
    void shutdown();

}
