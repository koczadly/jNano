/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseKeyPair;
import uk.oczadly.karl.jnano.util.WalletUtil;

/**
 * This request class is used to generate a random public and private key.
 * <br>Calls the RPC command {@code key_create}, and returns a {@link ResponseKeyPair} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#key_create">Official RPC documentation</a>
 *
 * @deprecated Use of built-in {@link WalletUtil#generateRandomKey()} and {@link NanoAccount#fromPrivateKey(HexData)}
 * methods should be preferred.
 */
@Deprecated
public class RequestKeyCreate extends RpcRequest<ResponseKeyPair> {
    
    public RequestKeyCreate() {
        super("key_create", ResponseKeyPair.class);
    }
    
}
