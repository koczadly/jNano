/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.work.generator;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.model.work.generator.policy.NodeWorkDifficultyPolicy;
import uk.oczadly.karl.jnano.model.work.generator.policy.WorkDifficultyPolicy;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkGenerate;

/**
 * This {@code WorkGenerator} computes the work solution on the provided RPC endpoint, using whichever computation
 * method is configured on the node.
 */
public class NodeWorkGenerator extends WorkGenerator {

    private final RpcQueryNode rpc;
    private final boolean usePeers;
    
    /**
     * Constructs a {@code NodeWorkGenerator} using the {@link NodeWorkDifficultyPolicy} difficulty policy with the
     * current recommended active difficulty, and {@code usePeers} set to true.
     *
     * @param rpc the RPC endpoint
     *
     * @see NodeWorkDifficultyPolicy
     */
    public NodeWorkGenerator(RpcQueryNode rpc) {
        this(rpc, true);
    }
    
    /**
     * Constructs a {@code NodeWorkGenerator} using the {@link NodeWorkDifficultyPolicy} difficulty policy with the
     * current recommended active difficulty.
     *
     * @param rpc      the RPC endpoint
     * @param usePeers whether work peers should be used
     *
     * @see NodeWorkDifficultyPolicy
     */
    public NodeWorkGenerator(RpcQueryNode rpc, boolean usePeers) {
        this(new NodeWorkDifficultyPolicy(rpc), rpc, usePeers);
    }
    
    /**
     * Constructs a {@code NodeWorkGenerator} using the specified difficulty policy.
     *
     * @param difficultyPolicy the difficulty policy to use (may be null)
     * @param rpc              the RPC endpoint
     * @param usePeers         whether work peers should be used
     */
    public NodeWorkGenerator(WorkDifficultyPolicy difficultyPolicy, RpcQueryNode rpc, boolean usePeers) {
        super(difficultyPolicy);
        if (rpc == null)
            throw new IllegalArgumentException("RPC node object cannot be null.");
        this.rpc = rpc;
        this.usePeers = usePeers;
    }
    

    @Override
    protected WorkSolution generateWork(HexData root, WorkDifficulty difficulty) throws Exception {
        RequestWorkGenerate req = new RequestWorkGenerate(root.toHexString(), usePeers, difficulty);
        return rpc.processRequest(req).getWorkSolution();
    }
    
}
