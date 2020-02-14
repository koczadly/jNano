package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletId;

/**
 * This request class is used to create a new local wallet.
 * The server responds with a {@link ResponseWalletId} data object.<br>
 * Calls the internal RPC method {@code wallet_create}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_create">Official RPC documentation</a>
 */
public class RequestWalletCreate extends RpcRequest<ResponseWalletId> {
    
    @Expose @SerializedName("seed")
    private final String seed;
    
    
    public RequestWalletCreate() {
        this(null);
    }
    
    /**
     * @param seed the seed for the wallet to use
     */
    public RequestWalletCreate(String seed) {
        super("wallet_create", ResponseWalletId.class);
        this.seed = seed;
    }
    
    
    /**
     * @return the seed for the wallet to use
     */
    public String getSeed() {
        return seed;
    }
    
}
