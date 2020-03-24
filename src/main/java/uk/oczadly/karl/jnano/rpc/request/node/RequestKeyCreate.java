package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseKeyPair;

/**
 * This request class is used to generate a random public and private key.
 * <br>Calls the RPC command {@code key_create}, and returns a {@link ResponseKeyPair} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#key_create">Official RPC documentation</a>
 */
public class RequestKeyCreate extends RpcRequest<ResponseKeyPair> {
    
    public RequestKeyCreate() {
        super("key_create", ResponseKeyPair.class);
    }
    
}
