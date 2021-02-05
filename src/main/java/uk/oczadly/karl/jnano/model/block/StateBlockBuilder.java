/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.NanoUnit;
import uk.oczadly.karl.jnano.util.workgen.WorkGenerator;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

/**
 * This class can be used to easily construct new {@link StateBlock} instances from a set of parameters.
 *
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
    private WorkGenerator workGenerator;
    
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the no parameters.
     */
    public StateBlockBuilder() {}
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the specified subtype.
     *
     * @param subtype the block subtype
     */
    public StateBlockBuilder(StateBlockSubType subtype) {
        setSubtype(subtype);
    }
    
    /**
     * Constructs a new {@link StateBlockBuilder} from an existing {@link StateBlock}.
     *
     * @param block the state block to copy from
     */
    public StateBlockBuilder(StateBlock block) {
        this.subtype = block.getSubType();
        this.account = block.getAccount();
        this.prevHash = block.getPreviousBlockHash();
        this.rep = block.getRepresentative();
        this.balance = block.getBalance();
        this.signature = block.getSignature();
        this.work = block.getWorkSolution();
        setLink(block.getLink());
    }
    
    /**
     * Clones an existing {@link StateBlockBuilder}.
     *
     * @param builder the builder to copy from
     */
    public StateBlockBuilder(StateBlockBuilder builder) {
        this.subtype = builder.subtype;
        this.account = builder.account;
        this.prevHash = builder.prevHash;
        this.rep = builder.rep;
        this.balance = builder.balance;
        this.signature = builder.signature;
        this.linkData = builder.linkData;
        this.linkAccount = builder.linkAccount;
        this.work = builder.work;
        this.workGenerator = builder.workGenerator;
    }
    
    
    /**
     * Sets the {@code signature} field of the block.
     * @param signature the signature (64-character hexadecimal string)
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setSignature(String signature) {
        return setSignature(signature == null ? null : new HexData(signature));
    }
    
    /**
     * Sets the {@code signature} field of the block.
     * @param signature the signature (64-character hexadecimal value)
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setSignature(HexData signature) {
        if (signature != null && signature.length() != NanoConst.LEN_SIGNATURE_B)
            throw new IllegalArgumentException("Invalid signature length.");
    
        this.signature = signature;
        return this;
    }
    
    
    /**
     * Sets the {@code work} field of the block.
     * @param work the computed work value
     * @return this builder instance
     *
     * @see #generateWork(WorkGenerator)
     */
    public synchronized StateBlockBuilder setWork(WorkSolution work) {
        this.work = work;
        return this;
    }
    
    /**
     * Sets the {@code work} field of the block.
     * @param work the computed work value (16-character hexadecimal value)
     * @return this builder instance
     *
     * @see #generateWork(WorkGenerator)
     */
    public synchronized StateBlockBuilder setWork(String work) {
        return setWork(work != null ? new WorkSolution(work) : null);
    }
    
    /**
     * Sets the {@link WorkGenerator} used to generate the {@code work} value of the block.
     *
     * <p>The work will be computed when calling one of the {@code build} methods. The build method(s) may also throw a
     * {@link BlockCreationException} if the work could not be computed.</p>
     *
     * @param workGenerator the work generator object
     * @return this builder instance
     *
     * @see #setWork(WorkSolution)
     */
    public synchronized StateBlockBuilder generateWork(WorkGenerator workGenerator) {
        this.workGenerator = workGenerator;
        this.work = null;
        return this;
    }
    
    
    /**
     * Sets the {@code subtype} field of the block.
     * @param subtype the block subtype
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setSubtype(StateBlockSubType subtype) {
        if (subtype == null)
            throw new IllegalArgumentException("Subtype cannot be null.");
        this.subtype = subtype;
        return this;
    }
    
    
    /**
     * Sets the {@code account} field of the block.
     * @param account the account which owns this block
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setAccount(NanoAccount account) {
        this.account = account;
        return this;
    }
    
    /**
     * Sets the {@code account} field of the block.
     * @param account the account which owns this block
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setAccount(String account) {
        this.account = account == null ? null : NanoAccount.parse(account);
        return this;
    }
    
    
    /**
     * Sets the {@code previous} field of the block.
     *
     * <p>This field doesn't need to be set for {@link StateBlockSubType#OPEN open} subtypes, as a zero-value hash
     * will be used instead.</p>
     *
     * @param block the previous block in this account chain
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setPreviousHash(Block block) {
        this.prevHash = block == null ? null : block.getHash();
        return this;
    }
    
    /**
     * Sets the {@code previous} field of the block.
     *
     * <p>This field doesn't need to be set for {@link StateBlockSubType#OPEN open} subtypes, as a zero-value hash
     * will be used instead.</p>
     *
     * @param hash the hash of the previous block in this account chain (64-character hexadecimal string)
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setPreviousHash(String hash) {
        if (hash != null && hash.length() != NanoConst.LEN_HASH_H)
            throw new IllegalArgumentException("Invalid previous hash length.");
        
        this.prevHash = hash == null ? null : new HexData(hash);
        return this;
    }
    
    /**
     * Sets the {@code previous} field of the block.
     *
     * <p>This field doesn't need to be set for {@link StateBlockSubType#OPEN open} subtypes, as a zero-value hash
     * will be used instead.</p>
     *
     * @param hash the hash of the previous block in this account chain (64-character hexadecimal value)
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setPreviousHash(HexData hash) {
        if (hash != null && hash.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Invalid previous hash length.");
        
        this.prevHash = hash;
        return this;
    }
    
    
    /**
     * Sets the {@code representative} field of the block.
     * 
     * <p>If this field isn't set, then the {@link #setAccount(NanoAccount) account} field will be used as the
     * representative.</p>
     * 
     * @param representative the representative of the account
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setRepresentative(NanoAccount representative) {
        this.rep = representative;
        return this;
    }
    
    /**
     * Sets the {@code representative} field of the block.
     *
     * <p>If this field isn't set, then the {@link #setAccount(NanoAccount) account} field will be used as the
     * representative.</p>
     *
     * @param representative the representative of the account
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setRepresentative(String representative) {
        this.rep = representative == null ? null : NanoAccount.parse(representative);
        return this;
    }
    
    
    /**
     * Sets the {@code balance} field of the block.
     * @param balance the balance of the account after this transaction, in {@link NanoUnit#RAW raw}
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setBalance(String balance) {
        if (balance == null)
            throw new IllegalArgumentException("Balance cannot be null.");
        return setBalance(NanoAmount.valueOfRaw(balance));
    }
    
    /**
     * Sets the {@code balance} field of the block.
     * @param balance the balance of the account after this transaction, in {@link NanoUnit#RAW raw}
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setBalance(BigInteger balance) {
        if (balance == null)
            throw new IllegalArgumentException("Balance cannot be null.");
        return setBalance(NanoAmount.valueOfRaw(balance));
    }
    
    /**
     * Sets the {@code balance} field of the block.
     * @param balance the balance of the account after this transaction
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setBalance(NanoAmount balance) {
        if (balance == null)
            throw new IllegalArgumentException("Balance cannot be null.");
        this.balance = balance;
        return this;
    }
    
    
    /**
     * Sets the {@code link} field of the block, ignoring the intent value of the {@code LinkData} object.
     *
     * <p>Do not use this method if you are constructing the link data yourself, this should only be used when
     * copying from a pre-existing block. This field doesn't need to be set for {@link StateBlockSubType#CHANGE change}
     * subtypes, as the value will be ignored.</p>
     *
     * @param link the {@link LinkData} object (intent is ignored)
     * @return this builder instance
     *
     * @see #setLink(String)
     */
    public synchronized StateBlockBuilder setLink(LinkData link) {
        this.linkData = link == null ? null : link.asHex();
        return this;
    }
    
    /**
     * Sets the {@code link} field of the block, in either account or hexadecimal format.
     *
     * <p>This field doesn't need to be set for {@link StateBlockSubType#CHANGE change} subtypes, as the value will be
     * ignored.</p>
     *
     * @param link the link value, either in hexadecimal (64-character) or account format
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setLink(String link) {
        setLink(link != null ? NanoAccount.parse(link) : null);
        return this;
    }
    
    /**
     * Sets the {@code link} field of the block.
     *
     * <p>This field doesn't need to be set for {@link StateBlockSubType#CHANGE change} subtypes, as the value will be
     * ignored.</p>
     *
     * @param link the link value, in hexadecimal format (64-character hex string)
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setLink(HexData link) {
        if (link != null && link.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Invalid link length.");
        
        this.linkData = link;
        this.linkAccount = null;
        return this;
    }
    
    /**
     * Sets the {@code link} field of the block.
     *
     * <p>This field doesn't need to be set for {@link StateBlockSubType#CHANGE change} subtypes, as the value will be
     * ignored.</p>
     *
     * @param link the link value, in an account format
     * @return this builder instance
     */
    public synchronized StateBlockBuilder setLink(NanoAccount link) {
        this.linkData = null;
        this.linkAccount = link;
        return this;
    }
    
    
    /**
     * Constructs a {@link StateBlock} from the configured parameters.
     *
     * <p>If a {@link #generateWork(WorkGenerator) work generator} is specified, then this method will block until
     * the work has been generated. If the work could not be generated, then this method will throw a
     * {@link BlockCreationException}.</p>
     *
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     * @throws BlockCreationException if there is an error with block creation (eg. invalid argument, work generation)
     */
    public synchronized StateBlock build() {
        return build(subtype, signature, work, workGenerator, account, prevHash, rep, balance, linkData, linkAccount);
    }
    
    /**
     * Constructs a {@link StateBlock} from the configured parameters, and then signs the block.
     *
     * <p>If a {@link #generateWork(WorkGenerator) work generator} is specified, then this method will block until
     * the work has been generated. If the work could not be generated, then this method will throw a
     * {@link BlockCreationException}.</p>
     *
     * <p>Calling this method and signing the block will override any configured {@code account} and {@code signature}
     * value set in this builder object to match the private key. This will not update the state of this builder.</p>
     *
     * @param privateKey the private key of the account used to sign the block
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     * @throws BlockCreationException if there is an error with block creation (eg. invalid argument, work generation)
     */
    public synchronized StateBlock buildAndSign(String privateKey) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        return buildAndSign(new HexData(privateKey));
    }
    
    /**
     * Constructs a {@link StateBlock} from the configured parameters, and then signs the block.
     *
     * <p>If a {@link #generateWork(WorkGenerator) work generator} is specified, then this method will block until
     * the work has been generated. If the work could not be generated, then this method will throw a
     * {@link BlockCreationException}.</p>
     *
     * <p>Calling this method and signing the block will override any configured {@code account} and {@code signature}
     * value set in this builder object to match the private key. This will not update the state of this builder.</p>
     *
     * @param privateKey the private key of the account used to sign the block
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     * @throws BlockCreationException if there is an error with block creation (eg. invalid argument, work generation)
     */
    public synchronized StateBlock buildAndSign(HexData privateKey) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        
        NanoAccount account = NanoAccount.fromPrivateKey(privateKey);
        StateBlock sb = build(subtype, null, work, workGenerator, account, prevHash, rep, balance, linkData,
                linkAccount);
        sb.sign(privateKey); // Sign the block
        return sb;
    }
    
    private static StateBlock build(StateBlockSubType subtype, HexData signature, WorkSolution work,
                                    WorkGenerator workGen, NanoAccount account, HexData prevHash, NanoAccount rep,
                                    NanoAmount bal, HexData linkHex, NanoAccount linkAcc) {
        StateBlock block;
        try {
            block = new StateBlock(
                    subtype, signature, work, account,
                    subtype == StateBlockSubType.OPEN ? JNC.ZEROES_64_HD : prevHash,
                    rep == null ? account : rep,
                    bal, linkHex, linkAcc);
        } catch (RuntimeException e) {
            throw new BlockCreationException(e);
        }
    
        // Work
        if (work == null && workGen != null) {
            try {
                block.setWorkSolution(workGen.generate(block).get());
            } catch (InterruptedException | ExecutionException e) {
                throw new BlockCreationException("Couldn't generate work.", e);
            }
        }
        return block;
    }
    
    
    /** Thrown when there is an error with creating a block. */
    public static class BlockCreationException extends RuntimeException {
        BlockCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    
        BlockCreationException(Throwable cause) {
            super(cause);
        }
    }
    
}
