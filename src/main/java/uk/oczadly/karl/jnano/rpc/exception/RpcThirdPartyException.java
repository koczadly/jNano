/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * RPC exception for errors relating to third-party RPC providers.
 */
public class RpcThirdPartyException extends RpcExternalException {
    
    public RpcThirdPartyException(String rawMessage) {
        super(rawMessage);
    }
    
    public RpcThirdPartyException(String message, String rawMessage) {
        super(message, rawMessage);
    }
    
    
    /**
     * Third-party RPC exception; thrown when too many requests have been made.
     */
    public static class TooManyRequestsException extends RpcThirdPartyException {
        public TooManyRequestsException(String message, String rawMessage) {
            super(message, rawMessage);
        }
    }
    
    /**
     * Third-party RPC exception; thrown when your free allowance or prepaid tokens have been depleted.
     */
    public static class TokensExhaustedException extends RpcThirdPartyException {
        public TokensExhaustedException(String message, String rawMessage) {
            super(message, rawMessage);
        }
    }
    
}
