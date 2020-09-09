/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This response class contains a set of representatives.
 */
public class ResponseRepresentatives extends RpcResponse {
    
    @Expose @SerializedName("representatives")
    private LinkedHashMap<NanoAccount, NanoAmount> representatives;
    
    
    /**
     * Map follows the structure {@code address -> voting weight}.
     *
     * @return a map of representatives
     */
    public Map<NanoAccount, NanoAmount> getRepresentatives() {
        return representatives;
    }
    
    /**
     * @param accountAddress a representative's address
     * @return the voting weight of the specified representative, or null if not present in the response
     */
    public NanoAmount getVotingWeight(NanoAccount accountAddress) {
        return representatives.get(accountAddress);
    }
    
}
