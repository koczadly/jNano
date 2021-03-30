/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.blockproducer;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockBuilder;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;

/**
 * A block producer which constructs and signs {@link StateBlock} blocks.
 */
public class StateBlockProducer extends BlockProducer {
    
    /**
     * Constructs a {@link StateBlockProducer} using the {@link BlockProducerSpecification#DEFAULT} specification.
     * This construction method isn't recommended, as you should always configure your own specification.
     * @see #StateBlockProducer(BlockProducerSpecification)
     */
    public StateBlockProducer() {
        super();
    }
    
    /**
     * Constructs a {@link StateBlockProducer} using the given specification.
     * @param spec the producer specification
     */
    public StateBlockProducer(BlockProducerSpecification spec) {
        super(spec);
    }
    
    
    @Override
    protected BlockAndState _createSend(HexData privateKey, AccountState state, NanoAccount destination,
                                        NanoAmount amount) {
        return sign(privateKey, builder(state)
                .subtype(StateBlockSubType.SEND)
                .balance(state.getBalance().subtract(amount))
                .link(destination));
    }
    
    @Override
    protected BlockAndState _createReceive(HexData privateKey, AccountState state, HexData sourceHash,
                                           NanoAmount amount) {
        NanoAmount newBal;
        try {
            newBal = state.getBalance().add(amount);
        } catch (ArithmeticException e) {
            throw new BlockCreationException("Receiving more funds than possible.");
        }
        return sign(privateKey, builder(state)
                .subtype(state.isOpened() ? StateBlockSubType.RECEIVE : StateBlockSubType.OPEN)
                .balance(newBal)
                .link(sourceHash));
    }
    
    @Override
    protected BlockAndState _createChangeRepresentative(HexData privateKey, AccountState state,
                                                        NanoAccount representative) {
        return sign(privateKey, builder(state)
                .subtype(StateBlockSubType.CHANGE)
                .representative(representative));
    }
    
    
    private StateBlockBuilder builder(AccountState state) {
        return StateBlock.builder()
                .balance(state.getBalance())
                .previous(state.getFrontierHash())
                .representative(state.isOpened() ? state.getRepresentative() :
                        getSpecification().getDefaultRepresentative())
                .generateWork(getSpecification().getWorkGenerator())
                .usingAddressPrefix(getSpecification().getAddressPrefix());
    }
    
    private static BlockAndState sign(HexData privateKey, StateBlockBuilder builder) {
        StateBlock block;
        try {
            block = builder.buildAndSign(privateKey);
        } catch (StateBlockBuilder.BlockCreationException e) {
            throw new BlockCreationException(e);
        }
        return new BlockAndState(block, AccountState.fromBlock(block));
    }
    
}
