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
 * This request class is used to initialize bootstrap connection to random peers.
 * <br>Calls the RPC command {@code bootstrap_any}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#bootstrap_any">Official RPC documentation</a>
 */
public class RequestBootstrapAny extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("force")
    private Boolean forceClose;
    
    @Expose @SerializedName("id")
    private String trackingId;
    
    
    public RequestBootstrapAny() {
        this(null);
    }
    
    /**
     * @param forceClose (optional) whether current bootstraps should be force-closed
     */
    public RequestBootstrapAny(Boolean forceClose) {
        this(forceClose, null);
    }
    
    /**
     * @param forceClose (optional) whether current bootstraps should be force-closed
     * @param trackingId (optional) the tracking ID for this request
     */
    public RequestBootstrapAny(Boolean forceClose, String trackingId) {
        super("bootstrap_any", ResponseSuccessful.class);
        this.forceClose = forceClose;
        this.trackingId = trackingId;
    }
    
    
    /**
     * @return whether current bootstraps should be force-closed
     */
    public Boolean getForceClose() {
        return forceClose;
    }
    
    /**
     * @return the tracking identifier for this request
     */
    public String getTrackingId() {
        return trackingId;
    }
    
}
