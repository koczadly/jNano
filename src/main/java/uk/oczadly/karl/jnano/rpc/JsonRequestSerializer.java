/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;

/**
 * The standard implementation of {@link RpcRequestSerializer}, which serializes requests into a JSON object and adds
 * the command as the {@code action} attribute.
 *
 * <p>Example output JSON (for an account balance request):</p>
 * <pre>{"account":"nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz","action":"account_balance"}</pre>
 */
public class JsonRequestSerializer implements RpcRequestSerializer {
    
    private final Gson gson;
    
    
    public JsonRequestSerializer() {
        this(JNC.GSON);
    }
    
    public JsonRequestSerializer(Gson gson) {
        this.gson = gson;
    }
    
    
    public final Gson getGson() {
        return gson;
    }
    
    
    @Override
    public String serialize(RpcRequest<?> request) {
        JsonObject obj = getGson().toJsonTree(request).getAsJsonObject();
        obj.addProperty("action", request.getActionCommand());
        return obj.toString();
    }

}
