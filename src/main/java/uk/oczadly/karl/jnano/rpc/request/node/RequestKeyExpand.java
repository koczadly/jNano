package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseKeyPair;

/**
 * This request class is used to derive a public key and account from a given private key.
 * The server responds with a {@link ResponseKeyPair} data object.<br>
 * Calls the internal RPC method {@code key_expand}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#key_expand">Official RPC documentation</a>
 */
public class RequestKeyExpand extends RpcRequest<ResponseKeyPair> {
    
    @Expose @SerializedName("key")
    private final String privateKey;
    
    
    /**
     * @param privateKey the private key to derive from
     */
    public RequestKeyExpand(String privateKey) {
        super("key_expand", ResponseKeyPair.class);
        this.privateKey = privateKey;
    }
    
    
    /**
     * @return the requested private key
     */
    public String getPrivateKey() {
        return privateKey;
    }
    
}
