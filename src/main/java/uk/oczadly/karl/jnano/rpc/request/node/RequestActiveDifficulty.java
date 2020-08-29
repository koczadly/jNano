/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseActiveDifficulty;

/**
 * This request class is used to request the current active difficulty.
 * <br>Calls the RPC command {@code active_difficulty}, and returns a {@link ResponseActiveDifficulty} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#active_difficulty">Official RPC documentation</a>
 */
public class RequestActiveDifficulty extends RpcRequest<ResponseActiveDifficulty> {
    
    @Expose @SerializedName("include_trend")
    private final boolean trend = true;
    
    
    public RequestActiveDifficulty() {
        super("active_difficulty", ResponseActiveDifficulty.class);
    }
    
}
