package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseKeyPair;

/**
 * This request class is used to generate a private and public key from the given seed.
 * <br>Calls the RPC command {@code deterministic_key}, and returns a {@link ResponseKeyPair} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#deterministic_key">Official RPC documentation</a>
 */
public class RequestDeterministicKey extends RpcRequest<ResponseKeyPair> {
    
    @Expose @SerializedName("seed")
    private final String seed;
    
    @Expose @SerializedName("index")
    private final long accountIndex;
    
    
    /**
     * Expands an account from a given seed, using the first ({@code 0}) index.
     * @param seed         the seed to generate private keys from
     */
    public RequestDeterministicKey(String seed) {
        this(seed, 0L);
    }
    
    /**
     * Expands an account from a given seed and index.
     * @param seed         the seed to generate private keys from
     * @param accountIndex the index of the account
     */
    public RequestDeterministicKey(String seed, long accountIndex) {
        super("deterministic_key", ResponseKeyPair.class);
        this.seed = seed;
        this.accountIndex = accountIndex;
    }
    
    
    /**
     * @return the requested seed
     */
    public String getSeed() {
        return seed;
    }
    
    /**
     * @return the requested index
     */
    public long getAccountIndex() {
        return accountIndex;
    }
    
}
