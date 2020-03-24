package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

/**
 * This request class is used to request the block hash of the block currently being confirmed.
 * <br>Calls the RPC command {@code confirmation_height_currently_processing}, and returns a {@link ResponseBlockHash} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#confirmation_height_currently_processing">
 *     Official RPC documentation</a>
 *
 * @deprecated This request is for debugging purposes only and is subject to change with each node revision.
 */
public class RequestConfirmationHeightProcessing extends RpcRequest<ResponseBlockHash> {
    
    public RequestConfirmationHeightProcessing() {
        super("confirmation_height_currently_processing", ResponseBlockHash.class);
    }
    
}
