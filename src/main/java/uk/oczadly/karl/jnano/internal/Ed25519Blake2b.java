/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal;

import com.rfksystems.blake2b.security.Blake2b512Digest;
import com.rfksystems.blake2b.security.Blake2bProvider;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.math.GroupElement;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.Security;

/**
 * Ed25519 helper implementation using Blake2b-512 hash algorithm.
 */
public class Ed25519Blake2b {
    
    static {
        // Register Blake2b MessageDigest provider
        Security.addProvider(new Blake2bProvider());
    }
    
    /** The message digest algorithm used (Blake2b-512) */
    private static final MessageDigest MESSAGE_DIGEST = new Blake2b512Digest();
    
    /** Curve spec, based on the default ED_25519_CURVE_SPEC implementation. */
    private static final EdDSANamedCurveSpec CURVE_SPEC = new EdDSANamedCurveSpec(
            "Ed25519Blake2b",
            EdDSANamedCurveTable.ED_25519_CURVE_SPEC.getCurve(),
            MESSAGE_DIGEST.getAlgorithm(),
            EdDSANamedCurveTable.ED_25519_CURVE_SPEC.getScalarOps(),
            EdDSANamedCurveTable.ED_25519_CURVE_SPEC.getB());
    
    
    /**
     * Creates a new engine using the blake2b MessageDigest.
     * @return a new EdDSAEngine
     */
    public static EdDSAEngine newEngine() {
        return new EdDSAEngine(MESSAGE_DIGEST);
    }
    
    /**
     * Creates a new EdDSAPrivateKeySpec.
     * @param privKey the private key
     * @return the new spec object
     */
    public static EdDSAPrivateKeySpec getPrivKeySpec(byte[] privKey) {
        return new EdDSAPrivateKeySpec(privKey, CURVE_SPEC);
    }
    
    /**
     * Creates a new EdDSAPublicKeySpec.
     * @param pubKey the public key
     * @return the new spec object
     */
    public static EdDSAPublicKeySpec getPubKeySpec(byte[] pubKey) {
        return new EdDSAPublicKeySpec(pubKey, CURVE_SPEC);
    }
    
    
    /**
     * Tests whether a public key is valid.
     * @param pubKey the public key
     * @return true if valid
     */
    public static boolean validatePubKey(byte[] pubKey) {
        try {
            new GroupElement(CURVE_SPEC.getCurve(), pubKey);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Derives a public key from a private key.
     * @param privKey the private key
     * @return the public key bytes
     */
    public static byte[] derivePublicKey(byte[] privKey) {
        return getPrivKeySpec(privKey).getA().toByteArray();
    }
    
    /**
     * Signs a set of data.
     * @param privKey the private key
     * @param data    the data to sign
     * @return the signature, as a byte array
     */
    public static byte[] sign(byte[] privKey, byte[][] data) {
        try {
            EdDSAEngine engine = newEngine();
            engine.initSign(new EdDSAPrivateKey(getPrivKeySpec(privKey)));
            
            if (data.length == 1) {
                return engine.signOneShot(data[0]);
            } else {
                for (byte[] arr : data)
                    engine.update(arr);
                return engine.sign();
            }
        } catch (GeneralSecurityException e) {
            throw new AssertionError("Could not sign message.", e);
        }
    }
    
    /**
     * Verifies a signature.
     * @param pubKey the public key
     * @param data   the data
     * @param sig    the signature
     * @return true if the signature matches, false if not or if pubkey is invalid
     */
    public static boolean verify(byte[] pubKey, byte[][] data, byte[] sig) {
        // Parse pubkey
        EdDSAPublicKeySpec pubKeySpec;
        try {
            pubKeySpec = getPubKeySpec(pubKey);
        } catch (IllegalArgumentException ignored) {
            return false; // Invalid public key
        }
        // Verify
        try {
            EdDSAEngine engine = newEngine();
            engine.initVerify(new EdDSAPublicKey(pubKeySpec));
            
            if (data.length == 1) {
                return engine.verifyOneShot(data[0], sig);
            } else {
                for (byte[] arr : data)
                    engine.update(arr);
                return engine.verify(sig);
            }
        } catch (GeneralSecurityException e) {
            throw new AssertionError("Could not sign message.", e);
        }
    }
    
}
