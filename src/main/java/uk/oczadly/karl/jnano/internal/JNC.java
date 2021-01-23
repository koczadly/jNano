/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.oczadly.karl.jnano.internal.gsonadapters.*;
import uk.oczadly.karl.jnano.internal.utils.BaseEncoder;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.BlockDeserializer;

import java.math.BigInteger;
import java.time.Instant;

/**
 * JNano helper constants.
 */
public class JNC {
    
    public static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new EnumTypeAdapterFactory())          // Case-insensitive enums
            .registerTypeAdapterFactory(new ArrayTypeAdapterFactoryFix())      // Empty array hotfix
            .registerTypeAdapter(boolean.class, new BooleanTypeDeserializer()) // Boolean deserializer
            .registerTypeAdapter(Boolean.class, new BooleanTypeDeserializer()) // Boolean deserializer
            .registerTypeAdapter(BigInteger.class, new BigIntSerializer())     // BigInt serializer (string)
            .registerTypeAdapter(Instant.class, new InstantAdapter.Millis())   // Instant adapter (epoch millis)
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    
    public static final BlockDeserializer BLOCK_DESERIALIZER = BlockDeserializer.withDefaults();
    
    public static final char[] HEX_CHARS_UC = "0123456789ABCDEF".toCharArray();
    
    public static final BaseEncoder ENC_16 = new BaseEncoder(HEX_CHARS_UC);
    public static final BaseEncoder ENC_32 = new BaseEncoder("13456789abcdefghijkmnopqrstuwxyz");
    
    public static final String ZEROES_16 = JNH.repeatChar('0', 16);
    public static final HexData ZEROES_16_HD = new HexData(ZEROES_16);
    public static final String ZEROES_64 = JNH.repeatChar('0', 64);
    public static final HexData ZEROES_64_HD = new HexData(ZEROES_64);
    public static final String ZEROES_128 = JNH.repeatChar('0', 128);
    public static final HexData ZEROES_128_HD = new HexData(ZEROES_128);
    
    public static final BigInteger BIGINT_MAX_128 = new BigInteger(1, JNH.filledByteArray(16, (byte)0xFF));
    
    public static final BigInteger BIGINT_MAX_256 = new BigInteger(1, JNH.filledByteArray(32, (byte)0xFF));
    
}
