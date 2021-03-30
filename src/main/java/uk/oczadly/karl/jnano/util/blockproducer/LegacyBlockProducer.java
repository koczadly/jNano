/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.blockproducer;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.*;
import uk.oczadly.karl.jnano.util.workgen.GeneratedWork;

import java.util.concurrent.ExecutionException;

/**
 * A block producer which constructs and signs legacy block types ({@link SendBlock}, {@link OpenBlock},
 * {@link ReceiveBlock} and {@link ChangeBlock}).
 */
public class LegacyBlockProducer extends BlockProducer {
    
    /**
     * Constructs a {@link LegacyBlockProducer} using the {@link BlockProducerSpecification#DEFAULT} specification.
     * This construction method isn't recommended, as you should always configure your own specification.
     * @see #LegacyBlockProducer(BlockProducerSpecification)
     */
    public LegacyBlockProducer() {
        super();
    }
    
    /**
     * Constructs a {@link LegacyBlockProducer} using the given specification.
     * @param spec the producer specification
     */
    public LegacyBlockProducer(BlockProducerSpecification spec) {
        super(spec);
    }
    
    
    @Override
    protected BlockAndState _createSend(HexData privateKey, AccountState state, NanoAccount destination,
                                        NanoAmount amount) {
        NanoAmount balance = state.getBalance().subtract(amount);
        return createBlock(privateKey, balance, state.getRepresentative(),
                new SendBlock(null, null, state.getFrontierHash(), destination, balance));
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
        
        if (state.isOpened()) {
            // Receive
            return createBlock(privateKey, newBal, state.getRepresentative(),
                    new ReceiveBlock(null, null, state.getFrontierHash(), sourceHash));
        } else {
            // Open
            return createBlock(privateKey, newBal, getSpecification().getDefaultRepresentative(),
                    new OpenBlock(null, null, sourceHash,
                            NanoAccount.fromPrivateKey(privateKey, getSpecification().getAddressPrefix()),
                            getSpecification().getDefaultRepresentative()));
        }
    }
    
    @Override
    protected BlockAndState _createChangeRepresentative(HexData privateKey, AccountState state,
                                                        NanoAccount representative) {
        return createBlock(privateKey, state.getBalance(), representative,
                new ChangeBlock(null, null, state.getFrontierHash(), representative));
    }
    
    
    private BlockAndState createBlock(HexData privateKey, NanoAmount stateBal, NanoAccount stateRep, Block block) {
        try {
            GeneratedWork work = getSpecification().getWorkGenerator().generate(block).get();
            block.setWorkSolution(work.getWork());
        } catch (InterruptedException | ExecutionException e) {
            throw new BlockCreationException("Couldn't generate work.", e);
        }
        block.sign(privateKey);
        return new BlockAndState(block, new AccountState(block.getHash(), stateBal, stateRep));
    }
    
}
