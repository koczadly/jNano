/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.blockproducer;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;

import java.util.Optional;

/**
 * This class can be used to create and sign new blocks based on an existing account state.
 *
 * @see StateBlockProducer
 * @see LegacyBlockProducer
 */
public abstract class BlockProducer {
    
    private final BlockProducerSpecification spec;
    
    /**
     * Creates a block producer with the given specification.
     * @param spec the specification
     */
    public BlockProducer(BlockProducerSpecification spec) {
        this.spec = spec;
    }
    
    
    /**
     * Returns the specification used by this block producer.
     * @return the specification of this block producer
     */
    public final BlockProducerSpecification getSpecification() {
        return spec;
    }
    
    
    /**
     * Constructs and signs a new block which sends all of the account's funds to the specified destination. An empty
     * optional will be returned if the account has no funds or hasn't been opened.
     *
     * @param privateKey  the private key of the account
     * @param state       the state of the account prior to the block
     * @param destination the destination account where the funds will be sent
     * @return the constructed block and new account state, or empty if the account has no funds
     * @throws BlockCreationException if the block couldn't be constructed, or work couldn't be generated
     */
    public final Optional<BlockAndState> createSendAll(HexData privateKey, AccountState state,
                                                       NanoAccount destination) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        if (state == null)
            throw new IllegalArgumentException("State cannot be null.");
        if (destination == null)
            throw new IllegalArgumentException("Destination cannot be null.");
    
        if (state.getBalance().compareTo(NanoAmount.ZERO) <= 0) {
            return Optional.empty();
        } else {
            return Optional.of(createSend(privateKey, state, destination, state.getBalance()));
        }
    }
    
    
    /**
     * Constructs and signs a new block which sends a specified amount of funds from the account.
     *
     * @param privateKey  the private key of the account
     * @param state       the state of the account prior to the block
     * @param destination the destination account where the funds will be sent
     * @param amount      the amount to send
     * @return the constructed block and new account state
     * @throws BlockCreationException if the block couldn't be constructed, work couldn't be generated, or the account
     *                                state doesn't match the arguments (eg. not enough funds)
     */
    public final BlockAndState createSend(HexData privateKey, AccountState state, NanoAccount destination,
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
            throw new BlockCreationException("Account must already be open.");
        if (amount.compareTo(NanoAmount.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero.");
        if (state.getBalance().compareTo(amount) < 0)
            throw new BlockCreationException(String.format("Not enough funds (%s < %s)", state.getBalance(), amount));
        
        return _createSend(privateKey, state, destination, amount);
    }
    
    /**
     * Constructs and signs a new block which receives a pending block.
     *
     * @param privateKey the private key of the account
     * @param state      the state of the account prior to the block
     * @param sourceHash the hash of the pending {@code send} block
     * @param amount     the amount of the pending send block
     * @return the constructed block and new account state
     * @throws BlockCreationException if the block couldn't be constructed, work couldn't be generated, or the account
     *                                state doesn't match the arguments (eg. receiving too many funds)
     */
    public final BlockAndState createReceive(HexData privateKey, AccountState state, HexData sourceHash,
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
    
    /**
     * Constructs and signs a new block which changes the account's representative. An empty optional will be returned
     * if the representative is already set to the one given.
     *
     * @param privateKey     the private key of the account
     * @param state          the state of the account prior to the block
     * @param representative the representative account
     * @return the constructed block and new account state, or empty if the representative is already set
     * @throws BlockCreationException if the block couldn't be constructed, or work couldn't be generated
     */
    public final Optional<BlockAndState> createChangeRepresentative(HexData privateKey, AccountState state,
                                                                    NanoAccount representative) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        if (state == null)
            throw new IllegalArgumentException("State cannot be null.");
        if (representative == null)
            throw new IllegalArgumentException("Representative cannot be null.");
        if (!state.isOpened())
            throw new BlockCreationException("Account must already be open.");
        
        if (representative.equalsIgnorePrefix(state.getRepresentative())) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(_createChangeRepresentative(privateKey, state, representative));
        }
    }
    
    protected abstract BlockAndState _createSend(HexData privateKey, AccountState state, NanoAccount destination,
                                                 NanoAmount amount);
    
    protected abstract BlockAndState _createReceive(HexData privateKey, AccountState state, HexData sourceHash,
                                                    NanoAmount amount);
    
    protected abstract BlockAndState _createChangeRepresentative(HexData privateKey, AccountState state,
                                                                 NanoAccount representative);
    
    
    public static class BlockCreationException extends RuntimeException {
        public BlockCreationException(String message) {
            super(message);
        }
        
        public BlockCreationException(Throwable cause) {
            super(cause);
        }
        
        public BlockCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
