/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;

/**
 * This response class contains a string produced when exporting a wallet.
 */
public class ResponseWalletExport extends RpcResponse {
    
    @Expose @SerializedName("json")
    private String exportedJson;
    
    
    /**
     * @return a string of the exported wallet in JSON format
     */
    public String getExportedJson() {
        return exportedJson;
    }
    
    /**
     * @return the exported wallet in JSON format
     */
    public JsonObject getExportedJsonObject() {
        return JNH.parseJson(exportedJson);
    }
    
}
