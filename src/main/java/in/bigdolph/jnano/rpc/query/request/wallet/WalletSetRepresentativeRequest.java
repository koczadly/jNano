package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class WalletSetRepresentativeRequest extends RPCRequest<RPCResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    
    public WalletSetRepresentativeRequest(String walletId, String representativeAccount) {
        super("wallet_representative_set", RPCResponse.class);
        this.walletId = walletId;
        this.representativeAccount = representativeAccount;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
}
