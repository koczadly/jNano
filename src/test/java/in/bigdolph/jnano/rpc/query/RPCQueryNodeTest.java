package in.bigdolph.jnano.rpc.query;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.TestNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class RPCQueryNodeTest {
    
    @Test
    public void testBlankArrays() throws Exception {
        TestNode node = new TestNode();
        ArrayTestResponse deserialized;
        
        //2 items
        System.out.println("Test 2 items");
        deserialized = node.getGson().fromJson("{arr:[1,2]}", ArrayTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.array);
        assertEquals(2, deserialized.array.length);
    
        //0 items
        System.out.println("Test empty array");
        deserialized = node.getGson().fromJson("{arr:[]}", ArrayTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.array);
        assertEquals(0, deserialized.array.length);
    
        //empty string
        System.out.println("Test empty string");
        deserialized = node.getGson().fromJson("{arr:\"\"}", ArrayTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.array);
        assertEquals(0, deserialized.array.length);
    }
    
    
    
    
    private static class ArrayTestResponse {
        @Expose
        @SerializedName("arr")
        public int[] array;
    }
    
}