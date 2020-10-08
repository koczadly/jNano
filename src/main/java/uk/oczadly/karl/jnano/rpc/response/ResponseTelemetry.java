/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

/**
 * This response class contains statistics regarding the node.
 */
public class ResponseTelemetry extends RpcResponse {
    
    @Expose @SerializedName("block_count") private long blockCount;
    @Expose @SerializedName("cemented_count") private long cementedCount;
    @Expose @SerializedName("unchecked_count") private long uncheckedCount;
    @Expose @SerializedName("account_count") private long accountCount;
    @Expose @SerializedName("bandwidth_cap") private int bandwidthCap;
    @Expose @SerializedName("peer_count") private int peerCount;
    @Expose @SerializedName("protocol_version") private int protocolVersion;
    @Expose @SerializedName("uptime") private long uptime;
    @Expose @SerializedName("genesis_block") private HexData genesisBlockHash;
    @Expose @SerializedName("major_version") private int majorVersion;
    @Expose @SerializedName("minor_version") private int minorVersion;
    @Expose @SerializedName("patch_version") private int patchVersion;
    @Expose @SerializedName("pre_release_version") private int preReleaseVersion;
    @Expose @SerializedName("maker") private int maker;
    @Expose @SerializedName("timestamp") private long timestamp;
    @Expose @SerializedName("active_difficulty") private WorkDifficulty activeDifficulty;
    
    
    public long getBlockCount() {
        return blockCount;
    }
    
    public long getCementedCount() {
        return cementedCount;
    }
    
    public long getUncheckedCount() {
        return uncheckedCount;
    }
    
    public long getAccountCount() {
        return accountCount;
    }
    
    public int getBandwidthCap() {
        return bandwidthCap;
    }
    
    public int getPeerCount() {
        return peerCount;
    }
    
    public int getProtocolVersion() {
        return protocolVersion;
    }
    
    public long getUptime() {
        return uptime;
    }
    
    public HexData getGenesisBlockHash() {
        return genesisBlockHash;
    }
    
    public int getMajorVersion() {
        return majorVersion;
    }
    
    public int getMinorVersion() {
        return minorVersion;
    }
    
    public int getPatchVersion() {
        return patchVersion;
    }
    
    public int getPreReleaseVersion() {
        return preReleaseVersion;
    }
    
    public int getMaker() {
        return maker;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public WorkDifficulty getActiveDifficulty() {
        return activeDifficulty;
    }
}
