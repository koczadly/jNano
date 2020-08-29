/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

import java.net.InetAddress;
import java.time.Instant;

public class TopicMessageTelemetry {
    
    @Expose @SerializedName("block_count")
    private int blockCount;
    
    @Expose @SerializedName("cemented_count")
    private int cementedCount;
    
    @Expose @SerializedName("unchecked_count")
    private int uncheckedCount;
    
    @Expose @SerializedName("account_count")
    private int accountCount;
    
    @Expose @SerializedName("bandwidth_cap")
    private long bandwidthCap;
    
    @Expose @SerializedName("peer_count")
    private int peerCount;
    
    @Expose @SerializedName("protocol_version")
    private int protocolVersion;
    
    @Expose @SerializedName("uptime")
    private long uptime;
    
    @Expose @SerializedName("genesis_block")
    private String genesisBlock;
    
    @Expose @SerializedName("major_version")
    private int versionMajor;
    
    @Expose @SerializedName("minor_version")
    private int versionMinor;
    
    @Expose @SerializedName("patch_version")
    private int versionPatch;
    
    @Expose @SerializedName("pre_release_version")
    private int versionPreRelease;
    
    @Expose @SerializedName("maker")
    private int maker;
    
    @Expose @SerializedName("timestamp")
    private Instant timestamp;
    
    @Expose @SerializedName("active_difficulty")
    private WorkDifficulty activeDifficulty;
    
    @Expose @SerializedName("node_id")
    private NanoAccount nodeId;
    
    @Expose @SerializedName("signature")
    private String signature;
    
    @Expose @SerializedName("address")
    private InetAddress address;
    
    @Expose @SerializedName("port")
    private int port;
    
    
    public int getBlockCount() {
        return blockCount;
    }
    
    public int getCementedCount() {
        return cementedCount;
    }
    
    public int getUncheckedCount() {
        return uncheckedCount;
    }
    
    public int getAccountCount() {
        return accountCount;
    }
    
    public long getBandwidthCap() {
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
    
    public String getGenesisBlock() {
        return genesisBlock;
    }
    
    public int getVersionMajor() {
        return versionMajor;
    }
    
    public int getVersionMinor() {
        return versionMinor;
    }
    
    public int getVersionPatch() {
        return versionPatch;
    }
    
    public int getVersionPreRelease() {
        return versionPreRelease;
    }
    
    public int getMaker() {
        return maker;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public WorkDifficulty getActiveDifficulty() {
        return activeDifficulty;
    }
    
    public NanoAccount getNodeId() {
        return nodeId;
    }
    
    public String getSignature() {
        return signature;
    }
    
    public InetAddress getAddress() {
        return address;
    }
    
    public int getPort() {
        return port;
    }
}
