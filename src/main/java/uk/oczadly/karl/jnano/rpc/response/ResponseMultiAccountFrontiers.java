/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * This response class contains a set of accounts and their head blocks.
 */
public class ResponseMultiAccountFrontiers extends RpcResponse {
    
    @Expose private Map<NanoAccount, HexData> frontiers;
    
    
    /**
     * Map follows the structure {@code address -> head block hash}.
     * @return a map of account frontiers
     */
    public Map<NanoAccount, HexData> getFrontiers() {
        return frontiers;
    }
    
    /**
     * @return the list of accounts
     */
    public Set<NanoAccount> getAccounts() {
        return frontiers.keySet();
    }
    
    /**
     * @param address an account's address
     * @return the head block hash of the specified account, or null if not present in the response
     */
    public HexData getFrontierBlockHash(NanoAccount address) {
        return frontiers.get(address);
    }
    
    /**
     * @return the next account to check when using pagination, or null if the end has been reached
     */
    public NanoAccount getNextAccount() {
        return frontiers.keySet().stream()
                .max(Comparator.comparing(NanoAccount::getAccountIndex))
                .map(JNH::nextSequentialAccount).orElse(null);
    }
    
}
