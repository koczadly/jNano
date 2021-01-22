/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWorkValidation;

/**
 * This request class is used to check whether the specified work is valid for the specified block.
 * <br>Calls the RPC command {@code work_validate}, and returns a {@link ResponseWorkValidation} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#work_validate">Official RPC documentation</a>
 * @see WorkSolution#calculateDifficulty(Block)
 */
public class RequestWorkValidate extends RpcRequest<ResponseWorkValidation> {
    
    @Expose @SerializedName("work")
    private final String workSolution;
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    
    @Expose @SerializedName("difficulty")
    private final WorkDifficulty difficulty;
    
    @Expose @SerializedName("multiplier")
    private final Double multiplier;
    
    
    /**
     * @param workSolution the computed work value
     * @param blockHash    the block's hash
     */
    public RequestWorkValidate(String workSolution, String blockHash) {
        this(workSolution, blockHash, null, null);
    }
    
    /**
     * Constructs a work validation request with a specific difficulty value.
     *
     * @param workSolution the computed work value
     * @param blockHash    the block's hash
     * @param difficulty   the difficulty value
     *                     
     * @deprecated use {@link #RequestWorkValidate(String, String, WorkDifficulty)}
     */
    @Deprecated(forRemoval = true)
    public RequestWorkValidate(String workSolution, String blockHash, String difficulty) {
        this(workSolution, blockHash, new WorkDifficulty(difficulty));
    }
    
    /**
     * Constructs a work validation request with a specific difficulty value.
     *
     * @param workSolution the computed work value
     * @param blockHash    the block's hash
     * @param difficulty   the difficulty value
     */
    public RequestWorkValidate(String workSolution, String blockHash, WorkDifficulty difficulty) {
        this(workSolution, blockHash, difficulty, null);
    }
    
    /**
     * Constructs a work validation request with a specific difficulty multiplier.
     *
     * @param workSolution the computed work value
     * @param blockHash    the block's hash
     * @param multiplier   the difficulty multiplier
     */
    public RequestWorkValidate(String workSolution, String blockHash, Double multiplier) {
        this(workSolution, blockHash, null, multiplier);
    }
    
    private RequestWorkValidate(String workSolution, String blockHash, WorkDifficulty difficulty, Double multiplier) {
        super("work_validate", ResponseWorkValidation.class);
        this.workSolution = workSolution;
        this.blockHash = blockHash;
        this.difficulty = difficulty;
        this.multiplier = multiplier;
    }
    
    
    /**
     * @return the requested work solution
     */
    public String getWorkSolution() {
        return workSolution;
    }
    
    /**
     * @return the requested block hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return the requested work difficulty
     */
    public WorkDifficulty getDifficulty() {
        return difficulty;
    }
    
    /**
     * @return the requested work multiplier
     */
    public Double getMultiplier() {
        return multiplier;
    }
    
}
