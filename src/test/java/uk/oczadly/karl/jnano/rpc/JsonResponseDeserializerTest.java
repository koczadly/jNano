/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import org.junit.Assert;
import org.junit.Test;
import uk.oczadly.karl.jnano.rpc.exception.*;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Karl Oczadly
 */
public class JsonResponseDeserializerTest {
    
    JsonResponseDeserializer deserializer = new JsonResponseDeserializer();
    
    
    @Test
    public void testDeserializeNormal() throws Exception {
        String json = "{\"hash\": \"6AACA0D90E760840A3418F6C961423A15501DD693B96C9A7327CBD93D2B7D6EC\"}";
        ResponseBlockHash res = deserializer.deserialize(json, ResponseBlockHash.class);
        
        assertNotNull(res);
        assertEquals(res.getBlockHash(),"6AACA0D90E760840A3418F6C961423A15501DD693B96C9A7327CBD93D2B7D6EC");
        assertNotNull(res.getRawResponseJson());
    }
    
    @Test
    public void testDeserializeEmptyResponse() throws Exception {
        String json = errorJson("Empty response");
        
        // Assert returns for ResponseSuccessful
        assertNotNull(deserializer.deserialize(json, ResponseSuccessful.class));
        
        // Assert throws error for other types
        Assert.assertThrows(RpcException.class, () -> deserializer.deserialize(json, ResponseBlockHash.class));
    }
    
    @Test
    public void testInternalExceptions() throws Exception {
        assertThrows(RpcInvalidResponseException.class, "{420}");
        assertThrows(RpcUnrecognizedException.class, errorJson("Node is morbidly obese"));
    }
    
    @Test
    public void testNodeExceptions() throws Exception {
        assertThrows(RpcConfigForbiddenException.class, errorJson("Configuration disallows x"));
        assertThrows(RpcControlDisabledException.class, errorJson("RPC control is disabled"));
        assertThrows(RpcEntityNotFoundException.class, errorJson("Source not found"));
        assertThrows(RpcEntityNotFoundException.class, errorJson("Wallet not found"));
        assertThrows(RpcFeatureDisabledException.class, errorJson("Signing by block hash is disabled"));
        assertThrows(RpcInternalException.class, errorJson("Internal error"));
        assertThrows(RpcInternalException.class, errorJson("Empty response"));
        assertThrows(RpcInvalidArgumentException.class, errorJson("Block is invalid"));
        assertThrows(RpcInvalidArgumentException.class, errorJson("Bad account number"));
        assertThrows(RpcRequestCancelledException.class, errorJson("Cancelled"));
        assertThrows(RpcUnknownCommandException.class, errorJson("Unknown command"));
        assertThrows(RpcUnsafeNotAllowedException.class, errorJson("Unsafe RPC not allowed"));
        assertThrows(RpcWalletLockedException.class, errorJson("Wallet is locked"));
    }
    
    
    
    private void assertThrows(Class<? extends RpcException> clazz, String json) {
        Assert.assertThrows(clazz, () -> deserializer.deserialize(json, ResponseBlockHash.class));
    }
    
    private String errorJson(String msg) {
        return "{\"error\": \"" + msg + "\"}";
    }
    
}