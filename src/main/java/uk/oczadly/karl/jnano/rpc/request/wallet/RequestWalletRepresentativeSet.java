package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to set a new representative for accounts within a wallet.
 * <br>Calls the RPC command {@code wallet_representative_set}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_representative_set">Official RPC documentation</a>
 */
public class RequestWalletRepresentativeSet extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("representative")
    private final String representativeAccount;
    
    
    @Expose @SerializedName("update_existing_accounts")
    private final Boolean updateExistingAccounts;
    
    
    /**
     * @param walletId the wallet's ID
     * @param representativeAccount the new representative's account address
     */
    public RequestWalletRepresentativeSet(String walletId, String representativeAccount) {
        this(walletId, representativeAccount, null);
    }
    
    /**
     * Constructs a new representative request. If {@code updateExistingAccounts} is set to true, then all existing
     * accounts will need to generate and broadcast new blocks, which may take a long time.
     *
     * @param walletId                  the wallet's ID
     * @param representativeAccount     the new representative's account address
     * @param updateExistingAccounts    whether existing accounts should generate new blocks and change their
     *                                  representative.
     */
    public RequestWalletRepresentativeSet(String walletId, String representativeAccount,
                                          Boolean updateExistingAccounts) {
        super("wallet_representative_set", ResponseSuccessful.class);
        this.walletId = walletId;
        this.representativeAccount = representativeAccount;
        this.updateExistingAccounts = updateExistingAccounts;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the new representative's account address
     */
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
    /**
     * @return whether existing accounts should update their representative
     */
    public Boolean getUpdateExistingAccounts() {
        return updateExistingAccounts;
    }
    
}
