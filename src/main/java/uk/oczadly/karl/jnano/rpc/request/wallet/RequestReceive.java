package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

/**
 * This request class is used to manually receive a pending block for a local wallet account.
 * <br>Calls the RPC command {@code receive}, and returns a {@link ResponseBlockHash} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#receive">Official RPC documentation</a>
 */
public class RequestReceive extends RpcRequest<ResponseBlockHash> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("account")
    private final String account;
    
    @Expose @SerializedName("block")
    private final String blockHash;
    
    @Expose @SerializedName("work")
    private final String workSolution;
    
    
    /**
     * @param walletId  the wallet's ID
     * @param account   the account's address
     * @param blockHash the pending block's hash
     */
    public RequestReceive(String walletId, String account, String blockHash) {
        this(walletId, account, blockHash, null);
    }
    
    /**
     * @param walletId     the wallet's ID
     * @param account      the account's address
     * @param blockHash    the pending block's hash
     * @param workSolution a pre-computed work solution
     */
    public RequestReceive(String walletId, String account, String blockHash, String workSolution) {
        super("receive", ResponseBlockHash.class);
        this.walletId = walletId;
        this.account = account;
        this.blockHash = blockHash;
        this.workSolution = workSolution;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the account's address
     */
    public String getAccount() {
        return account;
    }
    
    /**
     * @return the pending block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return a pre-computed work solution
     */
    public String getWorkSolution() {
        return workSolution;
    }
    
}
