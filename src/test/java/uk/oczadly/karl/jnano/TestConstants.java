/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.*;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;
import java.util.Random;

public class TestConstants {
    
    public static final Random RANDOM = new Random();
    
    
    
    public static String randHex(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i=0; i<len; i++)
            sb.append(JNH.HEX_CHARS_UC[(i == 0) ? (RANDOM.nextInt(15) + 1) : RANDOM.nextInt(16)]);
        return sb.toString();
    }
    
    
    public static NanoAccount randAccount() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return new NanoAccount(bytes);
    }
    
    
    public static NanoAmount randBalance() {
        return new NanoAmount(new BigInteger(127, RANDOM).add(BigInteger.ONE));
    }
    
    
    public static OpenBlock randOpenBlock() {
        return new OpenBlock(randHex(128), new WorkSolution(RANDOM.nextLong()), randHex(64), randAccount(),
                randAccount());
    }
    
    public static ChangeBlock randChangeBlock() {
        return new ChangeBlock(randHex(128), new WorkSolution(RANDOM.nextLong()), randHex(64), randAccount());
    }
    
    public static SendBlock randSendBlock() {
        return new SendBlock(randHex(128), new WorkSolution(RANDOM.nextLong()), randHex(64), randAccount(),
                randBalance());
    }
    
    public static ReceiveBlock randReceiveBlock() {
        return new ReceiveBlock(randHex(128), new WorkSolution(RANDOM.nextLong()), randHex(64), randHex(64));
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
    
    public static TestBlock randTestBlock() {
        return new TestBlock(randHex(128), new WorkSolution(RANDOM.nextLong()), randHex(64));
    }

}
