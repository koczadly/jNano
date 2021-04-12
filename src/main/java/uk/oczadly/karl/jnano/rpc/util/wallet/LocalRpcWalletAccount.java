/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.util.wallet;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.StateBlockBuilder;
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
import uk.oczadly.karl.jnano.util.blockproducer.AccountState;
import uk.oczadly.karl.jnano.util.blockproducer.BlockAndState;
import uk.oczadly.karl.jnano.util.blockproducer.BlockProducer;

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
 * This should be used when connecting via a third-party RPC provider. The class is also thread safe, though use as a
 * single-threaded execution should be preferred.</p>
 *
 * <p>Due to the asynchronous nature of Nano, you should not use multiple instances representing the same account, nor
 * should you use the same account on another wallet, node or system at the same time — doing so can result in
 * unexpected transaction failures.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * RpcQueryNode rpcClient = RpcServiceProviders.nanex(); // Use nanex.cc public RPC API
 *
 * // Construct a block producer object with your configuration
 * BlockProducer blockProducer = new StateBlockProducer(
 *         BlockProducerSpecification.builder()
 *                 .defaultRepresentative("nano_3caprkc56ebsaakn4j4n7g9p8h358mycfjcyzkrfw1nai6prbyk8ihc5yjjk")
 *                 .workGenerator(new NodeWorkGenerator(rpcClient)) // Generate work on the node
 *                 .build()
 * );
 *
 * // Create account from private key
 * LocalRpcWalletAccount account = new LocalRpcWalletAccount(
 *         new HexData("183A1DEDCA9CD37029456C8A2ED31460A0E9A8D18032676010AC11B02A442417"), // Private key
 *         rpcClient, blockProducer); // Using our RPC client and BlockProducer defined above
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
 */
public class LocalRpcWalletAccount {
    
    private static final NanoAmount DEFAULT_THRESHOLD = NanoAmount.valueOfRawExponent(24);
    private static final int RECEIVE_BATCH_SIZE = 15;
    private static final int MAX_RETRY_ATTEMPTS = 3;
    
