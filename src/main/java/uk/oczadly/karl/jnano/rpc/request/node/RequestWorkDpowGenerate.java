package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWork;

/**
 * This request class is used to generate distributed proof of work (DPoW) for the specified block hash.
 * The server responds with a {@link ResponseWork} data object.<br>
 * Calls the DPoW work server. Users of this class must have a user name and API key
 *
 * @see <a href="https://github.com/guilhermelawless/nano-dpow/tree/master/service">nano-DPoW README.md</a>
 */
public class RequestWorkDpowGenerate extends RpcRequest<ResponseWork> {
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    @Expose @SerializedName("use_peers")
    private final Boolean usePeers;
    
    @Expose @SerializedName("difficulty")
    private final String difficulty;
    
    @Expose @SerializedName("multiplier")
    private final Double multiplier;
    
    @Expose @SerializedName("dpow_user")
    private final String dpow_user;

    @Expose @SerializedName("dpow_user_api_key")
    private final String dpow_user_api_key;
    
    
    /**
     * @param blockHash the block's hash
     */
    public RequestWorkDpowGenerate(String blockHash) {
        this(blockHash, null, null, null);
    }
    
    /**
     * Constructs a work generation request with a specified difficulty value.
     * @param dpow_user             the user name for DPoW
     * @param dpow_user_api_key     the api key for DPoW
     * @param blockHash             the block's hash
     * @param usePeers              (optional) whether work peers should be used
     * @param difficulty            (optional) the absolute difficulty value
     */
    public RequestWorkDpowGenerate(String dpow_user, String dpow_user_api_key, String blockHash, Boolean usePeers, String difficulty) {
        this(dpow_user, dpow_user_api_key, blockHash, usePeers, difficulty, null);
    }
    
    /**
     * Constructs a work generation request with a specified difficulty multiplier.
     * @param dpow_user             the user name for DPoW
     * @param dpow_user_api_key     the api key for DPoW
     * @param blockHash             the block's hash
     * @param usePeers              (optional) whether work peers should be used
     * @param multiplier            (optional) the difficulty multiplier
     */
    public RequestWorkDpowGenerate(String dpow_user, String dpow_user_api_key, String blockHash, Boolean usePeers, Double multiplier) {
        this(dpow_user, dpow_user_api_key, blockHash, usePeers, null, multiplier);
    }
    
    private RequestWorkGenerate(String dpow_user, String dpow_user_api_key, String blockHash, Boolean usePeers, String difficulty, Double multiplier) {
        super("work_generate", ResponseWork.class);
        this.dpow_user = dpow_user;
        this.dpow_user_api_key = dpow_user_api_key;
        this.blockHash = blockHash;
        this.usePeers = usePeers;
        this.difficulty = difficulty;
        this.multiplier = multiplier;
    }
    
    
    /**
     * @return the block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return whether remote work peers should be used to generate the work
     */
    public boolean getUsePeers() {
        return usePeers;
    }
    
    /**
     * @return the request difficulty value
     */
    public String getDifficulty() {
        return difficulty;
    }
    
    /**
     * @return the request difficulty multiplier
     */
    public Double getMultiplier() {
        return multiplier;
    }

    /**
     * @return the dpow user //NOTE <- Perhaps we do not want to return this
     */
    public String getDpowUser() {
        return dpow_user;
    }

    /**
     * @return the dpow user API Key //NOTE <- Perhaps we do not want to return this
     */
    public String getDpowUserApiKey() {
        return dpow_user_api_key;
    }
    
    
}
