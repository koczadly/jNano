module uk.oczadly.karl.jnano {
    
    //Models
    exports uk.oczadly.karl.jnano.model.block;
    //Callback
    exports uk.oczadly.karl.jnano.callback;
    //RPC
    exports uk.oczadly.karl.jnano.rpc;
    exports uk.oczadly.karl.jnano.rpc.exception;
    exports uk.oczadly.karl.jnano.rpc.request.conversion;
    exports uk.oczadly.karl.jnano.rpc.request.node;
    exports uk.oczadly.karl.jnano.rpc.request.wallet;
    exports uk.oczadly.karl.jnano.rpc.response;
    //Utils
    exports uk.oczadly.karl.jnano.util;
    
    //Dependencies
    requires gson;
    requires Java.WebSocket;

}