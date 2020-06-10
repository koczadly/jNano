package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseTelemetry;

/**
 * This request class is used to request node telemetry information and statistics.
 * <br>Calls the RPC command {@code telemetry}, and returns a {@link ResponseTelemetry} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#telemetry">Official RPC documentation</a>
 */
public class RequestTelemetry extends RpcRequest<ResponseTelemetry> {
    
    public RequestTelemetry() {
        super("telemetry", ResponseTelemetry.class);
    }
    
}
