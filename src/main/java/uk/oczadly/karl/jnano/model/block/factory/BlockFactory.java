/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.factory;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.util.workgen.WorkGenerator;

import java.util.Optional;

/**
 * This class can be used to create and sign new blocks based on an existing account state.
 *
 * @param <B> the base block type
 *
 * @see StateBlockFactory
 * @see LegacyBlockFactory
 */
public abstract class BlockFactory<B extends Block> {
    
    private final NanoAccount defaultRepresentative;
    private final String addressPrefix;
    private final WorkGenerator workGenerator;
    
    protected BlockFactory(NanoAccount defaultRepresentative, String addressPrefix, WorkGenerator workGenerator) {
        if (defaultRepresentative == null)
            throw new IllegalArgumentException("Default representative cannot be null.");
        if (addressPrefix == null)
            throw new IllegalArgumentException("Address prefix cannot be null.");
        if (workGenerator == null)
            throw new IllegalArgumentException("Work generator cannot be null.");
        
        this.defaultRepresentative = defaultRepresentative.withPrefix(addressPrefix);
        this.addressPrefix = addressPrefix;
        this.workGenerator = workGenerator;
    }
    
    
    /**
     * @return the default representative for new accounts
     */
    public NanoAccount getDefaultRepresentative() {
        return defaultRepresentative;
    }
    
    /**
     * @return the prefix for addresses, excluding the underscore separator
     */
    public String getAddressPrefix() {
        return addressPrefix;
    }
    
    /**
     * @return the work generator used to generate work
     */
    public WorkGenerator getWorkGenerator() {
        return workGenerator;
    }
    
    
    /**
     * Constructs and signs a new block which sends a specified amount of funds from the account.
     *
     * @param privateKey  the private key of the account
     * @param state       the state of the account prior to the block
     * @param destination the destination account where the funds will be sent
     * @param amount      the amount to send
     * @return the constructed block and new account state
     * @throws CreationException if the block couldn't be constructed, work couldn't be generated, or the account
     *                                state doesn't match the arguments (eg. not enough funds)
     */
    public final BlockAndState<? extends B> createSend(HexData privateKey, AccountState state, NanoAccount destination,
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
            throw new CreationException("Account has not been opened.");
        if (amount.compareTo(NanoAmount.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero.");
        if (state.getBalance().compareTo(amount) < 0)
            throw new CreationException(String.format("Not enough funds (requested: %s, balance: %s)",
                    amount, state.getBalance()));
        
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
     * @throws CreationException if the block couldn't be constructed, work couldn't be generated, or the account
     *                                state doesn't match the arguments (eg. receiving too many funds)
     */
    public final BlockAndState<? extends B> createReceive(HexData privateKey, AccountState state, HexData sourceHash,
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
     * @throws CreationException if the block couldn't be constructed, or work couldn't be generated
     */
    public final Optional<BlockAndState<? extends B>> createChange(HexData privateKey, AccountState state,
                                                                   NanoAccount representative) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        if (state == null)
            throw new IllegalArgumentException("State cannot be null.");
        if (representative == null)
            throw new IllegalArgumentException("Representative cannot be null.");
        if (!state.isOpened())
            throw new CreationException("Account has not been opened.");
        
        if (representative.equalsIgnorePrefix(state.getRepresentative())) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(_createChange(privateKey, state, representative));
        }
    }
    
    protected abstract BlockAndState<? extends B> _createSend(HexData privateKey, AccountState state,
                                                              NanoAccount destination, NanoAmount amount);
    
    protected abstract BlockAndState<? extends B> _createReceive(HexData privateKey, AccountState state,
                                                                 HexData sourceHash, NanoAmount amount);
    
    protected abstract BlockAndState<? extends B> _createChange(HexData privateKey, AccountState state,
                                                                NanoAccount representative);
    
    
    
    public static class CreationException extends RuntimeException {
        public CreationException(String message) {
            super(message);
        }
        
        public CreationException(Throwable cause) {
            super(cause);
        }
        
        public CreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
