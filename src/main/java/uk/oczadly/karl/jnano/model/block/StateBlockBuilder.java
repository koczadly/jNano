package uk.oczadly.karl.jnano.model.block;

import uk.oczadly.karl.jnano.model.NanoAccount;

import java.math.BigInteger;

/**
 * <p>This class can be used to construct new State block ({@link StateBlock} instances.</p>
 * <p>Simply call the constructor with the required parameters, then set the additional properties using the
 * provided setter methods. Finally, call {@link #build()} to create the state block instance from the
 * information supplied to this builder class.</p>
 */
public class StateBlockBuilder {
    
    private static final String NULL_DATA = "0000000000000000000000000000000000000000000000000000000000000000";
    
    private StateBlockSubType subtype;
    private NanoAccount accountAddress;
    private String previousBlockHash;
    private NanoAccount representativeAddress;
    private BigInteger balance;
    private NanoAccount linkAccount;
    private String linkData;
    private String hash;
    private String signature;
    private String workSolution;
    
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the non-optional parameters.
     *
     * @param accountAddress        the account which owns this block
     * @param previousBlockHash     the hash of the previous (or latest) block for the account
     * @param representativeAddress the representative for this account
     * @param balance               the (newly updated) balance of this account
     */
    public StateBlockBuilder(NanoAccount accountAddress, String previousBlockHash,
                             NanoAccount representativeAddress, BigInteger balance) {
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
        this.subtype = subtype;
        return this;
    }
    
    public NanoAccount getAccountAddress() {
        return accountAddress;
    }
    
    public StateBlockBuilder setAccountAddress(NanoAccount accountAddress) {
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
    
    public NanoAccount getRepresentativeAddress() {
        return representativeAddress;
    }
    
    public StateBlockBuilder setRepresentativeAddress(NanoAccount representativeAddress) {
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
     * @param linkData the link data, in hexadecimal format
     * @return this builder
     */
    public StateBlockBuilder setLinkData(String linkData) {
        this.linkData = linkData;
        this.linkAccount = null;
        return this;
    }
    
    public String getLinkData() {
        return linkData;
    }
    
    /**
     * @param linkAccount the link data, as an account
     * @return this builder
     */
    public StateBlockBuilder setLinkAccount(NanoAccount linkAccount) {
        this.linkAccount = linkAccount;
        this.linkData = null;
        return this;
    }
    
    public NanoAccount getLinkAccount() {
        return linkAccount;
    }
    
    
    /**
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     */
    public StateBlock build() {
        // Construct
        return new StateBlock(null, subtype, hash, signature, workSolution, accountAddress,
                previousBlockHash, representativeAddress, balance,
                (linkAccount == null && linkData == null) ? NULL_DATA : linkData,
                linkAccount);
    }
    
}
