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
    private NanoAccount account, rep, linkAccount;
    private NanoAmount balance;
    private HexData prevHash, signature;
    private WorkSolution work;
    
    private String addressPrefix;
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
        subtype(subtype);
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
        this.addressPrefix = block.getAccount().getPrefix();
        link(block.getLink());
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
        this.linkAccount = builder.linkAccount;
        this.work = builder.work;
        this.workGenerator = builder.workGenerator;
        this.addressPrefix = builder.addressPrefix;
    }
    
    
    /**
     * Sets the {@code signature} field of the block.
     * @param signature the signature (64-character hexadecimal string)
     * @return this builder instance
     */
    public synchronized StateBlockBuilder signature(String signature) {
        return signature(signature == null ? null : new HexData(signature));
    }
    
    /**
     * Sets the {@code signature} field of the block.
     * @param signature the signature (64-character hexadecimal value)
     * @return this builder instance
     */
    public synchronized StateBlockBuilder signature(HexData signature) {
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
    public synchronized StateBlockBuilder work(WorkSolution work) {
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
    public synchronized StateBlockBuilder work(String work) {
        return work(work != null ? new WorkSolution(work) : null);
    }
    
    /**
     * Sets the {@link WorkGenerator} used to generate the {@code work} value of the block.
     *
     * <p>The work generation will be deferred until one of the {@code build} methods are called. The build method(s)
     * may also throw a {@link BlockCreationException} if the work could not be computed.</p>
     *
     * @param workGenerator the work generator object
     * @return this builder instance
     *
     * @see #work(WorkSolution)
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
    public synchronized StateBlockBuilder subtype(StateBlockSubType subtype) {
        if (subtype == null) throw new IllegalArgumentException("Subtype cannot be null.");
        this.subtype = subtype;
        return this;
    }
    
    /**
     * Sets the {@code subtype} field of the block.
     * @param subtype the block subtype (case insensitive)
     * @return this builder instance
     * @throws IllegalArgumentException if the subtype string isn't a recognized subtype value
     * @see #subtype(StateBlockSubType)
     */
    public synchronized StateBlockBuilder subtype(String subtype) {
        if (subtype == null) throw new IllegalArgumentException("Subtype cannot be null.");
        StateBlockSubType parsed = StateBlockSubType.getFromName(subtype.trim());
        if (parsed == null) throw new IllegalArgumentException("Unrecognized subtype '" + subtype + "'.");
        return subtype(parsed);
    }
    
    
    /**
     * Sets the {@code account} field of the block.
     * @param account the account which owns this block
     * @return this builder instance
     */
    public synchronized StateBlockBuilder account(NanoAccount account) {
        this.account = account;
        return this;
    }
    
    /**
     * Sets the {@code account} field of the block.
     * @param account the account which owns this block
     * @return this builder instance
     */
    public synchronized StateBlockBuilder account(String account) {
        this.account = account == null ? null : NanoAccount.parse(account);
        return this;
    }
    
    
    /**
     * Sets the {@code previous} field to an empty value (all zeroes) if supported by the subtype. If the subtype
     * requires a previous field, then an exception will be thrown when building the block.
     *
     * @return this builder instance
     */
    public synchronized StateBlockBuilder emptyPrevious() {
        this.prevHash = null;
        return this;
    }
    
    /**
     * Sets the {@code previous} field to the hash of the given block.
     *
     * <p>This field doesn't need to be set for {@link StateBlockSubType#OPEN open} subtypes, as a zero-value
     * placeholder will be used instead.</p>
     *
     * @param block the previous block in this account chain
     * @return this builder instance
     */
    public synchronized StateBlockBuilder previous(Block block) {
        return previous(block != null ? block.getHash() : null);
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
    public synchronized StateBlockBuilder previous(String hash) {
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
    public synchronized StateBlockBuilder previous(HexData hash) {
        if (hash != null && hash.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Invalid previous hash length.");
        this.prevHash = hash;
        return this;
    }
    
    
    /**
     * Sets the {@code representative} field of the block.
     * 
     * <p>If this field isn't set, then the {@link #account(NanoAccount) account} field will be used as the
     * representative.</p>
     * 
     * @param representative the representative of the account
     * @return this builder instance
     */
    public synchronized StateBlockBuilder representative(NanoAccount representative) {
        this.rep = representative;
        return this;
    }
    
    /**
     * Sets the {@code representative} field of the block.
     *
     * <p>If this field isn't set, then the {@link #account(NanoAccount) account} field will be used as the
     * representative.</p>
     *
     * @param representative the representative of the account
     * @return this builder instance
     */
    public synchronized StateBlockBuilder representative(String representative) {
        this.rep = representative == null ? null : NanoAccount.parse(representative);
        return this;
    }
    
    
    /**
     * Sets the {@code balance} field of the block to the given {@code raw} value.
     * @param balance the balance of the account after this transaction, in {@link NanoUnit#RAW raw} units
     * @return this builder instance
     */
    public synchronized StateBlockBuilder balance(String balance) {
        if (balance == null) throw new IllegalArgumentException("Balance cannot be null.");
        return balance(NanoAmount.valueOfRaw(balance));
    }
    
    /**
     * Sets the {@code balance} field of the block to the given {@code raw} value.
     * @param balance the balance of the account after this transaction, in {@link NanoUnit#RAW raw} units
     * @return this builder instance
     */
    public synchronized StateBlockBuilder balance(BigInteger balance) {
        if (balance == null) throw new IllegalArgumentException("Balance cannot be null.");
        return balance(NanoAmount.valueOfRaw(balance));
    }
    
    /**
     * Sets the {@code balance} field of the block.
     * @param balance the balance of the account after this transaction
     * @return this builder instance
     */
    public synchronized StateBlockBuilder balance(NanoAmount balance) {
        if (balance == null) throw new IllegalArgumentException("Balance cannot be null.");
        this.balance = balance;
        return this;
    }
    
    
    /**
     * Sets the {@code link} field to an empty value (all zeroes) if supported by the subtype. If the subtype
     * requires a link field, then an exception will be thrown when building the block.
     *
     * @return this builder instance
     */
    public synchronized StateBlockBuilder emptyLink() {
        this.linkAccount = null;
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
     * @see #link(String)
     */
    public synchronized StateBlockBuilder link(LinkData link) {
        return link(link == null ? null : link.asHex());
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
    public synchronized StateBlockBuilder link(String link) {
        return link(link != null ? NanoAccount.parse(link) : null);
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
    public synchronized StateBlockBuilder link(HexData link) {
        if (link != null && link.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Invalid link length.");
        return link(link != null ? new NanoAccount(link.toByteArray()) : null);
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
    public synchronized StateBlockBuilder link(NanoAccount link) {
        this.linkAccount = link;
        return this;
    }
    
    /**
     * Sets the address prefix to be enforced and applied to all account-based fields.
     *
     * <p>If this value isn't set then the prefix will be obtained from the {@link #account(NanoAccount) account}
     * value, otherwise the default {@value NanoAccount#DEFAULT_PREFIX} prefix will be used.</p>
     *
     * @param prefix the address prefix, excluding the separator character (eg "{@code nano}")
     * @return this builder instance
     */
    public synchronized StateBlockBuilder usingAddressPrefix(String prefix) {
        this.addressPrefix = prefix;
        return this;
    }
    
    
    /**
     * Constructs a {@link StateBlock} from the configured parameters.
     *
     * <p>If a {@link #generateWork(WorkGenerator) work generator} is specified, then this method will block until
     * the work has been generated. If the work could not be generated, then this method will throw a
     * {@link BlockCreationException}.</p>
     *
     * <p>Fields with an address format ({@code account}, {@code representative} and {@code link_as_account}) will use
     * the prefix specified by {@link #usingAddressPrefix(String)}. If no value has been set, then the prefix used by
     * the {@code account} field will be used.</p>
     *
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     * @throws BlockCreationException if there is an error with block creation (eg. invalid argument, work generation)
     */
    public synchronized StateBlock build() {
        return build(subtype, signature, work, workGenerator, account, prevHash, rep, balance, linkAccount,
                getAddressPrefix());
    }
    
    /**
     * Constructs a {@link StateBlock} from the configured parameters, and then signs the block.
     *
     * <p>Calling this method and signing the block will override any configured {@code account} and {@code signature}
     * value set in this builder object to match the private key. This will not update the state of this builder.</p>
     *
     * <p>If a {@link #generateWork(WorkGenerator) work generator} is specified, then this method will block until
     * the work has been generated. If the work could not be generated, then this method will throw a
     * {@link BlockCreationException}.</p>
     *
     * <p>Fields with an address format ({@code account}, {@code representative} and {@code link_as_account}) will use
     * the prefix specified by {@link #usingAddressPrefix(String)}. If no value has been set, then the prefix used by
     * the {@code account} field will be used. If no account is set prior to signing, then the default Nano prefix will
     * be used.</p>
     *
     * @param privateKey the private key of the account used to sign the block
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     * @throws BlockCreationException if there is an error with block creation (eg. invalid argument, work generation)
     */
    public synchronized StateBlock buildAndSign(String privateKey) {
        if (privateKey == null) throw new IllegalArgumentException("Private key cannot be null.");
        return buildAndSign(new HexData(privateKey));
    }
    
    /**
     * Constructs a {@link StateBlock} from the configured parameters, and then signs the block.
     *
     * <p>If the {@code account} value is set in the builder, then it <em>must</em> match the account belonging to the
     * given private key, otherwise an exception will be thrown. Calling this method and signing the block will override
     * the {@code signature} value if configured in this builder object. This will not update the state of this builder
     * object, and is only applied to the constructed block.</p>
     *
     * <p>If a {@link #generateWork(WorkGenerator) work generator} is specified, then this method will block until
     * the work has been generated. If the work could not be generated, then this method will throw a
     * {@link BlockCreationException}.</p>
     *
     * <p>Fields with an address format ({@code account}, {@code representative} and {@code link_as_account}) will use
     * the prefix specified by {@link #usingAddressPrefix(String)}. If no value has been set, then the prefix used by
     * the {@code account} field will be used. If no account is set prior to signing, then the default Nano prefix will
     * be used.</p>
     *
     * @param privateKey the private key of the account used to sign the block
     * @return a new instance of the {@link StateBlock} class using the configured parameters
     * @throws BlockCreationException if there is an error with block creation (eg. invalid argument, work generation)
     */
    public synchronized StateBlock buildAndSign(HexData privateKey) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        
        String addressPrefix = getAddressPrefix();
        NanoAccount account = NanoAccount.fromPrivateKey(privateKey, addressPrefix);
        if (this.account != null && !this.account.equalsIgnorePrefix(account))
            throw new BlockCreationException("Private key doesn't match the set account value.");
        
        StateBlock sb = build(subtype, null, work, workGenerator, account, prevHash, rep, balance, linkAccount,
                addressPrefix);
        sb.sign(privateKey); // Sign the block
        return sb;
    }
    
    private static StateBlock build(StateBlockSubType subtype, HexData signature, WorkSolution work,
                                    WorkGenerator workGen, NanoAccount account, HexData prevHash, NanoAccount rep,
                                    NanoAmount bal, NanoAccount linkAcc, String addressPrefix) {
        // Basic validation
        if (subtype == null) throw new BlockCreationException("Block subtype has not been set.");
        if (account == null) throw new BlockCreationException("Account field has not been set.");
        if (bal == null) throw new BlockCreationException("Balance field has not been set.");
        if (prevHash == null && subtype.requiresPrevious())
            throw new BlockCreationException("Previous field has not been set.");
        if (linkAcc == null && subtype.getLinkIntent() != LinkData.Intent.UNUSED)
            throw new BlockCreationException("Link field has not been set.");
        
        // Preprocess objects
        account = account.withPrefix(addressPrefix);
        if (prevHash == null || subtype == StateBlockSubType.OPEN)
            prevHash = JNC.ZEROES_64_HD;
        if (linkAcc != null)
            linkAcc = linkAcc.withPrefix(addressPrefix);
        rep = rep != null ? rep.withPrefix(addressPrefix) : account;
        
        // Construct block
        StateBlock block;
        try {
            block = new StateBlock(subtype, signature, work, account, prevHash, rep, bal, null, linkAcc);
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
    
    private synchronized String getAddressPrefix() {
        if (addressPrefix != null) {
            return addressPrefix;
        } else if (account != null) {
            return account.getPrefix();
        } else {
            return NanoAccount.DEFAULT_PREFIX;
        }
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
        if (srcAmount.compareTo(NanoAmount.ZERO) <= 0)
            throw new IllegalArgumentException("Must receive at least 1 raw.");
        
        return new StateBlockBuilder(StateBlockSubType.OPEN)
                .balance(srcAmount)
                .link(srcHash);
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
        if (srcAmount.compareTo(NanoAmount.ZERO) <= 0)
            throw new IllegalArgumentException("Must receive at least 1 raw.");
        try {
            return next(previous)
                    .subtype(StateBlockSubType.RECEIVE)
                    .link(srcHash)
                    .balance(previous.getBalance().add(srcAmount));
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
        if (amount.compareTo(NanoAmount.ZERO) <= 0)
            throw new IllegalArgumentException("Must send a positive amount.");
        if (amount.compareTo(previous.getBalance()) > 0)
            throw new IllegalArgumentException("Attempting to send more funds than the account has.");
        
        return next(previous)
                .subtype(StateBlockSubType.SEND)
                .link(destination)
                .balance(previous.getBalance().subtract(amount));
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
            throw new IllegalArgumentException("Account has no funds to send.");
        
        return next(previous)
                .subtype(StateBlockSubType.SEND)
                .link(destination)
                .balance(NanoAmount.ZERO);
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
                .subtype(StateBlockSubType.CHANGE)
                .representative(representative);
    }
    
    /**
     * Constructs a new {@link StateBlockBuilder} with the fields pre-assigned for the next block in the account.
     *
     * <p>This will not set all of the necessary fields, including {@link #subtype(StateBlockSubType) subtype},
     * {@link #link(HexData) link} (if required), {@link #work(WorkSolution) work} and
     * {@link #signature(HexData) signature}. The following fields are initialized by this method (and may be
     * manually overridden after):</p>
     * <ul>
     *     <li>{@link #previous(HexData) previous} — the hash of {@code frontier}</li>
     *     <li>{@link #account(NanoAccount) account} — the account of {@code frontier}</li>
     *     <li>{@link #representative(NanoAccount) representative} — the representative of {@code frontier}</li>
     *     <li>{@link #balance(NanoAmount) balance} — the same balance as {@code frontier}</li>
     * </ul>
     *
     * @param previous the current frontier block of the account which this block is being created for
     * @return a new builder object for the next block in the account
     */
    public static StateBlockBuilder next(StateBlock previous) {
        if (previous == null)
            throw new IllegalArgumentException("Previous block cannot be null.");
        
        return new StateBlockBuilder()
                .account(previous.getAccount())
                .previous(previous)
                .representative(previous.getRepresentative())
                .balance(previous.getBalance());
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
