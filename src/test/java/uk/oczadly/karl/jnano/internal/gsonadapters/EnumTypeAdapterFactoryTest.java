/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Karl Oczadly
 */
public class EnumTypeAdapterFactoryTest {
    
    static final Gson GSON = new GsonBuilder()
                    .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
                    .create();
    
    
    @Test // Standard valueOf
    public void testStandard() {
        assertEquals(EnumDefault.VAL_A, GSON.fromJson("VAL_A", EnumDefault.class));
        assertEquals(EnumDefault.VAL_B, GSON.fromJson("VAL_B", EnumDefault.class));
        assertNull(GSON.fromJson("VAL_C", EnumDefault.class));
    
        assertEquals(new JsonPrimitive("val_a"), GSON.toJsonTree(EnumDefault.VAL_A));
        assertEquals(new JsonPrimitive("val_b"), GSON.toJsonTree(EnumDefault.VAL_B));
    }
    
    @Test // Incorrect case valueOf
    public void testCaseInsensitive() {
        assertEquals(EnumDefault.VAL_A, GSON.fromJson("vAl_a", EnumDefault.class));
        assertEquals(EnumDefault.VAL_B, GSON.fromJson("vAl_b", EnumDefault.class));
        assertNull(GSON.fromJson("vAl_c", EnumDefault.class));
    }
    
    @Test // Ensure SerializedName works as expected
    public void testNamed() {
        assertEquals(EnumNamed.VAL_A, GSON.fromJson("A", EnumNamed.class));
        assertEquals(EnumNamed.VAL_A, GSON.fromJson("AA", EnumNamed.class));
        assertEquals(EnumNamed.VAL_A, GSON.fromJson("a", EnumNamed.class));
        assertEquals(EnumNamed.VAL_A, GSON.fromJson("aa", EnumNamed.class));
        assertEquals(EnumNamed.VAL_B, GSON.fromJson("VAL_B", EnumNamed.class));
        assertEquals(EnumNamed.VAL_B, GSON.fromJson("vAl_b", EnumNamed.class));
        assertNull(GSON.fromJson("VAL_A", EnumNamed.class));
    
        assertEquals(new JsonPrimitive("A"), GSON.toJsonTree(EnumNamed.VAL_A));
        assertEquals(new JsonPrimitive("val_b"), GSON.toJsonTree(EnumNamed.VAL_B));
    }
    
    @Test // Ensure JsonAdapter works as expected
    public void testCustomAdapter() {
        assertEquals(EnumCustomAdapter.VAL_A, GSON.fromJson("ar", EnumCustomAdapter.class));
        assertEquals(EnumCustomAdapter.VAL_B, GSON.fromJson("br", EnumCustomAdapter.class));
        assertNull(GSON.fromJson("AR", EnumCustomAdapter.class));
        assertNull(GSON.fromJson("VAL_A", EnumCustomAdapter.class));
        assertNull(GSON.fromJson("VAL_B", EnumCustomAdapter.class));
    
        assertEquals(new JsonPrimitive("AW"), GSON.toJsonTree(EnumCustomAdapter.VAL_A));
        assertEquals(new JsonPrimitive("BW"), GSON.toJsonTree(EnumCustomAdapter.VAL_B));
    }
    
    
    enum EnumDefault {
        VAL_A, VAL_B
    }
    
    enum EnumNamed {
        @SerializedName(value = "A", alternate = "aA") VAL_A,
        VAL_B;
    }
    
    @JsonAdapter(CustomAdapter.class)
    enum EnumCustomAdapter {
        VAL_A,
        VAL_B;
    }
    
    static class CustomAdapter extends TypeAdapter<EnumCustomAdapter> {
        @Override
        public void write(JsonWriter out, EnumCustomAdapter value) throws IOException {
            out.value(value == EnumCustomAdapter.VAL_A ? "AW" : "BW");
        }
    
        @Override
        public EnumCustomAdapter read(JsonReader in) throws IOException {
            switch (in.nextString()) {
                case "ar": return EnumCustomAdapter.VAL_A;
                case "br": return EnumCustomAdapter.VAL_B;
                default: return null;
            }
        }
    }
    
}