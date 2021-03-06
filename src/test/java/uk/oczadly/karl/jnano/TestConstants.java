/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano;

import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.*;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;
import java.util.Random;

public class TestConstants {
    
    public static final Random RANDOM = new Random();
    
    
    
    public static HexData randHash() {
        return randHex(NanoConst.LEN_HASH_H);
    }
    
    public static HexData randHex(int len) {
        return new HexData(randHexString(len));
    }
    
    public static String randHexString(int len) {
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
        return new OpenBlock(randHex(128), new WorkSolution(RANDOM.nextLong()), randHash(), randAccount(),
                randAccount());
    }
    
    public static ChangeBlock randChangeBlock() {
        return new ChangeBlock(randHex(128), new WorkSolution(RANDOM.nextLong()), randHash(), randAccount());
    }
    
    public static SendBlock randSendBlock() {
        return new SendBlock(randHex(128), new WorkSolution(RANDOM.nextLong()), randHash(), randAccount(),
                randBalance());
    }
    
    public static ReceiveBlock randReceiveBlock() {
        return new ReceiveBlock(randHex(128), new WorkSolution(RANDOM.nextLong()), randHash(), randHash());
    }
    
    public static StateBlock randStateBlock(StateBlockSubType subtype) {
        return randStateBlock().subtype(subtype).build();
    }
    
    public static StateBlockBuilder randStateBlock() {
        return new StateBlockBuilder()
                .subtype(StateBlockSubType.SEND)
                .account(randAccount())
                .representative(randAccount())
                .previous(randHash())
                .work(new WorkSolution(RANDOM.nextLong()))
                .link(randHash())
                .balance(randBalance());
                
    }
    
    public static TestBlock randTestBlock() {
        return new TestBlock(randHex(128), new WorkSolution(RANDOM.nextLong()), randHexString(64));
    }

}
