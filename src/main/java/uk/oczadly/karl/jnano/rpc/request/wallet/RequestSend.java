package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

import java.math.BigInteger;

/**
 * This request class is used to send an amount from a specified local wallet account to another account.
 * <br>Calls the RPC command {@code send}, and returns a {@link ResponseBlockHash} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#send">Official RPC documentation</a>
 */
public class RequestSend extends RpcRequest<ResponseBlockHash> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("source")
    private final String sourceAccount;
    
    @Expose @SerializedName("destination")
    private final String destinationAccount;
    
    @Expose @SerializedName("amount")
    private final BigInteger amount;
    
    @Expose @SerializedName("id")
    private final String transactionId;
    
    @Expose @SerializedName("work")
    private final String workSolution;
    
    
    /**
     * @param walletId              the wallet's ID
     * @param sourceAccount         the account's address to send from
     * @param destinationAccount    the destination account's address (recipient)
     * @param amount                the amount to send (in RAW)
     *
     * @deprecated a unique ID should be supplied for idempotency
     */
    @Deprecated
    public RequestSend(String walletId, String sourceAccount, String destinationAccount, BigInteger amount) {
        this(walletId, sourceAccount, destinationAccount, amount, null);
    }
    
    /**
     * @param walletId              the wallet's ID
     * @param sourceAccount         the account's address to send from
     * @param destinationAccount    the destination account's address (recipient)
     * @param amount                the amount to send (in RAW)
     * @param transactionId         a unique ID for idempotency
     */
    public RequestSend(String walletId, String sourceAccount, String destinationAccount, BigInteger amount,
                       String transactionId) {
        this(walletId, sourceAccount, destinationAccount, amount, transactionId, null);
    }
    
    /**
     * @param walletId              the wallet's ID
     * @param sourceAccount         the account's address to send from
     * @param destinationAccount    the destination account's address (recipient)
     * @param amount                the amount to send (in RAW)
     * @param transactionId         a unique ID for idempotency
     * @param workSolution          a pre-computed work solution
     */
    public RequestSend(String walletId, String sourceAccount, String destinationAccount, BigInteger amount,
                       String transactionId, String workSolution) {
        super("send", ResponseBlockHash.class);
        this.walletId = walletId;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.transactionId = transactionId;
        this.workSolution = workSolution;
    }
    
    
    /**
     * @return the source account's wallet ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the source account's address
     */
    public String getSourceAccount() {
        return sourceAccount;
    }
    
    /**
     * @return the destination account's address
     */
    public String getDestinationAccount() {
        return destinationAccount;
    }
    
    /**
     * @return the amount to be sent (in RAW)
     */
    public BigInteger getAmount() {
        return amount;
    }
    
    /**
     * @return the supplied unique transaction ID
     */
    public String getUniqueTransactionId() {
        return transactionId;
    }
    
    /**
     * @return a pre-computed work solution
     */
    public String getWorkSolution() {
        return workSolution;
    }
    
}
