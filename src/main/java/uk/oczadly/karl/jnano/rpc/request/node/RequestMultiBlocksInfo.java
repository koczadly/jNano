/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiBlockInfo;

/**
 * This request class is used to fetch detailed information about the specified block hashes.
 * <br>Calls the RPC command {@code blocks_info}, and returns a {@link ResponseMultiBlockInfo} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#blocks_info">Official RPC documentation</a>
 */
public class RequestMultiBlocksInfo extends RpcRequest<ResponseMultiBlockInfo> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    @Expose @SerializedName("pending")
    private final boolean fetchPending = true;
    
    @Expose @SerializedName("source")
    private final boolean fetchSource = true;
    
    @Expose @SerializedName("balance")
    private final boolean fetchBalance = true;
    
    
    @Expose @SerializedName("include_not_found")
    private final Boolean includeNotFound;
    
    @Expose @SerializedName("hashes")
    private final String[] blockHashes;
    
    
    /**
     * @param blockHashes the block hashes
     */
    public RequestMultiBlocksInfo(String... blockHashes) {
        this(null, blockHashes);
    }
    
    /**
     * @param includeNotFound (optional) whether blocks should be included if they aren't found in the ledger
     * @param blockHashes     the block hashes
     */
    public RequestMultiBlocksInfo(Boolean includeNotFound, String... blockHashes) {
        super("blocks_info", ResponseMultiBlockInfo.class);
        this.blockHashes = blockHashes;
        this.includeNotFound = includeNotFound;
    }
    
    
    /**
     * @return the requested block hashes
     */
    public String[] getBlockHashes() {
        return blockHashes;
    }
    
    /**
     * @return whether blocks should be included if they aren't found in the ledger
     */
    public Boolean getIncludeNotFound() {
        return includeNotFound;
    }
    
}
