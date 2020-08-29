/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to initialize bootstrap from a given block hash.
 * <br>Calls the RPC command {@code bootstrap_lazy}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#bootstrap_lazy">Official RPC documentation</a>
 */
public class RequestBootstrapLazy extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    @Expose @SerializedName("force")
    private final Boolean force;
    
    @Expose @SerializedName("id")
    private String trackingId;
    
    
    /**
     * @param blockHash the block's hash
     */
    public RequestBootstrapLazy(String blockHash) {
        this(blockHash, null);
    }
    
    /**
     * @param blockHash the block's hash
     * @param force     (optional) whether all current bootstraps should be forcefully closed
     */
    public RequestBootstrapLazy(String blockHash, Boolean force) {
        this(blockHash, force, null);
    }
    
    /**
     * @param blockHash  the block's hash
     * @param force      (optional) whether all current bootstraps should be forcefully closed
     * @param trackingId (optional) the tracking ID for this request
     */
    public RequestBootstrapLazy(String blockHash, Boolean force, String trackingId) {
        super("bootstrap_lazy", ResponseSuccessful.class);
        this.blockHash = blockHash;
        this.force = force;
        this.trackingId = trackingId;
    }
    
    
    /**
     * @return the requested block hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return whether all current bootstraps should be forcefully closed
     */
    public Boolean getForce() {
        return force;
    }
    
    /**
     * @return the tracking identifier for this request
     */
    public String getTrackingId() {
        return trackingId;
    }
    
}
