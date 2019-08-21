package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class ResponseWalletInfo extends RpcResponse {
    
    @Expose @SerializedName("balance")
    private BigInteger balance;
    
    @Expose @SerializedName("pending")
    private BigInteger pendingBalance;
    
    @Expose @SerializedName("accounts_count")
    private int accountsCount;
    
    @Expose @SerializedName("adhoc_count")
    private int adhocCount;
    
    @Expose @SerializedName("deterministic_count")
    private int deterministicAccountCount;
    
    @Expose @SerializedName("deterministic_index")
    private int currentDeterministicIndex;
    
    
    public BigInteger getBalance() {
        return balance;
    }
    
    public BigInteger getPendingBalance() {
        return pendingBalance;
    }
    
    public int getAccountsCount() {
        return accountsCount;
    }
    
    public int getAdhocCount() {
        return adhocCount;
    }
    
    public int getDeterministicAccountCount() {
        return deterministicAccountCount;
    }
    
    public int getCurrentDeterministicIndex() {
        return currentDeterministicIndex;
    }
    
}
