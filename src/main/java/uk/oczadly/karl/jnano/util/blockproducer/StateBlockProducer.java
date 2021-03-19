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

import java.util.function.Consumer;

/**
 * A block producer which constructs and signs {@link StateBlock} blocks.
 */
public class StateBlockProducer extends BlockProducer {
    
    /**
     * Constructs a {@link StateBlockProducer}.
     * @param spec the producer specification
     */
    public StateBlockProducer(BlockProducerSpecification spec) {
        super(spec);
    }
    
    
    @Override
    public BlockAndState _createSend(HexData privateKey, AccountState state, NanoAccount destination,
                                     NanoAmount amount) {
        return createBlock(privateKey, state, builder -> builder
                .subtype(StateBlockSubType.SEND)
                .balance(state.getBalance().subtract(amount))
                .link(destination));
    }
    
    @Override
    public BlockAndState _createReceive(HexData privateKey, AccountState state, HexData sourceHash, NanoAmount amount) {
        NanoAmount newBal;
        try {
            newBal = state.getBalance().add(amount);
        } catch (ArithmeticException e) {
            throw new BlockCreationException("Receiving more funds than possible.");
        }
        return createBlock(privateKey, state, builder -> builder
                .subtype(state.isOpened() ? StateBlockSubType.RECEIVE : StateBlockSubType.OPEN)
                .balance(newBal)
                .link(sourceHash));
    }
    
    @Override
    public BlockAndState _createChangeRepresentative(HexData privateKey, AccountState state,
                                                     NanoAccount representative) {
        return createBlock(privateKey, state, builder -> builder
                .subtype(StateBlockSubType.CHANGE)
                .representative(representative));
    }
    
    
    private BlockAndState createBlock(HexData privateKey, AccountState state,
                                      Consumer<StateBlockBuilder> builderConsumer) {
        // Set params
        StateBlockBuilder builder = StateBlock.builder()
                .balance(state.getBalance())
                .previous(state.getFrontierHash())
                .representative(state.isOpened() ? state.getRepresentative() :
                        getSpecification().getDefaultRepresentative())
                .generateWork(getSpecification().getWorkGenerator())
                .usingAddressPrefix(getSpecification().getAddressPrefix());
        builderConsumer.accept(builder);
        // Build block
        StateBlock block = builder.buildAndSign(privateKey);
        return new BlockAndState(block, AccountState.fromBlock(block));
    }
    
}
