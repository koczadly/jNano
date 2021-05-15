/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.wallet;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.factory.AccountState;
import uk.oczadly.karl.jnano.model.block.factory.BlockFactory;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.exception.RpcEntityNotFoundException;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.exception.RpcExternalException;
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountInfo;
import uk.oczadly.karl.jnano.rpc.request.node.RequestBlockInfo;
import uk.oczadly.karl.jnano.rpc.request.node.RequestPending;
import uk.oczadly.karl.jnano.rpc.request.node.RequestProcess;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountInfo;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockInfo;
import uk.oczadly.karl.jnano.rpc.response.ResponsePending;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * This class provides a set of methods for performing wallet actions on an account, without sending the private key
 * to an external RPC node.
 *
 * <p>All of the methods in this class are secure, and will never send or expose any private keys to the RPC server.
 * This should be used when connecting via a third-party RPC provider. The class is also completely thread safe, though
 * use in a single-threaded execution flow should be preferred.</p>
 *
 * <p>Due to the asynchronous nature of Nano, you should not create multiple instances representing the same account,
 * nor should you use the same account on another client/wallet/node at the same time — doing so can result in
 * unexpected transaction failures. If a block is rejected by the node due to an outdated cached state, the state will
 * be refreshed and a new block generated up to 2 times before failing and throwing an exception.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * RpcQueryNode rpcClient = RpcServiceProviders.myNanoNinja(); // Use mynano.ninja public RPC
 *
 * // Construct a block factory with your configuration
 * BlockFactory<StateBlock> blockFactory = new StateBlockFactory(
 *         BlockProducerSpecification.builder()
 *                 .defaultRepresentative("nano_3caprkc56ebsaakn4j4n7g9p8h358mycfjcyzkrfw1nai6prbyk8ihc5yjjk")
 *                 .workGenerator(new NodeWorkGenerator(rpcClient)) // Generate work on the node
 *                 .build()
 * );
 *
 * // Create account from private key
 * LocalRpcWalletAccount<StateBlock> account = new LocalRpcWalletAccount<>(
 *         new HexData("183A1DEDCA9CD37029456C8A2ED31460A0E9A8D18032676010AC11B02A442417"), // Private key
 *         rpcClient, blockFactory); // Using the RPC client and BlockFactory defined above
 *
 * // Print account info
 * System.out.printf("Using account address %s%n", account.getAccount());
 * System.out.printf("Initial balance: %s%n", account.getBalance());
 *
 * // Receive all pending funds
 * System.out.printf("Received %,d blocks%n", account.receiveAll().size());
 * System.out.printf("Balance: %s%n", account.getBalance());
 *
 * // Send funds to another account
 * System.out.printf("Send block hash: %s%n", account.send(
 *         NanoAccount.parseAddress("nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz"),
 *         NanoAmount.valueOfNano("0.01"))
 *         .getHash());
 * }</pre>
 *
 * <p>This class relies on the following RPC queries: {@link RequestProcess process}, {@link RequestAccountInfo
 * account_info}, {@link RequestBlockInfo block_info}, {@link RequestPending pending}.</p>
 *
 * @param <B> the base block type created by this account (specified by the {@link BlockFactory})
 */
//todo test fork resolution/reattempt
public class LocalRpcWalletAccount<B extends Block> {
    
    private static final NanoAmount DEFAULT_THRESHOLD = NanoAmount.valueOfRawExponent(24);
    private static final int RECEIVE_BATCH_SIZE = 25;
    private static final int MAX_RETRY_ATTEMPTS = 3;
    
    private final RpcQueryNode rpcClient;
    private final LocalWalletAccount<B> walletAccount;
    private volatile boolean hasRetrievedState = false;
    private final Lock lock = new ReentrantLock(true);
    
    
    /**
     * Constructs a new local RPC wallet account.
     * @param privateKey    the private key of the account
     * @param rpcClient     the RPC client where requests will be sent to
     * @param blockFactory the block producer
     */
    public LocalRpcWalletAccount(HexData privateKey, RpcQueryNode rpcClient, BlockFactory<B> blockFactory) {
        if (privateKey == null) throw new IllegalArgumentException("Private key cannot be null.");
        if (rpcClient == null) throw new IllegalArgumentException("RPC client cannot be null.");
        if (blockFactory == null) throw new IllegalArgumentException("BlockProducer cannot be null.");
        this.walletAccount = new LocalWalletAccount<>(privateKey, blockFactory);
        this.rpcClient = rpcClient;
    }
    
    
    /**
     * Returns the account which this wallet represents.
     * @return the account of this wallet
     */
    public final NanoAccount getAccount() {
        return walletAccount.getAccount();
    }
    
