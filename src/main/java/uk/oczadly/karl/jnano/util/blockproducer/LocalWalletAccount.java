/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.blockproducer;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.util.wallet.LocalRpcWalletAccount;

import java.util.Optional;

/**
 * This class can be used to locally create and sign blocks for an account. Each instance represents a single account,
 * and contains an internal state which is updated when a new block is created (from one of the methods), or manually
 * using {@link #updateState(AccountState)}.
 *
 * @see LocalRpcWalletAccount
 */
public class LocalWalletAccount {

    private final HexData privateKey;
    private final NanoAccount account;
    private final BlockProducer blockProducer;
    private volatile AccountState state;
    
    /**
     * Constructs a new LocalWalletAccount with an unopened initial state.
     * @param privateKey    the private key of the account
     * @param blockProducer the block producer
     */
    public LocalWalletAccount(HexData privateKey, BlockProducer blockProducer) {
        this(privateKey, blockProducer, null);
    }
    
    /**
     * Constructs a new LocalWalletAccount.
     * @param privateKey    the private key of the account
     * @param blockProducer the block producer
     * @param state         the initial account state, or null if not opened
     */
    public LocalWalletAccount(HexData privateKey, BlockProducer blockProducer, AccountState state) {
        if (privateKey == null)
            throw new IllegalArgumentException("Account private key cannot be null.");
        if (blockProducer == null)
            throw new IllegalArgumentException("BlockProducer cannot be null.");
        this.privateKey = privateKey;
        this.account = NanoAccount.fromPrivateKey(privateKey, blockProducer.getSpecification().getAddressPrefix());
        this.blockProducer = blockProducer;
        this.state = state != null ? state : AccountState.UNOPENED;
    }
    
    
    /**
     * Returns the account which this wallet represents.
     * @return the account of this wallet
     */
    public final NanoAccount getAccount() {
        return account;
    }
    
    /**
     * Returns the secret private key of the account.
     * @return the private key
     */
    public final HexData getPrivateKey() {
        return privateKey;
    }
    
    /**
     * Returns the block producer which constructs blocks for this account.
     * @return the block producer object
     */
    public final BlockProducer getBlockProducer() {
        return blockProducer;
    }
    
    /**
     * Returns the current internal state of this account.
     * @return the state of this account
     */
    public final synchronized AccountState getState() {
        return state;
    }
    
    /**
     * Updates the internal state of this account to the given state data.
     * @param state the new state object
     */
    public final synchronized void updateState(AccountState state) {
        this.state = state;
    }
    
    
    /**
     * Constructs and signs a new block which sends a specified amount of funds from the account.
     *
     * <p>When invoked and constructed, the internal state of the account will be updated with the adjustments made by
     * this block.</p>
     *
     * @param destination the destination account where the funds will be sent
     * @param amount      the amount to send
     * @return the constructed block
     * @throws BlockProducer.BlockCreationException if the block couldn't be constructed, work couldn't be generated,
     *         or the account state doesn't match the arguments (eg. not enough funds)
     */
    public synchronized Block createSend(NanoAccount destination, NanoAmount amount) {
        return updateState(blockProducer.createSend(privateKey, state, destination, amount));
    }
    
    /**
     * Constructs and signs a new block which sends all of the account's funds to the specified destination. An empty
     * optional will be returned if the account has no funds or hasn't been opened.
     *
     * <p>When invoked and constructed, the internal state of the account will be updated with the adjustments made by
     * this block.</p>
     *
     * @param destination the destination account where the funds will be sent
     * @return the constructed block, or empty if the account has no funds
     * @throws BlockProducer.BlockCreationException if the block couldn't be constructed, or work couldn't be generated
     */
    public synchronized Optional<Block> createSendAll(NanoAccount destination) {
        return blockProducer.createSendAll(privateKey, state, destination).map(this::updateState);
    }
    
    /**
     * Constructs and signs a new block which receives a pending block.
     *
     * <p>When invoked and constructed, the internal state of the account will be updated with the adjustments made by
     * this block.</p>
     *
     * @param sourceHash the hash of the pending {@code send} block
     * @param amount     the amount of the pending send block
     * @return the constructed block
     * @throws BlockProducer.BlockCreationException if the block couldn't be constructed, work couldn't be generated,
     *         or the account state doesn't match the arguments (eg. receiving too many funds)
     */
    public synchronized Block createReceive(HexData sourceHash, NanoAmount amount) {
        return updateState(blockProducer.createReceive(privateKey, state, sourceHash, amount));
    }
    
    /**
     * Constructs and signs a new block which changes the account's representative. An empty optional will be returned
     * if the representative is already set to the one given.
     *
     * <p>When invoked and constructed, the internal state of the account will be updated with the adjustments made by
     * this block.</p>
     *
     * @param representative the representative account
     * @return the constructed block, or empty if the representative is already set
     * @throws BlockProducer.BlockCreationException if the block couldn't be constructed, or work couldn't be generated
     */
    public synchronized Optional<Block> createChangeRepresentative(NanoAccount representative) {
        return blockProducer.createChangeRepresentative(privateKey, state, representative).map(this::updateState);
    }
    
    
    private synchronized Block updateState(BlockAndState block) {
        updateState(block.getState());
        return block.getBlock();
    }
    
    
    @Override
    public String toString() {
        return "LocalWalletAccount{" +
                "account=" + account +
                '}';
    }
}
