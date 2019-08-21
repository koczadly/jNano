package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;


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
    
    
    public BigInteger getQuorumDelta() {
        return quorumDelta;
    }
    
    public double getOnlineQuorumPercent() {
        return onlineQuorumPercent;
    }
    
    public BigInteger getOnlineQuorumMinimum() {
        return onlineQuorumMinimum;
    }
    
    public BigInteger getOnlineStakeTotal() {
        return onlineStakeTotal;
    }
    
    public BigInteger getPeersStakeTotal() {
        return peersStakeTotal;
    }
    
    public BigInteger getPeersStakeRequired() {
        return peersStakeRequired;
    }
    
    public List<PeerInfo> getPeers() {
        return peers;
    }
    
    
    public static class PeerInfo {
        @Expose @SerializedName("account")
        private String account;
    
        @Expose @SerializedName("ip")
        private String ip;
    
        @Expose @SerializedName("weight")
        private BigInteger weight;
    
    
        public String getAccount() {
            return account;
        }
    
        public String getIp() {
            return ip;
        }
    
        public BigInteger getWeight() {
            return weight;
        }
    }
    
}
