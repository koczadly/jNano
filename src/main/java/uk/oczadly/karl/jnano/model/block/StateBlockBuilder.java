package uk.oczadly.karl.jnano.model.block;

import java.math.BigInteger;

public class StateBlockBuilder {
    
    private final String accountAddress;
    private final String previousBlockHash;
    private final String representativeAddress;
    private final BigInteger balance;
    private LinkFormat linkFormat;
    private String linkData;
    
    private String hash;
    private String signature;
    private String workSolution;
    
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the non-optional parameters.
     * @param accountAddress        the account which owns this block
     * @param previousBlockHash     the hash of the previous (or latest) block for the account
     * @param representativeAddress the representative for this account
     * @param balance               the (newly updated) balance of this account
     */
    public StateBlockBuilder(String accountAddress, String previousBlockHash, String representativeAddress,
                             BigInteger balance) {
        // Validate params
        if (accountAddress == null)
            throw new IllegalArgumentException("Account address argument cannot be null.");
        if (previousBlockHash == null)
            throw new IllegalArgumentException("Previous block hash argument cannot be null.");
        if (representativeAddress == null)
            throw new IllegalArgumentException("Representative address argument cannot be null.");
        if (balance == null)
            throw new IllegalArgumentException("Balance argument cannot be null.");
        if (balance.compareTo(BigInteger.ZERO) < 0)
            throw new IllegalArgumentException("Balance argument must be zero or greater.");
        
        this.accountAddress = accountAddress;
        this.previousBlockHash = previousBlockHash;
        this.representativeAddress = representativeAddress;
        this.balance = balance;
    }
    
    
    public String getHash() {
        return hash;
    }
    
    public StateBlockBuilder setHash(String hash) {
        this.hash = hash;
        return this;
    }
    
    public String getSignature() {
        return signature;
    }
    
    public StateBlockBuilder setSignature(String signature) {
        this.signature = signature;
        return this;
    }
    
    public String getWorkSolution() {
        return workSolution;
    }
    
    public StateBlockBuilder setWorkSolution(String workSolution) {
        this.workSolution = workSolution;
        return this;
    }
    
    public String getAccountAddress() {
        return accountAddress;
    }
    
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public String getRepresentativeAddress() {
        return representativeAddress;
    }
    
    public BigInteger getBalance() {
        return balance;
    }
    
    public StateBlockBuilder setLinkData(String linkData, LinkFormat format) {
        this.linkData = linkData;
        this.linkFormat = (format != null) ? format : LinkFormat.RAW_HEX;
        return this;
    }
    
    public String getLinkData() {
        return linkData;
    }
    
    public LinkFormat getLinkFormat() {
        return linkFormat;
    }
    
    
    /**
     * @return a new instance of the {@link StateBlock} class using the configured parameters.
     */
    @SuppressWarnings("deprecation")
    public StateBlock build() {
        // Process link data
        String processedLinkData = linkData;
        LinkFormat processedLinkFormat = linkFormat;
        if (linkData == null) {
            processedLinkData = "0000000000000000000000000000000000000000000000000000000000000000";
            processedLinkFormat = LinkFormat.RAW_HEX;
        }
        
        // Construct
        return new StateBlock(null, hash, signature, workSolution, accountAddress, previousBlockHash,
                representativeAddress, balance,
                (processedLinkFormat == LinkFormat.RAW_HEX) ? processedLinkData : null,
                (processedLinkFormat == LinkFormat.ACCOUNT) ? processedLinkData : null);
    }
    
    
    /**
     * The format of the specified link data.
     */
    public enum LinkFormat {
        /** Raw hexadecimal format. */
        RAW_HEX,
        
        /** Account address format, including address prefix. */
        ACCOUNT
    }
    
}
