/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

/**
 * Classes which implement this interface are responsible for converting the response from the node into the
 * {@link RpcResponse} class, as well as parsing the appropriate {@link RpcException}.
 */
public interface RpcResponseDeserializer {
    
    /**
     * Deserializes the raw response string given from the node into the appropriate {@link RpcResponse} object, or
     * throws the relevant {@link RpcException} exception if an error is returned.
     *
     * @param response      the raw response data sent from the node
     * @param responseClass the expected response class to populate the data into
     * @param <R>           the response class
     * @return the parsed response object
     * @throws RpcException if an exception occurs during parsing, or if an error is returned by the node
     */
    <R extends RpcResponse> R deserialize(String response, Class<R> responseClass) throws RpcException;

}
