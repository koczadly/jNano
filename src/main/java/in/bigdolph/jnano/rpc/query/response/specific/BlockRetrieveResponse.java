package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.Block;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.lang.reflect.Type;

public class BlockRetrieveResponse extends RPCResponse {
    
    @Expose
    @SerializedName("contents")
    private Block block;
    
    
    
    public Block getBlock() {
        return block;
    }
    
}
