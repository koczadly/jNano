package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;

public interface RpcRequestSerializer {
    
    /**
     * Serializes a given {@link RpcRequest} object into the expected string format. This string value is then
     * submitted to the node as the request body.
     * @param request the RPC request data
     * @return the serialized request
     */
    String serialize(RpcRequest<?> request);

}
