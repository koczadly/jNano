/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This response class contains information about an account's keys.
 */
public class ResponseKeyPair extends RpcResponse {
    
    @Expose @SerializedName("private")
    private HexData privateKey;
    
    @Expose @SerializedName("public")
    private HexData publicKey;
    
    @Expose @SerializedName("account")
    private NanoAccount accountAddress;
    
    
    /**
     * @return the private key
     */
    public HexData getPrivateKey() {
        return privateKey;
    }
    
    /**
     * @return the public key
     * @deprecated use {@link #getAddress()}
     */
    @Deprecated
    public HexData getPublicKey() {
        return publicKey;
    }
    
    /**
     * @return the account's address
     * @deprecated use {@link #getAddress()}
     */
    @Deprecated
    public String getAccountAddress() {
        return getAddress().toAddress();
    }
    
    /**
     * @return an {@link NanoAccount} object representing the account returned
     */
    public NanoAccount getAddress() {
        return accountAddress;
    }
    
}
