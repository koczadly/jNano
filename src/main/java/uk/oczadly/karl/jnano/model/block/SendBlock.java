/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockBalance;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a {@code send} block, and the associated data.
 *
 * <p>Note that this is a legacy block and has since been officially deprecated. For new blocks, use
 * {@link StateBlock state} blocks.</p>
 */
public class SendBlock extends Block implements IBlockPrevious, IBlockBalance {
    
    /** A function which converts a {@link JsonObject} into a {@link SendBlock} instance. */
    public static final Function<JsonObject, SendBlock> DESERIALIZER = json -> new SendBlock(
            JNH.getJson(json, "signature"),
            JNH.getJson(json, "work", WorkSolution::new),
            JNH.getJson(json, "previous"),
            JNH.getJson(json, "destination", NanoAccount::parseAddress),
            (NanoAmount)JNH.getJson(json, "balance", NanoAmount::new));
    
    private static final BlockIntent INTENT = new BlockIntent(true, false, false, false, false, false);
    
    
    @Expose @SerializedName("previous")
    private final String previousBlockHash;
    
    @Expose @SerializedName("destination")
    private final NanoAccount destinationAccount;
    
    @Expose @SerializedName("balance")
    private final NanoAmount balance;
    
    
    @Deprecated
    public SendBlock(String signature, WorkSolution work, String previousBlockHash, NanoAccount destinationAccount,
                     BigInteger balance) {
        this(signature, work, previousBlockHash, destinationAccount, new NanoAmount(balance));
    }
    
    public SendBlock(String signature, WorkSolution work, String previousBlockHash, NanoAccount destinationAccount,
                     NanoAmount balance) {
        super(BlockType.SEND, signature, work);
    
        if (previousBlockHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNH.isValidHex(previousBlockHash, HASH_LENGTH))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (destinationAccount == null) throw new IllegalArgumentException("Block destination account cannot be null.");
        if (balance == null) throw new IllegalArgumentException("Account balance cannot be null.");
        
        this.previousBlockHash = previousBlockHash;
        this.destinationAccount = destinationAccount;
        this.balance = balance;
    }
    
    
    @Override
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    /**
     * @return the destination account which the funds will be sent to
     */
    public final NanoAccount getDestinationAccount() {
        return destinationAccount;
    }
    
    @Override
    public final NanoAmount getBalance() {
        return balance;
    }
    
    @Override
    public BlockIntent getIntent() {
        return INTENT;
    }
    
    @Override
    public boolean contentEquals(Block block) {
        if (!(block instanceof SendBlock)) return false;
        SendBlock sb = (SendBlock)block;
        return super.contentEquals(sb)
                && Objects.equals(getBalance(), sb.getBalance())
                && Objects.equals(getDestinationAccount(), sb.getDestinationAccount())
                && Objects.equals(getPreviousBlockHash(), sb.getPreviousBlockHash());
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                JNH.ENC_16.decode(getPreviousBlockHash()),
                getDestinationAccount().getPublicKeyBytes(),
                JNH.leftPadByteArray(getBalance().getAsRaw().toByteArray(), 16, false)
        };
    }
    
    
    /**
     * Parses a {@code send} block from a given JSON string using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link SendBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(String)
     */
    public static SendBlock parse(String json) {
        return JNH.tryRethrow(Block.parse(json), b -> (SendBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not a send block.", e));
    }
    
    /**
     * Parses a {@code send} block from a given {@link JsonObject} instance using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link SendBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(JsonObject)
     */
    public static SendBlock parse(JsonObject json) {
        return JNH.tryRethrow(Block.parse(json), b -> (SendBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not a send block.", e));
    }
    
}
