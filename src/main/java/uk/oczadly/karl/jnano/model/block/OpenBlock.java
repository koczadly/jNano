package uk.oczadly.karl.jnano.model.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockRepresentative;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockSource;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

/**
 * Represents an {@code open} block, and the associated data.
 */
public class OpenBlock extends Block implements IBlockSource, IBlockAccount, IBlockRepresentative {
    
    @Expose @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose @SerializedName("account")
    private NanoAccount accountAddress;
    
    @Expose @SerializedName("representative")
    private NanoAccount representativeAccount;
    
    
    OpenBlock() {
        super(BlockType.OPEN);
    }
    
    public OpenBlock(String signature, WorkSolution work, String sourceBlockHash, NanoAccount accountAddress,
                     NanoAccount representativeAccount) {
        this(null, signature, work, sourceBlockHash, accountAddress, representativeAccount);
    }
    
    protected OpenBlock(String hash, String signature, WorkSolution work,
                     String sourceBlockHash, NanoAccount accountAddress, NanoAccount representativeAccount) {
        super(BlockType.OPEN, hash, signature, work);
    
        if (sourceBlockHash == null) throw new IllegalArgumentException("Source block hash cannot be null.");
        if (!JNanoHelper.isValidHex(sourceBlockHash, 64))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (accountAddress == null) throw new IllegalArgumentException("Block account cannot be null.");
        if (representativeAccount == null) throw new IllegalArgumentException("Block representative cannot be null.");
        
        this.sourceBlockHash = sourceBlockHash;
        this.accountAddress = accountAddress;
        this.representativeAccount = representativeAccount;
    }
    
    
    @Override
    public final String getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    @Override
    public final NanoAccount getAccount() {
        return accountAddress;
    }
    
    @Override
    public final NanoAccount getRepresentative() {
        return representativeAccount;
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                JNanoHelper.ENCODER_HEX.decode(getSourceBlockHash()),
                getRepresentative().getPublicKeyBytes(),
                getAccount().getPublicKeyBytes()
        };
    }
    
}
