/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.factory;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockBuilder;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;

/**
 * A {@link BlockFactory} which constructs and signs {@link StateBlock} blocks.
 */
public final class StateBlockFactory extends AbstractBlockFactory<StateBlock> {
    
    /**
     * Constructs a new StateBlockFactory with the given parameters.
     * @param config the block factory config
     *
     * @see BlockFactoryConfig#builder()
     */
    public StateBlockFactory(BlockFactoryConfig config) {
        super(config);
    }
    
    
    @Override
    protected BlockAndState<StateBlock> _createSend(HexData privateKey, AccountState state, NanoAccount destination, NanoAmount amount) {
        return construct(privateKey,
                builder(state)
                        .subtype(StateBlockSubType.SEND)
                        .balance(state.getBalance().subtract(amount))
                        .link(destination));
    }
    
    @Override
    protected BlockAndState<StateBlock> _createReceive(HexData privateKey, AccountState state, HexData sourceHash, NanoAmount amount) {
        NanoAmount newBal;
        try {
            newBal = state.getBalance().add(amount);
        } catch (ArithmeticException e) {
            throw new BlockCreationException("Receiving more funds than possible.");
        }
        return construct(privateKey,
                builder(state)
                        .subtype(state.isOpened() ? StateBlockSubType.RECEIVE : StateBlockSubType.OPEN)
                        .balance(newBal)
                        .link(sourceHash));
    }
    
    @Override
    protected BlockAndState<StateBlock> _createChange(HexData privateKey, AccountState state, NanoAccount representative) {
        return construct(privateKey,
                builder(state)
                        .subtype(StateBlockSubType.CHANGE)
                        .representative(representative));
    }
    
    
    private StateBlockBuilder builder(AccountState state) {
        return StateBlock.builder()
                .balance(state.getBalance())
                .previous(state.getFrontierHash().orElse(null))
                .representative(state.getRepresentative().orElse(getConfig().getDefaultRepresentative()))
                .generateWork(getConfig().getWorkGenerator())
                .usingAddressPrefix(getConfig().getAddressPrefix());
    }
    
    private static BlockAndState<StateBlock> construct(HexData privKey, StateBlockBuilder builder) {
        try {
            StateBlock block = builder.buildAndSign(privKey);
            return new BlockAndState<>(block, block.getAccountState());
        } catch (StateBlockBuilder.BlockCreationException e) {
            throw new BlockCreationException(e.getMessage(), e);
        }
    }

}
