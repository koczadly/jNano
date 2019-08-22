package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseValidation;

/**
 * This request class is used to unlock an encrypted local wallet.
 * The server responds with a {@link ResponseValidation} data object.<br>
 * Calls the internal RPC method {@code password_enter}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#password_enter">Official RPC documentation</a>
 */
public class RequestPasswordEnter extends RpcRequest<ResponseValidation> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("password")
    private final String password;
    
    
    /**
     * @param walletId  the wallet's ID
     * @param password  the decryption password
     */
    public RequestPasswordEnter(String walletId, String password) {
        super("password_enter", ResponseValidation.class);
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
     * @return the decryption password
     */
    public String getPassword() {
        return password;
    }
    
}
