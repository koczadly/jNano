/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseConfirmationQuorum;

/**
 * This request class is used to request information about the network state.
 * <br>Calls the RPC command {@code confirmation_quorum}, and returns a {@link ResponseConfirmationQuorum} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#confirmation_quorum">Official RPC documentation</a>
 */
public class RequestConfirmationQuorum extends RpcRequest<ResponseConfirmationQuorum> {
    
    @Expose @SerializedName("peer_details")
    private final boolean peerDetails = true;
    
    @Expose @SerializedName("peers_stake_required")
    private final boolean peersStakeRequired = true;
    
    
    public RequestConfirmationQuorum() {
        super("confirmation_quorum", ResponseConfirmationQuorum.class);
    }
    
}
