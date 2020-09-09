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
 * This response class contains information a representatives delegators.
 */
public class ResponseDelegators extends RpcResponse {
    
    @Expose @SerializedName("delegators")
    private Map<NanoAccount, NanoAmount> delegators;
    
    
    /**
     * Map follows the structure {@code delegator address -> delegated amount (in RAW)}.
     *
     * @return a map of delegators
     */
    public Map<NanoAccount, NanoAmount> getDelegators() {
        return delegators;
    }
    
    /**
     * @param accountAddress the account's address
     * @return the balance delegated to the representative
     */
    public NanoAmount getDelegatedBalance(NanoAccount accountAddress) {
        return delegators.get(accountAddress);
    }
    
}
