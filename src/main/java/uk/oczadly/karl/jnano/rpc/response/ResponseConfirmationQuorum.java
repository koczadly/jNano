/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;

import java.util.List;

/**
 * This response class contains information about the current confirmation requirements.
 */
public class ResponseConfirmationQuorum extends RpcResponse {
    
    @Expose @SerializedName("quorum_delta")
    private NanoAmount quorumDelta;
    
    @Expose @SerializedName("online_weight_quorum_percent")
    private double onlineQuorumPercent;
    
    @Expose @SerializedName("online_weight_minimum")
    private NanoAmount onlineQuorumMinimum;
    
    @Expose @SerializedName("online_stake_total")
    private NanoAmount onlineStakeTotal;
    
    @Expose @SerializedName("peers_stake_total")
    private NanoAmount peersStakeTotal;
    
    @Expose @SerializedName("peers_stake_required")
    private NanoAmount peersStakeRequired;
    
    @Expose @SerializedName("peers")
    private List<PeerInfo> peers;
    
    
    /**
     * @return the total voting weight required for a block to be confirmed
     */
    public NanoAmount getQuorumDelta() {
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
    public NanoAmount getOnlineQuorumMinimum() {
        return onlineQuorumMinimum;
    }
    
    /**
     * @return the total online stake from representatives
     */
    public NanoAmount getOnlineStakeTotal() {
        return onlineStakeTotal;
    }
    
    /**
     * @return the total online stake from peers
     */
    public NanoAmount getPeersStakeTotal() {
        return peersStakeTotal;
    }
    
    /**
     * @return the required voting weight from all peers
     */
    public NanoAmount getPeersStakeRequired() {
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
        private NanoAmount weight;
        
        
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
        public NanoAmount getWeight() {
            return weight;
        }
    }
    
}
