/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.wallet;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.factory.AccountState;
import uk.oczadly.karl.jnano.model.block.factory.BlockAndState;
import uk.oczadly.karl.jnano.model.block.factory.BlockFactory;

import java.util.Optional;

/**
 * This class can be used to locally create and sign blocks for an account. Each instance represents a single account,
 * and contains an internal account state which is updated when creating a block and committing the state, or manually
 * by calling {@link #updateState(AccountState)}.
 *
 * <p>When calling a block creation method, you will receive the block contents and the uncommitted transaction state
 * will be set. If the block is approved (eg. work is valid, block accepted on the Nano network) then you <em>must</em>
 * call the {@link #commitState()} method to update the internal state for the next block.</p>
 *
 * <p>Consider the example below, which creates a brand new account:</p>
 * <pre>{@code
 * // Construct a block producer object with your configuration
 * BlockProducer blockProducer = new StateBlockProducer(BlockProducerSpecification.builder()
 *         .defaultRepresentative("nano_3caprkc56ebsaakn4j4n7g9p8h358mycfjcyzkrfw1nai6prbyk8ihc5yjjk")
 *         .workGenerator(new CPUWorkGenerator())
 *         .build());
 *
 * // Generate random account & private key
 * HexData privateKey = WalletUtil.generateRandomKey();
 *
 * // Create new LocalWalletAccount using an unopened (not used) state
 * LocalWalletAccount account = new LocalWalletAccount(privateKey, blockProducer, AccountState.UNOPENED);
 *
 * // Create a new receive block
 * Block block = account.createReceive(
 *         new HexData("B17D37FCE28328BFB989D990C223F52366B222670C6DEF428C5119478B700B92"),
 *         NanoAmount.valueOfRaw("1000000000000000000000000"));
 *
 * System.out.println(account.getState().getBalance()); // Balance still reads zero as state has not been committed
 *
 * // ...do something with the block object (eg. publish to network)...
 * System.out.println("Block hash: " + block.getHash());
 *
 * account.commitState(); // Commit the new state (if the block is accepted)
 * System.out.println(account.getState().getBalance()); // State balance is now 0.000001 NANO
 * }</pre>
 *
 * @see LocalRpcWalletAccount
 */
public class LocalWalletAccount<B extends Block> {

    private final HexData privateKey;
    private final NanoAccount account;
    private final BlockFactory<B> blockFactory;
    private volatile AccountState state, transactionState;
    
    /**
     * Constructs a new LocalWalletAccount with an unopened initial state.
     *
     * @param privateKey    the private key of the account
     * @param blockFactory the block producer
     */
    public LocalWalletAccount(HexData privateKey, BlockFactory<B> blockFactory) {
        this(privateKey, blockFactory, AccountState.UNOPENED);
    }
    
