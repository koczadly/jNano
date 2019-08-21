package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseConfirmationQuorum;

public class RequestConfirmationQuorum extends RpcRequest<ResponseConfirmationQuorum> {
    
    @Expose @SerializedName("peer_details")
    private final boolean peerDetails = true;
    
    @Expose @SerializedName("peers_stake_required")
    private final boolean peersStakeRequired = true;
    
    
    public RequestConfirmationQuorum() {
        super("confirmation_quorum", ResponseConfirmationQuorum.class);
    }
    
}
