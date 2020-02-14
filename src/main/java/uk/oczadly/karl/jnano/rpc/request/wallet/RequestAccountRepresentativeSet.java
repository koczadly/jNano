package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

/**
 * This request class is used to set the representative for a local account.
 * The server responds with a {@link ResponseBlockHash} data object.<br>
 * Calls the internal RPC method {@code account_representative_set}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_representative_set">Official RPC documentation</a>
 */
public class RequestAccountRepresentativeSet extends RpcRequest<ResponseBlockHash> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("account")
    private final String account;
    
    @Expose @SerializedName("representative")
    private final String representativeAccount;
    
    @Expose @SerializedName("work")
    private final String workSolution;
    
    
    /**
     * @param walletId              the wallet ID
     * @param account               the account address
     * @param representativeAccount the new representative address
     */
    public RequestAccountRepresentativeSet(String walletId, String account, String representativeAccount) {
        this(walletId, account, representativeAccount, null);
    }
    
    /**
     * @param walletId              the wallet ID
     * @param account               the account address
     * @param representativeAccount the new representative address
     * @param workSolution          a pre-computed work solution
     */
    public RequestAccountRepresentativeSet(String walletId, String account, String representativeAccount, String workSolution) {
        super("account_representative_set", ResponseBlockHash.class);
        this.walletId = walletId;
        this.account = account;
        this.representativeAccount = representativeAccount;
        this.workSolution = workSolution;
    }
    
    /**
     * @return the wallet ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the account address
     */
    public String getAccount() {
        return account;
    }
    
    /**
     * @return the representative address
     */
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
    /**
     * @return the pre-computed work solution
     */
    public String getWorkSolution() {
        return workSolution;
    }
}
