package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Map;

/**
 * This response class contains a set of account balances.
 */
public class ResponseMultiAccountBalances extends RpcResponse {
    
    @Expose @SerializedName("balances")
    private Map<String, AccountBalance> balances;
    
    
    /**
     * Map follows the structure {@code address -> balance}.
     * @return a map of account balances
     */
    public Map<String, AccountBalance> getBalances() {
        return balances;
    }
    
    /**
     * @param account the account's address
     * @return the balance of the specified account, or null if not present in the response
     */
    public AccountBalance getBalance(String account) {
        return balances.get(account.toLowerCase());
    }
    
    
    public static class AccountBalance {
        @Expose @SerializedName("balance")
        private BigInteger pocketed;
    
        @Expose @SerializedName("pending")
        private BigInteger pending;
    
    
        /**
         * @return the pocketed balance of this account
         */
        public BigInteger getPocketed() {
            return pocketed;
        }
    
        /**
         * @return the pending (unreceived) balance of this account
         */
        public BigInteger getPending() {
            return pending;
        }
    
        /**
         * @return the total balance of this account
         */
        public BigInteger getTotal() {
            return pending.add(pocketed);
        }
    }
    
}
