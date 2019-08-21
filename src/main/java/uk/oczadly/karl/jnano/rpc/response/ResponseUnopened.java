package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Map;

public class ResponseUnopened extends RpcResponse {
    
    @Expose @SerializedName("accounts")
    private Map<String, BigInteger> accounts;
    
    
    public Map<String, BigInteger> getAccounts() {
        return accounts;
    }
    
    public BigInteger getPendingBalance(String accountAddress) {
        return this.accounts.get(accountAddress.toLowerCase());
    }
    
}
