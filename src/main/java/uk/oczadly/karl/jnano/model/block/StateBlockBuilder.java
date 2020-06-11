package uk.oczadly.karl.jnano.model.block;

import java.math.BigInteger;

/**
 * <p>This class can be used to construct new State block ({@link StateBlock} instances.</p>
 * <p>Simply call the constructor with the required parameters, then set the additional properties using the
 * provided setter methods. Finally, call {@link #build()} to create the state block instance from the
 * information supplied to this builder class.</p>
 */
public class StateBlockBuilder {
    
    private StateBlockSubType subtype;
    private String accountAddress;
    private String previousBlockHash;
    private String representativeAddress;
    private BigInteger balance;
    private LinkFormat linkFormat;
    private String linkData;
    private String hash;
    private String signature;
    private String workSolution;
    
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the non-optional parameters.
     *
     * @param subtype               the context of the state block's action
     * @param accountAddress        the account which owns this block
     * @param previousBlockHash     the hash of the previous (or latest) block for the account
     * @param representativeAddress the representative for this account
     * @param balance               the (newly updated) balance of this account
     */
    public StateBlockBuilder(StateBlockSubType subtype, String accountAddress, String previousBlockHash,
                             String representativeAddress, BigInteger balance) {
        setSubtype(subtype);
        setAccountAddress(accountAddress);
        setPreviousBlockHash(previousBlockHash);
        setRepresentativeAddress(representativeAddress);
        setBalance(balance);
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
    
    public StateBlockSubType getSubtype() {
        return subtype;
    }
    
    public StateBlockBuilder setSubtype(StateBlockSubType subtype) {
        if (subtype == null)
            throw new IllegalArgumentException("Block subtype cannot be null.");
        
        this.subtype = subtype;
        return this;
    }
    
    public String getAccountAddress() {
        return accountAddress;
    }
    
    public StateBlockBuilder setAccountAddress(String accountAddress) {
        if (accountAddress == null)
            throw new IllegalArgumentException("Account address argument cannot be null.");
        
        this.accountAddress = accountAddress;
        return this;
    }
    
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public StateBlockBuilder setPreviousBlockHash(String previousBlockHash) {
        if (previousBlockHash == null)
            throw new IllegalArgumentException("Previous block hash argument cannot be null.");
        
        this.previousBlockHash = previousBlockHash;
        return this;
    }
    
    public String getRepresentativeAddress() {
        return representativeAddress;
    }
    
    public StateBlockBuilder setRepresentativeAddress(String representativeAddress) {
        if (representativeAddress == null)
            throw new IllegalArgumentException("Representative address argument cannot be null.");
        
        this.representativeAddress = representativeAddress;
        return this;
    }
    
    public BigInteger getBalance() {
        return balance;
    }
    
    public StateBlockBuilder setBalance(BigInteger balance) {
        if (balance == null)
            throw new IllegalArgumentException("Balance argument cannot be null.");
        if (balance.compareTo(BigInteger.ZERO) < 0)
            throw new IllegalArgumentException("Balance argument must be zero or greater.");
        
        this.balance = balance;
        return this;
    }
    
    /**
     * @param linkData the link data in the format specified
     * @param format   the format of the link data
     * @return this builder
     */
    public StateBlockBuilder setLink(String linkData, LinkFormat format) {
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
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     */
    public StateBlock build() {
        // Process link data
        String processedLinkData = linkData;
        LinkFormat processedLinkFormat = linkFormat;
        if (linkData == null) {
            processedLinkData = "0000000000000000000000000000000000000000000000000000000000000000";
            processedLinkFormat = LinkFormat.RAW_HEX;
        }
        
        // Construct
        return new StateBlock(null, subtype, hash, signature, workSolution, accountAddress,
                previousBlockHash, representativeAddress, balance,
                (processedLinkFormat == LinkFormat.RAW_HEX) ? processedLinkData : null,
                (processedLinkFormat == LinkFormat.ACCOUNT) ? processedLinkData : null);
    }
    
    
    /**
     * The format of the specified link data.
     */
    public enum LinkFormat {
        /**
         * Raw hexadecimal format.
         */
        RAW_HEX,
        
        /**
         * Account address format, including address prefix.
         */
        ACCOUNT
    }
    
}
