/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model;

import com.google.gson.JsonParser;
import org.junit.Test;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WorkDifficultyTest {
    
    static final String TEST_WORK_1 = "fffffe0000000000";
    static final long TEST_WORK_1_INT = Long.parseUnsignedLong(TEST_WORK_1, 16);
    static final String TEST_WORK_2 = "fffffff800000000";
    
    
    @Test
    public void testConstructor() {
        WorkDifficulty obj1 = new WorkDifficulty(TEST_WORK_1);
        WorkDifficulty obj2 = new WorkDifficulty(TEST_WORK_1_INT);
    
        assertEquals(TEST_WORK_1, obj1.getAsHexadecimal());
        assertEquals(TEST_WORK_1, obj2.getAsHexadecimal());
        assertEquals(TEST_WORK_1_INT, obj1.getAsLong());
        assertEquals(TEST_WORK_1_INT, obj2.getAsLong());
    }
    
    @Test
    public void testMultiplyCalculation() {
        double multiplier = new WorkDifficulty(TEST_WORK_2)
                .calculateMultiplier(new WorkDifficulty(TEST_WORK_1));
        assertEquals(64, multiplier, 1e-6);
    }
    
    @Test
    public void testMultiply() {
        WorkDifficulty result = new WorkDifficulty(TEST_WORK_1).multiply(64);
        assertEquals(TEST_WORK_2, result.getAsHexadecimal());
    }
    
    @Test
    public void testJsonDeserialize() {
        WorkDifficulty obj = JNH.GSON.fromJson("\"" + TEST_WORK_1 + "\"", WorkDifficulty.class);
        assertEquals(TEST_WORK_1, obj.getAsHexadecimal());
    }
    
    @Test
    public void testJsonSerialize() {
        WorkDifficulty obj = new WorkDifficulty(TEST_WORK_1);
        
        assertEquals(JsonParser.parseString("\"" + TEST_WORK_1 + "\""),
                JsonParser.parseString(JNH.GSON.toJson(obj)));
    }
    
    @Test
    public void testEquality() {
        WorkDifficulty obj1 = new WorkDifficulty(TEST_WORK_1);
        WorkDifficulty obj2 = new WorkDifficulty(TEST_WORK_1);
        WorkDifficulty obj3 = new WorkDifficulty(TEST_WORK_2);
        
        assertEquals(obj1, obj2);
        assertNotEquals(obj1, obj3);
        assertNotEquals(obj2, obj3);
    }

}