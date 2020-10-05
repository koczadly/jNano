/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;
import java.util.Objects;

/**
 * <p>This class can be used to construct new State block ({@link StateBlock} instances.</p>
 * <p>Simply call the constructor with the required parameters, then set the additional properties using the
 * provided setter methods. Finally, call {@link #build()} to create the state block instance from the
 * information supplied to this builder class.</p>
 */
public final class StateBlockBuilder {
    
    private StateBlockSubType subtype;
    private NanoAccount accountAddress;
    private HexData previousBlockHash;
    private NanoAccount representativeAddress;
    private NanoAmount balance;
    private NanoAccount linkAccount;
    private HexData linkData;
    private HexData signature;
    private WorkSolution work;
    
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the no parameters.
     */
    public StateBlockBuilder() {}
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the specified subtype.
     *
     * @param subType the block subtype
     */
    public StateBlockBuilder(StateBlockSubType subType) {
        setSubtype(subType);
    }
    
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
    @Deprecated
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
    @Deprecated
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
        this();
        setSubtype(block.getSubType());
        setAccountAddress(block.getAccount());
        setPreviousBlockHash(block.getPreviousBlockHash());
        setRepresentativeAddress(block.getRepresentative());
        setBalance(getBalance());
        setSignature(block.getSignature());
        setWorkSolution(block.getWorkSolution());
        setLinkData(block.getLinkData());
    }
    
    /**
     * Clones an existing {@link StateBlockBuilder}.
     *
     * @param builder the builder to copy from
     */
    public StateBlockBuilder(StateBlockBuilder builder) {
        this();
        this.subtype = builder.subtype;
        this.accountAddress = builder.accountAddress;
        this.previousBlockHash = builder.previousBlockHash;
        this.representativeAddress = builder.representativeAddress;
        this.balance = builder.balance;
        this.signature = builder.signature;
        this.work = builder.work;
        this.linkData = builder.linkData;
        this.linkAccount = builder.linkAccount;
    }
    
    
    /**
     * @param hash the block hash
     * @return this builder
     * @deprecated This method will do nothing, and the hash of the block will be computed automatically.
     */
    @Deprecated(forRemoval = true)
    public StateBlockBuilder setHash(String hash) {
        return this;
    }
    
    
    public String getSignature() {
        return signature.toHexString();
    }
    
    public StateBlockBuilder setSignature(String signature) {
        return setSignature(signature != null ? new HexData(signature, NanoConst.LEN_SIGNATURE_B) : null);
    }
    
    public StateBlockBuilder setSignature(HexData signature) {
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
        return previousBlockHash.toHexString();
    }
    
    public StateBlockBuilder setPreviousBlockHash(String previousBlockHash) {
        return setPreviousBlockHash(previousBlockHash != null
                ? new HexData(previousBlockHash, NanoConst.LEN_HASH_B) : null);
    }
    
    public StateBlockBuilder setPreviousBlockHash(HexData previousBlockHash) {
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
    
    
    public NanoAmount getBalance() {
        return balance;
    }
    
    public StateBlockBuilder setBalance(BigInteger balance) {
        return setBalance(new NanoAmount(balance));
    }
    
    public StateBlockBuilder setBalance(NanoAmount balance) {
        this.balance = balance;
        return this;
    }
    
    
    public String getLinkData() {
        return linkData.toHexString();
    }
    
    public NanoAccount getLinkAccount() {
        return linkAccount;
    }
    
    /**
     * @param linkData the link data, in either hexadecimal or account format
     * @return this builder
     */
    public StateBlockBuilder setLink(String linkData) {
        if (linkData == null || JNH.isValidHex(linkData, 64)) {
            setLinkData(linkData);
        } else {
            setLinkAccount(NanoAccount.parseAddress(linkData));
        }
        return this;
    }
    
    /**
     * @param linkData the link data, in hexadecimal format
     * @return this builder
     */
    public StateBlockBuilder setLinkData(String linkData) {
        return setLinkData(linkData != null ? new HexData(linkData, NanoConst.LEN_HASH_B) : null);
    }
    
    /**
     * @param linkData the link data, in hexadecimal format
     * @return this builder
     */
    public StateBlockBuilder setLinkData(HexData linkData) {
        this.linkData = linkData;
        this.linkAccount = null;
        return this;
    }
    
    /**
     * @param linkAccount the link data, as an account
     * @return this builder
     */
    public StateBlockBuilder setLinkAccount(String linkAccount) {
        return setLinkAccount((NanoAccount)JNH.nullable(linkAccount, NanoAccount::parse));
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
        return new StateBlock(subtype, signature, work, accountAddress,
                Objects.requireNonNullElse(previousBlockHash, JNH.ZEROES_64_HD),
                representativeAddress, balance,
                (linkData == null && linkAccount == null) ? JNH.ZEROES_64_HD : linkData, linkAccount);
    }
    
}
