package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanTypeDeserializerTest {
    
    private Gson gson = new GsonBuilder().registerTypeAdapter(boolean.class, new BooleanTypeDeserializer())
            .registerTypeAdapter(Boolean.class, new BooleanTypeDeserializer()).create();
    
    
    @Test
    public void test() {
        runTests("true", "false");
        runTests("TRUE", "FALSE");
        runTests("TrUe", "FaLsE");
        runTests("1", "0");
    }
    
    
    
    
    public void runTests(String valTrue, String valFalse) {
        String trueJson = "{\"val\":" + valTrue + "}",
                falseJson = "{\"val\":" + valFalse + "}";
        
        //Primitive
        TestContainerPrimitive primitive = gson.fromJson(trueJson, TestContainerPrimitive.class);
        assertTrue(primitive.val);
        primitive = gson.fromJson(falseJson, TestContainerPrimitive.class);
        assertFalse(primitive.val);
        
        //Encapsulated
        TestContainerWrapper wrapper = gson.fromJson(trueJson, TestContainerWrapper.class);
        assertTrue(wrapper.val);
        wrapper = gson.fromJson(falseJson, TestContainerWrapper.class);
        assertFalse(wrapper.val);
    }
    
    
    public static class TestContainerPrimitive {
        @Expose
        @SerializedName("val")
        public boolean val;
    }
    
    public static class TestContainerWrapper {
        @Expose
        @SerializedName("val")
        public Boolean val;
    }
    
}