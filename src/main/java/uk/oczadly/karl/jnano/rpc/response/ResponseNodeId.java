/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This response class contains debug information about the node's ID.
 */
public class ResponseNodeId extends RpcResponse {
    
    @Expose @SerializedName("private")
    private String privateKey;
    
    @Expose @SerializedName("public")
    private String publicKey;
    
    @Expose @SerializedName("as_account")
    private NanoAccount asAccount;
    
    @Expose @SerializedName("node_id")
    private NanoAccount nodeId;
    
    
    public String getPrivateKey() {
        return privateKey;
    }
    
    public String getPublicKey() {
        return publicKey;
    }
    
    public NanoAccount getPublicKeyAccount() {
        return asAccount;
    }
    
    public NanoAccount getNodeId() {
        return nodeId;
    }
    
}
