/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.junit.Test;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Karl Oczadly
 */
public class JsonRequestSerializerTest {

    @Test
    public void testSerialize() {
        TestRequest req = new TestRequest(69, 420);
        String json = new JsonRequestSerializer().serialize(req);
        
        assertNotNull(json);
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        assertEquals("test_command", obj.get("action").getAsString());
        assertEquals(69, obj.get("val_a").getAsInt());
        assertEquals(420, obj.get("val_b").getAsInt());
    }
    
    
    static class TestRequest extends RpcRequest<ResponseSuccessful> {
        @Expose @SerializedName("val_a") private int a;
        @Expose @SerializedName("val_b") private int b;
        
        public TestRequest(int a, int b) {
            super("test_command", ResponseSuccessful.class);
            this.a = a;
            this.b = b;
        }
    }
    
}