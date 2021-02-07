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
abstract class WorkRequestSpec {
    
    private final WorkDifficultyPolicy policy;
    private final HexData root;
    
    protected WorkRequestSpec(WorkDifficultyPolicy policy, HexData root) {
        this.policy = policy;
        this.root = root;
    }
    
    
    public final WorkDifficultyPolicy getPolicy() {
        return policy;
    }
    
    public final HexData getRoot() {
        return root;
    }
    
    public abstract DifficultySet getDifficulty() throws DifficultyRetrievalException;
    
    
    
    /** Obtain the difficulty from the block (using forBlock()) */
    static class WithBlock extends WorkRequestSpec {
        private final Block block;
        private final double multiplier;
    
        public WithBlock(WorkDifficultyPolicy policy, Block block, double multiplier) {
            super(policy, NanoUtil.getWorkRoot(block));
            this.block = block;
            this.multiplier = multiplier;
        }
        
        @Override
        public DifficultySet getDifficulty() throws DifficultyRetrievalException {
            WorkDifficulty base = getPolicy().forBlock(block);
            return new DifficultySet(base,
                    base.multiply(getPolicy().multiplier() * multiplier));
        }
    }
    
    /** Obtain the difficulty from the root and difficulty (or policy difficulty using forAny()) */
    static class WithRoot extends WorkRequestSpec {
        private final WorkDifficulty difficulty;
        
        public WithRoot(WorkDifficultyPolicy policy, HexData root, WorkDifficulty difficulty) {
            super(policy, root);
            this.difficulty = difficulty;
        }
        
        @Override
        public DifficultySet getDifficulty() throws DifficultyRetrievalException {
            if (difficulty == null) {
                // Policy
                WorkDifficulty base = getPolicy().forAny();
                return new DifficultySet(base, base.multiply(getPolicy().multiplier()));
            } else {
                // Difficulty
                return new DifficultySet(difficulty, difficulty);
            }
        }
    }
    
    
    static final class DifficultySet {
        private final WorkDifficulty base, target;
    
        public DifficultySet(WorkDifficulty base, WorkDifficulty target) {
            this.base = base;
            this.target = target;
        }
        
        public WorkDifficulty getBase() {
            return base;
        }
    
        public WorkDifficulty getTarget() {
            return target;
        }
    }
    
}
