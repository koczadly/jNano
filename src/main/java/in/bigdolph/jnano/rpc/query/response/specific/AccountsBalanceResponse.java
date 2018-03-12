package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class AccountsBalanceResponse extends RPCResponse {
    
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
