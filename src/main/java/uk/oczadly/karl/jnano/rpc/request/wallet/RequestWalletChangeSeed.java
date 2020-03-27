package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletChangeSeed;

/**
 * This request class is used to change a local wallet's seed.
 * <br>Calls the RPC command {@code wallet_change_seed}, and returns a {@link ResponseWalletChangeSeed} data object.
 * <br><br>
 * Note: this method will clear existing deterministic accounts from the wallet.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_change_seed">Official RPC documentation</a>
 */
public class RequestWalletChangeSeed extends RpcRequest<ResponseWalletChangeSeed> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("seed")
    private final String seed;
    
    @Expose @SerializedName("count")
    private final Integer count;
    
    
    /**
     * @param walletId the wallet's ID
     * @param seed     the new seed
     */
    public RequestWalletChangeSeed(String walletId, String seed) {
        this(walletId, seed, null);
    }
    
    /**
     * @param walletId the wallet's ID
     * @param seed     the new seed
     * @param count    the number of accounts to restore from the seed
     */
    public RequestWalletChangeSeed(String walletId, String seed, Integer count) {
        super("wallet_change_seed", ResponseWalletChangeSeed.class);
        this.walletId = walletId;
        this.seed = seed;
        this.count = count;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the new seed
     */
    public String getSeed() {
        return seed;
    }
    
    /**
     * @return the number of accounts to restore from the seed
     */
    public Integer getCount() {
        return count;
    }
    
}
