/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHashes;

/**
 * This request class is used to request a consecutive list of block hashes for an account.
 * <br>Calls the RPC command {@code chain}, and returns a {@link ResponseBlockHashes} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#chain">Official RPC documentation</a>
 */
public class RequestChain extends RpcRequest<ResponseBlockHashes> {
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    @Expose @SerializedName("count")
    private final int count;
    
    @Expose @SerializedName("offset")
    private final Integer offset;
    
    @Expose @SerializedName("reverse")
    private final Boolean reverse;
    
    
    /**
     * @param blockHash the starting block's hash
     */
    public RequestChain(String blockHash) {
        this(blockHash, null);
    }
    
    /**
     * @param blockHash the starting block's hash
     * @param count     (optional) the number of blocks to limit
     */
    public RequestChain(String blockHash, Integer count) {
        this(blockHash, count, null);
    }
    
    /**
     * @param blockHash the starting block's hash
     * @param count     (optional) the number of blocks to limit
     * @param offset    (optional) the offset to start listing from
     */
    public RequestChain(String blockHash, Integer count, Integer offset) {
        this(blockHash, count, offset, null);
    }
    
    /**
     * @param blockHash the starting block's hash
     * @param count     (optional) the number of blocks to limit
     * @param offset    (optional) the offset to start listing from
     * @param reverse   (optional) whether the blocks should be listed in reverse order
     */
    public RequestChain(String blockHash, Integer count, Integer offset, Boolean reverse) {
        super("chain", ResponseBlockHashes.class);
        this.blockHash = blockHash;
        this.count = count != null ? count : -1;
        this.offset = offset;
        this.reverse = reverse;
    }
    
    
    /**
     * @return the requested starting block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return the requested limit
     */
    public int getCount() {
        return count;
    }
    
    /**
     * @return the requested offset
     */
    public Integer getOffset() {
        return offset;
    }
    
    /**
     * @return the requested order
     */
    public Boolean getReverse() {
        return reverse;
    }
    
}
