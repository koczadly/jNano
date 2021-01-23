/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;

import java.lang.reflect.Type;
import java.time.Instant;

/**
 * This response class contains detailed information about a block.
 */
@JsonAdapter(ResponseBlockInfo.Adapter.class)
public class ResponseBlockInfo extends RpcResponse {
    
    private final NanoAccount account;
    private final NanoAmount amount;
    private final NanoAmount balance;
    private final long height;
    private final Instant timestamp;
    private final boolean confirmed;
    private final Block blockContents;
    
    private ResponseBlockInfo(ResponseMultiBlockInfo.BlockInfo block) {
        this.account = block.getAccount();
        this.amount = block.getAmount();
        this.balance = block.getBalance();
        this.height = block.getHeight();
        this.timestamp = block.getLocalTimestamp();
        this.confirmed = block.isConfirmed();
        this.blockContents = block.getContents();
    }
    
    /**
     * @return the account who created this block
     */
    public NanoAccount getAccount() {
        return account;
    }
    
    /**
     * @return the transactional amount associated with this block in RAW
     */
    public NanoAmount getAmount() {
        return amount;
    }
    
    /**
     * @return the final balance after executing this block
     */
    public NanoAmount getBalance() {
        return balance;
    }
    
    /**
     * @return the height of this block in the source account chain
     */
    public long getHeight() {
        return height;
    }
    
    /**
     * @return the local timestamp when this block was processed
     */
    public Instant getLocalTimestamp() {
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
    public Block getContents() {
        return blockContents;
    }
    
    
    static class Adapter implements JsonDeserializer<ResponseBlockInfo> {
        static final ResponseMultiBlockInfo.InfoAdapter ADAPTER = new ResponseMultiBlockInfo.InfoAdapter();
        
        @Override
        public ResponseBlockInfo deserialize(JsonElement jsonEl, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new ResponseBlockInfo(ADAPTER.deserialize(jsonEl, ResponseMultiBlockInfo.BlockInfo.class, context));
        }
    }
    
}
