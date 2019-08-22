package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to change the password of a local wallet.
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code password_change}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#password_change">Official RPC documentation</a>
 */
public class RequestPasswordChange extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("password")
    private final String password;
    
    
    /**
     * @param walletId  the wallet's ID
     * @param password  the new encryption password
     */
    public RequestPasswordChange(String walletId, String password) {
        super("password_change", ResponseSuccessful.class);
        this.walletId = walletId;
        this.password = password;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the new encryption password
     */
    public String getPassword() {
        return password;
    }
    
}
