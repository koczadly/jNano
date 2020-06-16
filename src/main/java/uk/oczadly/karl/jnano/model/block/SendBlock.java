package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;

/**
 * Represents a {@code send} block, and the associated data.
 *
 * @deprecated Only state blocks ({@link StateBlock}) are supported by the Nano node.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class SendBlock extends Block {
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("destination")
    private NanoAccount destinationAccount;
    
    @Expose @SerializedName("balance")
    private BigInteger newBalance;
    
    
    SendBlock() {
        super(BlockType.SEND);
    }
    
    public SendBlock(String signature, WorkSolution work, String previousBlockHash, NanoAccount destinationAccount,
                     BigInteger newBalance) {
        this(null, null, signature, work, previousBlockHash, destinationAccount, newBalance);
    }
    
    protected SendBlock(JsonObject jsonRepresentation, String hash, String signature, WorkSolution work,
                     String previousBlockHash, NanoAccount destinationAccount, BigInteger newBalance) {
        super(BlockType.SEND, hash, jsonRepresentation, signature, work);
    
        if (previousBlockHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNanoHelper.isValidHex(previousBlockHash, 64))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (destinationAccount == null) throw new IllegalArgumentException("Block destination account cannot be null.");
        if (newBalance == null) throw new IllegalArgumentException("Account balance cannot be null.");
        if (!JNanoHelper.isBalanceValid(newBalance))
            throw new IllegalArgumentException("Account balance is an invalid amount.");
        
        this.previousBlockHash = previousBlockHash;
        this.destinationAccount = destinationAccount;
        this.newBalance = newBalance;
    }
    
    
    /**
     * @return the previous block hash in this account's blockchain
     */
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    /**
     * @return the destination account which the funds will be sent to
     */
    public final NanoAccount getDestinationAccount() {
        return destinationAccount;
    }
    
    /**
     * @return the balance of the account after this transaction
     */
    public final BigInteger getNewBalance() {
        return newBalance;
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                JNanoHelper.ENCODER_HEX.decode(getPreviousBlockHash()),
                getDestinationAccount().getPublicKeyBytes(),
                JNanoHelper.padByteArray(getNewBalance().toByteArray(), 16)
        };
    }
    
}
