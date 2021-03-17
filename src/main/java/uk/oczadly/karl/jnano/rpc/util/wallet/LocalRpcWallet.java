/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.util.wallet;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.util.WalletUtil;
import uk.oczadly.karl.jnano.util.blockproducer.BlockProducer;

import java.util.*;

/**
 * This class can be used to generate multiple instances of {@link LocalRpcWalletAccount} objects based on a provided
 * seed value.
 */
public class LocalRpcWallet {
    
    private final RpcQueryNode rpcClient;
    private final BlockProducer signer;
    private final HexData seed;
    private volatile int index;
    private final Set<LocalRpcWalletAccount> accounts = new HashSet<>();
    private final LinkedHashSet<LocalRpcWalletAccount> unusedAccounts = new LinkedHashSet<>();
    
    
    /**
     * Constructs a new local RPC wallet, starting at seed index 0.
     * @param seed       the seed of the wallet
     * @param rpcClient  the RPC client to be used by the {@link LocalRpcWalletAccount} objects
     * @param signer     the block signer to be used by the {@link LocalRpcWalletAccount} objects
     */
    public LocalRpcWallet(HexData seed, RpcQueryNode rpcClient, BlockProducer signer) {
        this(seed, 0, rpcClient, signer);
    }
    
    /**
     * Constructs a new local RPC wallet.
     * @param seed         the seed of the wallet
     * @param initialIndex the starting seed index value (as an unsigned int)
     * @param rpcClient    the RPC client where requests will be sent to
     * @param signer       the block signer
     */
    public LocalRpcWallet(HexData seed, int initialIndex, RpcQueryNode rpcClient, BlockProducer signer) {
        if (seed == null) throw new IllegalArgumentException("Seed cannot be null.");
        if (rpcClient == null) throw new IllegalArgumentException("RPC client cannot be null.");
        if (signer == null) throw new IllegalArgumentException("Block signer cannot be null.");
        this.seed = seed;
        this.index = initialIndex;
        this.rpcClient = rpcClient;
        this.signer = signer;
    }
    
    
    /**
     * Returns the secret seed of the wallet.
     * @return the seed of the wallet
     */
    public final HexData getSeed() {
        return seed;
    }
    
    /**
     * Returns the current seed index of the next account.
     * @return the current seed index
     */
    public final int getAccountIndex() {
        return index;
    }
    
    /**
     * Returns the block signer used for created {@link LocalRpcWalletAccount} instances.
     * @return the block signer
     */
    public final BlockProducer getBlockSigner() {
        return signer;
    }
    
    /**
     * Returns the RPC client which will execute the remote wallet operations.
     * @return the RPC client
     */
    public final RpcQueryNode getRpcClient() {
        return rpcClient;
    }
    
    /**
     * Returns all the generated accounts in the wallet. A new account will be added each time {@link #createAccount()}
     * is invoked.
     * @return a set of generated accounts
     */
    public final synchronized Set<LocalRpcWalletAccount> getAccounts() {
        return Collections.unmodifiableSet(new HashSet<>(accounts));
    }
    
    /**
     * Returns an unused account, either by incrementing the seed index or retrieving it from the inactive accounts
     * pool (from calling {@link #freeAccount(LocalRpcWalletAccount)}).
     *
     * <p>Note that each account gets added to the internal active accounts list. When you are finished with an
     * account, you should call {@link #freeAccount(LocalRpcWalletAccount)} to remove the account from the list and
     * allow the account and object to be re-used.</p>
     *
     * @return an unused local wallet account
     */
    public final synchronized LocalRpcWalletAccount createAccount() {
        LocalRpcWalletAccount account;
        Iterator<LocalRpcWalletAccount> unusedIterator = unusedAccounts.iterator();
        if (unusedIterator.hasNext()) {
            // From unused accounts pool
            account = unusedIterator.next();
            unusedIterator.remove();
        } else {
            // From seed
            account = new LocalRpcWalletAccount(WalletUtil.deriveKeyFromSeed(seed, index++), rpcClient, signer);
        }
        if (!accounts.add(account))
            throw new IllegalStateException("No free accounts available.");
        return account;
    }
    
    /**
     * Frees the account for future re-use, removing it from the active accounts list. Calling {@link #createAccount()}
     * may return the same account object in the future.
     * @param account the account to free
     */
    public final synchronized void freeAccount(LocalRpcWalletAccount account) {
        if (accounts.remove(account))
            unusedAccounts.add(account);
    }
    
}
