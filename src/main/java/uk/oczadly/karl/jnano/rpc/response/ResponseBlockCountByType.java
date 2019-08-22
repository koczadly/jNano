package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains a series of categorized block counts.
 */
public class ResponseBlockCountByType extends RpcResponse {
    
    @Expose @SerializedName("send")
    private long blocksSend;
    
    @Expose @SerializedName("receive")
    private long blocksReceive;
    
    @Expose @SerializedName("open")
    private long blocksOpen;
    
    @Expose @SerializedName("change")
    private long blocksChange;
    
    @Expose @SerializedName("state")
    private long blocksState;
    
    @Expose @SerializedName("state_v0")
    private long blocksStateV0;
    
    @Expose @SerializedName("state_v1")
    private long blocksStateV1;
    
    
    /**
     * @return the number of {@code SEND} blocks
     */
    public long getBlocksSend() {
        return blocksSend;
    }
    
    /**
     * @return the number of {@code RECEIVE} blocks
     */
    public long getBlocksReceive() {
        return blocksReceive;
    }
    
    /**
     * @return the number of {@code OPEN} blocks
     */
    public long getBlocksOpen() {
        return blocksOpen;
    }
    
    /**
     * @return the number of {@code CHANGE} blocks
     */
    public long getBlocksChange() {
        return blocksChange;
    }
    
    /**
     * @return the number of {@code STATE} blocks
     */
    public long getBlocksState() {
        return blocksState;
    }
    
    /**
     * @return the number of {@code STATE} (V0) blocks
     */
    public long getBlocksStateVersion0() {
        return blocksStateV0;
    }
    
    /**
     * @return the number of {@code STATE} (V1) blocks
     */
    public long getBlocksStateVersion1() {
        return blocksStateV1;
    }
    
}
