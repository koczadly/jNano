package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseValidation;

/**
 * This request class is used to verify whether an address contains a valid checksum.
 * The server responds with a {@link ResponseValidation} data object.<br>
 * Calls the internal RPC method {@code validate_account_number}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#validate_account_number">Official RPC documentation</a>
 */
public class RequestValidateAccount extends RpcRequest<ResponseValidation> {
    
    @Expose @SerializedName("account")
    private final String address;
    
    
    /**
     * @param address the address to validate
     */
    public RequestValidateAccount(String address) {
        super("validate_account_number", ResponseValidation.class);
        this.address = address;
    }
    
    
    /**
     * @return the address to be validated
     */
    public String getAddress() {
        return address;
    }
    
}
