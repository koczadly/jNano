/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.Ed25519Blake2b;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.internal.utils.BaseEncoder;
import uk.oczadly.karl.jnano.util.WalletUtil;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <p>This is an immutable class which represents an individual Nano account address. A wide range of Java-based
 * utilities are provided through the methods of this class without needing to connect or depend on an external
 * node.</p>
 *
 * <p>To instantiate this class, use the provided static parsing methods (eg. {@link #parse(String)}), or use one of
 * the constructors to create from a byte array. The prefix of an address can be changed (or removed) using the
 * {@link #withPrefix(String)} method, which will return a copy of the class using the new prefix.</p>
 *
 * <p>Using the built-in JSON adapter, this class will call {@link #parse(String)} for deserializing, allowing
 * accounts encoded in either address or public key formats. When serializing this class, the {@link #toAddress()}
 * method will be used to convert into address format.</p>
 */
@JsonAdapter(NanoAccount.Adapter.class)
public final class NanoAccount {
    
    private static final BigInteger MAX_INDEX_VAL = JNC.BIGINT_MAX_256;
    private static final Pattern PREFIX_VALIDATE_PATTERN = Pattern.compile("^[0-9A-Za-z]*$");
    
    /**
     * The character which separates the prefix from the address string.
     */
    public static final char PREFIX_SEPARATOR_CHAR = '_';
    
    /**
     * The default Nano prefix, officially used on the live network. This value does not contain the additional
     * separator char ({@link #PREFIX_SEPARATOR_CHAR}).
     */
    public static final String DEFAULT_PREFIX = "nano";
    
    /**
     * An immutable set of permitted prefixes on the Nano network. This includes the {@link #DEFAULT_PREFIX} value.
     */
    public static final Set<String> DEFAULT_PERMITTED_PREFIXES = Set.of(DEFAULT_PREFIX, "xrb");
    
    /**
     * <p>The zeroth index account, represented by all zeroes for the public key. This address is also the burn address
     * used within Nano and other forks.</p>
     * <p>A common use of this account would be for requests which require an initial starting account to initiate a
     * traversal (as seen with {@link uk.oczadly.karl.jnano.rpc.request.node.RequestLedger}).</p>
     */
    public static final NanoAccount ZERO_ACCOUNT = new NanoAccount(BigInteger.ZERO);
    
    
    private final byte[] keyBytes;
    private final String prefix;
    // Fields below may be initialized lazily
    private volatile byte[] checksumBytes;
    private volatile String cachedAddress, publicKeyHex, segAddress, segChecksum;
    private volatile BigInteger index;
    
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
                address.segAddress, address.segChecksum, address.index);
    }
    
    /**
     * Constructs an AccountAddress instance from a positive integer index, using the
     * {@link #DEFAULT_PREFIX default prefix}.
     * @param index the integer representation of this account
     * @see #parse(String)
     */
    public NanoAccount(BigInteger index) {
        this(index, DEFAULT_PREFIX);
    }
    
    /**
     * Constructs an AccountAddress instance from a positive integer index, using the specified prefix.
     * @param index  the integer representation of this account
     * @param prefix the protocol identifier prefix (without separator), or null for no prefix
     * @see #parse(String)
     */
    public NanoAccount(BigInteger index, String prefix) {
        this(prefix, JNH.leftPadByteArray(index.toByteArray(), NanoConst.LEN_KEY_B, true), null, null, null);
        if (index.compareTo(BigInteger.ZERO) < 0 || index.compareTo(MAX_INDEX_VAL) > 0)
            throw new IllegalArgumentException("Account index is out of bounds.");
    }
    
    /**
     * Constructs an AccountAddress instance from an array of key bytes, using the
     * {@link #DEFAULT_PREFIX default prefix}.
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
        this(prefix, Arrays.copyOf(keyBytes, keyBytes.length), null, null, null);
    }
    
    private NanoAccount(String prefix, byte[] keyBytes, String segAddress, String publicKeyHex, BigInteger index) {
        this(prefix, keyBytes, null, null, publicKeyHex, segAddress, null, index);
    }
    
    private NanoAccount(String prefix, byte[] keyBytes, byte[] checksumBytes, String cachedAddress,
                        String publicKeyHex, String segAddress, String segChecksum, BigInteger index) {
        validatePrefix(prefix);
        if (keyBytes == null) throw new IllegalArgumentException("Key byte array cannot be null.");
        if (keyBytes.length != NanoConst.LEN_KEY_B)
            throw new IllegalArgumentException("Key byte array must have a length of " + NanoConst.LEN_KEY_B + ".");
        this.keyBytes = keyBytes;
        this.prefix = (prefix != null && !prefix.isEmpty()) ? prefix : null;
        this.checksumBytes = checksumBytes;
        this.cachedAddress = cachedAddress;
        this.publicKeyHex = publicKeyHex;
        this.segAddress = segAddress;
        this.segChecksum = segChecksum;
        this.index = index;
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
     * @return an array of bytes which represent the checksum of this address
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
     * <p>Returns an integer-based encoding of the account's public key. This could be used to compare different
     * {@link NanoAccount} instances against each other in a logical order.</p>
     * <p>Note that this is <em>not</em> the same as the account index within the context of seeds and wallets.</p>
     * @return the integer-based index of this account, derived from the key byte array
     */
    public BigInteger getAccountIndex() {
        if (index == null) {
            synchronized (this) {
                if (index == null)
                    index = new BigInteger(1, keyBytes);
            }
        }
        return index;
    }
    
    /**
     * <p>Formats this account as a 64-character hexadecimal string.</p>
     * <p>Example returned value: {@code 8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97}</p>
     * @return this address, represented by a 64-character hexadecimal string
     */
    public String toPublicKey() {
        if (publicKeyHex == null) {
            synchronized (this) {
                if (publicKeyHex == null)
                    publicKeyHex = JNC.ENC_16.encode(keyBytes);
            }
        }
        return publicKeyHex;
    }
    
    /**
     * <p>Formats this account as a standard address string, containing the prefix (if applicable) and checksum.</p>
     * <p>Example returned value: {@code nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz}</p>
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
                    segAddress = JNC.ENC_32.encode(getPublicKeyBytes());
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
                    segChecksum = JNC.ENC_32.encode(getChecksumBytes());
            }
        }
        return segChecksum;
    }
    
    
    /**
     * Formats this account to an address string. Use of {@link #toAddress()} is preferred for clarity and future
     * consistency.
     * @see #toAddress()
     */
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
    
    /**
     * Compares this instance with the provided, and verifies the equality of the public key component.
     * @param that the other NanoAccount instance to compare
     * @return true if the public key of both accounts match
     */
    public boolean equalsIgnorePrefix(NanoAccount that) {
        if (that == null) return false;
        if (this == that) return true;
        return Arrays.equals(keyBytes, that.keyBytes);
    }
    
    @Override
    public int hashCode() {
        int result = prefix.hashCode();
        result = 31 * result + Arrays.hashCode(keyBytes);
        return result;
    }
    
    
    /**
     * Creates a new {@link NanoAccount} representing the same public key with the newly assigned protocol prefix, or
     * returns this instance if the prefix already matches.
     * @param prefix the new protocol identifier prefix (without separator), or null for no prefix
     * @return a new instance with the specified prefix
     */
    public NanoAccount withPrefix(String prefix) {
        if (Objects.equals(this.prefix, prefix))
            return this;
        return new NanoAccount(this, prefix);
    }
    
    /**
     * Checks whether the address prefix meets the criteria for a valid Nano address (eg. {@code nano} and {@code xrb}).
     *
     * <p>This method does <em>not</em> check whether the public key is {@link #isCryptoValid()
     * cryptographically valid}, and simply tests the prefix.</p>
     *
     * @return whether the given address is a valid Nano account
     */
    public boolean isValidNano() {
        return comparePrefix(getPrefix(), DEFAULT_PERMITTED_PREFIXES.toArray(new String[0]));
    }
    
    /**
     * Tests whether the public key of the account is cryptographically valid, and that the corresponding point lies on
     * the Ed25519 curve.
     *
     * @return true if the account is a cryptographically valid public key
     */
    public boolean isCryptoValid() {
        return Ed25519Blake2b.validatePubKey(keyBytes);
    }
    
    
    /**
     * <p>Creates a new {@link NanoAccount} from a given address or public key.</p>
     * <p>This method supports the following encoding string formats:</p>
     * <ul>
     *     <li>Address (eg: {@code nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz})</li>
     *     <li>Address segment (eg: {@code 34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q})</li>
     *     <li>Public key (eg: {@code 8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97})</li>
     * </ul>
     *
     * <p>If a protocol prefix is not passed as part of the string because the format doesn't support it, then the
     * {@link #DEFAULT_PREFIX} value will be used instead. Note that this will not be used for addresses which contain a
     * checksum and no prefix; for these addresses, a null prefix will be used when creating the object.</p>
     *
     * @param str the account address string
     * @return the created account object
     * @throws AddressFormatException if the address does not meet the required format criteria
     * @see #parse(String, String)
     */
    public static NanoAccount parse(String str) {
        return parse(str, DEFAULT_PREFIX);
    }
    
    /**
     * <p>Creates a new {@link NanoAccount} from a given address or public key.</p>
     * <p>This method supports the following encoding string formats:</p>
     * <ul>
     *     <li>Address (eg: {@code nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz})</li>
     *     <li>Address segment (eg: {@code 34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q})</li>
     *     <li>Public key (eg: {@code 8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97})</li>
     * </ul>
     *
     * <p>If a protocol prefix is not passed as part of the string because the format doesn't support it, then the
     * {@code defaultPrefix} value will be used instead. Note that this will not be used for addresses which contain a
     * checksum and no prefix; for these addresses, a null prefix will be used when creating the object.</p>
     *
     * @param str           the account address string
     * @param defaultPrefix the default protocol identifier prefix (without separator), or null for no prefix
     * @return the created account object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parse(String str, String defaultPrefix) {
        if (str == null) throw new IllegalArgumentException("Account string cannot be null.");
        
        boolean hasSeparator = str.indexOf(PREFIX_SEPARATOR_CHAR) != -1;
        if (str.length() == 64 && !hasSeparator) {
            return parsePublicKey(str, defaultPrefix); // Hex
        } else if (str.length() == 60 || hasSeparator) {
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
     * @return the created account object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parseAddress(String address) {
        if (address == null) throw new IllegalArgumentException("Address argument cannot be null.");
        if (address.length() < 60) throw new AddressFormatException("Address string is too short.");
        
        int separatorIndex = address.lastIndexOf(PREFIX_SEPARATOR_CHAR);
        
        if ((separatorIndex == -1 && address.length() != 60) // No prefix
                || (separatorIndex != -1 && (address.length() - separatorIndex - 1) != 60)) // With prefix
            throw new AddressFormatException("Address/checksum segment is not the right length.");
        
        return parseAddressSegment(
                address.substring(address.length() - 60, address.length() - 8),
                (separatorIndex <= 0 ? null : address.substring(0, separatorIndex)),
                address.substring(address.length() - 8));
    }
    
    /**
     * Creates a new {@link NanoAccount} from a given address segment, using the {@link #DEFAULT_PREFIX default prefix}.
     * The segment excludes the initial prefix and checksum (the last 8 characters), (eg.
     * {@code 34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q}).
     * @param address the 52-character account address segment
     * @return the created account object
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
     * @return the created account object
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
     * @return the created account object
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
        NanoAccount createdAddr = new NanoAccount(
                prefix, calculateKeyBytes(address, JNC.ENC_32), address, null, null);
        
        // Verify checksum (if provided)
        if (checksum != null && !checksum.equals(createdAddr.getAddressChecksumSegment()))
            throw new AddressFormatException("Provided checksum did not match the computed checksum.");
        
        return createdAddr;
    }
    
    /**
     * Creates a new {@link NanoAccount} from a given public key, using the {@link #DEFAULT_PREFIX default prefix}.
     * This consists of a 64-character hexadecimal string (eg.
     * {@code 8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97}).
     * @param key the public key of the account, encoded in hexadecimal
     * @return the created account object
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
     * @return the created account object
     * @throws AddressFormatException if the address does not meet the required format criteria
     */
    public static NanoAccount parsePublicKey(String key, String prefix) {
        if (key == null) throw new IllegalArgumentException("Public key argument cannot be null.");
        if (key.length() != 64) throw new AddressFormatException("Key string must be 64 characters long.");
        
        key = key.toUpperCase();
        return new NanoAccount(prefix, calculateKeyBytes(key, JNC.ENC_16), null, key, null);
    }
    
    
    /**
     * Derives and creates a new {@link NanoAccount} from a given private key, and uses the the {@link #DEFAULT_PREFIX
     * default prefix}.
     *
     * @param privKey the private key
     * @return the created account object
     *
     * @see WalletUtil#deriveKeyFromSeed(HexData)
     */
    public static NanoAccount fromPrivateKey(HexData privKey) {
        return fromPrivateKey(privKey, DEFAULT_PREFIX);
    }
    
    /**
     * Derives and creates a new {@link NanoAccount} from a given private key, and uses the specified prefix.
     *
     * @param privKey the private key
     * @param prefix  the protocol identifier prefix to use (without separator), or null for no prefix
     * @return the created account object
     *
     * @see WalletUtil#deriveKeyFromSeed(HexData)
     */
    public static NanoAccount fromPrivateKey(HexData privKey, String prefix) {
        if (privKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        if (privKey.length() != NanoConst.LEN_KEY_B)
            throw new IllegalArgumentException("Invalid private key length.");
        
        byte[] pubKey = Ed25519Blake2b.derivePublicKey(privKey.toByteArray());
        return new NanoAccount(pubKey, prefix);
    }
    
    
    /**
     * Checks whether a given address string is a valid Nano address. For an address to be considered valid, the format
     * must be of an appropriate length, contain an approved prefix, and have a matching checksum value.
     *
     * <p>This method does <em>not</em> check whether the public key is {@link #isCryptoValid()
     * cryptographically valid}, and simply tests the format, checksum and prefix.</p>
     *
     * @param address the account address string
     * @return whether the given address string is a valid Nano account
     */
    public static boolean isValidNano(String address) {
        try {
            return parseAddress(address).isValidNano();
        } catch (AddressFormatException e) {
            return false;
        }
    }
    
    /**
     * Checks whether a given address string is a valid Nano address. For an address to be considered valid, the
     * prefix must match one of the pre-defined Nano prefix strings.
     *
     * <p>This method does <em>not</em> check whether the public key is {@link #isCryptoValid()
     * cryptographically valid}, and simply tests the format, checksum and prefix.</p>
     *
     * @param address the account address
     * @return whether the given address is a valid Nano account
     * @see #isValidNano()
     * 
     * @deprecated Use instance method {@link #isValidNano()}
     */
    @Deprecated
    public static boolean isValidNano(NanoAccount address) {
        return address.isValidNano();
    }
    
    /**
     * Checks whether a given address string is valid. For an address to be considered valid, the format must be of
     * an appropriate length, contain the defined prefix, and have a matching checksum value.
     *
     * <p>This method does <em>not</em> check whether the public key is {@link #isCryptoValid()
     * cryptographically valid}, and simply tests the format, checksum and prefix.</p>
     *
     * @param address  the account address string
     * @param prefixes an array of permittable prefixes (without separator), or null/empty to allow any
     * @return whether the given address string is valid
     */
    public static boolean isValid(String address, String...prefixes) {
        try {
            NanoAccount addr = parseAddress(address);
            return comparePrefix(addr.getPrefix(), prefixes);
        } catch (AddressFormatException e) {
            return false;
        }
    }
    
    /**
     * Compares the given address with the checksum component, and checks whether the address string is valid.
     *
     * <p>This method does <em>not</em> check whether the public key is {@link #isCryptoValid()
     * cryptographically valid}, and simply tests the format and checksum.</p>
     *
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
        return JNH.reverseArray(JNH.blake2b(5, keyBytes));
    }
    
    /** Helper method to calculate bytes from an encoded address. */
    private static byte[] calculateKeyBytes(String encodedData, BaseEncoder decoder) {
        byte[] keyBytes;
        try {
            keyBytes = decoder.decode(encodedData);
        } catch (IllegalArgumentException e) { // Catch illegal characters
            throw new AddressFormatException(e);
        }
        if (keyBytes.length != NanoConst.LEN_KEY_B)
            throw new AddressFormatException("Address/key bytes could not be decoded.");
        return keyBytes;
    }
    
    private static void validatePrefix(String prefix) {
        if (prefix != null && !PREFIX_VALIDATE_PATTERN.matcher(prefix).matches())
            throw new AddressFormatException("Address prefix contains an illegal character.");
    }
    
    private static boolean comparePrefix(String prefix, String[] prefixes) {
        if (prefixes == null || prefixes.length == 0) return true; // Allow any prefix
        if (prefix == null) return false; // Prefix is null, cannot be valid
        return Arrays.stream(prefixes).anyMatch(prefix::equalsIgnoreCase); // Match
    }
    
    
    static class Adapter implements JsonSerializer<NanoAccount>, JsonDeserializer<NanoAccount> {
        @Override
        public NanoAccount deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String str = json.getAsString();
            if (str.equals("") || str.equals("0"))
                return null;
            return parse(str);
        }
        
        @Override
        public JsonElement serialize(NanoAccount src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toAddress());
        }
    }
    
    public static class AddressFormatException extends IllegalArgumentException {
        AddressFormatException(String msg) { super(msg); }
        AddressFormatException(Throwable cause) { super(cause); }
    }

}
