/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.StateBlockBuilder;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;
import java.util.Random;

public class TestConstants {
    
    private static final Random RANDOM = new Random(420);
    
    
    public static String randHex(int len) {
        return randHex(len, true);
    }
    
    public static String randHex(int len, boolean nonZero) {
        StringBuilder sb = new StringBuilder(len);
        for (int i=0; i<len; i++)
            sb.append(JNH.HEX_CHARS_UC[(nonZero && i == 0) ? (RANDOM.nextInt(15) + 1) : RANDOM.nextInt(16)]);
        return sb.toString();
    }
    
    
    public static NanoAccount randAccount() {
        return randAccount(NanoAccount.DEFAULT_PREFIX);
    }
    
    public static NanoAccount randAccount(String prefix) {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return new NanoAccount(bytes);
    }
    
    
    public static BigInteger randBalance() {
        return new BigInteger(127, RANDOM).add(BigInteger.ONE);
    }
    
    
    public static StateBlockBuilder randStateBlock() {
        return new StateBlockBuilder(randAccount())
                .setSubtype(StateBlockSubType.values()[RANDOM.nextInt(StateBlockSubType.values().length)])
                .setRepresentativeAddress(randAccount())
                .setPreviousBlockHash(randHex(64))
                .setSignature(randHex(128))
                .setWorkSolution(new WorkSolution(RANDOM.nextLong()))
                .setLinkData(randHex(64))
                .setBalance(randBalance());
                
    }

}
