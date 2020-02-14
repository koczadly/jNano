package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseKeyPair;

/**
 * This request class is used to generate a random public and private key.
 * The server responds with a {@link ResponseKeyPair} data object.<br>
 * Calls the internal RPC method {@code key_create}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#key_create">Official RPC documentation</a>
 */
public class RequestKeyCreate extends RpcRequest<ResponseKeyPair> {
    
    public RequestKeyCreate() {
        super("key_create", ResponseKeyPair.class);
    }
    
}
