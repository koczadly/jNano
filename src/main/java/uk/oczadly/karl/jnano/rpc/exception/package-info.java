/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

/**
 * This package contains a set of exception classes which can be thrown during the processing of an RPC request.
 *
 * <p>All the provided classes extend {@link uk.oczadly.karl.jnano.rpc.exception.RpcException} (with the exception of
 * the mentioned class itself, which simply extends {@link java.lang.Exception}).</p>
 *
 * <p>Note that the library may be unable to correctly parse the correct exception type for certain RPC exceptions,
 * due to inconsistencies with the node's response and handling of errors. In these cases, the base
 * {@link uk.oczadly.karl.jnano.rpc.exception.RpcException} class will be thrown instead.</p>
 *
 * @author Karl Oczadly
 */
package uk.oczadly.karl.jnano.rpc.exception;