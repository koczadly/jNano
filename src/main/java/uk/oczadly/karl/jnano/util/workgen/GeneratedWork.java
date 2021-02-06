/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

/**
 * This class contains a generated work solution, and the originating request parameters.
 */
public final class GeneratedWork {
    
    private final WorkSolution work;
    private final HexData reqRoot;
    private final WorkDifficulty reqDifficulty;
    
    GeneratedWork(WorkSolution work, HexData reqRoot, WorkDifficulty reqDifficulty) {
        this.work = work;
        this.reqRoot = reqRoot;
        this.reqDifficulty = reqDifficulty;
    }
    
    
    /**
     * Returns the generated work solution.
     * @return the generated work
     */
    public WorkSolution getWork() {
        return work;
    }
    
    /**
     * Returns the difficulty of the generated work solution.
     * @return the difficulty of the work
     */
    public WorkDifficulty getDifficulty() {
        return work.calculateDifficulty(reqRoot);
    }
    
    /**
     * Returns the difficulty multiplier of the generated work solution, in respect to the requested difficulty (or
     * difficulty specified by the {@link WorkDifficultyPolicy}).
     *
     * <p>The value returned by this method also factors in the original multiplier, if one was specified in the
     * request. If the call to {@link WorkGenerator#generate(Block, double)} requested a multiplier of {@code 4}
     * and the generated work was exactly {@code 4} times the difficulty specified by the policy, then this method would
     * return {@code 1.0}, despite being 4 times the recommended difficulty.</p>
     *
     * @return the difficulty multiplier of the work
     */
    public double getMultiplier() {
        return getDifficulty().calculateMultiplier(reqDifficulty);
    }
    
    /**
     * Returns the root hash of the originating work request.
     *
     * <p>The value returned by this method also factors in the requested multiplier, if one was specified in the
     * by invoking {@link WorkGenerator#generate(Block, double)}.</p>
     *
     * @return the requested work root
     */
    public HexData getRequestRoot() {
        return reqRoot;
    }
    
    /**
     * Returns the requested minimum difficulty threshold. If no difficulty was specified, then this would be
     * provided by the {@link WorkDifficultyPolicy}.
     *
     * @return the requested difficulty
     */
    public WorkDifficulty getRequestDifficulty() {
        return reqDifficulty;
    }
    
    @Override
    public String toString() {
        return work.getAsHexadecimal();
    }
    
}
