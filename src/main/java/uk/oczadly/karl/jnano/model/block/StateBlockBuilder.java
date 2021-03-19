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
        if (subtype == null) throw new IllegalArgumentException("Subtype cannot be null.");
        this.subtype = subtype;
        return this;
    }
    
    /**
     * Sets the {@code subtype} field of the block.
     * @param subtype the block subtype (case insensitive)
     * @return this builder instance
     * @throws IllegalArgumentException if the subtype string isn't a recognized subtype value
     * @see #setSubtype(StateBlockSubType)
     */
    public synchronized StateBlockBuilder setSubtype(String subtype) {
        if (subtype == null) throw new IllegalArgumentException("Subtype cannot be null.");
        StateBlockSubType parsed = StateBlockSubType.getFromName(subtype.trim());
        if (parsed == null) throw new IllegalArgumentException("Unrecognized subtype '" + subtype + "'.");
        return setSubtype(parsed);
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
     * <p>All fields in the address format will use the prefix specified in the {@code account} field.</p>
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
     * <p>All fields in the address format will use the prefix specified in the {@code account} field if set, otherwise
     * the {@link NanoAccount#DEFAULT_PREFIX default prefix} will be used.</p>
     *
     * @param privateKey the private key of the account used to sign the block
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     * @throws BlockCreationException if there is an error with block creation (eg. invalid argument, work generation)
     */
    public synchronized StateBlock buildAndSign(String privateKey) {
        return buildAndSign(privateKey, account != null ? account.getPrefix() : NanoAccount.DEFAULT_PREFIX);
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
     * <p>All fields in the address format will use the prefix specified in the {@code account} field if set, otherwise
     * the {@link NanoAccount#DEFAULT_PREFIX default prefix} will be used.</p>
     *
     * @param privateKey the private key of the account used to sign the block
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     * @throws BlockCreationException if there is an error with block creation (eg. invalid argument, work generation)
     */
    public synchronized StateBlock buildAndSign(HexData privateKey) {
        return buildAndSign(privateKey, account != null ? account.getPrefix() : NanoAccount.DEFAULT_PREFIX);
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
     * <p>All fields in the address format will use the prefix specified when calling this method.</p>
     *
     * @param privateKey    the private key of the account used to sign the block
     * @param addressPrefix the address prefix to be applied to account-related fields
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     * @throws BlockCreationException if there is an error with block creation (eg. invalid argument, work generation)
     */
    public synchronized StateBlock buildAndSign(String privateKey, String addressPrefix) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        return buildAndSign(new HexData(privateKey), addressPrefix);
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
     * <p>All fields in the address format will use the prefix specified when calling this method.</p>
     *
     * @param privateKey the private key of the account used to sign the block
     * @param addressPrefix the address prefix to be applied to account-related fields
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     * @throws BlockCreationException if there is an error with block creation (eg. invalid argument, work generation)
     */
    public synchronized StateBlock buildAndSign(HexData privateKey, String addressPrefix) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        
        NanoAccount account = NanoAccount.fromPrivateKey(privateKey, addressPrefix);
        StateBlock sb = build(subtype, null, work, workGenerator, account, prevHash, rep, balance, linkData,
                linkAccount);
        sb.sign(privateKey); // Sign the block
        return sb;
    }
    
    private static StateBlock build(StateBlockSubType subtype, HexData signature, WorkSolution work,
                                    WorkGenerator workGen, NanoAccount account, HexData prevHash, NanoAccount rep,
                                    NanoAmount bal, HexData linkHex, NanoAccount linkAcc) {
        // Basic validation
        if (subtype == null) throw new BlockCreationException("Block subtype has not been set.");
        if (account == null) throw new BlockCreationException("Account field has not been set.");
        if (bal == null) throw new BlockCreationException("Balance field has not been set.");
        if (prevHash == null && subtype.requiresPrevious())
            throw new BlockCreationException("Previous field has not been set.");
        if (linkHex == null && linkAcc == null && subtype.getLinkIntent() != LinkData.Intent.UNUSED)
            throw new BlockCreationException("Link field has not been set.");
        
        // Construct block
        StateBlock block;
        try {
            block = new StateBlock(subtype, signature, work, account,
                    (prevHash == null || subtype == StateBlockSubType.OPEN) ? JNC.ZEROES_64_HD : prevHash,
                    rep == null ? account : rep, bal, linkHex, linkAcc);
        } catch (RuntimeException e) {
            throw new BlockCreationException(e);
        }
        
        // Generate work
        if (work == null && workGen != null) {
            try {
                block.setWorkSolution(workGen.generate(block).get().getWork());
            } catch (ExecutionException e) {
                throw new BlockCreationException("Couldn't generate work.", e.getCause());
            } catch (InterruptedException e) {
                throw new BlockCreationException("Work generation was interrupted.", e);
            }
        }
        return block;
    }
    
    
    /**
     * Constructs a new StateBlockBuilder based on the fields of the given block.
     * @param block the block to copy from
     * @return a new builder object
     */
    public static StateBlockBuilder copyOf(StateBlock block) {
        return new StateBlockBuilder(block);
    }
    
    
    /**
     * Constructs a new {@link StateBlockBuilder} which acts as an {@link StateBlockSubType#OPEN open} block,
     * initializing the account and receiving a pending transactional block.
     *
     * <p>This will <em>not</em> assign any values to the {@code account}, {@code work} or {@code signature} fields.
     * You will need to manually assign these values yourself using the setter or {@code build} methods.</p>
     *
     * @param srcHash   the hash of the pending {@code send} block
     * @param srcAmount the amount which the pending {@code send} block sent to this account
     * @return a new builder object for the first block in the account
     */
    public static StateBlockBuilder open(HexData srcHash, NanoAmount srcAmount) {
        if (srcHash == null) throw new IllegalArgumentException("Source hash cannot be null.");
        if (srcAmount == null) throw new IllegalArgumentException("Source amount cannot be null.");
        
        return new StateBlockBuilder(StateBlockSubType.OPEN)
                .setBalance(srcAmount)
                .setLink(srcHash);
    }
    
    /**
     * Constructs a new {@link StateBlockBuilder} which acts as a {@link StateBlockSubType#RECEIVE receive} block,
     * receiving a pending transactional block into this account.
     *
     * <p>This will <em>not</em> assign any values to the {@code work} or {@code signature} fields. You will need to
     * manually assign these values yourself using the setter or {@code build} methods.</p>
     *
     * @param previous  the current frontier block of the account which this block is being created for
     * @param srcHash   the hash of the pending {@code send} block
     * @param srcAmount the amount which the pending {@code send} block sent to this account
     * @return a new builder object for the next block in the account
     * @throws IllegalArgumentException if the account is attempting to receive more funds than possible
     *                                  ({@code balance + srcAmount > MAX_BALANCE})
     */
    public static StateBlockBuilder receive(StateBlock previous, HexData srcHash, NanoAmount srcAmount) {
        if (previous == null) throw new IllegalArgumentException("Previous block cannot be null.");
        if (srcHash == null) throw new IllegalArgumentException("Source hash cannot be null.");
        if (srcAmount == null) throw new IllegalArgumentException("Source amount cannot be null.");
        try {
            return next(previous)
                    .setSubtype(StateBlockSubType.RECEIVE)
                    .setLink(srcHash)
                    .setBalance(previous.getBalance().add(srcAmount));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Receiving more funds than possible.");
        }
    }
    
    /**
     * Constructs a new {@link StateBlockBuilder} which acts as a {@link StateBlockSubType#SEND send} block, sending
     * the specified amount of funds to the given account.
     *
     * <p>This will <em>not</em> assign any values to the {@code work} or {@code signature} fields. You will need to
     * manually assign these values yourself using the setter or {@code build} methods.</p>
     *
     * @param previous    the current frontier block of the account which this block is being created for
     * @param destination the account where the funds will be sent
     * @param amount      the amount of funds to be sent to {@code destination}
     * @return a new builder object for the next block in the account
     * @throws IllegalArgumentException if there aren't enough funds to send ({@code amount > balance})
     */
    public static StateBlockBuilder send(StateBlock previous, NanoAccount destination, NanoAmount amount) {
        if (previous == null) throw new IllegalArgumentException("Previous block cannot be null.");
        if (destination == null) throw new IllegalArgumentException("Destination account cannot be null.");
        if (amount == null) throw new IllegalArgumentException("Send amount cannot be null.");
        if (amount.compareTo(previous.getBalance()) > 0)
            throw new IllegalArgumentException("Attempting to send more funds than the account has.");
        
        return next(previous)
                .setSubtype(StateBlockSubType.SEND)
                .setLink(destination)
                .setBalance(previous.getBalance().subtract(amount));
    }
    
    /**
     * Constructs a new {@link StateBlockBuilder} which acts as a {@link StateBlockSubType#SEND send} block, sending
     * the entire balance to the given account.
     *
     * <p>This will <em>not</em> assign any values to the {@code work} or {@code signature} fields. You will need to
     * manually assign these values yourself using the setter or {@code build} methods.</p>
     *
     * @param previous    the current frontier block of the account which this block is being created for
     * @param destination the account where the funds will be sent
     * @return a new builder object for the next block in the account
     * @throws IllegalArgumentException if the account has no balance/funds to send
     */
    public static StateBlockBuilder sendAll(StateBlock previous, NanoAccount destination) {
        if (previous == null) throw new IllegalArgumentException("Previous block cannot be null.");
        if (destination == null) throw new IllegalArgumentException("Destination account cannot be null.");
        if (previous.getBalance().compareTo(NanoAmount.ZERO) <= 0)
            throw new IllegalArgumentException("Account has zero balance.");
        
        return next(previous)
                .setSubtype(StateBlockSubType.SEND)
                .setLink(destination)
                .setBalance(NanoAmount.ZERO);
    }
    
    /**
     * Constructs a new {@link StateBlockBuilder} which acts as a {@link StateBlockSubType#CHANGE change} block,
     * changing the account's representative.
     *
     * <p>This will <em>not</em> assign any values to the {@code work} or {@code signature} fields. You will need to
     * manually assign these values yourself using the setter or {@code build} methods.</p>
     *
     * @param previous       the current frontier block of the account which this block is being created for
     * @param representative the new representative of the account
     * @return a new builder object for the next block in the account
     * @throws IllegalArgumentException if the representative is already set to the given account
     */
    public static StateBlockBuilder change(StateBlock previous, NanoAccount representative) {
        if (previous == null) throw new IllegalArgumentException("Previous block cannot be null.");
        if (representative == null) throw new IllegalArgumentException("Representative account cannot be null.");
        if (previous.getRepresentative().equalsIgnorePrefix(representative))
            throw new IllegalArgumentException("Representative is already set to the given account.");
        
        return next(previous)
                .setSubtype(StateBlockSubType.CHANGE)
                .setRepresentative(representative);
    }
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the fields pre-assigned for the next block in the account.
     *
     * <p>This will not set all of the necessary fields, including {@link #setSubtype(StateBlockSubType) subtype},
     * {@link #setLink(HexData) link} (if required), {@link #setWork(WorkSolution) work} and
     * {@link #setSignature(HexData) signature}. The following fields are initialized by this method (and may be
     * manually overridden after):</p>
     * <ul>
     *     <li>{@link #setPreviousHash(HexData) previous} — the hash of {@code frontier}</li>
     *     <li>{@link #setAccount(NanoAccount) account} — the account of {@code frontier}</li>
     *     <li>{@link #setRepresentative(NanoAccount) representative} — the representative of {@code frontier}</li>
     *     <li>{@link #setBalance(NanoAmount) balance} — the same balance as {@code frontier}</li>
     * </ul>
     *
     * @param previous the current frontier block of the account which this block is being created for
     * @return a new builder object for the next block in the account
     */
    public static StateBlockBuilder next(StateBlock previous) {
        if (previous == null)
            throw new IllegalArgumentException("Previous block cannot be null.");
        
        return new StateBlockBuilder()
                .setPreviousHash(previous)
                .setRepresentative(previous.getRepresentative())
                .setAccount(previous.getAccount())
                .setBalance(previous.getBalance());
    }
    
    
    /** Thrown when there is an error with creating a block. */
    public static class BlockCreationException extends RuntimeException {
        BlockCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    
        BlockCreationException(Throwable cause) {
            super(cause);
        }
        
        BlockCreationException(String message) {
            super(message);
        }
    }
    
}
