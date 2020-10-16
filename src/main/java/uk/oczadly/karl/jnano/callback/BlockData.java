/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.callback;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockType;

/**
 * This class encapsulates a variety of data relating to newly-processed blocks. Instances of this class are generated
 * and returned by a {@link BlockCallbackServer} as new blocks are received.
 */
public class BlockData {
    
    private final String rawJson;
    private final HexData blockHash;
    private final NanoAccount account;
    private final Block block;
    private final BlockType subtype;
    private final boolean isSend;
    private final NanoAmount amount;
    
    public BlockData(String rawJson, NanoAccount account, HexData blockHash, Block block, BlockType subtype,
                     boolean isSend, NanoAmount amount) {
        this.rawJson = rawJson;
        this.account = account;
        this.blockHash = blockHash;
        this.block = block;
        this.subtype = subtype;
        this.amount = amount;
        this.isSend = isSend;
    }
    
    
    /**
     * @return the raw JSON block received from the node
     */
    public String getRawJson() {
        return rawJson;
    }
    
    
    /**
     * @return the account who the block belongs to
     */
    public NanoAccount getAccountAddress() {
        return account;
    }
    
    /**
     * @return the identifying hash of the block
     */
    public HexData getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return the block's contents
     */
    public Block getBlockContents() {
        return block;
    }
    
    /**
     * Returns the legacy block type if the block is of universal state type.
     *
     * @return the subtype of the block, or null if not a state block
     */
    public BlockType getSubtype() {
        return subtype;
    }
    
    /**
     * Returns whether or not the block is sending funds to another account.
     *
     * @return if the block is a SEND transaction
     */
    public boolean isSendTransaction() {
        return isSend;
    }
    
    /**
     * @return the value of funds involved, or null if non-transactional
     */
    public NanoAmount getTransactionalAmount() {
        return amount;
    }
    
}
