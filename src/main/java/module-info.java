open module uk.oczadly.karl.jnano {
    
    // Models
    exports uk.oczadly.karl.jnano.model;
    exports uk.oczadly.karl.jnano.model.block;
    exports uk.oczadly.karl.jnano.model.block.interfaces;
    exports uk.oczadly.karl.jnano.model.block.factory;
    exports uk.oczadly.karl.jnano.model.work;
    exports uk.oczadly.karl.jnano.model.epoch;
    // Callback
    exports uk.oczadly.karl.jnano.callback;
    // RPC
    exports uk.oczadly.karl.jnano.rpc;
    exports uk.oczadly.karl.jnano.rpc.exception;
    exports uk.oczadly.karl.jnano.rpc.request;
    exports uk.oczadly.karl.jnano.rpc.request.conversion;
    exports uk.oczadly.karl.jnano.rpc.request.node;
    exports uk.oczadly.karl.jnano.rpc.request.wallet;
    exports uk.oczadly.karl.jnano.rpc.response;
    exports uk.oczadly.karl.jnano.util.wallet;
    exports uk.oczadly.karl.jnano.util.workgen;
    exports uk.oczadly.karl.jnano.util.workgen.policy;
    // WebSocket
    exports uk.oczadly.karl.jnano.websocket;
    exports uk.oczadly.karl.jnano.websocket.topic;
    exports uk.oczadly.karl.jnano.websocket.topic.message;
    
    // Dependencies
    requires transitive com.google.gson;
    requires blake2b;
    requires net.i2p.crypto.eddsa;
    requires Java.WebSocket;
    requires jocl;

}