package uk.oczadly.karl.jnano.model;

import com.google.gson.JsonParser;
import org.junit.Test;
import uk.oczadly.karl.jnano.internal.JNanoHelper;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WorkDifficultyTest {
    
    static final String TEST_WORK_1 = "fffffe0000000000";
    static final BigInteger TEST_WORK_1_INT = new BigInteger("18446741874686296064");
    static final String TEST_WORK_2 = "fffffff800000000";
    
    
    @Test
    public void testConstructor() {
        WorkDifficulty obj1 = new WorkDifficulty(TEST_WORK_1);
        WorkDifficulty obj2 = new WorkDifficulty(TEST_WORK_1_INT);
    
        assertEquals(TEST_WORK_1, obj1.getAsHexadecimal());
        assertEquals(TEST_WORK_1, obj2.getAsHexadecimal());
        assertEquals(TEST_WORK_1_INT, obj1.getAsInteger());
        assertEquals(TEST_WORK_1_INT, obj2.getAsInteger());
    }
    
    @Test
    public void testMultiplyCalculation() {
        double multiplier = new WorkDifficulty(TEST_WORK_2).calculateMultiplier(new WorkDifficulty(TEST_WORK_1));
        assertEquals(64, multiplier, 1e-9);
    }
    
    @Test
    public void testMultiply() {
        WorkDifficulty result = new WorkDifficulty(TEST_WORK_1).multiply(64);
        assertEquals(TEST_WORK_2, result.getAsHexadecimal());
    }
    
    @Test
    public void testJsonDeserialize() {
        WorkDifficulty obj = JNanoHelper.GSON.fromJson("\"" + TEST_WORK_1 + "\"", WorkDifficulty.class);
        assertEquals(TEST_WORK_1, obj.getAsHexadecimal());
    }
    
    @Test
    public void testJsonSerialize() {
        WorkDifficulty obj = new WorkDifficulty(TEST_WORK_1);
        
        assertEquals(JsonParser.parseString("\"" + TEST_WORK_1 + "\""),
                JsonParser.parseString(JNanoHelper.GSON.toJson(obj)));
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