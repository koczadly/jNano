/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen.policy;

import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.request.node.RequestActiveDifficulty;
import uk.oczadly.karl.jnano.rpc.response.ResponseActiveDifficulty;

import java.io.IOException;

/**
 * A dynamic work difficulty policy retrieved from an external node. This policy will retrieve the thresholds via the
 * {@code active_difficulty} RPC command.
 *
 * <p>Note that the difficulty retrieval methods should not vary dynamically, and only the {@link #multiplier()} value
 * will change with network load. All difficulty retrievals should be multiplied by the multiplier.</p>
 *
 * <p>The values will be internally cached for a short period, unless specified otherwise in the constructors.</p>
 *
 * <p>Note that the methods will throw a {@link DifficultyRetrievalException} if the node returns an
 * {@link RpcException} or an {@link IOException} occurs.</p>
 *
 * @see BoundedWorkDifficultyPolicy
 */
public class NodeWorkDifficultyPolicy implements WorkDifficultyPolicy {
    
    private static final RequestActiveDifficulty REQUEST_DIFF = new RequestActiveDifficulty(false);
    
    private final RpcQueryNode rpc;
    private final int cacheMillis;
    
    private volatile WorkDifficultyPolicy cachedDifficulties;
    private volatile long cachedTime;
    
    /**
     * Constructs a new {@code NodeWorkDifficultyPolicy} with the given {@link RpcQueryNode} instance.
     *
     * <p>This method will cache the difficulty and multiplier values for 10 seconds before refreshing the internal
     * cache.</p>
     *
     * @param rpc the rpc endpoint
     */
    public NodeWorkDifficultyPolicy(RpcQueryNode rpc) {
        this(rpc, 10000);
    }
    
    /**
     * Constructs a new {@code NodeWorkDifficultyPolicy} with the given {@link RpcQueryNode} instance.
     *
     * <p>This method will cache the difficulty and multiplier values for the specified time period before refreshing
     * the internal cache.</p>
     *
     * @param rpc         the rpc endpoint
     * @param cacheMillis the cache expiry duration in milliseconds, or {@code 0} for no caching
     */
    public NodeWorkDifficultyPolicy(RpcQueryNode rpc, int cacheMillis) {
        if (rpc == null)
            throw new IllegalArgumentException("RPC object cannot be null.");
        if (cacheMillis < 0)
            throw new IllegalArgumentException("Cache duration must be zero or positive.");
        this.rpc = rpc;
        this.cacheMillis = cacheMillis;
    }
    
    
    @Override
    public WorkDifficulty forBlock(Block block) throws DifficultyRetrievalException {
        return fetchDifficulties().forBlock(block);
    }
    
    @Override
    public WorkDifficulty forAny() throws DifficultyRetrievalException {
        return fetchDifficulties().forAny();
    }
    
    @Override
    public double multiplier() throws DifficultyRetrievalException {
        return fetchDifficulties().multiplier();
    }
    
    
    private synchronized WorkDifficultyPolicy fetchDifficulties() throws DifficultyRetrievalException {
        if (hasCacheExpired()) {
            try {
                ResponseActiveDifficulty res = rpc.processRequest(REQUEST_DIFF);
                
                cachedTime = System.currentTimeMillis();
                cachedDifficulties = new ConstantDifficultyPolicyV2(
                        res.getNetworkMinimum(),
                        res.getNetworkReceiveMinimum(),
                        res.getMultiplier().doubleValue());
            } catch (RpcException e) {
                throw new DifficultyRetrievalException(e);
            } catch (IOException e) {
                throw new DifficultyRetrievalException("An exception occurred with remote node communication.", e);
            }
        }
        return cachedDifficulties;
    }
    
    private boolean hasCacheExpired() {
        return cacheMillis == 0 || cachedDifficulties == null
                || (System.currentTimeMillis() - cachedTime) > cacheMillis;
    }
    
}
