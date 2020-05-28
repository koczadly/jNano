package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountsMoved;

/**
 * This request class is used to move accounts from one local wallet to another.
 * <br>Calls the RPC command {@code account_move}, and returns a {@link ResponseAccountsMoved} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_move">Official RPC documentation</a>
 */
public class RequestAccountsMove extends RpcRequest<ResponseAccountsMoved> {
    
    @Expose @SerializedName("wallet")
    private final String destinationWalletId;
    
    @Expose @SerializedName("source")
    private final String sourceWalletId;
    
    @Expose @SerializedName("accounts")
    private final String[] accounts;
    
    
    /**
     * @param sourceWalletId      the source wallet ID
     * @param destinationWalletId the destination wallet ID
     * @param accounts            an array of accounts' addresses to be moved
     */
    public RequestAccountsMove(String sourceWalletId, String destinationWalletId, String... accounts) {
        super("account_move", ResponseAccountsMoved.class);
        this.sourceWalletId = sourceWalletId;
        this.destinationWalletId = destinationWalletId;
        this.accounts = accounts;
    }
    
    
    /**
     * @return the source wallet ID
     */
    public String getSourceWalletId() {
        return sourceWalletId;
    }
    
    /**
     * @return the destination wallet ID
     */
    public String getDestinationWalletId() {
        return destinationWalletId;
    }
    
    /**
     * @return the accounts to be moved
     */
    public String[] getAccounts() {
        return accounts;
    }
    
}
