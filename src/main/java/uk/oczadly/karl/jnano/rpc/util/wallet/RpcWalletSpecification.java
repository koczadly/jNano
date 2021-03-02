/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.util.wallet;

import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.util.workgen.NodeWorkGenerator;
import uk.oczadly.karl.jnano.util.workgen.WorkGenerator;

/**
 * Represents a specification of a wallet. Use the provided {@link Builder builder} object to construct this class.
 *
 * @see #builder()
 */
public class RpcWalletSpecification {
    
    private final RpcQueryNode rpcClient;
    private final NanoAccount defaultRepresentative;
    private final String addressPrefix;
    private final WorkGenerator workGenerator;
    
    
    private RpcWalletSpecification(RpcQueryNode rpcClient, NanoAccount defaultRepresentative, String addressPrefix,
                                  WorkGenerator workGenerator) {
        this.rpcClient = rpcClient;
        this.addressPrefix = addressPrefix;
        this.defaultRepresentative = defaultRepresentative.withPrefix(addressPrefix);
        this.workGenerator = workGenerator;
    }
    
    
    /**
     * @return the RPC client through which requests are made
     */
    public RpcQueryNode getRpcClient() {
        return rpcClient;
    }
    
    /**
     * @return the address prefix
     */
    public String getAddressPrefix() {
        return addressPrefix;
    }
    
    /**
     * @return the work generator for new blocks
     */
    public WorkGenerator getWorkGenerator() {
        return workGenerator;
    }
    
    /**
     * @return the default representative address for new accounts
     */
    public NanoAccount getDefaultRepresentative() {
        return defaultRepresentative;
    }
    
    
    /**
     * @return a new builder object
     */
    public static Builder builder() {
        return new Builder();
    }
    
    
    public static final class Builder {
        private RpcQueryNode rpcClient;
        private NanoAccount defaultRepresentative;
        private String addressPrefix;
        private WorkGenerator workGenerator;
    
        /**
         * Sets the RPC client over which requests will be made.
         * @param rpcClient the RPC client to use
         * @return this builder
         */
        public Builder rpcClient(RpcQueryNode rpcClient) {
            this.rpcClient = rpcClient;
            return this;
        }
    
        /**
         * Sets the default representative address for new accounts.
         * @param representative the default representative
         * @return this builder
         */
        public Builder defaultRepresentative(NanoAccount representative) {
            this.defaultRepresentative = representative;
            return this;
        }
    
        /**
         * Sets the default representative address for new accounts.
         * @param representative the default representative
         * @return this builder
         */
        public Builder defaultRepresentative(String representative) {
            return defaultRepresentative(NanoAccount.parse(representative));
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
         * Builds and returns the wallet specification from the set parameters.
         * @return the constructed specification object
         */
        public RpcWalletSpecification build() {
            if (rpcClient == null) throw new IllegalStateException("No RPC client is specified.");
            return new RpcWalletSpecification(rpcClient,
                    defaultRepresentative != null ? defaultRepresentative : NanoAccount.ZERO_ACCOUNT,
                    addressPrefix != null ? addressPrefix : NanoAccount.DEFAULT_PREFIX,
                    workGenerator != null ? workGenerator : new NodeWorkGenerator(rpcClient));
        }
    }
    
}
