/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model;

import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Karl Oczadly
 */
public class HexDataTest {
    
    @Test
    public void testConstructBytes() {
        byte[] bytes = new byte[] {(byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE};
        HexData data = new HexData(bytes);
        
        assertArrayEquals(bytes, data.toByteArray());
        assertEquals("CAFEBABE", data.toHexString());
        assertEquals(4, data.length());
    }
    
    @Test
    public void testConstructBytesLength() {
        byte[] bytes = new byte[] {(byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE};
        byte[] bytesZero = new byte[] {0, (byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE};
        HexData data = new HexData(bytes, 5);
        
        assertArrayEquals(bytesZero, data.toByteArray());
        assertEquals("00CAFEBABE", data.toHexString());
        assertEquals(5, data.length());
    }
    
    @Test
    public void testConstructString() {
        HexData data = new HexData("CAFEbabe");
        
        assertArrayEquals(new byte[] {(byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE}, data.toByteArray());
        assertEquals("CAFEBABE", data.toHexString());
        assertEquals(4, data.length());
    }
    
    @Test
    public void testConstructStringLength() {
        HexData data = new HexData("CAFEBABE", 5);
        
        assertArrayEquals(new byte[] {(byte)0x00, (byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE}, data.toByteArray());
        assertEquals("00CAFEBABE", data.toHexString());
        assertEquals(5, data.length());
    }
    
    @Test
    public void testEquality() {
        HexData o1 = new HexData("CAFE");
        HexData o2 = new HexData("CAFE");
        HexData o3 = new HexData("BABE");
    
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
        assertNotEquals(o1, o3);
        assertNotEquals(o2, o3);
    }
    
    @Test
    public void testValueEquality() {
        HexData o1 = new HexData("CAFE");
        HexData o2 = new HexData("00CAFE");
        assertTrue(o1.equalsValue("CAFE"));
        assertTrue(o1.equalsValue("000CAFE"));
        assertTrue(o2.equalsValue("CAFE"));
        assertTrue(o2.equalsValue("000CAFE"));
        assertFalse(o1.equalsValue(""));
        assertFalse(o1.equalsValue("BABE"));
        assertFalse(o1.equalsValue("SLUG"));
    }
    
    @Test
    public void testJson() {
        assertEquals("CAFEBABE", new Gson().fromJson("CAFEBABE", HexData.class).toHexString());
        assertEquals("\"CAFEBABE\"", new Gson().toJson(new HexData("CAFEBABE")));
    }
    
}