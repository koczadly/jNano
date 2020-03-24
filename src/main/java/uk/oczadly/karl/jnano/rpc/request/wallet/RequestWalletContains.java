package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseExists;

/**
 * This request class is used to check whether a wallet contains a specified account.
 * <br>Calls the RPC command {@code wallet_contains}, and returns a {@link ResponseExists} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_contains">Official RPC documentation</a>
 */
public class RequestWalletContains extends RpcRequest<ResponseExists> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param walletId  the wallet's ID
     * @param account   the account's address
     */
    public RequestWalletContains(String walletId, String account) {
        super("wallet_contains", ResponseExists.class);
        this.walletId = walletId;
        this.account = account;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the account's address
     */
    public String getAccount() {
        return account;
    }
    
}
