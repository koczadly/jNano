package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

/**
 * This request class is used to add a new account to a local wallet from it's private key.
 * The server responds with a {@link ResponseAccount} data object.<br>
 * Calls the internal RPC method {@code wallet_add}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_add">Official RPC documentation</a>
 */
public class RequestWalletAdd extends RpcRequest<ResponseAccount> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("key")
    private final String key;
    
    @Expose @SerializedName("work")
    private final Boolean generateWork;
    
    
    /**
     * @param walletId  the wallet's ID
     * @param key       the private key for the account
     */
    public RequestWalletAdd(String walletId, String key) {
        this(walletId, key, null);
    }
    
    /**
     * @param walletId      the wallet's ID
     * @param key           the private key for the account
     * @param generateWork  whether work should be pre-computed for this account
     */
    public RequestWalletAdd(String walletId, String key, Boolean generateWork) {
        super("wallet_add", ResponseAccount.class);
        this.walletId = walletId;
        this.key = key;
        this.generateWork = generateWork;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the account's private key
     */
    public String getKey() {
        return key;
    }
    
    /**
     * @return whether work should be pre-computed for this account
     */
    public boolean getGenerateWork() {
        return generateWork;
    }
    
}
