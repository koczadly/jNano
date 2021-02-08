/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.service.workgen;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.service.workgen.policy.WorkDifficultyPolicy;

/**
 * This class contains a generated work solution, and the originating request parameters.
 */
public final class GeneratedWork {
    
    private final WorkSolution work;
    private final HexData reqRoot;
    private final WorkDifficulty baseDifficulty, targetDifficulty;
    
    GeneratedWork(WorkSolution work, HexData reqRoot, WorkDifficulty baseDifficulty, WorkDifficulty targetDifficulty) {
        this.work = work;
        this.reqRoot = reqRoot;
        this.baseDifficulty = baseDifficulty;
        this.targetDifficulty = targetDifficulty;
    }
    
    
    /**
     * Returns the generated work solution.
     * @return the generated work
     */
    public WorkSolution getWork() {
        return work;
    }
    
    /**
     * Returns the difficulty of the generated work solution for the provided root hash.
     * @return the difficulty of the work
     */
    public WorkDifficulty getDifficulty() {
        return work.calculateDifficulty(reqRoot);
    }
    
    /**
     * Returns the difficulty multiplier of the generated work solution, <em>in respect to the requested base
     * difficulty</em>. The base difficulty in this instance is either the specified difficulty constant, or the
     * difficulty provided by the {@link WorkDifficultyPolicy}).
     *
     * <p>This value factors in all difficulty multipliers; the original request multiplier (if specified through
     * {@link WorkGenerator#generate(Block, double)}), and the recommended multiplier specified by the
     * {@link WorkDifficultyPolicy#multiplier()}.</p>
     *
     * @return the difficulty multiplier in respect to the base difficulty
     */
    public double getMultiplier() {
        return getDifficulty().calculateMultiplier(getRequestBaseDifficulty());
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
     * Returns the requested minimum base difficulty threshold, not including any multipliers. If no difficulty was
     * specified, then this would be provided by the {@link WorkDifficultyPolicy}.
     *
     * @return the requested base difficulty
     */
    public WorkDifficulty getRequestBaseDifficulty() {
        return baseDifficulty;
    }
    
    /**
     * Returns the requested target difficulty, including all difficulty multipliers.
     *
     * @return the requested target difficulty
     */
    public WorkDifficulty getRequestTargetDifficulty() {
        return targetDifficulty;
    }
    
    
    @Override
    public String toString() {
        return work.getAsHexadecimal();
    }
    
}
