package uk.oczadly.karl.jnano.model.block;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;

/**
 * <p>This class can be used to construct new State block ({@link StateBlock} instances.</p>
 * <p>Simply call the constructor with the required parameters, then set the additional properties using the
 * provided setter methods. Finally, call {@link #build()} to create the state block instance from the
 * information supplied to this builder class.</p>
 */
public final class StateBlockBuilder {
    
    private StateBlockSubType subtype;
    private NanoAccount accountAddress;
    private String previousBlockHash;
    private NanoAccount representativeAddress;
    private BigInteger balance;
    private NanoAccount linkAccount;
    private String linkData;
    private String hash;
    private String signature;
    private WorkSolution work;
    
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the account parameter. This will also set the representative
     * as the given account, which can be overridden through the setter.
     *
     * @param accountAddress the account which owns this block
     */
    public StateBlockBuilder(NanoAccount accountAddress) {
        setAccountAddress(accountAddress);
        setRepresentativeAddress(accountAddress);
    }
    
    /**
     * Constructs a new {@link StateBlockBuilder} with a set of parameters.
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
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the non-optional parameters.
     *
     * @param accountAddress        the account which owns this block
     * @param representativeAddress the representative for this account
     * @param balance               the (newly updated) balance of this account
     */
    public StateBlockBuilder(NanoAccount accountAddress, NanoAccount representativeAddress, BigInteger balance) {
        setAccountAddress(accountAddress);
        setRepresentativeAddress(representativeAddress);
        setBalance(balance);
    }
    
    /**
     * Constructs a new {@link StateBlockBuilder} from an existing {@link StateBlock}.
     *
     * @param block the state block to copy from
     */
    public StateBlockBuilder(StateBlock block) {
        this(block.getAccount(), block.getPreviousBlockHash(), block.getRepresentative(), block.getBalance());
        setHash(block.getHash());
        setSignature(block.getSignature());
        setWorkSolution(block.getWorkSolution());
        setSubtype(block.getSubType());
        setLinkData(block.getLinkData());
    }
    
    
    
    public String getHash() {
        return hash;
    }
    
    /**
     * @param hash the block hash
     * @return this builder
     * @deprecated Leaving this value unassigned is preferred, as hashes will be computed programatically.
     */
    @Deprecated
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
    
    
    public WorkSolution getWorkSolution() {
        return work;
    }
    
    public StateBlockBuilder setWorkSolution(WorkSolution work) {
        this.work = work;
        return this;
    }
    
    public StateBlockBuilder setWorkSolution(String work) {
        return setWorkSolution(work != null ? new WorkSolution(work) : null);
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
    
    public StateBlockBuilder setAccountAddress(String accountAddress) {
        if (accountAddress == null)
            throw new IllegalArgumentException("Account address argument cannot be null.");
        return setAccountAddress(NanoAccount.parseAddress(accountAddress));
    }
    
    
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public StateBlockBuilder setPreviousBlockHash(String previousBlockHash) {
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
    
    public StateBlockBuilder setRepresentativeAddress(String representativeAddress) {
        if (representativeAddress == null)
            throw new IllegalArgumentException("Representative address argument cannot be null.");
        return setRepresentativeAddress(NanoAccount.parseAddress(representativeAddress));
    }
    
    
    public BigInteger getBalance() {
        return balance;
    }
    
    public StateBlockBuilder setBalance(BigInteger balance) {
        if (balance == null)
            throw new IllegalArgumentException("Balance argument cannot be null.");
        if (!JNH.isBalanceValid(balance))
            throw new IllegalArgumentException("Provided balance value is not in the valid range.");
        
        this.balance = balance;
        return this;
    }
    
    
    public String getLinkData() {
        return linkData;
    }
    
    public NanoAccount getLinkAccount() {
        return linkAccount;
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
    
    /**
     * @param linkAccount the link data, as an account
     * @return this builder
     */
    public StateBlockBuilder setLinkAccount(String linkAccount) {
        return setLinkAccount(linkAccount != null ? NanoAccount.parseAddress(linkAccount) : null);
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
    
    
    /**
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     */
    public StateBlock build() {
        return new StateBlock(subtype, hash, signature, work, accountAddress,
                previousBlockHash, representativeAddress, balance, linkData, linkAccount);
    }
    
}
