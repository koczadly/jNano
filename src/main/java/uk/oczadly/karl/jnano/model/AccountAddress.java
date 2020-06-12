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
 * <p>This class represents an immutable Nano account address string. A wide range of native utilities are provided
 * through the methods of this class, without needing to configure or connect to an external node.</p>
 * <p>To instantiate this class, use the provided static methods (eg. {@link #parseAddress(String)}), or use the
 * constructor to clone an existing object.</p>
 */
@JsonAdapter(AccountAddress.Adapter.class)
public final class AccountAddress {
    
    /**
     * The default Nano prefix, officially used on the live network. This value does not contain the additional
     * separator char ({@link #PREFIX_SEPARATOR_CHAR}).
     */
    public static final String DEFAULT_PREFIX = "nano";
    
    /**
     * The character which separates the prefix from the address string.
     */
    public static final char PREFIX_SEPARATOR_CHAR = '_';
    
    
    private final String prefix;
    private final byte[] keyBytes;
    
    // Values below may be initialized lazily
    private volatile byte[] checksumBytes;
    private volatile String cachedAddress, publicKeyHex, segAddress, segChecksum;
    
    /**
     * Clones an existing {@link AccountAddress} object.
     * @param address the address to clone
     */
    public AccountAddress(AccountAddress address) {
        this(address.prefix, address.keyBytes, address.checksumBytes, address.cachedAddress, address.publicKeyHex,
                address.segAddress, address.segChecksum);
    }
    
    /**
     * Clones an existing {@link AccountAddress} object, but using a different prefix.
     * @param address   the address to clone
     * @param newPrefix the prefix to assign
     */
    public AccountAddress(AccountAddress address, String newPrefix) {
        this(newPrefix, address.keyBytes, address.checksumBytes, null, address.publicKeyHex,
                address.segAddress, address.segChecksum);
    }
    
    private AccountAddress(String prefix, byte[] keyBytes, String segAddress, String publicKeyHex) {
        this(prefix, keyBytes, null, null, publicKeyHex, segAddress, null);
    }
    
    private AccountAddress(String prefix, byte[] keyBytes, byte[] checksumBytes, String cachedAddress,
                           String publicKeyHex, String segAddress, String segChecksum) {
        this.prefix = (prefix != null && !prefix.isEmpty()) ? prefix : null;
        this.keyBytes = keyBytes;
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
    public String getAsPublicKey() {
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
    public synchronized String getAsAddress() {
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
        return getAsAddress();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountAddress that = (AccountAddress)o;
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
     * Creates a new {@link AccountAddress} from a given address. The address should an encoded public key and checksum,
     * along with an optional prefix (eg. {@code nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz}).
     * @param address the account address string
     * @return the created address object
     */
    public static AccountAddress parseAddress(String address) {
        if (address.length() < 60) throw new AddressFormatException("Address string is too short.");
        
        int separatorIndex = address.indexOf(PREFIX_SEPARATOR_CHAR);
        return parseAddressSegment(
                address.substring(address.length() - 60, address.length() - 8),
                separatorIndex <= 0 ? null : address.substring(0, separatorIndex),
                address.substring(address.length() - 8));
    }
    
    /**
     * Creates a new {@link AccountAddress} from a given address segment, using the default prefix. The segment
     * excludes the initial prefix and checksum (the last 8 characters), (eg.
     * {@code 34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q}).
     * @param address the 52-character account address segment
     * @return the created address object
     */
    public static AccountAddress parseAddressSegment(String address) {
        return parseAddressSegment(address, DEFAULT_PREFIX);
    }
    
    /**
     * Creates a new {@link AccountAddress} from a given address segment and prefix. The segment excludes the initial
     * prefix and checksum (the last 8 characters), (eg. {@code 34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q}).
     * @param address the 52-character account address segment
     * @param prefix  the protocol identifier prefix to use (without separator), or null for no prefix
     * @return the created address object
     */
    public static AccountAddress parseAddressSegment(String address, String prefix) {
        return parseAddressSegment(address, prefix, null);
    }
    
    /**
     * Creates a new {@link AccountAddress} from a given address segment and prefix, and validates the checksum. The
     * segment excludes the initial prefix and checksum (the last 8 characters), (eg.
     * {@code 34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q}).
     * @param address  the 52-character account address segment
     * @param prefix   the protocol identifier prefix to use (without separator), or null for no prefix
     * @param checksum the 8-character checksum to compare, or null for no comparison
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static AccountAddress parseAddressSegment(String address, String prefix, String checksum) {
        if (address == null) throw new IllegalArgumentException("Address argument cannot be null.");
        if (address.length() != 52)
            throw new AddressFormatException("Address string must be 52 characters long.");
        if (checksum != null && checksum.length() != 8)
            throw new AddressFormatException("Expected checksum string must be 8 characters long.");
        
        // Preprocess
        address = address.toLowerCase();
        checksum = checksum != null ? checksum.toLowerCase() : null;
        
        // Create object
        AccountAddress createdAddr = new AccountAddress(
                prefix,
                calculateKeyBytes(address, JNanoHelper.ENCODER_NANO_B32),
                address, null);
        
        // Verify checksum (if provided)
        if (checksum != null && !checksum.equals(createdAddr.getAddressChecksumSegment()))
            throw new AddressFormatException("Provided checksum did not match the computed checksum.");
        
        return createdAddr;
    }
    
    /**
     * Creates a new {@link AccountAddress} from a given public key, using the default prefix. This consists of a
     * 64-character hexadecimal string (eg. {@code 8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97}).
     * @param key the public key of the account, encoded in hexadecimal
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static AccountAddress parsePublicKey(String key) {
        return parsePublicKey(key, DEFAULT_PREFIX);
    }
    
    /**
     * Creates a new {@link AccountAddress} from a given public key and prefix. This consists of a 64-character
     * hexadecimal string (eg. {@code 8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97}).
     * @param key    the public key of the account, encoded in hexadecimal
     * @param prefix the protocol identifier prefix to use (without separator), or null for no prefix
     * @return the created address object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static AccountAddress parsePublicKey(String key, String prefix) {
        if (key == null) throw new IllegalArgumentException("Public key argument cannot be null.");
        if (key.length() != 64) throw new AddressFormatException("Key string must be 64 characters long.");
        
        // Preprocess
        key = key.toUpperCase();
        
        // Create and return
        return new AccountAddress(
                prefix,
                calculateKeyBytes(key, JNanoHelper.ENCODER_HEX),
                null, key);
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
     * @param prefix  the prefix value to compare, or null to allow any
     * @return whether the given address string is valid
     */
    public static boolean isValid(String address, String prefix) {
        try {
            AccountAddress addr = parseAddress(address);
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
        
        //Reverse byte array
        byte[] rev = new byte[out.length];
        for(int i=0; i<out.length; i++) {
            rev[i] = out[out.length - i - 1];
        }
        return rev;
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
    
    
    
    static class Adapter implements JsonSerializer<AccountAddress>, JsonDeserializer<AccountAddress> {
        @Override
        public AccountAddress deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String str = json.getAsString();
            if (str.length() == 64 && str.indexOf(PREFIX_SEPARATOR_CHAR) == -1) {
                return parsePublicKey(str); // Hex
            } else {
                return parseAddress(str); // Address
            }
        }
        
        @Override
        public JsonElement serialize(AccountAddress src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getAsAddress());
        }
    }
    
    public static class AddressFormatException extends IllegalArgumentException {
        public AddressFormatException() {
            super();
        }
    
        public AddressFormatException(String msg) {
            super(msg);
        }
    
        public AddressFormatException(Throwable cause) {
            super(cause);
        }
    }

}
