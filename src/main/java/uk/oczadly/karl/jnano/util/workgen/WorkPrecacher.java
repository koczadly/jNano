/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

public final class WorkPrecacher {

    private volatile WorkSolution work;
    private volatile WorkDifficulty difficulty;
    private volatile HexData root;
    
    
    private synchronized void update(GeneratedWork work) {
        this.work = work.getWork();
        this.difficulty = work.getDifficulty();
        this.root = work.getRequestRoot();
    }
    
}
