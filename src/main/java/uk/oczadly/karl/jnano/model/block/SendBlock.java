package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockBalance;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockLink;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;

/**
 * Represents a {@code send} block, and the associated data.
 */
public class SendBlock extends Block implements IBlockPrevious, IBlockLink, IBlockBalance {
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("destination")
    private NanoAccount destinationAccount;
    
    @Expose @SerializedName("balance")
    private BigInteger balance;
    
    
    SendBlock() {
        super(BlockType.SEND);
    }
    
    @Deprecated
    public SendBlock(String signature, WorkSolution work, String previousBlockHash, NanoAccount destinationAccount,
                     BigInteger balance) {
        this(null, null, signature, work, previousBlockHash, destinationAccount, balance);
    }
    
    protected SendBlock(JsonObject jsonRepresentation, String hash, String signature, WorkSolution work,
                     String previousBlockHash, NanoAccount destinationAccount, BigInteger balance) {
        super(BlockType.SEND, hash, jsonRepresentation, signature, work);
    
        if (previousBlockHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNanoHelper.isValidHex(previousBlockHash, 64))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (destinationAccount == null) throw new IllegalArgumentException("Block destination account cannot be null.");
        if (balance == null) throw new IllegalArgumentException("Account balance cannot be null.");
        if (!JNanoHelper.isBalanceValid(balance))
            throw new IllegalArgumentException("Account balance is an invalid amount.");
        
        this.previousBlockHash = previousBlockHash;
        this.destinationAccount = destinationAccount;
        this.balance = balance;
    }
    
    
    @Override
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    /**
     * @return the destination account which the funds will be sent to
     */
    public final NanoAccount getDestinationAccount() {
        return destinationAccount;
    }
    
    @Override
    public final BigInteger getBalance() {
        return balance;
    }
    
    
    @Override
    public final String getLinkData() {
        return getDestinationAccount().toPublicKey();
    }
    
    @Override
    public final NanoAccount getLinkAsAccount() {
        return getDestinationAccount();
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                JNanoHelper.ENCODER_HEX.decode(getPreviousBlockHash()),
                getDestinationAccount().getPublicKeyBytes(),
                JNanoHelper.leftPadByteArray(getBalance().toByteArray(), 16, false)
        };
    }
    
}
