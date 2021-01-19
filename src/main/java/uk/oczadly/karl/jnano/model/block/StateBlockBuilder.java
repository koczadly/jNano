/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
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
    private NanoAccount account;
    private HexData prevHash;
    private NanoAccount rep;
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
     * @param prevHash     the hash of the previous (or latest) block for the account
     * @param rep the representative for this account
     * @param balance               the (newly updated) balance of this account
     */
    @Deprecated
    public StateBlockBuilder(NanoAccount accountAddress, String prevHash,
                             NanoAccount rep, BigInteger balance) {
        setAccountAddress(accountAddress);
        setPreviousBlockHash(prevHash);
        setRepresentativeAddress(rep);
        setBalance(balance);
    }
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the non-optional parameters.
     *
     * @param accountAddress        the account which owns this block
     * @param rep the representative for this account
     * @param balance               the (newly updated) balance of this account
     */
    @Deprecated
    public StateBlockBuilder(NanoAccount accountAddress, NanoAccount rep, BigInteger balance) {
        setAccountAddress(accountAddress);
        setRepresentativeAddress(rep);
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
        this.account = builder.account;
        this.prevHash = builder.prevHash;
        this.rep = builder.rep;
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
        return account;
    }
    
    public StateBlockBuilder setAccountAddress(NanoAccount account) {
        if (account == null)
            throw new IllegalArgumentException("Account address argument cannot be null.");
        
        this.account = account;
        return this;
    }
    
    public StateBlockBuilder setAccountAddress(String accountAddress) {
        if (accountAddress == null)
            throw new IllegalArgumentException("Account address argument cannot be null.");
        return setAccountAddress(NanoAccount.parseAddress(accountAddress));
    }
    
    
    public String getPreviousBlockHash() {
        return prevHash.toHexString();
    }
    
    public StateBlockBuilder setPreviousBlockHash(String previousBlockHash) {
        return setPreviousBlockHash(previousBlockHash != null
                ? new HexData(previousBlockHash, NanoConst.LEN_HASH_B) : null);
    }
    
    public StateBlockBuilder setPreviousBlockHash(HexData previousBlockHash) {
        this.prevHash = previousBlockHash;
        return this;
    }
    
    
    public NanoAccount getRepresentativeAddress() {
        return rep;
    }
    
    public StateBlockBuilder setRepresentativeAddress(NanoAccount rep) {
        if (rep == null)
            throw new IllegalArgumentException("Representative address argument cannot be null.");
        
        this.rep = rep;
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
        return setBalance(NanoAmount.valueOfRaw(balance));
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
     * @param linkData the link data (intent is ignored)
     * @return this builder
     */
    public StateBlockBuilder setLink(LinkData linkData) {
        switch (linkData.getType()) {
            case ACCOUNT:
                return setLinkAccount(linkData.asAccount());
            case HEXADECIMAL:
                return setLinkData(linkData.asHex());
            case UNUSED:
                return setLinkData(JNC.ZEROES_64_HD);
            default:
                throw new AssertionError("Unexpected link type.");
        }
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
     * Constructs a {@link StateBlock} from the configured parameters.
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     */
    public StateBlock build() {
        return build(subtype, signature, work, account, prevHash, rep, balance, linkData, linkAccount);
    }
    
    /**
     * Constructs a {@link StateBlock} from the configured parameters, and then signs the block.
     * <p>This will override any configured {@code account} and {@code signature} value set in this builder object, but
     * won't update this builder's state.</p>
     *
     * @param privateKey the private key of the account used to sign the block
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     */
    public StateBlock buildAndSign(String privateKey) {
        return buildAndSign(new HexData(privateKey));
    }
    
    /**
     * Constructs a {@link StateBlock} from the configured parameters, and then signs the block.
     * <p>This will override any configured {@code account} and {@code signature} value set in this builder object, but
     * won't update this builder's state.</p>
     *
     * @param privateKey the private key of the account used to sign the block
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     */
    public StateBlock buildAndSign(HexData privateKey) {
        NanoAccount account = NanoAccount.fromPrivateKey(privateKey);
        StateBlock sb = build(subtype, null, work, account, prevHash, rep, balance, linkData, linkAccount);
        sb.sign(privateKey); // Sign the block
        return sb;
    }
    
    private static StateBlock build(StateBlockSubType subtype, HexData signature, WorkSolution work,
                                    NanoAccount account, HexData prevHash, NanoAccount rep, NanoAmount bal,
                                    HexData linkHex, NanoAccount linkAcc) {
        return new StateBlock(subtype, signature, work, account,
                subtype == StateBlockSubType.OPEN ? JNC.ZEROES_64_HD : prevHash,
                rep, bal, linkHex, linkAcc);
    }
    
}
