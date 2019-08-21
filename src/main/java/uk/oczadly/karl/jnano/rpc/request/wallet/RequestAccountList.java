package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccounts;

public class RequestAccountList extends RpcRequest<ResponseAccounts> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public RequestAccountList(String walletId) {
        super("account_list", ResponseAccounts.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
