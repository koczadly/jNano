package uk.oczadly.karl.jnano.rpc.query;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.query.exception.RpcUnknownCommandException;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.request.node.NodeVersionRequest;
import uk.oczadly.karl.jnano.rpc.query.response.RpcResponse;
import uk.oczadly.karl.jnano.rpc.query.response.NodeVersionResponse;
import uk.oczadly.karl.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class RpcQueryNodeTest {
    
    private TestNode node;
    
    public RpcQueryNodeTest() throws Exception {
        this.node = new TestNode();
    }
    
    
    
    @Test
    @Category(NodeTests.class)
    public void testSyncQuery() throws Exception {
        //Test valid query
        NodeVersionResponse res = node.processRequest(new NodeVersionRequest());
        assertNotNull(res);
        System.out.println(res.getNodeVendor());
        
        //Test invalid query
        try {
            RpcResponse invalidRes = node.processRequest(new TestRequest("invalid_command_ayy_lmao"));
            fail("Invalid command was processed");
        } catch (RpcUnknownCommandException e) {}
    }
    
    @Test
    @Category(NodeTests.class)
    public void testAsyncQuery() throws Exception {
        //Test valid query
        TestCallback callback = new TestCallback();
        Future<RpcResponse> futureRes = node.processRequestAsync(new TestRequest("version"), 2500, callback);
        assertNotNull(futureRes);
        while(!callback.processed); //Wait for query to respond
        System.out.println("Async query processed");
        assertNull(callback.exception);
        assertNotNull(callback.response);
        
        //Test valid query
        callback = new TestCallback();
        futureRes = node.processRequestAsync(new TestRequest("invalid_command_ayy_lmao"), 2500, callback);
        assertNotNull(futureRes);
        while(!callback.processed); //Wait for query to respond
        System.out.println("Async query processed");
        assertNotNull(callback.exception);
        assertTrue(callback.exception instanceof RpcException);
        assertNull(callback.response);
    }
    
    
    private static class TestCallback implements QueryCallback<RpcResponse> {
        volatile boolean processed = false;
        volatile RpcResponse response;
        volatile Exception exception;
        
        @Override
        public void onResponse(RpcResponse response) {
            this.response = response;
            this.processed = true;
        }
        
        @Override
        public void onFailure(Exception ex) {
            this.exception = ex;
            this.processed = true;
        }
    }
    
    public static class TestRequest extends RpcRequest<RpcResponse> {
        public TestRequest(String cmd) {
            super(cmd, RpcResponse.class);
        }
    }
    
    
    
    @Test
    public void testEmptyArray() throws Exception {
        TestNode node = new TestNode();
        ArrayTestResponse deserialized;
        
        //2 items
        System.out.println("Test 2 items");
        deserialized = node.getGsonInstance().fromJson("{arr:[1,2]}", ArrayTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.array);
        assertEquals(2, deserialized.array.length);
    
        //0 items
        System.out.println("Test empty array");
        deserialized = node.getGsonInstance().fromJson("{arr:[]}", ArrayTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.array);
        assertEquals(0, deserialized.array.length);
    
        //empty string
        System.out.println("Test empty string");
        deserialized = node.getGsonInstance().fromJson("{arr:\"\"}", ArrayTestResponse.class);
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
        deserialized = node.getGsonInstance().fromJson("{col:[1,2]}", CollectionTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.col);
        assertEquals(2, deserialized.col.size());
        
        //0 items
        System.out.println("Test empty collection");
        deserialized = node.getGsonInstance().fromJson("{col:[]}", CollectionTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.col);
        assertEquals(0, deserialized.col.size());
        
        //empty string
        System.out.println("Test empty string");
        deserialized = node.getGsonInstance().fromJson("{col:\"\"}", CollectionTestResponse.class);
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
        deserialized = node.getGsonInstance().fromJson("{map:{1:1,2:2}}", MapTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.map);
        assertEquals(2, deserialized.map.size());
        
        //0 items
        System.out.println("Test empty map");
        deserialized = node.getGsonInstance().fromJson("{map:{}}", MapTestResponse.class);
        assertNotNull(deserialized);
        assertNotNull(deserialized.map);
        assertEquals(0, deserialized.map.size());
        
        //empty string
        System.out.println("Test empty string");
        deserialized = node.getGsonInstance().fromJson("{map:\"\"}", MapTestResponse.class);
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