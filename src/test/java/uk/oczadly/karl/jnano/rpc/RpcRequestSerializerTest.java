/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import com.google.gson.JsonParser;
import org.junit.Test;
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountBalance;
import uk.oczadly.karl.jnano.rpc.request.node.RequestVersion;

import static org.junit.Assert.assertEquals;

public class RpcRequestSerializerTest {
    
    RpcRequestSerializer serializer = new JsonRequestSerializer();
    
    
    @Test
    public void testSerialization() {
        // No params
        assertEquals(JsonParser.parseString("{\"action\":\"version\"}"),
                JsonParser.parseString(serializer.serialize(new RequestVersion())));
        
        // With param
        assertEquals(JsonParser.parseString("{\"action\":\"account_balance\", \"account\":\"123\"}"),
                JsonParser.parseString(serializer.serialize(new RequestAccountBalance("123"))));
    }
    
}