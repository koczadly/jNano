package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Map;

public class BalancesResponse extends RpcResponse {
    
    @Expose
    @SerializedName("balances")
    private Map<String, AccountBalance> balances;
    
    
    
    public Map<String, AccountBalance> getBalances() {
        return balances;
    }
    
    public AccountBalance getBalance(String account) {
        return balances.get(account.toLowerCase());
    }
    
    
    
    public static class AccountBalance {
        
        @Expose
        @SerializedName("balance")
        private BigInteger pocketed;
    
        @Expose
        @SerializedName("pending")
        private BigInteger pending;
    
    
    
        public BigInteger getPocketed() {
            return pocketed;
        }
    
        public BigInteger getPending() {
            return pending;
        }
    
        public BigInteger getTotal() {
            return pending.add(pocketed);
        }
        
    }
    
}
