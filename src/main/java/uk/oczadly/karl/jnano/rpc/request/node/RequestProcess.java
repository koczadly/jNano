/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

/**
 * This request class is used to manually publish a block to the network.
 * <br>Calls the RPC command {@code process}, and returns a {@link ResponseBlockHash} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#process">Official RPC documentation</a>
 */
public class RequestProcess extends RpcRequest<ResponseBlockHash> {
    
    @Expose private final boolean jsonBlock = true;
    
    @Expose private final Block block;
    @Expose private final boolean force;
    @Expose private final Boolean watchWork;
    @Expose private final StateBlockSubType subtype;
    
    
    /**
     * @param block the block to process
     */
    public RequestProcess(Block block) {
        this(block, null, null, null);
    }
    
    /**
     * @param block the block to process
     * @param force (optional) whether fork resolution should be forced
     */
    public RequestProcess(Block block, Boolean force) {
        this(block, force, null, null);
    }
    
    /**
     * @param block     the block to process
     * @param force     (optional) whether fork resolution should be forced
     * @param subtype   (optional) the subtype of the block
     */
    public RequestProcess(Block block, Boolean force, StateBlockSubType subtype) {
        this(block, force, null, subtype);
    }
    
    /**
     * @param block     the block to process
     * @param force     (optional) whether fork resolution should be forced
     * @param watchWork (optional) whether the block should be placed on watch for confirmation
     */
    public RequestProcess(Block block, Boolean force, Boolean watchWork) {
        this(block, force, watchWork, null);
    }
    
    /**
     * @param block     the block to process
     * @param force     (optional) whether fork resolution should be forced
     * @param watchWork (optional) whether the block should be placed on watch for confirmation
     * @param subtype   (optional) the subtype of the block
     */
    public RequestProcess(Block block, Boolean force, Boolean watchWork, StateBlockSubType subtype) {
        super("process", ResponseBlockHash.class);
        if (subtype == null && block instanceof StateBlock) {
            this.subtype = ((StateBlock)block).getSubType(); // Infer from state block
        } else {
            this.subtype = subtype;
        }
        this.block = block;
        this.force = force;
        this.watchWork = watchWork;
    }
    
    
    /**
     * @return the requested block
     */
    public Block getBlock() {
        return block;
    }
    
    /**
     * @return whether fork resolution should be forced
     */
    public boolean shouldForce() {
        return force;
    }
    
    /**
     * @return the requested block's subtype
     */
    public StateBlockSubType getSubtype() {
        return subtype;
    }
    
    /**
     * @return whether the block should be placed on watch for confirmation
     */
    public Boolean getWatchWork() {
        return watchWork;
    }
    
}
