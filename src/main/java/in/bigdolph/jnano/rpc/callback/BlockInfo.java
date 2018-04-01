package in.bigdolph.jnano.rpc.callback;

import com.google.gson.annotations.JsonAdapter;
import in.bigdolph.jnano.model.block.Block;
import in.bigdolph.jnano.rpc.adapters.CallbackBlockTypeDeserializer;

import java.math.BigInteger;

@JsonAdapter(CallbackBlockTypeDeserializer.class)
public class BlockInfo {
    
    private String rawJson, accountAddress, blockHash;
    private Block block;
    private boolean isSend;
    private BigInteger amount;
    
    public BlockInfo(String rawJson, String accountAddress, String blockHash, Block block, boolean isSend, BigInteger amount) {
        this.rawJson = rawJson;
        this.accountAddress = accountAddress;
        this.blockHash = blockHash;
        this.block = block;
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
    public String getAccountAddress() {
        return accountAddress;
    }
    
    /**
     * @return the identifying hash of the block
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return the block's contents
     */
    public Block getBlock() {
        return block;
    }
    
    /**
     * Part of the new universal blocks API
     * @return if the block is a SEND transaction
     */
    public boolean isSendTransaction() {
        return isSend;
    }
    
    /**
     * @return the value of funds involved in the transaction, or null if non-transactional
     */
    public BigInteger getTransactionalAmount() {
        return amount;
    }
    
}
