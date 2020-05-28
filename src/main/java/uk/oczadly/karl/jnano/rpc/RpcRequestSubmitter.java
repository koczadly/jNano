package uk.oczadly.karl.jnano.rpc;

import java.io.IOException;
import java.net.URL;

/**
 * Classes which implement this interface are responsible for submitting requests and retrieving the responses from an
 * external Nano node.
 */
public interface RpcRequestSubmitter {
    
    /**
     * Submits a raw RPC request to the specified external node address.
     * @param address   the network address of the local or external node
     * @param authToken the authorization token to send with the request
     * @param request   the raw request data
     * @param timeout   the timeout value in milliseconds, or 0 for infinite
     * @return the raw string response
     * @throws IOException if an exception occurs with the remote connection
     */
    String submit(URL address, String authToken, String request, int timeout) throws IOException;
    
}
