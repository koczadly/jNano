/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.factory;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.*;
import uk.oczadly.karl.jnano.util.workgen.CPUWorkGenerator;
import uk.oczadly.karl.jnano.util.workgen.GeneratedWork;
import uk.oczadly.karl.jnano.util.workgen.NodeWorkGenerator;
import uk.oczadly.karl.jnano.util.workgen.WorkGenerator;

import java.util.concurrent.ExecutionException;

/**
 * A {@link BlockFactory} which constructs and signs legacy block types ({@link SendBlock}, {@link OpenBlock},
 * {@link ReceiveBlock} and {@link ChangeBlock}).
 *
 * @see Builder
 */
public final class LegacyBlockFactory extends BlockFactory<Block> {
    
    /**
     * Constructs a new LegacyBlockFactory with the given parameters.
     *
     * @param defaultRepresentative the default representative for new accounts
     * @param workGenerator         the work generator
     *
     * @see LegacyBlockFactory.Builder
     */
    public LegacyBlockFactory(NanoAccount defaultRepresentative, WorkGenerator workGenerator) {
        this(defaultRepresentative, defaultRepresentative.getPrefix(), workGenerator);
    }
    
    protected LegacyBlockFactory(NanoAccount defaultRepresentative, String addressPrefix, WorkGenerator workGenerator) {
        super(defaultRepresentative, addressPrefix, workGenerator);
    }
    
    
    @Override
    protected BlockAndState<Block> _createSend(HexData privateKey, AccountState state, NanoAccount destination,
                                               NanoAmount amount) {
        NanoAmount balance = state.getBalance().subtract(amount);
        return construct(privateKey, balance, state.getRepresentative(),
                new SendBlock(null, null, state.getFrontierHash(), destination, balance));
    }
    
    @Override
    protected BlockAndState<Block> _createReceive(HexData privateKey, AccountState state, HexData sourceHash,
                                                  NanoAmount amount) {
        NanoAmount newBal;
        try {
            newBal = state.getBalance().add(amount);
        } catch (ArithmeticException e) {
            throw new CreationException("Receiving more funds than possible.");
        }
        
        if (state.isOpened()) {
            // Receive
            return construct(privateKey, newBal, state.getRepresentative(),
                    new ReceiveBlock(null, null, state.getFrontierHash(), sourceHash));
        } else {
            // Open
            return construct(privateKey, newBal, getDefaultRepresentative(),
                    new OpenBlock(null, null, sourceHash,
                            NanoAccount.fromPrivateKey(privateKey, getAddressPrefix()),
                            getDefaultRepresentative()));
        }
    }
    
    @Override
    protected BlockAndState<Block> _createChange(HexData privateKey, AccountState state, NanoAccount representative) {
        return construct(privateKey, state.getBalance(), representative,
                new ChangeBlock(null, null, state.getFrontierHash(), representative));
    }
    
    
    private BlockAndState<Block> construct(HexData privateKey, NanoAmount stateBal, NanoAccount stateRep, Block block) {
        try {
            GeneratedWork work = getWorkGenerator().generate(block).get();
            block.setWorkSolution(work.getWork());
        } catch (InterruptedException | ExecutionException e) {
            throw new CreationException("Couldn't generate work.", e);
        }
        block.sign(privateKey);
        return new BlockAndState<>(block, new AccountState(block.getHash(), stateBal, stateRep));
    }
    
    
    public static final class Builder {
        private NanoAccount defaultRepresentative;
        private String addressPrefix;
        private WorkGenerator workGenerator;
        
        /**
         * Sets the default rep address for new accounts.
         * @param rep the default rep
         * @return this builder
         */
        public Builder defaultRepresentative(NanoAccount rep) {
            this.defaultRepresentative = rep;
            return this;
        }
        
        /**
         * Sets the default rep address for new accounts.
         * @param rep the default rep
         * @return this builder
         */
        public Builder defaultRepresentative(String rep) {
            return defaultRepresentative(NanoAccount.parse(rep));
        }
        
        /**
         * Sets the prefix to be used for addresses.
         *
         * <p>Defaults to {@value NanoAccount#DEFAULT_PREFIX}.</p>
         *
         * @param prefix the prefix (eg "{@code nano}")
         * @return this builder
         */
        public Builder addressPrefix(String prefix) {
            this.addressPrefix = prefix;
            return this;
        }
        
        /**
         * Sets the work generator to be used when constructing new blocks.
         *
         * <p>Defaults to a {@link NodeWorkGenerator} using the specified RPC client (work will be generated on the
         * node).</p>
         *
         * @param generator the work generator
         * @return this builder
         */
        public Builder workGenerator(WorkGenerator generator) {
            this.workGenerator = generator;
            return this;
        }
        
        /**
         * Builds and returns the StateBlockFactory from the given parameters.
         * @return the constructed block factory
         */
        public LegacyBlockFactory build() {
            return new LegacyBlockFactory(
                    defaultRepresentative != null ? defaultRepresentative : NanoAccount.ZERO_ACCOUNT,
                    addressPrefix != null ? addressPrefix : NanoAccount.DEFAULT_PREFIX,
                    workGenerator != null ? workGenerator : new CPUWorkGenerator());
        }
    }
    
}
