package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccounts;

/**
 * This request class is used to create a set of new accounts in a local wallet.
 * <br>Calls the RPC command {@code accounts_create}, and returns a {@link ResponseAccounts} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#accounts_create">Official RPC documentation</a>
 */
public class RequestAccountsCreate extends RpcRequest<ResponseAccounts> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("count")
    private final int count;
    
    
    @Expose @SerializedName("work")
    private final Boolean generateWork;
    
    
    /**
     * @param walletId the wallet ID
     * @param count    the number of accounts to create
     */
    public RequestAccountsCreate(String walletId, int count) {
        this(walletId, count, null);
    }
    
    /**
     * @param walletId     the wallet ID
     * @param count        the number of accounts to create
     * @param generateWork whether work should be pre-computed for each account
     */
    public RequestAccountsCreate(String walletId, int count, Boolean generateWork) {
        super("accounts_create", ResponseAccounts.class);
        this.walletId = walletId;
        this.count = count;
        this.generateWork = generateWork;
    }
    
    
    /**
     * @return the requested wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the requested number of accounts
     */
    public int getCount() {
        return count;
    }
    
    /**
     * @return whether work should be pre-computed for each account
     */
    public boolean getGenerateWork() {
        return generateWork;
    }
    
}
