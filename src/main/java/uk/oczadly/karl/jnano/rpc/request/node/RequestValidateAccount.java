package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseValidation;

/**
 * This request class is used to verify whether an address contains a valid checksum.
 * <br>Calls the RPC command {@code validate_account_number}, and returns a {@link ResponseValidation} data object.
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
