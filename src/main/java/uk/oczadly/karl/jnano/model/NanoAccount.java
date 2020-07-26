package uk.oczadly.karl.jnano.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.rfksystems.blake2b.Blake2b;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.internal.utils.BaseEncoder;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p>This class represents an immutable Nano account address string. A wide range of Java-based utilities are provided
 * through the methods of this class, without needing to configure or connect to an external node.</p>
 * <p>To instantiate this class, use the provided static methods (eg. {@link #parse(String)}), or use the
 * constructor to clone an existing object.</p>
 * <p>Using the built-in JSON adapter, this class will call {@link #parse(String)} for deserializing, allowing
 * accounts encoded in either address or public key formats. When serializing this class, the {@link #toAddress()}
 * method will be used to convert into address format.</p>
 */
@JsonAdapter(NanoAccount.Adapter.class)
public final class NanoAccount {
    
    /**
     * The character which separates the prefix from the address string.
     */
    public static final char PREFIX_SEPARATOR_CHAR = '_';
    
    /**
     * The default Nano prefix, officially used on the live network. This value does not contain the additional
     * separator char ({@link #PREFIX_SEPARATOR_CHAR}).
     */
    public static final String DEFAULT_PREFIX = "nano";
    
    private static final String[] DEFAULT_ALLOWED_PREFIXES = {DEFAULT_PREFIX, "xrb"};
    
    
    private final byte[] keyBytes;
    private final String prefix;
    // Fields below may be initialized lazily
    private volatile byte[] checksumBytes;
    private volatile String cachedAddress, publicKeyHex, segAddress, segChecksum;
    
    /**
     * Copies an existing {@link NanoAccount} object, but using a different protocol prefix.
     * @param address   the address to clone
     * @param newPrefix the prefix to assign, or null for no prefix
     * @see #parse(String)
     * @deprecated Use of {@link #withPrefix(String)} method is preferred for better clarity
     */
    @Deprecated
    public NanoAccount(NanoAccount address, String newPrefix) {
        this(newPrefix, address.keyBytes, address.checksumBytes, null, address.publicKeyHex,
                address.segAddress, address.segChecksum);
    }
    
    /**
     * Constructs an AccountAddress instance from an array of key bytes, using the default prefix.
     * @param keyBytes an array of 32 bytes representing the key
     * @see #parse(String)
     */
    public NanoAccount(byte[] keyBytes) {
        this(keyBytes, DEFAULT_PREFIX);
    }
    
    /**
     * Constructs an AccountAddress instance from an array of key bytes, using the specified prefix.
     * @param keyBytes an array of 32 bytes representing the key
     * @param prefix   the protocol identifier prefix (without separator), or null for no prefix
     * @see #parse(String)
     */
    public NanoAccount(byte[] keyBytes, String prefix) {
        this(prefix, Arrays.copyOf(keyBytes, keyBytes.length), null, null);
    }
    
    private NanoAccount(String prefix, byte[] keyBytes, String segAddress, String publicKeyHex) {
        this(prefix, keyBytes, null, null, publicKeyHex, segAddress, null);
    }
    
    private NanoAccount(String prefix, byte[] keyBytes, byte[] checksumBytes, String cachedAddress,
                        String publicKeyHex, String segAddress, String segChecksum) {
        validatePrefix(prefix);
        if (keyBytes == null) throw new IllegalArgumentException("Key byte array cannot be null.");
        if (keyBytes.length != 32) throw new IllegalArgumentException("Key byte array must have a length of 32.");
        this.keyBytes = keyBytes;
        this.prefix = (prefix != null && !prefix.isEmpty()) ? prefix : null;
        this.checksumBytes = checksumBytes;
        this.cachedAddress = cachedAddress;
        this.publicKeyHex = publicKeyHex;
        this.segAddress = segAddress;
        this.segChecksum = segChecksum;
    }
    
    
    /**
     * @return the prefix associated with this address (not including separator)
     */
    public String getPrefix() {
        return prefix;
    }
    
    /**
     * @return an array of bytes which represent the public key of this address
     */
    public byte[] getPublicKeyBytes() {
        return Arrays.copyOf(keyBytes, keyBytes.length);
    }
    
    /**
     * @return an array of bytes which represent the public key of this address
     */
    public byte[] getChecksumBytes() {
        if (checksumBytes == null) {
            synchronized (this) {
                if (checksumBytes == null)
                    checksumBytes = calculateChecksumBytes(keyBytes);
            }
        }
        return Arrays.copyOf(checksumBytes, checksumBytes.length);
    }
    
    /**
     * @return this address, represented by a 64-character hexadecimal string
     */
    public String toPublicKey() {
        if (publicKeyHex == null) {
            synchronized (this) {
                if (publicKeyHex == null)
                    publicKeyHex = JNanoHelper.ENCODER_HEX.encode(keyBytes);
            }
        }
        return publicKeyHex;
    }
    
    /**
     * @return this address, complete with prefix and checksum
     */
    public synchronized String toAddress() {
        if (cachedAddress == null) {
            synchronized (this) {
                if (cachedAddress == null) {
                    // Generate
                    StringBuilder sb = new StringBuilder(
                            60 + (getPrefix() == null ? 0 : getPrefix().length() + 1)); // Capacity
                    if (getPrefix() != null)
                        sb.append(getPrefix()).append(PREFIX_SEPARATOR_CHAR);
                    cachedAddress = sb.append(getAddressSegment()).append(getAddressChecksumSegment()).toString();
                    return cachedAddress;
                }
            }
        }
        return cachedAddress;
    }
    
    /**
     * @return this address, without the prefix or checksum segments
     */
    public String getAddressSegment() {
        if (segAddress == null) {
            synchronized (this) {
                if (segAddress == null)
                    segAddress = JNanoHelper.ENCODER_NANO_B32.encode(getPublicKeyBytes());
            }
        }
        return segAddress;
    }
    
    /**
     * @return the checksum segment of this address
     */
    public String getAddressChecksumSegment() {
        if (segChecksum == null) {
            synchronized (this) {
                if (segChecksum == null)
                    segChecksum = JNanoHelper.ENCODER_NANO_B32.encode(getChecksumBytes());
            }
        }
        return segChecksum;
    }
    
    
    @Override
    public String toString() {
        return toAddress();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NanoAccount that = (NanoAccount)o;
        return Objects.equals(prefix, that.prefix) &&
                Arrays.equals(keyBytes, that.keyBytes);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hash(prefix);
        result = 31 * result + Arrays.hashCode(keyBytes);
        return result;
    }
    
    
    /**
     * Creates a new {@link NanoAccount} representing the same public key with the newly assigned protocol prefix.
     * @param prefix the new protocol identifier prefix (without separator), or null for no prefix
     * @return a new instance with the specified prefix
     */
    public NanoAccount withPrefix(String prefix) {
        return new NanoAccount(this, prefix);
    }
    
    
    /**
     * Creates a new {@link NanoAccount} from a given address or public key.
     * If using an address format, the string should include a prefix, separator, address segment and checksum.
     * Accounts encoded as a public key should be a 64-character hexadecimal string. In cases where a hexadecimal
     * string is passed, the prefix will be the default ({@value #DEFAULT_PREFIX}).
     * @param str the account address string
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parse(String str) {
        return parse(str, DEFAULT_PREFIX);
    }
    
    /**
     * <p>Creates a new {@link NanoAccount} from a given address or public key.</p>
     * If using an address format, the string should include a prefix, separator, address segment and checksum - if
     * the address includes a prefix, then that prefix will be used, rather than the specified default.
     * Accounts encoded as a public key should be a 64-character hexadecimal string. In cases where a hexadecimal
     * string is passed, the prefix will be the specified prefix argument.
     * @param str           the account address string
     * @param defaultPrefix the default protocol identifier prefix (without separator), or null for no prefix
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parse(String str, String defaultPrefix) {
        if (str == null) throw new IllegalArgumentException("Account string cannot be null.");
        
        if (str.length() == 64 && str.indexOf(PREFIX_SEPARATOR_CHAR) == -1) {
            return parsePublicKey(str, defaultPrefix); // Hex
        } else if (str.length() == 60 || str.indexOf(PREFIX_SEPARATOR_CHAR) != -1) {
            return parseAddress(str); // Address
        } else if (str.length() == 52) {
            return parseAddressSegment(str, defaultPrefix); // Address segment
        }
        throw new AddressFormatException("Could not identify the encoding format of the given address.");
    }
    
    /**
     * Creates a new {@link NanoAccount} from a given address. The address should contain an encoded public key and
     * checksum, along with an optional prefix (eg.
     * {@code nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz}).
     * @param address the account address string
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parseAddress(String address) {
        if (address == null) throw new IllegalArgumentException("Address argument cannot be null.");
        if (address.length() < 60) throw new AddressFormatException("Address string is too short.");
        
        int separatorIndex = address.lastIndexOf(PREFIX_SEPARATOR_CHAR);
        
        if ((separatorIndex == -1 && address.length() != 60)
                || (separatorIndex != -1 && (address.length() - separatorIndex - 1) != 60))
            throw new AddressFormatException("Address/checksum segment is not the right length.");
        
        return parseAddressSegment(
                address.substring(address.length() - 60, address.length() - 8),
                (separatorIndex <= 0 ? null : address.substring(0, separatorIndex)),
                address.substring(address.length() - 8));
    }
    
    /**
     * Creates a new {@link NanoAccount} from a given address segment, using the default prefix. The segment excludes
     * the initial prefix and checksum (the last 8 characters), (eg.
     * {@code 34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q}).
     * @param address the 52-character account address segment
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parseAddressSegment(String address) {
        return parseAddressSegment(address, DEFAULT_PREFIX);
    }
    
    /**
     * Creates a new {@link NanoAccount} from a given address segment and prefix. The segment excludes the initial
     * prefix and checksum (the last 8 characters), (eg. {@code 34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q}).
     * @param address the 52-character account address segment
     * @param prefix  the protocol identifier prefix (without separator), or null for no prefix
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parseAddressSegment(String address, String prefix) {
        return parseAddressSegment(address, prefix, null);
    }
    
    /**
     * Creates a new {@link NanoAccount} from a given address segment and prefix, and validates the checksum. The
     * segment excludes the initial prefix and checksum (the last 8 characters), (eg.
     * {@code 34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q}).
     * @param address  the 52-character account address segment
     * @param prefix   the protocol identifier prefix to use (without separator), or null for no prefix
     * @param checksum the 8-character checksum to compare, or null for no comparison
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parseAddressSegment(String address, String prefix, String checksum) {
        if (address == null) throw new IllegalArgumentException("Address argument cannot be null.");
        if (address.length() != 52) throw new AddressFormatException("Address string must be 52 characters long.");
        if (address.charAt(0) != '1' && address.charAt(0) != '3')
            throw new AddressFormatException("Addresses may only begin with characters 1 or 3.");
        validatePrefix(prefix);
        if (checksum != null && checksum.length() != 8)
            throw new AddressFormatException("Expected checksum string must be 8 characters long.");
        
        // Preprocess
        address = address.toLowerCase();
        checksum = checksum != null ? checksum.toLowerCase() : null;
        
        // Create object
        NanoAccount createdAddr = new NanoAccount(prefix, calculateKeyBytes(address, JNanoHelper.ENCODER_NANO_B32),
                address, null);
        
        // Verify checksum (if provided)
        if (checksum != null && !checksum.equals(createdAddr.getAddressChecksumSegment()))
            throw new AddressFormatException("Provided checksum did not match the computed checksum.");
        
        return createdAddr;
    }
    
    /**
     * Creates a new {@link NanoAccount} from a given public key, using the default prefix. This consists of a
     * 64-character hexadecimal string (eg. {@code 8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97}).
     * @param key the public key of the account, encoded in hexadecimal
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parsePublicKey(String key) {
        return parsePublicKey(key, DEFAULT_PREFIX);
    }
    
    /**
     * Creates a new {@link NanoAccount} from a given public key and prefix. This consists of a 64-character
     * hexadecimal string (eg. {@code 8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97}).
     * @param key    the public key of the account, encoded in hexadecimal
     * @param prefix the protocol identifier prefix to use (without separator), or null for no prefix
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parsePublicKey(String key, String prefix) {
        if (key == null) throw new IllegalArgumentException("Public key argument cannot be null.");
        if (key.length() != 64) throw new AddressFormatException("Key string must be 64 characters long.");
        
        key = key.toUpperCase();
        return new NanoAccount(prefix, calculateKeyBytes(key, JNanoHelper.ENCODER_HEX), null, key);
    }
    
    
    /**
     * Checks whether a given address string is a valid Nano address. For an address to be considered valid, the format
     * must be of an appropriate length, contain an approved prefix, and have a matching checksum value.
     * @param address the account address string
     * @return whether the given address string is a valid Nano account
     */
    public static boolean isValidNano(String address) {
        try {
            NanoAccount addr = parseAddress(address);
            return isValidNano(addr);
        } catch (AddressFormatException e) {
            return false;
        }
    }
    
    /**
     * Checks whether a given address string is a valid Nano address. For an address to be considered valid, the
     * prefix must match one of the pre-defined Nano prefix strings.
     * @param address the account address
     * @return whether the given address is a valid Nano account
     */
    public static boolean isValidNano(NanoAccount address) {
        return Arrays.stream(DEFAULT_ALLOWED_PREFIXES).anyMatch(e -> e.equalsIgnoreCase(address.getPrefix()));
    }
    
    /**
     * Checks whether a given address string is valid. For an address to be considered valid, the format must be of
     * an appropriate length, contain a prefix, and have a matching checksum value.
     * @param address the account address string
     * @return whether the given address string is valid
     */
    public static boolean isValid(String address) {
        return isValid(address, null);
    }
    
    /**
     * Checks whether a given address string is valid. For an address to be considered valid, the format must be of
     * an appropriate length, contain the defined prefix, and have a matching checksum value.
     * @param address the account address string
     * @param prefix  the prefix value to compare (without separator), or null to allow any
     * @return whether the given address string is valid
     */
    public static boolean isValid(String address, String prefix) {
        try {
            NanoAccount addr = parseAddress(address);
            return prefix == null || prefix.equalsIgnoreCase(addr.getPrefix());
        } catch (AddressFormatException e) {
            return false;
        }
    }
    
    /**
     * Compares the given address with the checksum component, and checks whether the address string is valid.
     * @param addressSegment the account address segment
     * @param checksum       the checksum segment
     * @return whether the given address string is valid
     */
    public static boolean isSegmentValid(String addressSegment, String checksum) {
        try {
            parseAddressSegment(addressSegment, null, checksum);
            return true;
        } catch (AddressFormatException e) {
            return false;
        }
    }
    
    
    /** Helper method to calculate checksum bytes from a public key. */
    private static byte[] calculateChecksumBytes(byte[] keyBytes) {
        //Digest
        Blake2b digest = new Blake2b(null, 5, null, null); //Blake2b algorithm, 5 bytes length
        digest.update(keyBytes, 0, keyBytes.length);
        byte[] out = new byte[5];
        digest.digest(out, 0);
        return JNanoHelper.reverseArray(out);
    }
    
    /** Helper method to calculate bytes from an encoded address. */
    private static byte[] calculateKeyBytes(String str, BaseEncoder decoder) {
        byte[] keyBytes;
        try {
            keyBytes = decoder.decode(str);
        } catch (IllegalArgumentException e) { // Catch illegal characters
            throw new AddressFormatException(e);
        }
        if (keyBytes.length != 32)
            throw new AddressFormatException("Address/key bytes could not be decoded.");
        return keyBytes;
    }
    
    private static void validatePrefix(String prefix) {
        if (prefix != null && !prefix.isEmpty() && !prefix.matches("[0-9A-Za-z]+"))
            throw new IllegalArgumentException("Address prefix contains an illegal character.");
    }
    
    
    static class Adapter implements JsonSerializer<NanoAccount>, JsonDeserializer<NanoAccount> {
        @Override
        public NanoAccount deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return parse(json.getAsString());
        }
        
        @Override
        public JsonElement serialize(NanoAccount src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toAddress());
        }
    }
    
    public static class AddressFormatException extends IllegalArgumentException {
        public AddressFormatException() { super(); }
        public AddressFormatException(String msg) { super(msg); }
        public AddressFormatException(Throwable cause) { super(cause); }
    }

}
