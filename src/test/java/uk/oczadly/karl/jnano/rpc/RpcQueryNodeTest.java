/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import org.junit.Test;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * @author Karl Oczadly
 */
public class RpcQueryNodeTest {

    @Test
    public void testBuilder() throws Exception {
        URL url = new URL("https://api.nanex.cc");
        RpcRequestSerializer serializer = new JsonRequestSerializer();
        RpcResponseDeserializer deserializer = new JsonResponseDeserializer();
        RpcRequestExecutor executor = new HttpRequestExecutor();
        ExecutorService es = Executors.newFixedThreadPool(1);
        
        RpcQueryNode node = new RpcQueryNode.Builder()
                .setAddress(url)
                .setSerializer(serializer)
                .setDeserializer(deserializer)
                .setRequestExecutor(executor)
                .setAsyncExecutorService(es)
                .setDefaultTimeout(420)
                .build();
    
        assertSame(url, node.getAddress());
        assertSame(serializer, node.getRequestSerializer());
        assertSame(deserializer, node.getResponseDeserializer());
        assertSame(executor, node.getRequestExecutor());
        assertSame(es, node.getExecutorService());
        assertEquals(420, node.getDefaultTimeout());
    }
    
    @Test
    public void testProcess() throws Exception {
        MockRequest mockRequest = new MockRequest();
        MockResponse mockResponse = new MockResponse();
        RpcQueryNode node = new RpcQueryNode.Builder()
                .setSerializer(req -> "REQ")
                .setRequestExecutor((address, req, timeout) -> {
                    assertEquals("REQ", req);
                    return "EXEC";
                })
                .setDeserializer(new RpcResponseDeserializer() {
                    @Override
                    public <R extends RpcResponse> R deserialize(String response, Class<R> responseClass)
                            throws RpcException {
                        assertEquals("EXEC", response);
                        return (R)mockResponse;
                    }
                }).build();
        
        // Sync
        assertSame(mockResponse, node.processRequest(mockRequest));
    
        // Async (future)
        Future<MockResponse> futureRes = node.processRequestAsync(mockRequest);
        assertSame(mockResponse, futureRes.get());
    
        // Async (callback)
        MockCallback callback = new MockCallback();
        node.processRequestAsync(mockRequest, callback);
        callback.latch.await(5000, TimeUnit.MILLISECONDS);
        assertTrue("Callback was not successful", callback.success);
    }
    
    
    
    static class MockRequest extends RpcRequest<MockResponse> {
        public MockRequest() {
            super("mock_cmd", MockResponse.class);
        }
    }
    
    static class MockResponse extends RpcResponse {}
    
    static class MockCallback implements QueryCallback<MockRequest, MockResponse> {
        final CountDownLatch latch = new CountDownLatch(1);
        volatile boolean success = false;
        
        @Override
        public void onResponse(MockResponse response, MockRequest request) {
            success = true;
            latch.countDown();
        }
    
        @Override
        public void onFailure(RpcException ex, MockRequest request) {}
    
        @Override
        public void onFailure(IOException ex, MockRequest request) {}
    }

}