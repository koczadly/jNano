/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.InstantAdapter;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockType;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * This response class contains historical information about an account.
 */
@JsonAdapter(ResponseAccountHistory.Adapter.class)
public class ResponseAccountHistory extends RpcResponse {
    
    private final NanoAccount account;
    private final HexData previous, next;
    private final List<BlockInfo> history;
    
    private ResponseAccountHistory(NanoAccount account, HexData previous, HexData next, List<BlockInfo> history) {
        this.account = account;
        this.previous = previous;
        this.next = next;
        this.history = history;
    }
    
    
    /**
     * @return the account's address
     */
    public NanoAccount getAccount() {
        return account;
    }
    
    /**
     * Returns a list of historical blocks.
     * @return a list of historical blocks
     */
    public List<BlockInfo> getHistory() {
        return history;
    }
    
    /**
     * @return the previous block hash, or null if traversing backwards
     *
     * @see #getNextBlockHash()
     */
    public HexData getPreviousBlockHash() {
        return previous;
    }
    
    /**
     * @return the next block hash, or null if traversing forwards
     *
     * @see #getPreviousBlockHash()
     */
    public HexData getNextBlockHash() {
        return next;
    }
    
    /**
     * @return the previous or next block hash in the search sequence, or null
     * @see #getNextBlockHash()
     * @see #getPreviousBlockHash()
     */
    public HexData getSequenceBlockHash() {
        return previous != null ? previous : next;
    }
    
    
    public static class BlockInfo {
        @Expose private BlockType type;
        @Expose private NanoAccount account;
        @Expose private NanoAmount amount;
        @Expose @JsonAdapter(InstantAdapter.Seconds.class) private Instant localTimestamp;
        @Expose private int height;
        @Expose private HexData hash;
        private transient Block contents; // Will be manually injected
    
    
        /**
         * @return the block type
         */
        public BlockType getType() {
            return type;
        }
    
        /**
         * @return the account involved with the transaction
         */
        public NanoAccount getAccount() {
            return account;
        }
    
        /**
         * @return the amount of the transaction
         */
        public NanoAmount getAmount() {
            return amount;
        }
    
        /**
         * @return the local timestamp when the block was first seen
         */
        public Instant getTimestamp() {
            return localTimestamp;
        }
    
        /**
         * @return the height of the block in the account
         */
        public int getHeight() {
            return height;
        }
    
        /**
         * @return the hash of the block
         */
        public HexData getHash() {
            return hash;
        }
    
        /**
         * @return the contents of the block, or null if {@code raw} is false
         */
        public Block getContents() {
            return contents;
        }
    }
    
    /** Supports both raw and non-raw block deserialization. */
    static class Adapter implements JsonDeserializer<ResponseAccountHistory> {
        @Override
        public ResponseAccountHistory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();
    
            // Deserialize response
            ResponseAccountHistory response = new ResponseAccountHistory(
                    context.deserialize(jsonObj.get("account"), NanoAccount.class),
                    context.deserialize(jsonObj.get("previous"), HexData.class),
                    context.deserialize(jsonObj.get("next"), HexData.class),
                    new ArrayList<>());

            // Will return empty string if empty
            if (jsonObj.get("history").isJsonArray()) {
                JsonArray history = jsonObj.getAsJsonArray("history");
                // Deserialize block infos
                for (JsonElement blockJsonEl : history) {
                    JsonObject blockInfoJson = blockJsonEl.getAsJsonObject();
                    BlockInfo blockInfo = context.deserialize(blockInfoJson, BlockInfo.class); // Deserialize normally
                    if (blockInfoJson.has("signature")) {
                        // Raw == true, inject block contents
                        blockInfoJson.addProperty("account", response.account.toAddress()); // Use account being polled
                        blockInfo.contents = context.deserialize(blockInfoJson, Block.class);
                    }
                    response.history.add(blockInfo);
                }
            }
            return response;
        }
    }
    
}
