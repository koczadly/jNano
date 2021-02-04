/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.epoch;

import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * This class is used to represent a single epoch upgrade for an account.
 */
public class EpochUpgrade implements Comparable<EpochUpgrade> {
    
    private final int version;
    private final NanoAccount signer;
    private final HexData identifier;
    
    /**
     * Constructs an epoch upgrade using the default identifier inference.
     * @param version the version of the upgrade, starting at {@code 1}
     * @param signer  the signer account, or null if self-signed
     */
    public EpochUpgrade(int version, NanoAccount signer) {
        this(version, signer, generateIdentifier("epoch v" + version + " block"));
    }
    
    /**
     * Constructs an epoch upgrade from the given parameters.
     * @param version    the version of the upgrade, starting at {@code 1}
     * @param signer     the signer account, or null if self-signed
     * @param identifier the identifier value, used as the {@code link} field of the epoch block
     */
    public EpochUpgrade(int version, NanoAccount signer, HexData identifier) {
        if (version <= 0)
            throw new IllegalArgumentException("Version must be positive.");
        if (identifier == null)
            throw new IllegalArgumentException("Identifier cannot be null.");
        if (identifier.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Invalid identifier length.");
        
        this.version = version;
        this.signer = signer;
        this.identifier = identifier;
    }
    
    
    /**
     * Returns the version of this epoch upgrade, starting at {@code 1}.
     * @return the version of this epoch upgrade
     */
    public final int getVersion() {
        return version;
    }
    
    /**
     * Returns an {@link Optional} containing the account which signs this epoch upgrade. If the returned account is
     * empty, then the block should be self-signed by the account holder.
     * @return the signer of this epoch block
     */
    public final Optional<NanoAccount> getSigner() {
        return Optional.ofNullable(signer);
    }
    
    /**
     * Returns the identifier data used by epoch blocks in the {@code link} field.
     * @return the identifier of this upgrade
     */
    public final HexData getIdentifier() {
        return identifier;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EpochUpgrade)) return false;
        EpochUpgrade that = (EpochUpgrade)o;
        return version == that.version &&
                Objects.equals(signer, that.signer) &&
                Objects.equals(identifier, that.identifier);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(version, signer, identifier);
    }
    
    @Override
    public String toString() {
        return "EpochVersion{" +
                "v=" + version +
                ", id=" + identifier +
                '}';
    }
    
    @Override
    public int compareTo(EpochUpgrade o) {
        return getVersion() - o.getVersion();
    }
    
    
    /**
     * Generates an identifier from the given text string by converting it to hexadecimal.
     * @param message the text message
     * @return the padded hex-encoded text
     */
    public static HexData generateIdentifier(String message) {
        return new HexData(Arrays.copyOf(message.getBytes(StandardCharsets.US_ASCII), 32));
    }
    
}
