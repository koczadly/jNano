/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import com.google.gson.JsonObject;
import org.junit.Test;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.exception.RpcInvalidResponseException;
import uk.oczadly.karl.jnano.rpc.response.ResponseVersion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RpcResponseDeserializerTest {

    JsonResponseDeserializer deserializer = new JsonResponseDeserializer();
    
    
    @Test(expected = RpcException.class)
    public void testExceptionUnknown() throws RpcException {
        deserializer.deserialize("{\"error\":\"Unknown error\"}", ResponseVersion.class);
    }
    
    @Test(expected = RpcInvalidResponseException.class)
    public void testExceptionInvalidResponse() throws RpcException {
        deserializer.deserialize("INVALID JSON", ResponseVersion.class);
    }
    
    @Test
    public void testStandardParse() throws RpcException {
        ResponseVersion response = deserializer.deserialize("{\"rpc_version\":\"123\"}", ResponseVersion.class);
        assertEquals(123, response.getRpcVersion());
    }
    
    @Test
    public void testRawJsonObject() throws RpcException {
        JsonObject json = JNH.parseJson("{\"rpc_version\":\"123\"}");
        
        ResponseVersion response = deserializer.deserialize(json.toString(), ResponseVersion.class);
        assertNotNull(response.getRawResponseJson());
        assertEquals(json, response.getRawResponseJson());
    }

}