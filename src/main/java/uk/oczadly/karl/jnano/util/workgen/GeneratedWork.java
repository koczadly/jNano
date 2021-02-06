/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

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
     * Returns the difficulty multiplier of the generated work solution, in respect to the requested difficulty.
     * @return the difficulty multiplier of the work
     */
    public double getMultiplier() {
        return getDifficulty().calculateMultiplier(reqDifficulty);
    }
    
    /**
     * Returns the root hash of the originating work request.
     * @return the requested work root
     */
    public HexData getRequestRoot() {
        return reqRoot;
    }
    
    /**
     * Returns the required difficulty threshold of the originating work request.
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
