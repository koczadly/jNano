/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal;

import java.math.BigInteger;

/**
 * Internal constants class relating to the Nano protocol.
 */
public class NanoConst {
    
    public static final BigInteger MAX_BALANCE = JNC.BIGINT_MAX_128;
    
    public static final int LEN_HASH_B = 32;
    public static final int LEN_HASH_H = LEN_HASH_B * 2;
    
    public static final int LEN_KEY_B = LEN_HASH_B;
    public static final int LEN_KEY_H = LEN_HASH_H;
    
    public static final int LEN_SIGNATURE_B = 64;
    public static final int LEN_SIGNATURE_H = LEN_SIGNATURE_B * 2;

}
