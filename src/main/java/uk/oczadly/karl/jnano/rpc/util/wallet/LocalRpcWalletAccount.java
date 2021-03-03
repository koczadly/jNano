/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.util.wallet;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockBuilder;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class provides a set of methods for performing wallet actions on an account, without sending the private key
 * to an external RPC node.
 *
 * <p>All of the methods in this class are secure, and will not send any private keys over to the RPC server. This
 * should be used when connecting via a third-party RPC provider. The class is also thread-safe, although use as a
 * single-threaded execution should be preferred.</p>
 *
 * <p>Due to the asynchronous nature of the cryptocurrency, you should <em>not</em> use multiple instances
 * representing the same account, nor should you use the account on another wallet or system at the same time. Doing
 * so can result in failures with transactions.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 *   // Construct the specification
 *   RpcWalletSpecification spec = RpcWalletSpecification.builder()
 *           .rpcClient(RpcServiceProviders.nanex())
 *           .defaultRepresentative("nano_3caprkc56ebsaakn4j4n7g9p8h358mycfjcyzkrfw1nai6prbyk8ihc5yjjk")
 *           .workGenerator(new OpenCLWorkGenerator())
 *           .build();
 *
 *   // Construct account object
 *   HexData privateKey = new HexData("183A1DEDCA9CD37029456C8A2ED31460A0E9A8D18032676010AC11B02A442417");
 *   LocalRpcWalletAccount account = new LocalRpcWalletAccount(spec, privateKey);
 *   System.out.printf("Using account %s%n", account.getAddress());
 *
 *   // Receive pending funds
 *   account.receiveAllPending();
 *   System.out.printf("Balance: %s%n", account.getBalance());
 *
 *   // Send funds
 *   HexData hash = account.sendFunds(
 *           NanoAccount.parseAddress("nano_34prihdxwz3u4ps8qjnn14p7ujyewkoxkwyxm3u665it8rg5rdqw84qrypzk"),
 *           NanoAmount.valueOfNano("0.0001"));
 *   System.out.printf("Send block hash: %s%n", hash);
 * }</pre>
 */
public class LocalRpcWalletAccount {
    
    private static final NanoAmount DEFAULT_THRESHOLD = NanoAmount.valueOfRaw("1000000000000000000000000");
    private static final int RECEIVE_BATCH_SIZE = 15;
    
    private final RpcWalletSpecification spec;
    private final HexData privateKey;
    private final NanoAccount account;
    private final Lock lock = new ReentrantLock(true);
    private volatile State state;
    
    
    /**
     * Constructs a new local RPC wallet account.
     * @param spec       the wallet specification
     * @param privateKey the private key of the account
     */
    public LocalRpcWalletAccount(RpcWalletSpecification spec, HexData privateKey) {
        if (spec == null) throw new IllegalArgumentException("Wallet specification cannot be null.");
        if (privateKey == null) throw new IllegalArgumentException("Private key cannot be null.");
        
        this.spec = spec;
        this.privateKey = privateKey;
        this.account = NanoAccount.fromPrivateKey(privateKey, spec.getAddressPrefix());
    }
    
    
    /**
     * Returns the address of this account.
     * @return the address of this account
     */
    public NanoAccount getAddress() {
        return account;
    }
    
    @Override
    public String toString() {
        return account.toString();
    }
    
    
    /**
     * Returns the current balance of this account. This does not include the pending amount.
     * @return the current account balance
     * @throws WalletActionException if an error occurs when retrieving the account state
     */
    public NanoAmount getBalance() throws WalletActionException {
        return getState().balance;
    }
    
    /**
     * Return the hash of the current frontier block of this account.
     *
     * @return the current account frontier block hash
     * @throws WalletActionException if an error occurs when retrieving the account state
     */
    public Optional<HexData> getFrontierHash() throws WalletActionException {
        return Optional.ofNullable(getState().frontier);
    }
    
