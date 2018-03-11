package in.bigdolph.jnano.rpc.query;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.TestNode;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RPCQueryNodeTest {
    
    @Test
    public void testEmptyArray() throws Exception {
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
    
    @Test
    public void testEmptyCollection() throws Exception {
        TestNode node = new TestNode();
        CollectionTestResponse deserialized;
        
        //2 items
        System.out.println("Test 2 items");
        deserialized = node.getGson().fromJson("{col:[1,2]}", CollectionTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.col);
        assertEquals(2, deserialized.col.size());
        
        //0 items
        System.out.println("Test empty collection");
        deserialized = node.getGson().fromJson("{col:[]}", CollectionTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.col);
        assertEquals(0, deserialized.col.size());
        
        //empty string
        System.out.println("Test empty string");
        deserialized = node.getGson().fromJson("{col:\"\"}", CollectionTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.col);
        assertEquals(0, deserialized.col.size());
    }
    
    @Test
    public void testEmptyMap() throws Exception {
        TestNode node = new TestNode();
        MapTestResponse deserialized;
        
        //2 items
        System.out.println("Test 2 items");
        deserialized = node.getGson().fromJson("{map:{1:1,2:2}}", MapTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.map);
        assertEquals(2, deserialized.map.size());
        
        //0 items
        System.out.println("Test empty map");
        deserialized = node.getGson().fromJson("{map:{}}", MapTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.map);
        assertEquals(0, deserialized.map.size());
        
        //empty string
        System.out.println("Test empty string");
        deserialized = node.getGson().fromJson("{map:\"\"}", MapTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.map);
        assertEquals(0, deserialized.map.size());
    }
    
    
    
    
    private static class ArrayTestResponse {
        @Expose
        @SerializedName("arr")
        public int[] array;
    }
    
    private static class MapTestResponse {
        @Expose
        @SerializedName("map")
        public Map<Integer, Integer> map;
    }
    
    private static class CollectionTestResponse {
        @Expose
        @SerializedName("col")
        public List<Integer> col;
    }
    
}