    /**
     * Constructs a new LocalWalletAccount with the provided initial state.
     *
     * @param privateKey    the private key of the account
     * @param blockFactory the block producer
     * @param state         the initial account state, or null if not opened
     */
    public LocalWalletAccount(HexData privateKey, BlockFactory<B> blockFactory, AccountState state) {
        if (privateKey == null) throw new IllegalArgumentException("Account private key cannot be null.");
        if (blockFactory == null) throw new IllegalArgumentException("BlockProducer cannot be null.");
        this.privateKey = privateKey;
        this.account = NanoAccount.fromPrivateKey(privateKey, blockFactory.getAddressPrefix());
        this.blockFactory = blockFactory;
        this.state = JNH.nonNull(state, AccountState.UNOPENED);
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
     * Returns the block producer which constructs new blocks for this account.
     * @return the block producer object
     */
    public final BlockFactory<B> getBlockProducer() {
        return blockFactory;
    }
    
    /**
     * Returns the current internal committed state of this account.
     * @return the state of this account
     */
    public final AccountState getState() {
        return state;
    }
    
    /**
     * Updates the internal state of this account to the given state data. Calling this method will also erase the
     * transactional state (if not already committed), and you will not need to invoke {@link #commitState()} after
     * this method.
     *
     * @param state the new state object
     * @return true if the new state is different from the previous, false if it's the same
     */
    public final synchronized boolean updateState(AccountState state) {
        if (state == null)
            throw new IllegalArgumentException("State cannot be null.");
        boolean hasChanged = !this.state.equals(state);
        this.state = state;
        this.transactionState = null;
        return hasChanged;
    }
    
    /**
     * Updates the internal state of this account to the new state generated by one of the {@code create} methods.
     *
     * <p>Throws an {@link IllegalStateException} if there is no current transaction in progress. No exception will be
     * thrown if the creation method returned an empty optional, but the state will also remain the same.</p>
     *
     * @throws IllegalStateException if there is currently no uncommitted transaction
     */
    public final synchronized void commitState() {
        if (transactionState == null)
            throw new IllegalStateException("No transaction currently in progress.");
        updateState(transactionState);
    }
    
    
    /**
     * Constructs and signs a new block which sends a specified amount of funds from the account.
     *
     * <p>After constructing the block, you must call {@link #commitState()} to update the internal state. If the commit
     * method isn't called then the state will remain the same as before - this behaviour is useful if the block was
     * rejected or could not be published.</p>
     *
     * @param destination the destination account where the funds will be sent
     * @param amount      the amount to send
     * @return the constructed block
     * @throws BlockFactory.CreationException if the block couldn't be constructed, work couldn't be generated,
     *         or the account state doesn't match the arguments (eg. not enough funds)
     */
    public synchronized B createSend(NanoAccount destination, NanoAmount amount) {
        return updateState(blockFactory.createSend(privateKey, state, destination, amount));
    }
    
    /**
     * Constructs and signs a new block which sends all of the account's funds to the specified destination. An empty
     * optional will be returned if the account has no funds or hasn't been opened.
     *
     * <p>After constructing the block, you must call {@link #commitState()} to update the internal state. If the commit
     * method isn't called then the state will remain the same as before - this behaviour is useful if the block was
     * rejected or could not be published. If an empty value is returned by this method, calling {@link #commitState()}
     * won't have any effect on the state (but also won't throw an exception).</p>
     *
     * @param destination the destination account where the funds will be sent
     * @return the constructed block, or empty if the account has no funds
     * @throws BlockFactory.CreationException if the block couldn't be constructed, or work couldn't be generated
     */
    public synchronized Optional<B> createSendAll(NanoAccount destination) {
        if (state.getBalance().compareTo(NanoAmount.ZERO) > 0) {
            return Optional.of(updateState(blockFactory.createSend(
                    privateKey, state, destination, state.getBalance())));
        } else {
            return Optional.empty();
        }
    }
    
    /**
     * Constructs and signs a new block which receives a pending block.
     *
     * <p>After constructing the block, you must call {@link #commitState()} to update the internal state. If the commit
     * method isn't called then the state will remain the same as before - this behaviour is useful if the block was
     * rejected or could not be published.</p>
     *
     * @param sourceHash the hash of the pending {@code send} block
     * @param amount     the amount of the pending send block
     * @return the constructed block
     * @throws BlockFactory.CreationException if the block couldn't be constructed, work couldn't be generated,
     *         or the account state doesn't match the arguments (eg. receiving too many funds)
     */
    public synchronized B createReceive(HexData sourceHash, NanoAmount amount) {
        return updateState(blockFactory.createReceive(privateKey, state, sourceHash, amount));
    }
    
    /**
     * Constructs and signs a new block which changes the account's representative. An empty optional will be returned
     * if the representative is already set to the one given.
     *
     * <p>After constructing the block, you must call {@link #commitState()} to update the internal state. If the commit
     * method isn't called then the state will remain the same as before - this behaviour is useful if the block was
     * rejected or could not be published. If an empty value is returned by this method, calling {@link #commitState()}
     * won't have any effect on the state (but also won't throw an exception).</p>
     *
     * @param representative the representative account
     * @return the constructed block, or empty if the representative is already set
     * @throws BlockFactory.CreationException if the block couldn't be constructed, or work couldn't be generated
     */
    public synchronized Optional<B> createChange(NanoAccount representative) {
        return updateState(blockFactory.createChange(privateKey, state, representative));
    }
    
    
    private synchronized B updateState(BlockAndState<? extends B> block) {
        transactionState = block.getState();
        return block.getBlock();
    }
    
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private synchronized Optional<B> updateState(Optional<BlockAndState<? extends B>> block) {
        transactionState = block.map(BlockAndState::getState).orElse(state);
        return block.map(BlockAndState::getBlock);
    }
    
    
    @Override
    public String toString() {
        return "LocalWalletAccount{" +
                "account=" + getAccount() +
                ", blockFactory=" + getBlockProducer().getClass().getSimpleName() + '}';
    }
    
}
