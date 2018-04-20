package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.request.SortingOrder;
import uk.oczadly.karl.jnano.rpc.response.LedgerAccountsResponse;

public class WalletLedgerAccountsRequest extends RpcRequest<LedgerAccountsResponse> {
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("modified_since")
    private Integer modifiedSince;
    
    @Expose @SerializedName("sorting")
    private SortingOrder sortingOrder;
    
    @Expose @SerializedName("representative")
    private boolean fetchRepresentative = true;
    
    @Expose @SerializedName("weight")
    private boolean fetchWeight = true;
    
    @Expose @SerializedName("pending")
    private boolean fetchPending = true;
    
    
    public WalletLedgerAccountsRequest(String account, String walletId) {
        this(account, walletId, null);
    }
    
    public WalletLedgerAccountsRequest(String account, String walletId, Integer modifiedSince) {
        this(account, walletId, modifiedSince, SortingOrder.DEFAULT);
    }
    
    public WalletLedgerAccountsRequest(String account, String walletId, Integer modifiedSince, SortingOrder sortingOrder) {
        super("wallet_ledger", LedgerAccountsResponse.class);
        this.account = account;
        this.walletId = walletId;
        this.modifiedSince = modifiedSince;
        this.sortingOrder = sortingOrder;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
    public String getWalletId() {
        return walletId;
    }
    
    public Integer getModifiedSince() {
        return modifiedSince;
    }
    
    public SortingOrder getSortingOrder() {
        return sortingOrder;
    }
    
}
