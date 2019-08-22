package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

import java.math.BigInteger;

/**
 * This request class is used to set the minimum amount required for the node to automatically receive a pending block.
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code receive_minimum_set}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#receive_minimum_set">Official RPC documentation</a>
 */
public class RequestReceiveMinimumSet extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("amount")
    private final BigInteger amount;
    
    
    /**
     * @param amount the minimum amount
     */
    public RequestReceiveMinimumSet(BigInteger amount) {
        super("receive_minimum_set", ResponseSuccessful.class);
        this.amount = amount;
    }
    
    
    /**
     * @return the minimum amount
     */
    public BigInteger getAmount() {
        return amount;
    }
    
}