    /**
     * Returns the secret private key of the account.
     * @return the private key
     */
    public final HexData getPrivateKey() {
        return walletAccount.getPrivateKey();
    }
    
    /**
     * Returns the block factory which constructs new blocks for this account.
     * @return the block factory object
     */
    public final BlockFactory<B> getBlockFactory() {
        return walletAccount.getBlockProducer();
    }
    
    /**
     * Returns the RPC client which will execute the remote wallet operations.
     * @return the RPC client
     */
    public final RpcQueryNode getRpcClient() {
        return rpcClient;
    }
    
    @Override
    public String toString() {
        return "LocalRpcWalletAccount{" +
                "account=" + getAccount() +
                ", blockFactory=" + getBlockFactory().getClass().getSimpleName() +
                ", rpcClient=" + rpcClient + '}';
    }
    
    
    /**
     * Forcefully refreshes the internal cached state of the account by calling the {@link RequestAccountInfo} RPC
     * query.
     *
     * <p>Most implementations should never have to call this method, as the state will automatically be retrieved or
     * updated when necessary through the other methods.</p>
     *
     * @return true if the internally cached state was externally updated and has changed, false if it remains the same
     *         or is the first time retrieving the state
     * @throws WalletActionException if an error occurs with the RPC query
     */
    public boolean refreshState() throws WalletActionException {
        lock.lock();
        try {
            AccountState newState;
            try {
                // Retrieve state via RPC
                ResponseAccountInfo accInfo = rpcClient.processRequest(
                        new RequestAccountInfo(getAccount().toAddress()));
                newState = AccountState.fromAccountInfo(accInfo);
            } catch (RpcEntityNotFoundException e) {
                newState = AccountState.UNOPENED; // Account hasn't been opened
            } catch (RpcException | IOException e) {
                throw wrapRpcException("Couldn't retrieve account state.", e);
            }
            boolean hasChanged = walletAccount.updateState(newState) && !hasRetrievedState;
            hasRetrievedState = true;
            return hasChanged;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Returns the current balance of this account, not include any pending amounts. A value of zero if the account
     * hasn't yet been opened.
     *
     * <p>Note that this balance may include unconfirmed amounts. This shouldn't be an issue, as only people with
     * access to this account's private key may reverse these transactions with a fork block.</p>
     *
     * @return the current balance of the account
     * @throws WalletActionException if an error occurs with the RPC query when retrieving the account state
     */
    public NanoAmount getBalance() throws WalletActionException {
        return initState().getBalance();
    }
    
    /**
     * Returns the hash of the current frontier block of this account, or an empty value if the account hasn't been
     * opened yet.
     *
     * <p>Note that this may be an unconfirmed block. This shouldn't be an issue, as only people with access to this
     * account's private key may reverse this block with another fork block.</p>
     *
     * @return the current account frontier block hash, or empty if unopened
     * @throws WalletActionException if an error occurs with the RPC query when retrieving the account state
     */
    public Optional<HexData> getFrontierHash() throws WalletActionException {
        return Optional.ofNullable(initState().getFrontierHash());
    }
    
    
    /**
     * Sends the specified amount of funds to an account.
     *
     * <p>Calling this method will construct and sign a new block, generate the appropriate work for it, and publish the
     * block to the network via RPC.</p>
     *
     * @param destination the account where the funds should be sent
     * @param amount      the amount of funds to send
     * @return the generated and published {@code send} block
     * @throws WalletActionException if an error occurs with the RPC query, work generation, block processing, or if
     *                               there are not enough funds available in the account
     */
    public B send(NanoAccount destination, NanoAmount amount) throws WalletActionException {
        return processBlock(() -> walletAccount.createSend(destination, amount));
    }
    
    /**
     * Attempts to send the entire balance to the specified account, returning an empty value if there are no remaining
     * funds to send (account has zero balance).
     *
     * <p>This method will not receive/send any pending blocks; that can be done by calling {@link #receiveAll()}
     * prior to sending funds.</p>
     *
     * <p>Calling this method will construct and sign a new block, generate the appropriate work for it, and publish the
     * block to the network via RPC.</p>
     *
     * @param destination the account where the funds should be sent
     * @return the generated and published {@code send} block, or empty if the account has no funds to send
     * @throws WalletActionException if an error occurs with the RPC query, work generation or block processing
     */
    public Optional<B> sendAll(NanoAccount destination) throws WalletActionException {
        return processBlockOptional(() -> walletAccount.createSendAll(destination));
    }
    
    /**
     * Receives the specified pending {@code send} block.
     *
     * <p>Calling this method will construct and sign a new block, generate the appropriate work for it, and publish the
     * block to the network via RPC.</p>
     *
     * @param sourceHash the hash of the pending {@code send} block
     * @return the generated and published {@code receive} block
     * @throws WalletActionException if an error occurs with the RPC queries, work generation, block processing, or the
     *                               specified block could not be found in the ledger
     *
     * @see #receiveAll()
     * @see #receiveBatch(int)
     */
    public B receive(HexData sourceHash) throws WalletActionException {
        if (sourceHash == null)
            throw new IllegalArgumentException("Source hash cannot be null.");
        
        lock.lock();
        try {
            ResponseBlockInfo pendingBlockInfo;
            try {
                pendingBlockInfo = rpcClient.processRequest(new RequestBlockInfo(sourceHash.toHexString()));
            } catch (RpcEntityNotFoundException e) {
                throw new WalletActionException("Source block \"" + sourceHash + "\" could not be found.", e);
            } catch (RpcException | IOException e) {
                throw wrapRpcException("Failed to retrieve pending block info.", e);
            }
            if (!pendingBlockInfo.isConfirmed()) {
                throw new WalletActionException("Source block is unconfirmed.");
            } else if (pendingBlockInfo.getAmount() == null ||
                    !pendingBlockInfo.getContents().getIntent().isSendFunds().boolLenient()) {
                throw new WalletActionException("Specified block is not a send block.");
            }
            return receive(sourceHash, pendingBlockInfo.getAmount());
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Attempts to receive a batch of pending blocks of at least {@code 0.000001 NANO} in value, receiving no more than
     * {@code count} blocks (highest valued blocks are processed first).
     *
     * <p>Calling this method will construct and sign a set of new blocks, generate the appropriate work for them, and
     * publish the blocks to the network via RPC.</p>
     *
     * @param count the maximum number of blocks to receive in this batch
     * @return a set containing the generated and published {@code receive} blocks
     * @throws WalletActionException if an error occurs with the RPC queries, work generation or block processing
     *
     * @see #receiveAll()
     */
    public Set<B> receiveBatch(int count) throws WalletActionException {
        return receiveBatch(count, DEFAULT_THRESHOLD);
    }
    
    /**
     * Attempts to receive a batch of pending blocks of a value greater than or equal to the specified threshold
     * amount, receiving no more than {@code count} blocks (highest valued blocks are processed first).
     *
     * <p>Calling this method will construct and sign a set of new blocks, generate the appropriate work for them, and
     * publish the blocks to the network via RPC.</p>
     *
     * @param count     the maximum number of blocks to receive in this batch
     * @param threshold the minimum amount threshold
     * @return a set containing the generated and published {@code receive} blocks
     * @throws WalletActionException if an error occurs with the RPC queries, work generation or block processing
     *
     * @see #receiveAll(NanoAmount)
     */
    public Set<B> receiveBatch(int count, NanoAmount threshold) throws WalletActionException {
        if (count < 0) throw new IllegalArgumentException("Batch count must be zero or higher.");
        if (count == 0) return Collections.emptySet();
        if (threshold == null) threshold = NanoAmount.ZERO;
        
        lock.lock();
        try {
            // Fetch pending blocks
            ResponsePending pending;
            try {
                pending = rpcClient.processRequest(new RequestPending(
                        getAccount().toAddress(), count, threshold.getAsRaw(), false, true, true));
            } catch (RpcException | IOException e) {
                throw wrapRpcException("Failed to retrieve pending blocks.", e);
            }
            // Receive blocks
            Set<B> published = new HashSet<>(pending.getPendingBlocks().size());
            for (Map.Entry<HexData, ResponsePending.PendingBlock> block : pending.getPendingBlocks().entrySet()) {
                published.add(receive(block.getKey(), block.getValue().getAmount()));
            }
            return published;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Attempts to receive all pending blocks of at least {@code 0.000001 NANO} in value.
     *
     * <p>Calling this method will construct and sign a set of new blocks, generate the appropriate work for them, and
     * publish the blocks to the network via RPC.</p>
     *
     * <p><strong>Note:</strong> if a large amount of transactions are pending, or an attacker continues to send funds
     * to this account, this method may block and continue indefinitely. Receive operations are performed in small
     * batches to allow other operations to proceed between each batch.</p>
     *
     * @return a set containing the generated and published {@code receive} blocks
     * @throws WalletActionException if an error occurs with the RPC queries, work generation or block processing
     *
     * @see #receiveBatch(int)
     */
    public Set<B> receiveAll() throws WalletActionException {
        return receiveAll(DEFAULT_THRESHOLD);
    }
    
    /**
     * Attempts to receive all pending blocks of a value greater than or equal to the specified threshold amount.
     *
     * <p>Calling this method will construct and sign a set of new blocks, generate the appropriate work for them, and
     * publish the blocks to the network via RPC.</p>
     *
     * <p><strong>Note:</strong> if a large amount of transactions are pending, or an attacker continues to send funds
     * to this account, this method may block and continue indefinitely. Receive operations are performed in small
     * batches to allow other operations to proceed between each batch.</p>
     *
     * @param threshold the minimum amount threshold
     * @return a set containing the generated and published {@code receive} blocks
     * @throws WalletActionException if an error occurs with the RPC queries, work generation or block processing
     *
     * @see #receiveBatch(int, NanoAmount)
     */
    public Set<B> receiveAll(NanoAmount threshold) throws WalletActionException {
        Set<B> batch, published = new HashSet<>();
        do {
            batch = receiveBatch(RECEIVE_BATCH_SIZE, threshold);
            published.addAll(batch);
        } while (batch.size() >= RECEIVE_BATCH_SIZE);
        return published;
    }
    
    private B receive(HexData sourceHash, NanoAmount amount) throws WalletActionException {
        return processBlock(() -> walletAccount.createReceive(sourceHash, amount));
    }
    
    /**
     * Changes the representative of the account to the specified representative address.
     *
     * <p>Calling this method will construct and sign a new block, generate the appropriate work for it, and publish the
     * block to the network via RPC.</p>
     *
     * @param representative the new representative
     * @return the generated and published representative change block, or empty if the representative is already set
     *         to the specified account
     * @throws WalletActionException if an error occurs with the RPC query, work generation or block processing
     */
    public Optional<B> changeRepresentative(NanoAccount representative) throws WalletActionException {
        return processBlockOptional(() -> walletAccount.createChange(representative));
    }
    
    
    private Optional<B> processBlockOptional(Supplier<Optional<B>> blockSupplier)
            throws WalletActionException {
        return Optional.ofNullable(processBlock(() -> blockSupplier.get().orElse(null)));
    }
    
    private B processBlock(Supplier<B> blockSupplier) throws WalletActionException {
        lock.lock();
        try {
            initState();
            for (int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++) {
                // Create block
                B block = blockSupplier.get();
                if (block == null)
                    return null;
                try {
                    // Publish block to network
                    rpcClient.processRequest(new RequestProcess(block, false, false));
                    walletAccount.commitState();
                    return block;
                } catch (RpcExternalException e) {
                    // Refresh cached state and retry
                    if (!refreshState()) {
                        throw e; // State was already up to date, non-state error occurred
                    }
                }
            }
            throw new WalletActionException("Account state outdated, retried too many times. " +
                    "Is the account being concurrently accessed?");
        } catch (BlockFactory.CreationException e) {
            throw new WalletActionException(e.getMessage(), e);
        } catch (RpcException | IOException e) {
            throw wrapRpcException("Block rejected by node: " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }
    
    private AccountState initState() throws WalletActionException {
        if (!hasRetrievedState) {
            lock.lock();
            try {
                if (!hasRetrievedState) {
                    refreshState();
                }
            } finally {
                lock.unlock();
            }
        }
        return walletAccount.getState();
    }
    
    private WalletActionException wrapRpcException(String message, Exception e) {
        if (e instanceof RpcException) {
            return new WalletActionException(message, e);
        } else if (e instanceof IOException) {
            return new WalletActionException("Connection error with RPC client.", e);
        } else {
            return new WalletActionException("Unexpected exception thrown.", e);
        }
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalRpcWalletAccount)) return false;
        LocalRpcWalletAccount<?> that = (LocalRpcWalletAccount<?>)o;
        return Objects.equals(rpcClient, that.rpcClient)
                && Objects.equals(walletAccount, that.walletAccount);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(rpcClient, walletAccount);
    }
    
}
