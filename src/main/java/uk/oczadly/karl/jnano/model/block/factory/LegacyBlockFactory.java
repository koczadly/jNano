/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.factory;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.currency.NanoAmount;
import uk.oczadly.karl.jnano.model.block.*;
import uk.oczadly.karl.jnano.util.workgen.GeneratedWork;

import java.util.concurrent.ExecutionException;

/**
 * A {@link BlockFactory} which constructs and signs legacy block types ({@link SendBlock send}, {@link OpenBlock open},
 * {@link ReceiveBlock receive} and {@link ChangeBlock change}).
 */
public final class LegacyBlockFactory extends AbstractBlockFactory<Block> {

    /**
     * Constructs a new LegacyBlockFactory with the given parameters.
     * @param config the block factory config
     *
     * @see BlockFactoryConfig#builder()
     */
    public LegacyBlockFactory(BlockFactoryConfig config) {
        super(config);
    }
    
    
    @Override
    protected BlockAndState<Block> _createSend(HexData privateKey, AccountState state, NanoAccount destination,
                                               NanoAmount amount) {
        NanoAmount newBalance = state.getBalance().subtract(amount);
        return construct(privateKey, newBalance, state.getRepresentative().get(),
                new SendBlock(null, null, state.getFrontierHash().get(), destination, newBalance));
    }
    
    @Override
    protected BlockAndState<Block> _createReceive(HexData privateKey, AccountState state, HexData sourceHash,
                                                  NanoAmount amount) {
        NanoAmount newBal;
        try {
            newBal = state.getBalance().add(amount);
        } catch (ArithmeticException e) {
            throw new BlockCreationException("Receiving more funds than possible.");
        }
        
        if (state.isOpened()) {
            // Receive
            return construct(privateKey, newBal, state.getRepresentative().get(),
                    new ReceiveBlock(null, null, state.getFrontierHash().get(), sourceHash));
        } else {
            // Open
            return construct(privateKey, newBal, getConfig().getDefaultRepresentative(),
                    new OpenBlock(null, null, sourceHash,
                            NanoAccount.fromPrivateKey(privateKey, getConfig().getAddressPrefix()),
                            getConfig().getDefaultRepresentative()));
        }
    }
    
    @Override
    protected BlockAndState<Block> _createChange(HexData privateKey, AccountState state, NanoAccount representative) {
        return construct(privateKey, state.getBalance(), representative,
                new ChangeBlock(null, null, state.getFrontierHash().get(), representative));
    }
    
    
    private BlockAndState<Block> construct(HexData privateKey, NanoAmount stateBal, NanoAccount stateRep, Block block) {
        try {
            GeneratedWork work = getConfig().getWorkGenerator().generate(block).get();
            block.setWork(work.getWork());
        } catch (InterruptedException | ExecutionException e) {
            throw new BlockCreationException("Couldn't generate work.", e);
        }
        block.sign(privateKey);
        return new BlockAndState<>(block, new AccountState(block.getHash(), stateBal, stateRep));
    }
    
}
