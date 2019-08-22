package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseLedger;

/**
 * This request class is used to request detailed account information from the ledger within a specific wallet.
 * The server responds with a {@link ResponseLedger} data object.<br>
 * Calls the internal RPC method {@code wallet_ledger}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_ledger">Official RPC documentation</a>
 */
public class RequestWalletLedger extends RpcRequest<ResponseLedger> {
    
    @Expose @SerializedName("representative")
    private final boolean fetchRepresentative = true;
    
    @Expose @SerializedName("weight")
    private final boolean fetchWeight = true;
    
    @Expose @SerializedName("pending")
    private final boolean fetchPending = true;
    
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    @Expose @SerializedName("modified_since")
    private final Integer modifiedSince;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletLedger(String walletId) {
        this(walletId, null);
    }
    
    /**
     * @param walletId      the wallet's Id
     * @param modifiedSince filter out transactions before the specified UNIX timestamp
     */
    public RequestWalletLedger(String walletId, Integer modifiedSince) {
        super("wallet_ledger", ResponseLedger.class);
        this.walletId = walletId;
        this.modifiedSince = modifiedSince;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the modified since threshold
     */
    public Integer getModifiedSince() {
        return modifiedSince;
    }
    
}
