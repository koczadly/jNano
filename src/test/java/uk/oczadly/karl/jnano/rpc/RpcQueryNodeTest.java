/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import org.junit.Test;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.exception.RpcInvalidArgumentException;
import uk.oczadly.karl.jnano.rpc.exception.RpcUnhandledException;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static org.junit.Assert.*;

/**
 * @author Karl Oczadly
 */
public class RpcQueryNodeTest {

    @Test
    public void testBuilder() throws Exception {
        RpcRequestSerializer serializer = new JsonRequestSerializer();
        RpcResponseDeserializer deserializer = new JsonResponseDeserializer();
        RpcRequestExecutor executor = new HttpRequestExecutor(new URL("https://localhost:1337"));
        ExecutorService es = Executors.newFixedThreadPool(1);
        
        RpcQueryNode node = new RpcQueryNode.Builder()
                .setSerializer(serializer)
                .setDeserializer(deserializer)
                .setRequestExecutor(executor)
                .setAsyncExecutorService(es)
                .setDefaultTimeout(420)
                .build();
        
        assertSame(serializer, node.getRequestSerializer());
        assertSame(deserializer, node.getResponseDeserializer());
        assertSame(executor, node.getRequestExecutor());
        assertSame(es, node.getExecutorService());
        assertEquals(420, node.getDefaultTimeout());
    }
    
    @Test
    public void testPipeline() throws Exception {
        MockRequest request = new MockRequest();
        MockResponse response = new MockResponse();
        MockSerializer serializer = new MockSerializer();
        MockExecutor executor = new MockExecutor();
        MockDeserializer deserializer = new MockDeserializer(response);
        
        RpcQueryNode rpcClient = RpcQueryNode.builder()
                .setSerializer(serializer).setRequestExecutor(executor).setDeserializer(deserializer).build();
        rpcClient.processRequest(request);
    
        assertSame(request, serializer.request);
        assertEquals("{MockRequestData}", executor.request);
        assertEquals("{MockResponseData}", deserializer.responseData);
        assertSame(response.getClass(), deserializer.responseClass);
    }
    
    @Test
    public void testBlockingQuery() throws Exception {
        MockRequest request = new MockRequest();
        MockResponse response = new MockResponse();
        RpcQueryNode rpcClient = RpcQueryNode.builder()
                .setSerializer(new MockSerializer())
                .setRequestExecutor(new MockExecutor())
                .setDeserializer(new MockDeserializer(response))
                .build();
        
        assertSame(response, rpcClient.processRequest(request));
    }
    
    @Test
    public void testAsyncQuery() throws Exception {
        MockRequest request = new MockRequest();
        MockResponse response = new MockResponse();
        RpcQueryNode rpcClient = RpcQueryNode.builder()
                .setSerializer(new MockSerializer())
                .setRequestExecutor(new MockExecutor())
                .setDeserializer(new MockDeserializer(response))
                .build();
        
        MockCallback callback = new MockCallback();
        Future<MockResponse> futureRes = rpcClient.processRequestAsync(request, callback);
        assertSame(response, futureRes.get());
        if (!callback.latch.await(1, TimeUnit.SECONDS))
            fail("No callback response");
        assertSame(response, callback.response);
        assertSame(request, callback.request);
    }
    
    
    @Test
    public void testExceptionHandling() throws Exception {
        // Serializer unhandled exception
        assertRpcThrows(RpcUnhandledException.class, builder -> builder
                .setSerializer(request -> {
                    throw new RuntimeException();
                }));
    
        // Executor unhandled exception
        assertRpcThrows(RpcUnhandledException.class, builder -> builder
                .setRequestExecutor((request, timeout) -> {
                    throw new RuntimeException();
                }));
        // Executor IO exception
        assertRpcThrows(IOException.class, builder -> builder
                .setRequestExecutor((request, timeout) -> {
                    throw new IOException();
                }));
    
        // Deserializer unhandled exception
        assertRpcThrows(RpcUnhandledException.class, builder -> builder
                .setDeserializer(new RpcResponseDeserializer() {
                    @Override
                    public <R extends RpcResponse> R deserialize(String response, Class<R> responseClass) {
                        throw new RuntimeException();
                    }
                }));
        // Deserializer IO exception
        assertRpcThrows(RpcInvalidArgumentException.class, builder -> builder
                .setDeserializer(new RpcResponseDeserializer() {
                    @Override
                    public <R extends RpcResponse> R deserialize(String response, Class<R> responseClass)
                            throws RpcException {
                        throw new RpcInvalidArgumentException("Bad account");
                    }
                }));
    }
    
    private static void assertRpcThrows(Class<? extends Exception> e, Consumer<RpcQueryNode.Builder> nodeCreator) {
        RpcQueryNode.Builder builder = RpcQueryNode.builder()
                .setSerializer(new MockSerializer())
                .setRequestExecutor(new MockExecutor())
                .setDeserializer(new MockDeserializer(new MockResponse()));
        nodeCreator.accept(builder);
        assertThrows(e, () -> builder.build().processRequest(new MockRequest()));
    }
    
    
    
    static class MockSerializer implements RpcRequestSerializer {
        volatile RpcRequest<?> request;
        
        @Override
        public String serialize(RpcRequest<?> request) {
            this.request = request;
            return "{MockRequestData}";
        }
    }
    
    static class MockExecutor implements RpcRequestExecutor {
        volatile String request;
        
        @Override
        public String submit(String request, int timeout) throws IOException {
            this.request = request;
            return "{MockResponseData}";
        }
    }
    
    static class MockDeserializer implements RpcResponseDeserializer {
        final MockResponse response;
        volatile String responseData;
        volatile Class<?> responseClass;
    
        public MockDeserializer(MockResponse response) {
            this.response = response;
        }
    
        @Override
        public <R extends RpcResponse> R deserialize(String response, Class<R> responseClass) throws RpcException {
            this.responseData = response;
            this.responseClass = responseClass;
            return (R)this.response;
        }
    }
    
    static class MockRequest extends RpcRequest<MockResponse> {
        public MockRequest() {
            super("mock_cmd", MockResponse.class);
        }
    }
    
    static class MockResponse extends RpcResponse {}
    
    static class MockCallback implements QueryCallback<MockRequest, MockResponse> {
        final CountDownLatch latch = new CountDownLatch(1);
        volatile MockResponse response;
        volatile MockRequest request;
        
        @Override
        public void onResponse(MockResponse response, MockRequest request) {
            this.response = response;
            this.request = request;
            latch.countDown();
        }
    
        @Override
        public void onFailure(RpcException ex, MockRequest request) {
            ex.printStackTrace();
            latch.countDown();
        }
    
        @Override
        public void onFailure(IOException ex, MockRequest request) {
            ex.printStackTrace();
            latch.countDown();
        }
    }

}