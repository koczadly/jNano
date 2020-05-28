package uk.oczadly.karl.jnano.rpc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.exception.RpcInvalidResponseException;
import uk.oczadly.karl.jnano.rpc.response.ResponseVersion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RpcResponseDeserializerTest {

    RpcResponseDeserializer deserializer = new RpcResponseDeserializerImpl();
    
    
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
        JsonObject json = JsonParser.parseString("{\"rpc_version\":\"123\"}").getAsJsonObject();
        
        ResponseVersion response = deserializer.deserialize(json.toString(), ResponseVersion.class);
        assertNotNull(response.getRawResponseJson());
        assertEquals(json, response.getRawResponseJson());
    }

}