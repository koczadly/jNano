/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.service.workgen.policy;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

/**
 * This work difficulty policy uses the difficulty system implemented in node version {@code V21}.
 *
 * <p>With this policy, {@code send} and {@code change} state subtypes will use the {@code send} difficulty, and
 * {@code receive}, {@code open} and {@code epoch} subtypes equate to the {@code receive} difficulty. Legacy block
 * types will map to the largest of either two values.</p>
 */
public final class ConstantDifficultyPolicyV2 implements ConstantWorkDifficultyPolicy {
    
    private final WorkDifficulty base, send, receive;
    private final double multiplier;
    
    /**
     * Constructs a new policy instance from a set of constant values.
     *
     * @param diffSend    the difficulty threshold for {@code send} types
     * @param diffReceive the difficulty threshold for {@code receive} types
     */
    public ConstantDifficultyPolicyV2(WorkDifficulty diffSend, WorkDifficulty diffReceive) {
        this(diffSend, diffReceive, 1);
    }
    
    ConstantDifficultyPolicyV2(WorkDifficulty diffSend, WorkDifficulty diffReceive, double multiplier) {
        this.send = diffSend;
        this.receive = diffReceive;
        this.base = JNH.max(diffReceive, diffSend);
        this.multiplier = multiplier;
    }
    
    
    @Override
    public WorkDifficulty forBlock(Block block) {
        if (block.getType() == BlockType.STATE) {
            switch (((StateBlock)block).getSubType()) {
                case SEND:
                case CHANGE:
                    return send;
                default:
                    return receive;
            }
        }
        return base;
    }
    
    @Override
    public WorkDifficulty forAny() {
        return base;
    }
    
    @Override
    public double multiplier() {
        return multiplier;
    }
    
    /**
     * Returns the minimum difficulty threshold for {@code send} subtypes.
     * @return the difficulty for send blocks
     */
    public WorkDifficulty forSendTypes() {
        return send;
    }
    
    /**
     * Returns the minimum difficulty threshold for {@code receive} subtypes.
     * @return the difficulty for receive blocks
     */
    public WorkDifficulty forReceiveTypes() {
        return receive;
    }
    
}
