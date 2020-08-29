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
 * This request class is used to republish a set of blocks starting at the specified block hash.
 * <br>Calls the RPC command {@code republish}, and returns a {@link ResponseBlockHashes} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#republish">Official RPC documentation</a>
 */
public class RequestRepublish extends RpcRequest<ResponseBlockHashes> {
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    @Expose @SerializedName("count")
    private final Integer count;
    
    @Expose @SerializedName("sources")
    private final Integer sourcesDepth;
    
    @Expose @SerializedName("destinations")
    private final Integer destinationsDepth;
    
    
    /**
     * @param blockHash the starting block hash
     */
    public RequestRepublish(String blockHash) {
        this(blockHash, null, null, null);
    }
    
    /**
     * @param blockHash the starting block hash
     * @param count     (optional) the number of blocks to republish
     */
    public RequestRepublish(String blockHash, Integer count) {
        this(blockHash, count, null, null);
    }
    
    /**
     * @param blockHash         the starting block hash
     * @param count             (optional) the number of blocks to republish
     * @param sourcesDepth      (optional) additionally rebroadcast source chain blocks up to the sources depth
     * @param destinationsDepth (optional) additionally rebroadcast destination chain blocks up to destinations depth
     */
    public RequestRepublish(String blockHash, Integer count, Integer sourcesDepth, Integer destinationsDepth) {
        super("republish", ResponseBlockHashes.class);
        this.blockHash = blockHash;
        this.count = count;
        this.sourcesDepth = sourcesDepth;
        this.destinationsDepth = destinationsDepth;
    }
    
    
    /**
     * @return the requested starting block hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return the requested limit
     */
    public Integer getCount() {
        return count;
    }
    
    /**
     * @return the requested sources depth
     */
    public Integer getSourcesDepth() {
        return sourcesDepth;
    }
    
    /**
     * @return the requested destination depth
     */
    public Integer getDestinationsDepth() {
        return destinationsDepth;
    }
    
}
