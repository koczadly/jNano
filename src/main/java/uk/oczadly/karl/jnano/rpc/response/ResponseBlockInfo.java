/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockDeserializer;
import uk.oczadly.karl.jnano.model.block.SendBlock;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * This response class contains detailed information about a block.
 */
public class ResponseBlockInfo extends RpcResponse {
    
    @Expose @SerializedName("block_account")
    private NanoAccount account;
    
    @Expose @SerializedName("amount")
    private BigInteger amount;
    
    @Expose @SerializedName("balance")
    private BigInteger balance;
    
    @Expose @SerializedName("height")
    private long height;
    
    @Expose @SerializedName("local_timestamp")
    private long timestamp;
    
    @Expose @SerializedName("confirmed")
    private boolean confirmed;
    
    @Expose @SerializedName("contents") @JsonAdapter(BlockAdapter.class)
    private Block blockContents;
    
    @Expose @SerializedName("subtype")
    private String subtype;
    
    
    /**
     * @return the account who created this block
     */
    public NanoAccount getAccount() {
        return account;
    }
    
    /**
     * @return the transactional amount associated with this block in RAW
     */
    public BigInteger getAmount() {
        return amount;
    }
    
    /**
     * @return the final balance after executing this block
     */
    public BigInteger getBalance() {
        return balance;
    }
    
    /**
     * @return the height of this block in the source account chain
     */
    public long getHeight() {
        return height;
    }
    
    /**
     * @return the local UNIX timestamp when this block was processed
     */
    public long getLocalTimestamp() {
        return timestamp;
    }
    
    /**
     * @return whether the block is confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * @return the contents of the block
     */
    public Block getBlockContents() {
        return blockContents;
    }
    
    /**
     * @return the subtype of the block
     */
    public String getSubtype() {
        return subtype;
    }
    
    
    
    // Fix for hexadecimal-encoded balances with SEND blocks.
    private static class BlockAdapter implements JsonDeserializer<Block> {
        BlockDeserializer deserializer = BlockDeserializer.withDefaults();
        
        public BlockAdapter() {
            deserializer.registerDeserializer("send", json -> new SendBlock(
                    json.get("signature").getAsString(),
                    new WorkSolution(json.get("work").getAsString()),
                    json.get("previous").getAsString(),
                    NanoAccount.parseAddress(json.get("destination").getAsString()),
                    new NanoAmount(new BigInteger(json.get("balance").getAsString(), 16))
            ));
        }
        
        @Override
        public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return deserializer.deserialize(json.getAsJsonObject());
        }
    }
    
}
