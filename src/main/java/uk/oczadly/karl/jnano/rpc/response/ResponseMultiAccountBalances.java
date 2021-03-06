/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;

import java.util.Map;

/**
 * This response class contains a set of account balances.
 */
public class ResponseMultiAccountBalances extends RpcResponse {
    
    @Expose @SerializedName("balances")
    private Map<NanoAccount, AccountBalance> balances;
    
    
    /**
     * Map follows the structure {@code address -> balance}.
     *
     * @return a map of account balances
     */
    public Map<NanoAccount, AccountBalance> getBalances() {
        return balances;
    }
    
    /**
     * @param account the account's address
     * @return the balance of the specified account, or null if not present in the response
     */
    public AccountBalance getBalance(NanoAccount account) {
        return balances.get(account);
    }
    
    
    public static class AccountBalance {
        @Expose @SerializedName("balance")
        private NanoAmount pocketed;
        
        @Expose @SerializedName("pending")
        private NanoAmount pending;
        
        
        /**
         * @return the pocketed balance of this account
         */
        public NanoAmount getPocketed() {
            return pocketed;
        }
        
        /**
         * @return the pending (unreceived) balance of this account
         */
        public NanoAmount getPending() {
            return pending;
        }
        
        /**
         * @return the total balance of this account
         */
        public NanoAmount getTotal() {
            return NanoAmount.valueOfRaw(pending.getAsRaw().add(pocketed.getAsRaw()));
        }
    }
    
}
