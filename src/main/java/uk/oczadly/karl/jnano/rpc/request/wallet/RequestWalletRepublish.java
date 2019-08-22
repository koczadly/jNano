package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHashes;

/**
 * This request class is used to republish every account's blocks within the specified wallet.
 * The server responds with a {@link ResponseBlockHashes} data object.<br>
 * Calls the internal RPC method {@code wallet_republish}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_republish">Official RPC documentation</a>
 */
public class RequestWalletRepublish extends RpcRequest<ResponseBlockHashes> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("count")
    private final int count;
    
    
    /**
     * @param walletId  the wallet's ID
     * @param count     how many blocks should be re-broadcast per account
     */
    public RequestWalletRepublish(String walletId, int count) {
        super("wallet_republish", ResponseBlockHashes.class);
        this.walletId = walletId;
        this.count = count;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return how many blocks should be re-broadcast per account
     */
    public int getCount() {
        return count;
    }
    
}
