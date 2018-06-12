package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockType;

import java.math.BigInteger;
import java.util.Set;

public class AccountHistoryResponse extends RpcResponse {

    @Expose
    @SerializedName("history")
    private Set<Block> history;
    
    
    public Set<Block> getHistory() {
        return history;
    }
    
}
