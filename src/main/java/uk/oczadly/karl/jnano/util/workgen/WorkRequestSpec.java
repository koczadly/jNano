/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.internal.utils.NanoUtil;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.util.workgen.policy.DifficultyRetrievalException;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

/**
 * Represents a work request specification: the block root, and the difficulty value which should be chosen.
 *
 * This class is used to defer difficulty calculations (ie. active network difficulty) until the work is at the head
 * of the queue and due to be processed.
 */
class WorkRequestSpec {
    
    final WorkDifficultyPolicy policy;
    final HexData root;
    final double reqMultiplier;
    final WorkDifficulty reqDifficulty;
    final Block block;
    
    public WorkRequestSpec(WorkDifficultyPolicy policy, HexData root, double reqMultiplier,
                    WorkDifficulty reqDifficulty) {
        this(policy, root, reqMultiplier, reqDifficulty, null);
    }
    
    public WorkRequestSpec(WorkDifficultyPolicy policy, Block block, double reqMultiplier,
                    WorkDifficulty reqDifficulty) {
        this(policy, NanoUtil.getWorkRoot(block), reqMultiplier, reqDifficulty, block);
    }
    
    private WorkRequestSpec(WorkDifficultyPolicy policy, HexData root, double reqMultiplier,
                            WorkDifficulty reqDifficulty, Block block) {
        this.policy = policy;
        this.root = root;
        this.reqMultiplier = reqMultiplier;
        this.reqDifficulty = reqDifficulty;
        this.block = block;
    }
    
    
    public DifficultySet fetchDifficulty() throws DifficultyRetrievalException {
        WorkDifficulty base;
        if (reqDifficulty != null) {
            base = reqDifficulty;           // Using difficulty constant
        } else if (policy != null) {
            if (block != null) {
                base = policy.forBlock(block);  // Using block
            } else {
                base = policy.forAny();         // Using "any" difficulty
            }
        } else {
            throw new AssertionError("Cannot determine work difficulty.");
        }
        double baseMulti = policy != null ? policy.multiplier() : 1;
        return new DifficultySet(base, baseMulti * reqMultiplier);
    }
    
    
    static final class DifficultySet {
        private final WorkDifficulty base, target;
        private final double multiplier;
    
        public DifficultySet(WorkDifficulty base, double targetMultiplier) {
            this.base = base;
            this.multiplier = targetMultiplier;
            this.target = base.multiply(targetMultiplier);
        }
        
        public WorkDifficulty getBase() {
            return base;
        }
    
        public WorkDifficulty getTarget() {
            return target;
        }
    
        public double getMultiplier() {
            return multiplier;
        }
    }
    
}
