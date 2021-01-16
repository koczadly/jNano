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
        final String acc = "nano_3pg8khw8gs94c1qeq9741n99ubrut8sj3n9kpntim1rm35h4wdzirofazmwt";
        assertEquals(JsonParser.parseString("{\"action\":\"account_balance\", \"account\":\"" + acc + "\"}"),
                JsonParser.parseString(serializer.serialize(new RequestAccountBalance(acc))));
    }
    
}