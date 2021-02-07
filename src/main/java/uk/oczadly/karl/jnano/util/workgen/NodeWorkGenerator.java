/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkGenerate;
import uk.oczadly.karl.jnano.util.workgen.policy.NodeWorkDifficultyPolicy;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

/**
 * This {@code WorkGenerator} computes the work solution on the provided RPC endpoint, using whichever computation
 * method is configured on the node.
 *
 * <p>Instances of this class should be re-used throughout your application, as each instance will spawn new
 * background threads. This practice also ensures that tasks are queued correctly in the order of request.</p>
 */
public class NodeWorkGenerator extends AbstractWorkGenerator {

    private final RpcQueryNode rpc;
    private final boolean usePeers;
    
    /**
     * Constructs a {@code NodeWorkGenerator} using the {@link NodeWorkDifficultyPolicy} difficulty policy, and
     * {@code usePeers} set to true.
     *
     * @param rpc the RPC endpoint
     *
     * @see NodeWorkDifficultyPolicy
     */
    public NodeWorkGenerator(RpcQueryNode rpc) {
        this(rpc, true);
    }
    
    /**
     * Constructs a {@code NodeWorkGenerator} using the {@link NodeWorkDifficultyPolicy} difficulty policy.
     *
     * @param rpc      the RPC endpoint
     * @param usePeers whether work peers should be used
     *
     * @see NodeWorkDifficultyPolicy
     */
    public NodeWorkGenerator(RpcQueryNode rpc, boolean usePeers) {
        this(rpc, usePeers, new NodeWorkDifficultyPolicy(rpc));
    }
    
    /**
     * Constructs a {@code NodeWorkGenerator} using the specified difficulty policy.
     *
     * @param rpc              the RPC endpoint
     * @param usePeers         whether work peers should be used
     * @param difficultyPolicy the difficulty policy to use
     */
    public NodeWorkGenerator(RpcQueryNode rpc, boolean usePeers, WorkDifficultyPolicy difficultyPolicy) {
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
