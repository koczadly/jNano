package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class BlockCountByTypeResponse extends RPCResponse {
    
    @Expose
    @SerializedName("send")
    private long blocksSend;
    
    @Expose
    @SerializedName("receive")
    private long blocksReceive;
    
    @Expose
    @SerializedName("open")
    private long blocksOpen;
    
    @Expose
    @SerializedName("change")
    private long blocksChange;
    
    
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
    
    
    public long getBlocks(BlockType type) {
        if(type == null) throw new IllegalArgumentException("Block type cannot be null");
        switch(type) {
            case SEND:      return getBlocksSend();
            case RECEIVE:   return getBlocksReceive();
            case OPEN:      return getBlocksOpen();
            case CHANGE:    return getBlocksChange();
            default: return 0;
        }
    }
    
    
    public long getBlocksTotal() {
        return blocksSend + blocksReceive + blocksOpen + blocksChange;
    }
    
}
