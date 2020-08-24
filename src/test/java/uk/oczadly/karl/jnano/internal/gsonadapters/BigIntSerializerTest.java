package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class BigIntSerializerTest {
    
    private Gson gson = new GsonBuilder().registerTypeAdapter(BigInteger.class, new BigIntSerializer()).create();
    
    
    @Test
    public void testSerializer() {
        assertEquals("\"123\"", gson.toJson(new BigInteger("123")));
        assertEquals("null", gson.toJson(null, BigInteger.class));
    }
    
}