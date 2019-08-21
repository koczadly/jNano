package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountsMoved;

public class RequestAccountsMove extends RpcRequest<ResponseAccountsMoved> {
    
    @Expose @SerializedName("wallet")
    private String destinationWalletId;
    
    @Expose @SerializedName("source")
    private String sourceWalletId;
    
    @Expose @SerializedName("accounts")
    private String[] accounts;
    
    
    public RequestAccountsMove(String sourceWalletId, String destinationWalletId, String... accounts) {
        super("account_move", ResponseAccountsMoved.class);
        this.sourceWalletId = sourceWalletId;
        this.destinationWalletId = destinationWalletId;
        this.accounts = accounts;
    }
    
    
    public String getSourceWalletId() {
        return sourceWalletId;
    }
    
    public String getDestinationWalletId() {
        return destinationWalletId;
    }
    
    public String[] getAccounts() {
        return accounts;
    }
    
}
