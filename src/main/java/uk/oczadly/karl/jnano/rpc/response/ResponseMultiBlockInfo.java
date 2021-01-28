/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.gsonadapters.InstantAdapter;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.model.block.SendBlock;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * This response class contains a set of block information.
 */
public class ResponseMultiBlockInfo extends RpcResponse {
    
    @Expose @SerializedName("blocks")
    private Map<HexData, BlockInfo> blocks = Collections.emptyMap();
    
    
    /**
     * Returns a map of all retrieved blocks.
     * <p>The map follows the structure {@code block hash (k) -> block information (v)}.</p>
     *
     * @return a map of block hashes and information, or null if not present in the response
     */
    public Map<HexData, BlockInfo> getBlocks() {
        return blocks;
    }
    
    /**
     * Returns the retrieved block from the given hash.
     * @param hash the block hash
     * @return the block info
     */
    public BlockInfo getBlock(HexData hash) {
        return getBlocks().get(hash);
    }
    
    /**
     * Returns the retrieved block from the given hash.
     * @param hash the block hash
     * @return the block info
     */
    public BlockInfo getBlock(String hash) {
        return getBlock(new HexData(hash));
    }
    
    
    @JsonAdapter(InfoAdapter.class)
    public static class BlockInfo {
        private final NanoAccount account;
        private final NanoAmount amount;
        private final NanoAmount balance;
        private final boolean pending;
        private final NanoAccount source;
        private final long height;
        private final Instant timestamp;
        private final boolean confirmed;
        private final Block blockContents;
        
        
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
    
        /**
         * @return whether the funds have been accepted (if a send transaction)
         */
        public boolean getPending() {
            return pending;
        }
    
        /**
         * @return the source account, or null
         */
        public NanoAccount getSourceAccount() {
            return source;
        }
    
        BlockInfo(NanoAccount account, NanoAmount amount, NanoAmount balance, boolean pending,
                         NanoAccount source, long height, Instant timestamp, boolean confirmed, Block blockContents) {
            this.account = account;
            this.amount = amount;
            this.balance = balance;
            this.pending = pending;
            this.source = source;
            this.height = height;
            this.timestamp = timestamp;
            this.confirmed = confirmed;
            this.blockContents = blockContents;
        }
    }
    
    /*
     * Fix for weird formats and return types. Why should I have to do this??
     * SEND block:     Balance is encoded in hexadecimal
     * STATE block:    Subtype field is outside the contents object
     */
    static class InfoAdapter implements JsonDeserializer<BlockInfo> {
        private static final Function<JsonObject, SendBlock> SEND_DESERIALIZER = json -> new SendBlock(
                JNH.getJson(json, "signature",    HexData::new),
                JNH.getJson(json, "work",         WorkSolution::new),
                JNH.getJson(json, "previous",     HexData::new),
                JNH.getJson(json, "destination",  NanoAccount::parseAddress),
                JNH.getJson(json, "balance", v -> NanoAmount.valueOfRaw(new BigInteger(v, 16))));
        
        @Override
        public BlockInfo deserialize(JsonElement jsonEl, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject json = jsonEl.getAsJsonObject();
            
            // Parse block contents
            JsonObject contents = json.getAsJsonObject("contents");
            BlockType type = BlockType.fromName(contents.get("type").getAsString());
            Block block;
            if (type == BlockType.SEND) {
                block = SEND_DESERIALIZER.apply(contents);
            } else {
                if (type == BlockType.STATE)
                    contents.add("subtype", json.get("subtype")); // Add subtype property
                block = JNC.BLOCK_DESERIALIZER.deserialize(contents); // Default deserializer
            }
            
            // Parse response class
            return new BlockInfo(
                    context.deserialize(json.get("block_account"), NanoAccount.class),
                    context.deserialize(json.get("amount"), NanoAmount.class),
                    context.deserialize(json.get("balance"), NanoAmount.class),
                    context.deserialize(json.get("pending"), boolean.class),
                    context.deserialize(json.get("source_account"), NanoAccount.class),
                    json.get("height").getAsLong(),
                    InstantAdapter.Seconds.INSTANCE.deserialize(json.get("local_timestamp"), Instant.class, context),
                    context.deserialize(json.get("confirmed"), boolean.class),
                    block
            );
        }
    }
    
}
