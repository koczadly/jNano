/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.factory;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;

import java.util.Optional;

abstract class AbstractBlockFactory<B extends Block> implements BlockFactory<B> {
    
    private final BlockFactoryConfig spec;
    
    protected AbstractBlockFactory(BlockFactoryConfig spec) {
        if (spec == null)
            throw new IllegalArgumentException("BlockFactoryConfig is null.");
        this.spec = spec;
    }


    @Override
    public final BlockFactoryConfig getConfig() {
        return spec;
    }


    @Override
    public final BlockAndState<? extends B> createSendBlock(HexData privateKey, AccountState state, NanoAccount destination,
                                                            NanoAmount amount) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        if (state == null)
            throw new IllegalArgumentException("State cannot be null.");
        if (destination == null)
            throw new IllegalArgumentException("Destination cannot be null.");
        if (amount == null)
            throw new IllegalArgumentException("Amount cannot be null.");
        if (!state.isOpened())
            throw new BlockCreationException("Account has not been opened.");
        if (amount.compareTo(NanoAmount.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero.");
        if (state.getBalance().compareTo(amount) < 0)
            throw new BlockCreationException(String.format("Not enough funds (requested: %s, balance: %s)",
                    amount, state.getBalance()));
        
        return _createSend(privateKey, state, destination, amount);
    }

    protected abstract BlockAndState<? extends B> _createSend(HexData privateKey, AccountState state,
                                                              NanoAccount destination, NanoAmount amount);

    @Override
    public final BlockAndState<? extends B> createReceiveBlock(HexData privateKey, AccountState state, HexData sourceHash,
                                                               NanoAmount amount) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        if (state == null)
            throw new IllegalArgumentException("State cannot be null.");
        if (sourceHash == null)
            throw new IllegalArgumentException("Source hash cannot be null.");
        if (amount == null)
            throw new IllegalArgumentException("Amount cannot be null.");
        
        return _createReceive(privateKey, state, sourceHash, amount);
    }

    protected abstract BlockAndState<? extends B> _createReceive(HexData privateKey, AccountState state,
                                                                 HexData sourceHash, NanoAmount amount);

    @Override
    public final Optional<BlockAndState<? extends B>> createChangeBlock(HexData privateKey, AccountState state,
                                                                        NanoAccount representative) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        if (state == null)
            throw new IllegalArgumentException("State cannot be null.");
        if (representative == null)
            throw new IllegalArgumentException("Representative cannot be null.");
        if (!state.isOpened())
            throw new BlockCreationException("Account has not been opened.");
        
        if (representative.equalsIgnorePrefix(state.getRepresentative().get())) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(_createChange(privateKey, state, representative));
        }
    }

    
    protected abstract BlockAndState<? extends B> _createChange(HexData privateKey, AccountState state,
                                                                NanoAccount representative);

}
