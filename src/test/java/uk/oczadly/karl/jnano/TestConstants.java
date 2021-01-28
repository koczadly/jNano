/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano;

import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.*;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;
import java.util.Random;

public class TestConstants {
    
    public static final Random RANDOM = new Random();
    
    
    
    public static HexData randHexData(int len) {
        return new HexData(randHex(len));
    }
    
    public static String randHex(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i=0; i<len; i++)
            sb.append(JNC.HEX_CHARS_UC[(i == 0) ? (RANDOM.nextInt(15) + 1) : RANDOM.nextInt(16)]);
        return sb.toString();
    }
    
    
    public static NanoAccount randAccount() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return new NanoAccount(bytes);
    }
    
    
    public static NanoAmount randBalance() {
        return NanoAmount.valueOfRaw(new BigInteger(127, RANDOM).add(BigInteger.ONE));
    }
    
    
    public static OpenBlock randOpenBlock() {
        return new OpenBlock(randHexData(128), new WorkSolution(RANDOM.nextLong()), randHexData(64), randAccount(),
                randAccount());
    }
    
    public static ChangeBlock randChangeBlock() {
        return new ChangeBlock(randHexData(128), new WorkSolution(RANDOM.nextLong()), randHexData(64), randAccount());
    }
    
    public static SendBlock randSendBlock() {
        return new SendBlock(randHexData(128), new WorkSolution(RANDOM.nextLong()), randHexData(64), randAccount(),
                randBalance());
    }
    
    public static ReceiveBlock randReceiveBlock() {
        return new ReceiveBlock(randHexData(128), new WorkSolution(RANDOM.nextLong()), randHexData(64), randHexData(64));
    }
    
    public static StateBlock randStateBlock(StateBlockSubType subtype) {
        return randStateBlock().setSubtype(subtype).build();
    }
    
    public static StateBlockBuilder randStateBlock() {
        return new StateBlockBuilder()
                .setSubtype(StateBlockSubType.SEND)
                .setAccount(randAccount())
                .setRepresentative(randAccount())
                .setPreviousHash(randHexData(64))
                .setSignature(randHexData(128))
                .setWork(new WorkSolution(RANDOM.nextLong()))
                .setLink(randHexData(64))
                .setBalance(randBalance());
                
    }
    
    public static TestBlock randTestBlock() {
        return new TestBlock(randHexData(128), new WorkSolution(RANDOM.nextLong()), randHex(64));
    }

}
