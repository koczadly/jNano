/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * RPC exception for errors relating to third-party RPC providers.
 */
public class RpcThirdPartyException extends RpcException {
    
    public RpcThirdPartyException(String message) {
        super(message);
    }
    
    public RpcThirdPartyException(String message, String nodeMessage) {
        super(message, nodeMessage);
    }
    
    
    /**
     * Third-party RPC exception; thrown when too many requests have been made.
     */
    public static class TooManyRequestsException extends RpcThirdPartyException {
        public TooManyRequestsException(String message) {
            super(message);
        }
        
        public TooManyRequestsException(String message, String nodeMessage) {
            super(message, nodeMessage);
        }
    }
    
    /**
     * Third-party RPC exception; thrown when your free allowance or prepaid tokens have been depleted.
     */
    public static class TokensExhaustedException extends RpcThirdPartyException {
        public TokensExhaustedException(String message) {
            super(message);
        }
        
        public TokensExhaustedException(String message, String nodeMessage) {
            super(message, nodeMessage);
        }
    }
    
}
