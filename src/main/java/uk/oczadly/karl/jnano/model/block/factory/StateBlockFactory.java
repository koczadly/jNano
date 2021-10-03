/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.factory;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockBuilder;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;
import uk.oczadly.karl.jnano.util.workgen.CPUWorkGenerator;
import uk.oczadly.karl.jnano.util.workgen.NodeWorkGenerator;
import uk.oczadly.karl.jnano.util.workgen.WorkGenerator;

/**
 * A {@link BlockFactory} which constructs and signs {@link StateBlock} blocks.
 *
 * @see Builder
 */
public final class StateBlockFactory extends BlockFactory<StateBlock> {
    
    /**
     * Constructs a new StateBlockFactory with the given parameters.
     *
     * @param defaultRepresentative the default representative for new accounts
     * @param workGenerator         the work generator
     *
     * @see Builder
     */
    public StateBlockFactory(NanoAccount defaultRepresentative, WorkGenerator workGenerator) {
        this(defaultRepresentative, defaultRepresentative.getPrefix(), workGenerator);
    }
    
    private StateBlockFactory(NanoAccount defaultRepresentative, String addressPrefix, WorkGenerator workGenerator) {
        super(defaultRepresentative, addressPrefix, workGenerator);
    }
    
    
    @Override
    protected BlockAndState<StateBlock> _createSend(HexData privateKey, AccountState state, NanoAccount destination,
                                                    NanoAmount amount) {
        return construct(privateKey, builder(state)
                .subtype(StateBlockSubType.SEND)
                .balance(state.getBalance().subtract(amount))
                .link(destination));
    }
    
    @Override
    protected BlockAndState<StateBlock> _createReceive(HexData privateKey, AccountState state, HexData sourceHash,
                                                       NanoAmount amount) {
        NanoAmount newBal;
        try {
            newBal = state.getBalance().add(amount);
        } catch (ArithmeticException e) {
            throw new CreationException("Receiving more funds than possible.");
        }
        return construct(privateKey, builder(state)
                .subtype(state.isOpened() ? StateBlockSubType.RECEIVE : StateBlockSubType.OPEN)
                .balance(newBal)
                .link(sourceHash));
    }
    
    @Override
    protected BlockAndState<StateBlock> _createChange(HexData privateKey, AccountState state,
                                                      NanoAccount representative) {
        return construct(privateKey, builder(state)
                .subtype(StateBlockSubType.CHANGE)
                .representative(representative));
    }
    
    
    private StateBlockBuilder builder(AccountState state) {
        return StateBlock.builder()
                .balance(state.getBalance())
                .previous(state.getFrontierHash())
                .representative(state.isOpened() ? state.getRepresentative() : getDefaultRepresentative())
                .generateWork(getWorkGenerator())
                .usingAddressPrefix(getAddressPrefix());
    }
    
    private static BlockAndState<StateBlock> construct(HexData privKey, StateBlockBuilder builder) {
        StateBlock block;
        try {
            block = builder.buildAndSign(privKey);
        } catch (StateBlockBuilder.BlockCreationException e) {
            throw new CreationException(e.getMessage(), e);
        }
        return new BlockAndState<>(block, block.getAccountState());
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
        public StateBlockFactory build() {
            return new StateBlockFactory(
                    defaultRepresentative != null ? defaultRepresentative : NanoAccount.ZERO_ACCOUNT,
                    addressPrefix != null ? addressPrefix : NanoAccount.DEFAULT_PREFIX,
                    workGenerator != null ? workGenerator : new CPUWorkGenerator());
        }
    }
    
}
