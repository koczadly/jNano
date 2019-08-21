package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    
    
    public long getBlocksSend() {
        return blocksSend;
    }
    
    public long getBlocksReceive() {
        return blocksReceive;
    }
    
    public long getBlocksOpen() {
        return blocksOpen;
    }
    
    public long getBlocksChange() {
        return blocksChange;
    }
    
    public long getBlocksState() {
        return blocksState;
    }
    
    public long getBlocksStateVersion0() {
        return blocksStateV0;
    }
    
    public long getBlocksStateVersion1() {
        return blocksStateV1;
    }
    
}
