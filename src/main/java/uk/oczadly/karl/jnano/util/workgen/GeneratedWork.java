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
 *
 * @see #getWork()
 */
public final class GeneratedWork {
    
    private final WorkSolution work;
    private final HexData reqRoot;
    private final WorkDifficulty difficulty, baseDifficulty, targetDifficulty;
    private final double multiplier, reqMultiplier;
    
    /**
     * @param work             the computed work solution
     * @param reqRoot          the request block root
     * @param baseDifficulty   the request base (threshold) difficulty
     * @param targetDifficulty the request target difficulty
     */
    public GeneratedWork(WorkSolution work, HexData reqRoot, WorkDifficulty baseDifficulty,
                         WorkDifficulty targetDifficulty) {
        this.work = work;
        this.reqRoot = reqRoot;
        this.baseDifficulty = baseDifficulty;
        this.targetDifficulty = targetDifficulty;
        
        this.difficulty = work.calculateDifficulty(reqRoot);
        this.multiplier = difficulty.calculateMultiplier(baseDifficulty);
        this.reqMultiplier = targetDifficulty.calculateMultiplier(baseDifficulty);
        
        if (!difficulty.isValid(targetDifficulty))
            throw new IllegalArgumentException("Computed work did not meet target difficulty.");
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
        return difficulty;
    }
    
    /**
     * Returns the difficulty multiplier of the generated work solution, <em>in respect to the base difficulty</em>.
     * The base difficulty in this instance is either the provided difficulty value in the request, or the
     * difficulty provided by the {@link WorkDifficultyPolicy}.
     *
     * @return the difficulty multiplier in respect to the base difficulty
     */
    public double getMultiplier() {
        return multiplier;
    }
    
    /**
     * Returns the root hash of the originating work request.
     * Note that this is <em>not</em> the hash of the block the work is being generated for.
     *
     * @return the requested work root
     */
    public HexData getRequestRoot() {
        return reqRoot;
    }
    
    /**
     * Returns the requested minimum base difficulty threshold, not including any multipliers. If no difficulty was
     * specified in the request, then this value would be provided by the {@link WorkDifficultyPolicy}.
     *
     * @return the requested base difficulty
     */
    public WorkDifficulty getRequestBaseDifficulty() {
        return baseDifficulty;
    }
    
    /**
     * Returns the requested target difficulty, including all difficulty multipliers. Note that this may be lower
     * than the base difficulty if a multiplier smaller than {@code 1} was used.
     *
     * @return the requested target difficulty
     */
    public WorkDifficulty getRequestTargetDifficulty() {
        return targetDifficulty;
    }
    
    /**
     * Returns the requested difficulty multiplier, <em>in respect to the base difficulty</em>.
     *
     * <p>This value factors in all difficulty multipliers; the original request multiplier (if specified through
     * {@link WorkGenerator#generate(Block, double)}), and the recommended multiplier specified by the
     * {@link WorkDifficultyPolicy#multiplier()}.</p>
     *
     * @return the requested difficulty multiplier
     */
    public double getRequestMultiplier() {
        return reqMultiplier;
    }
    
    
    @Override
    public String toString() {
        return "GeneratedWork{" +
                "work=" + work +
                ", difficulty=" + difficulty +
                ", blockRoot=" + reqRoot +
                '}';
    }
}
