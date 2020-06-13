package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.math.BigInteger;
import java.util.List;

/**
 * This response class contains information about the current confirmation requirements.
 */
public class ResponseConfirmationQuorum extends RpcResponse {
    
    @Expose @SerializedName("quorum_delta")
    private BigInteger quorumDelta;
    
    @Expose @SerializedName("online_weight_quorum_percent")
    private double onlineQuorumPercent;
    
    @Expose @SerializedName("online_weight_minimum")
    private BigInteger onlineQuorumMinimum;
    
    @Expose @SerializedName("online_stake_total")
    private BigInteger onlineStakeTotal;
    
    @Expose @SerializedName("peers_stake_total")
    private BigInteger peersStakeTotal;
    
    @Expose @SerializedName("peers_stake_required")
    private BigInteger peersStakeRequired;
    
    @Expose @SerializedName("peers")
    private List<PeerInfo> peers;
    
    
    /**
     * @return the total voting weight required for a block to be confirmed
     */
    public BigInteger getQuorumDelta() {
        return quorumDelta;
    }
    
    /**
     * @return the percentage of online voting weight required to confirm a block (constant)
     */
    public double getOnlineQuorumPercent() {
        return onlineQuorumPercent;
    }
    
    /**
     * @return the minimum voting weight required (constant)
     */
    public BigInteger getOnlineQuorumMinimum() {
        return onlineQuorumMinimum;
    }
    
    /**
     * @return the total online stake from representatives
     */
    public BigInteger getOnlineStakeTotal() {
        return onlineStakeTotal;
    }
    
    /**
     * @return the total online stake from peers
     */
    public BigInteger getPeersStakeTotal() {
        return peersStakeTotal;
    }
    
    /**
     * @return the required voting weight from all peers
     */
    public BigInteger getPeersStakeRequired() {
        return peersStakeRequired;
    }
    
    /**
     * @return a list of online representative peers
     */
    public List<PeerInfo> getPeers() {
        return peers;
    }
    
    
    public static class PeerInfo {
        @Expose @SerializedName("account")
        private NanoAccount account;
        
        @Expose @SerializedName("ip")
        private String ip;
        
        @Expose @SerializedName("weight")
        private BigInteger weight;
        
        
        /**
         * @return the address of the representative
         */
        public NanoAccount getAccount() {
            return account;
        }
        
        /**
         * @return the IP address of the representative
         */
        public String getIp() {
            return ip;
        }
        
        /**
         * @return the voting weight of this representative
         */
        public BigInteger getWeight() {
            return weight;
        }
    }
    
}