    private final RpcQueryNode rpcClient;
    private final NanoAccount account;
    private final HexData privateKey;
    private final BlockProducer blockProducer;
    private final Lock lock = new ReentrantLock(true);
    private volatile AccountState cachedState;
    private volatile boolean hasRetrievedState = false;
    
    
    /**
     * Constructs a new local RPC wallet account.
     * @param privateKey    the private key of the account
     * @param rpcClient     the RPC client where requests will be sent to
     * @param blockProducer the block producer
     */
    public LocalRpcWalletAccount(HexData privateKey, RpcQueryNode rpcClient, BlockProducer blockProducer) {
        if (privateKey == null) throw new IllegalArgumentException("Private key cannot be null.");
        if (rpcClient == null) throw new IllegalArgumentException("RPC client cannot be null.");
        if (blockProducer == null) throw new IllegalArgumentException("BlockProducer cannot be null.");
        this.privateKey = privateKey;
        this.account = NanoAccount.fromPrivateKey(privateKey, blockProducer.getSpecification().getAddressPrefix());
        this.rpcClient = rpcClient;
        this.blockProducer = blockProducer;
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
                ", blockProducer=" + getBlockProducer().getClass().getName() + '}';
    }
    
    
    /**
     * Forcefully refreshes the internal cached state of the account by calling the {@link RequestAccountInfo} RPC
     * query.
     *
     * <p>Most implementations should never have to call this method, as the state will automatically be retrieved or
     * updated when necessary through the other methods.</p>
     *
     * @throws WalletActionException if an error occurs with the RPC query
     *
     * @see #updateState(AccountState)
     */
    public void refreshState() throws WalletActionException {
        lock.lock();
        try {
            // Retrieve state from RPC
            ResponseAccountInfo accountInfo = rpcClient.processRequest(
                    new RequestAccountInfo(getAccount().toAddress()));
            updateState(AccountState.fromAccountInfo(accountInfo));
        } catch (RpcEntityNotFoundException e) {
            updateState(AccountState.UNOPENED); // Account hasn't been opened
        } catch (RpcException e) {
            throw new WalletActionException("Couldn't retrieve account state.", e);
        } catch (IOException e) {
            throw new WalletActionException("Connection error with RPC client.", e);
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Returns the current balance of this account, not include any pending amounts. Will return a value of zero if the
     * account hasn't been opened yet.
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
     * @param destination the destination account
     * @param amount      the amount of funds to send to the account
     * @return the generated and published {@code send} block
     * @throws WalletActionException if an error occurs with the RPC query, work generation, block processing, or if
     *                               there are not enough funds available in the account
     */
    public Block send(NanoAccount destination, NanoAmount amount) throws WalletActionException {
        return processBlock(() -> blockProducer
                .createSend(privateKey, cachedState, destination, amount));
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
     * @param destination the destination account
     * @return the generated and published {@code send} block, or empty if the account has no funds to send
     * @throws WalletActionException if an error occurs with the RPC query, work generation or block processing
     */
    public Optional<Block> sendAll(NanoAccount destination) throws WalletActionException {
        return processBlockOptional(() -> blockProducer
                .createSendAll(privateKey, cachedState, destination));
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
     */
    public Block receive(HexData sourceHash) throws WalletActionException {
        if (sourceHash == null)
            throw new IllegalArgumentException("Source hash cannot be null.");
        
        lock.lock();
        try {
            ResponseBlockInfo pendingBlockInfo;
            try {
                pendingBlockInfo = rpcClient.processRequest(new RequestBlockInfo(sourceHash.toHexString()));
            } catch (RpcEntityNotFoundException e) {
                throw new WalletActionException("Source block could not be found.", e);
            } catch (RpcException e) {
                throw new WalletActionException("Failed to retrieve pending block info.", e);
            } catch (IOException e) {
                throw new WalletActionException("Connection error with RPC client.", e);
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
     */
    public Set<Block> receiveBatch(int count) throws WalletActionException {
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
     */
    public Set<Block> receiveBatch(int count, NanoAmount threshold) throws WalletActionException {
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
            } catch (RpcException e) {
                throw new WalletActionException("Failed to retrieve pending blocks.", e);
            } catch (IOException e) {
                throw new WalletActionException("Connection error with RPC client.", e);
            }
            // Receive blocks
            Set<Block> published = new HashSet<>(pending.getPendingBlocks().size());
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
     */
    public Set<Block> receiveAll() throws WalletActionException {
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
     */
    public Set<Block> receiveAll(NanoAmount threshold) throws WalletActionException {
        Set<Block> batch, published = new HashSet<>();
        do {
            batch = receiveBatch(RECEIVE_BATCH_SIZE, threshold);
            published.addAll(batch);
        } while (batch.size() >= RECEIVE_BATCH_SIZE);
        return published;
    }
    
    private Block receive(HexData sourceHash, NanoAmount amount) throws WalletActionException {
        return processBlock(() -> blockProducer
                .createReceive(privateKey, cachedState, sourceHash, amount));
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
    public Optional<Block> changeRepresentative(NanoAccount representative) throws WalletActionException {
        return processBlockOptional(() -> blockProducer
                .createChangeRepresentative(privateKey, cachedState, representative));
    }
    
    
    private Optional<Block> processBlockOptional(Supplier<Optional<BlockAndState>> blockSupplier)
            throws WalletActionException {
        return Optional.ofNullable(processBlock(() -> blockSupplier.get().orElse(null)));
    }
    
    private Block processBlock(Supplier<BlockAndState> blockSupplier) throws WalletActionException {
        lock.lock();
        try {
            initState();
            for (int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++) {
                try {
                    // Create block
                    BlockAndState block = blockSupplier.get();
                    if (block == null)
                        return null;
                    // Publish block to network
                    rpcClient.processRequest(new RequestProcess(block.getBlock(), false, false));
                    cachedState = block.getState();
                    return block.getBlock();
                } catch (RpcExternalException e) {
                    if (e.getRawMessage().equals("Fork") || e.getRawMessage().equals("Gap previous block")) {
                        refreshState(); // Refresh state if invalid 'previous' and retry
                    } else {
                        throw e;
                    }
                }
            }
            throw new WalletActionException("Previous block was incorrect, retried too many times. " +
                    "Is the account being concurrently used elsewhere?");
        } catch (BlockProducer.BlockCreationException | StateBlockBuilder.BlockCreationException e) {
            throw new WalletActionException("Couldn't construct block.", e);
        } catch (IOException e) {
            throw new WalletActionException("Connection error with RPC client.", e);
        } catch (RpcException e) {
            throw new WalletActionException("Couldn't publish block: " + e.getMessage(), e);
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
        return cachedState;
    }
    
    private void updateState(AccountState state) {
        lock.lock();
        try {
            cachedState = state;
            hasRetrievedState = true;
        } finally {
            lock.unlock();
        }
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalRpcWalletAccount)) return false;
        LocalRpcWalletAccount that = (LocalRpcWalletAccount)o;
        return Objects.equals(rpcClient, that.rpcClient)
                && Objects.equals(blockProducer, that.blockProducer)
                && Objects.equals(privateKey, that.privateKey);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(account, rpcClient);
    }
    
}