    /**
     * Attempts to send an amount of funds to the specified account.
     *
     * @param destination the destination account
     * @param amount      the amount of funds to send to the account
     * @return the generated and published {@code send} block
     * @throws WalletActionException if an error occurs when sending the funds
     */
    public Block sendFunds(NanoAccount destination, NanoAmount amount) throws WalletActionException {
        if (amount.equals(NanoAmount.ZERO))
            throw new IllegalArgumentException("Amount must be greater than zero.");
        
        lock.lock();
        try {
            // Retrieve latest state
            State state = getState();
            if (!state.hasBlock())
                throw new WalletActionException("Account doesn't have enough funds (no open block).");
            if (state.balance.compareTo(amount) < 0)
                throw new WalletActionException("Account doesn't have enough funds.");
            
            // Create block
            return processBlock(newBlock(state)
                    .setSubtype(StateBlockSubType.SEND)
                    .setBalance(state.balance.subtract(amount))
                    .setLink(destination));
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Attempts to send <em>all</em> of the balance to the specified account.
     *
     * <p>This method will not receive/send any pending blocks; that can be done by calling {@link #receiveAllPending()}
     * prior to sending funds.</p>
     *
     * @param destination the destination account
     * @return the generated and published {@code send} block, or empty if the account has no funds to send
     * @throws WalletActionException if an error occurs when sending the funds
     */
    public Optional<Block> sendAllFunds(NanoAccount destination) throws WalletActionException {
        lock.lock();
        try {
            // Retrieve latest state
            State state = getState();
            if (state.balance.equals(NanoAmount.ZERO))
                return Optional.empty();
            
            // Create block
            return Optional.of(processBlock(newBlock(state)
                    .setSubtype(StateBlockSubType.SEND)
                    .setBalance(NanoAmount.ZERO)
                    .setLink(destination)));
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Attempts to receive the specified pending block.
     *
     * @param hash the hash of the pending block
     * @return the generated and published {@code receive} block
     * @throws WalletActionException if an error occurs when receiving the pending block
     */
    public Block receivePending(HexData hash) throws WalletActionException {
        lock.lock();
        try {
            // Retrieve pending block info
            ResponseBlockInfo pendingBlockInfo;
            try {
                pendingBlockInfo = spec.getRpcClient().processRequest(new RequestBlockInfo(hash.toHexString()));
            } catch (IOException e) {
                throw new WalletActionException("Connection error with RPC client.", e);
            } catch (RpcException e) {
                throw new WalletActionException("Couldn't retrieve pending block info.", e);
            }
            // Receive block
            return receivePending(hash, pendingBlockInfo.getAmount());
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Attempts to receive all pending blocks of at least {@code 0.000001 NANO} in value.
     *
     * <p>Note: if a large amount of transactions are pending, or an attacker continues to send funds to this
     * account, this method may block and continue indefinitely. Receive operations are performed in batches of
     * {@value #RECEIVE_BATCH_SIZE} receives to allow other operations to continue between batches.</p>
     *
     * @return a set containing the generated and published {@code receive} blocks
     * @throws WalletActionException if an error occurs when receiving the pending blocks
     */
    public Set<Block> receiveAllPending() throws WalletActionException {
        return receiveAllPending(DEFAULT_THRESHOLD);
    }
    
    /**
     * Attempts to receive all pending blocks with a value greater than or equal to the specified threshold amount.
     *
     * <p>Note: if a large amount of transactions are pending, or an attacker continues to send funds to this
     * account, this method may block and continue indefinitely. Receive operations are performed in batches of
     * {@value #RECEIVE_BATCH_SIZE} receives to allow other operations to continue between batches.</p>
     *
     * @param threshold the minimum amount threshold
     * @return a set containing the generated and published {@code receive} blocks
     * @throws WalletActionException if an error occurs when receiving the pending blocks
     */
    public Set<Block> receiveAllPending(NanoAmount threshold) throws WalletActionException {
        Set<Block> published = new HashSet<>();
        while (true) {
            lock.lock();
            try {
                // Retrieve pending blocks list
                ResponsePending pendingBlocks;
                try {
                    pendingBlocks = spec.getRpcClient().processRequest(new RequestPending(
                            account.toAddress(), RECEIVE_BATCH_SIZE, threshold.getAsRaw(), false, true, true));
                } catch (IOException e) {
                    throw new WalletActionException("Connection error with RPC client.", e);
                } catch (RpcException e) {
                    throw new WalletActionException("Couldn't retrieve pending blocks list.", e);
                }
                
                // Receive pending blocks
                Set<Map.Entry<HexData, ResponsePending.PendingBlock>> blocks =
                        pendingBlocks.getPendingBlocks().entrySet();
                if (blocks.isEmpty()) break; // No more blocks
                for (Map.Entry<HexData, ResponsePending.PendingBlock> pendingBlock : blocks) {
                    published.add(receivePending(pendingBlock.getKey(), pendingBlock.getValue().getAmount()));
                }
            } finally {
                lock.unlock();
            }
        }
        return published;
    }
    
    private Block receivePending(HexData hash, NanoAmount amount) throws WalletActionException {
        lock.lock();
        try {
            // Create block
            State state = getState();
            return processBlock(newBlock(state)
                    .setSubtype(state.hasBlock() ? StateBlockSubType.RECEIVE : StateBlockSubType.OPEN)
                    .setLink(hash)
                    .setBalance(state.balance.add(amount)));
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Changes the representative of the account to the specified representative address.
     *
     * @param representative the new representative
     * @return the generated and published representative change block, or empty if the representative is already set
     *         to the specified account
     * @throws WalletActionException if an error occurs when changing representative
     */
    public Optional<Block> changeRepresentative(NanoAccount representative) throws WalletActionException {
        lock.lock();
        try {
            // Retrieve latest state
            State state = getState();
            if (!state.hasBlock())
                throw new WalletActionException("Account needs to publish first block before changing rep.");
            if (state.representative.equalsIgnorePrefix(representative))
                return Optional.empty(); // Already set
    
            // Create block
            StateBlockBuilder sb = newBlock(state)
                    .setSubtype(StateBlockSubType.CHANGE)
                    .setRepresentative(representative);
            return Optional.of(processBlock(sb));
        } finally {
            lock.unlock();
        }
    }
    
    private StateBlock processBlock(StateBlockBuilder blockBuilder) throws WalletActionException {
        lock.lock();
        try {
            StateBlock block = blockBuilder.buildAndSign(privateKey, spec.getAddressPrefix());
            spec.getRpcClient().processRequest(new RequestProcess(block, false)); // Publish
            state.update(block.getBalance(), block.getRepresentative(), block.getHash()); // Update state
            return block;
        } catch (StateBlockBuilder.BlockCreationException e) {
            throw new WalletActionException("Couldn't construct block.", e);
        } catch (IOException e) {
            throw new WalletActionException("Connection error with RPC client.", e);
        } catch (RpcException e) {
            // Refresh state if invalid previous
            if (e instanceof RpcExternalException) {
                String message = ((RpcExternalException)e).getRawMessage();
                if (message.equals("Fork") || message.equals("Gap previous block")) {
                    updateState();
                    throw new WalletActionException("Invalid previous hash, state has been refreshed.", e);
                }
            }
            throw new WalletActionException("Couldn't publish block.", e);
        } finally {
            lock.unlock();
        }
    }
    
    private StateBlockBuilder newBlock(State state) {
        return new StateBlockBuilder()
                .setAccount(account)
                .setBalance(state.balance)
                .setPreviousHash(state.frontier)
                .setRepresentative(state.representative)
                .generateWork(spec.getWorkGenerator());
    }
    
    
    private State getState() throws WalletActionException {
        if (state == null) {
            lock.lock();
            try {
                if (state == null)
                    return updateState();
            } finally {
                lock.unlock();
            }
        }
        return state;
    }
    
    private State updateState() throws WalletActionException {
        lock.lock();
        try {
            ResponseAccountInfo info = spec.getRpcClient().processRequest(new RequestAccountInfo(account.toAddress()));
            if (state == null) state = new State();
            state.update(info.getBalanceConfirmed(), info.getRepresentativeAccount(), info.getFrontierBlockHash());
        } catch (RpcEntityNotFoundException e) {
            // Account doesn't exist yet
            if (state == null) state = new State();
            state.update(NanoAmount.ZERO, spec.getDefaultRepresentative(), null);
        } catch (IOException e) {
            throw new WalletActionException("Connection error with RPC client.", e);
        } catch (RpcException e) {
            throw new WalletActionException("Couldn't retrieve account state information.", e);
        } finally {
            lock.unlock();
        }
        return state;
    }
    
    
    private static class State {
        private volatile NanoAmount balance;
        private volatile NanoAccount representative;
        private volatile HexData frontier;
        
        public void update(NanoAmount balance, NanoAccount representative, HexData frontier) {
            this.balance = balance;
            this.representative = representative;
            this.frontier = frontier;
        }
        
        public boolean hasBlock() {
            return frontier != null;
        }
    }
    
}
