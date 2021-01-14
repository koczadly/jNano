/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

/**
 * This class can be used to represent a 32-byte link data field.
 *
 * <p>Convenience methods are supplied to retrieve the data in different formats.</p>
 */
public class LinkData {
    
    private static final int SIZE = 32;

    private final Intent intent;
    private final HexData hex;
    private final NanoAccount account;
    
    /**
     * Constructs a new LinkData.
     *
     * <p>Either one of the {@code hex} or {@code account} values may be null, or both can be null if the intent
     * indicates an unused value.</p>
     *
     * @param intent  the intent
     * @param hex     the link data, as hex
     * @param account the link data, as an account
     */
    public LinkData(Intent intent, HexData hex, NanoAccount account) {
        // Validate arguments
        if (intent == null)
            throw new IllegalArgumentException("Intent cannot be null.");
        if (hex == null && account == null) {
            if (intent != Intent.UNUSED)
                throw new IllegalArgumentException("Link data/account fields cannot be null.");
            hex = JNH.ZEROES_64_HD; // Allow null for both values if unused
        }
        if (!JNH.isValidLength(hex, SIZE))
            throw new IllegalArgumentException("Link data is an invalid length.");
        if (hex != null && account != null && !Arrays.equals(account.getPublicKeyBytes(), hex.toByteArray()))
            throw new IllegalArgumentException("Link data mismatch.");
        
        this.intent = intent;
        this.hex = hex != null ? hex : new HexData(account.getPublicKeyBytes(), SIZE);
        this.account = account != null ? account : new NanoAccount(hex.toByteArray());
    }
    
    
    /**
     * Returns this link data as an array of 32 bytes.
     * @return this link data as a byte array
     */
    public final byte[] asByteArray() {
        return asHex().toByteArray();
    }
    
    /**
     * Returns this link data as a 64-character {@link HexData hexadecimal value}.
     * @return this link data as an account
     */
    public final HexData asHex() {
        return hex;
    }
    
    /**
     * Returns this link data as an {@link NanoAccount account}.
     * @return this link data as an account
     */
    public final NanoAccount asAccount() {
        return account;
    }
    
    
    /**
     * Returns the intent of the data that this link field represents (eg. destination account, source hash).
     * @return the link intent
     */
    public final Intent getIntent() {
        return intent;
    }
    
    /**
     * Returns the type of data represented by this link field (eg. hexadecimal, account).
     * @return the data type
     */
    public final Type getType() {
        return intent.getFormat();
    }
    
    
    /**
     * Returns a friendly string representation of the link data. This method internally calls
     * {@link Type#format(LinkData)} on this LinkData object.
     *
     * <p>The output of this method should <em>not</em> be used for data serialization — use the appropriate getter
     * methods instead.</p>
     *
     * @return the a friendly formatted representation of the data
     */
    @Override
    public String toString() {
        return getType().format(this);
    }
    
    
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkData)) return false;
        LinkData linkData = (LinkData)o;
        return intent == linkData.intent && Objects.equals(hex, linkData.hex);
    }
    
    @Override
    public final int hashCode() {
        return Objects.hash(intent, hex);
    }
    
    /**
     * This enum represents the different types and formats of link data.
     */
    public enum Type {
        /** Encoded as an address.
         * @see LinkData#asAccount() */
        ACCOUNT     (l -> l.asAccount().toAddress()),
        
        /** Encoded as a 64-character hexadecimal string.
         * @see LinkData#asHex() */
        HEXADECIMAL (l -> l.asHex().toHexString()),
        
        /** The link field is not used for this block. */
        UNUSED      (b -> "N/A");
        
        
        final Function<LinkData, String> strFunc;
        Type(Function<LinkData, String> strFunc) {
            this.strFunc = strFunc;
        }
        
        /**
         * Returns the link data of the given block, encoded in this format (as a string).
         * <p>The following formats are used:</p>
         * <ul>
         *     <li>{@link Type#ACCOUNT} - the full address ({@link NanoAccount#toAddress()})</li>
         *     <li>{@link Type#HEXADECIMAL} - 64-character hexadecimal</li>
         *     <li>{@link Type#UNUSED} - "N/A"</li>
         * </ul>
         * @param data the link data to format
         * @return a string representation of the link data
         */
        public String format(LinkData data) {
            if (data == null) throw new IllegalArgumentException("Block cannot be null.");
            return strFunc.apply(data);
        }
    }
    
    /**
     * This enum contains constants which represent the different types of information that the link data can represent.
     */
    public enum Intent {
        /** A destination address for an outgoing transaction. */
        DESTINATION_ACCOUNT (Type.ACCOUNT),
        
        /** A hash of the source block for an incoming transaction. */
        SOURCE_HASH         (Type.HEXADECIMAL),
        
        /** An identification value of an epoch upgrade. */
        EPOCH_IDENTIFIER    (Type.HEXADECIMAL),
        
        /** The link field is not used to represent any data. */
        UNUSED              (Type.UNUSED);
        
        final Type type;
        Intent(Type type) {
            this.type = type;
        }
        
        /**
         * @return the intended format of this data field
         */
        public Type getFormat() {
            return type;
        }
    }

}
