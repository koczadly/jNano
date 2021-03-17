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
                .setSubtype(StateBlockSubType.SEND)
                .setBalance(state.getBalance().subtract(amount))
                .setLink(destination));
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
                .setSubtype(state.isOpened() ? StateBlockSubType.RECEIVE : StateBlockSubType.OPEN)
                .setBalance(newBal)
                .setLink(sourceHash));
    }
    
    @Override
    public BlockAndState _createChangeRepresentative(HexData privateKey, AccountState state,
                                                     NanoAccount representative) {
        return createBlock(privateKey, state, builder -> builder
                .setSubtype(StateBlockSubType.CHANGE)
                .setRepresentative(representative));
    }
    
    
    private BlockAndState createBlock(HexData privateKey, AccountState state,
                                      Consumer<StateBlockBuilder> builderConsumer) {
        // Set params
        StateBlockBuilder builder = StateBlock.builder()
                .setBalance(state.getBalance())
                .setPreviousHash(state.getFrontierHash())
                .setRepresentative(state.isOpened() ? state.getRepresentative() :
                        getSpecification().getDefaultRepresentative())
                .generateWork(getSpecification().getWorkGenerator());
        builderConsumer.accept(builder);
        // Build block
        StateBlock block = builder.buildAndSign(privateKey, getSpecification().getAddressPrefix());
        return new BlockAndState(block, AccountState.fromBlock(block));
    }
    
}
