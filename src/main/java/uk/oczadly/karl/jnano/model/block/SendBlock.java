package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.NanoAccount;

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
    
    public SendBlock(JsonObject jsonRepresentation, String hash, String signature, String workSolution,
                     String previousBlockHash, NanoAccount destinationAccount, BigInteger newBalance) {
        super(BlockType.SEND, hash, jsonRepresentation, signature, workSolution);
        this.previousBlockHash = previousBlockHash;
        this.destinationAccount = destinationAccount;
        this.newBalance = newBalance;
    }
    
    
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public final NanoAccount getDestinationAccount() {
        return destinationAccount;
    }
    
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
