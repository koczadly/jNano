package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

/**
 * This request class is used to create an account in the specified local wallet.
 * The server responds with a {@link ResponseAccount} data object.<br>
 * Calls the internal RPC method {@code account_create}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_create">Official RPC documentation</a>
 */
public class RequestAccountCreate extends RpcRequest<ResponseAccount> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("work")
    private final Boolean generateWork;
    
    @Expose @SerializedName("index")
    private final Integer index;
    
    
    /**
     * @param walletId  the wallet's ID
     */
    public RequestAccountCreate(String walletId) {
        this(walletId, null, null);
    }
    
    /**
     * @param walletId  the wallet's ID
     * @param index     (optional) the account index
     */
    public RequestAccountCreate(String walletId, Integer index) {
        this(walletId, index, null);
    }
    
    /**
     * @param walletId      the wallet's ID
     * @param index         (optional) the account index
     * @param generateWork  (optional) whether work should be generated
     */
    public RequestAccountCreate(String walletId, Integer index, Boolean generateWork) {
        super("account_create", ResponseAccount.class);
        this.walletId = walletId;
        this.index = index;
        this.generateWork = generateWork;
    }
    
    
    /**
     * @return the wallet ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return whether work should be generated
     */
    public boolean getGenerateWork() {
        return generateWork;
    }
    
    /**
     * @return the account index
     */
    public Integer getIndex() {
        return index;
    }
    
}
