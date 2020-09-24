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
    
    public static final BigInteger MAX_BALANCE = JNH.BIGINT_MAX_128;
    
    public static final int BLEN_HASH = 32;
    public static final int LEN_HASH = BLEN_HASH * 2;
    
    public static final int BLEN_SIGNATURE = 64;
    public static final int LEN_SIGNATURE = BLEN_SIGNATURE * 2;

}
