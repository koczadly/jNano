/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.blockproducer;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.util.NetworkConstants;
import uk.oczadly.karl.jnano.util.workgen.CPUWorkGenerator;
import uk.oczadly.karl.jnano.util.workgen.NodeWorkGenerator;
import uk.oczadly.karl.jnano.util.workgen.WorkGenerator;

/**
 * Represents a specification of a block producer or local wallet. Use the provided {@link Builder builder} object to
 * construct this class.
 *
 * @see #builder()
 */
public class BlockProducerSpecification {
    
    /**
     * A pre-constructed specification using suitable default values for the Nano network. Using this value isn't
     * recommended, but can be suitable for test or reference implementations.
     */
    public static final BlockProducerSpecification DEFAULT = builder().usingNetwork(NetworkConstants.NANO).build();
    
    private final NanoAccount defaultRepresentative;
    private final String addressPrefix;
    private final WorkGenerator workGenerator;
    
    
    private BlockProducerSpecification(NanoAccount defaultRepresentative, String addressPrefix,
                                       WorkGenerator workGenerator) {
        this.addressPrefix = addressPrefix;
        this.defaultRepresentative = defaultRepresentative.withPrefix(addressPrefix);
        this.workGenerator = workGenerator;
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
        private NanoAccount defaultRepresentative;
        private String addressPrefix;
        private WorkGenerator workGenerator;
        private NetworkConstants network;
    
        /**
         * Convenience method which sets the network to use for prefixes and work difficulties, if not manually set.
         * @param network the network specification to use
         * @return this builder
         */
        public Builder usingNetwork(NetworkConstants network) {
            this.network = network;
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
        public BlockProducerSpecification build() {
            NetworkConstants network = JNH.nonNull(this.network, NetworkConstants.NANO);
            return new BlockProducerSpecification(
                    defaultRepresentative != null ? defaultRepresentative : network.getBurnAddress(),
                    addressPrefix != null ? addressPrefix : network.getAddressPrefix(),
                    workGenerator != null ? workGenerator : new CPUWorkGenerator(network.getWorkDifficulties()));
        }
    }
    
}
