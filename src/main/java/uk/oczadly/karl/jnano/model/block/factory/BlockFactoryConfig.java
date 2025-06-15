package uk.oczadly.karl.jnano.model.block.factory;

import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.util.workgen.WorkGenerator;
import uk.oczadly.karl.jnano.util.workgen.provider.NodeWorkGeneratorProvider;

import java.util.Objects;

public class BlockFactoryConfig {

    private final NanoAccount defaultRepresentative;
    private final String addressPrefix;
    private final WorkGenerator workGenerator;


    protected BlockFactoryConfig(NanoAccount defaultRepresentative, String addressPrefix, WorkGenerator workGenerator) {
        this.defaultRepresentative = defaultRepresentative;
        this.addressPrefix = addressPrefix;
        this.workGenerator = workGenerator;
    }


    public NanoAccount getDefaultRepresentative() {
        return defaultRepresentative;
    }

    public String getAddressPrefix() {
        return addressPrefix;
    }

    public WorkGenerator getWorkGenerator() {
        return workGenerator;
    }


    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {

        private NanoAccount defaultRepresentative;
        private String addressPrefix;
        private WorkGenerator workGenerator;

        /**
         * Sets the default representative address for new accounts.
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
         * <p>Defaults to a {@link NodeWorkGeneratorProvider} using the specified RPC client (work will be generated on the
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
         * Builds and returns the BlockFactoryConfig from the given parameters.
         * @return the constructed block factory specification
         */
        public BlockFactoryConfig build() {
            if (workGenerator == null)
                throw new IllegalStateException("No work generator has been specified.");

            String prefix = Objects.requireNonNullElse(addressPrefix, NanoAccount.DEFAULT_PREFIX);
            return new BlockFactoryConfig(
                    defaultRepresentative != null
                            ? defaultRepresentative.withPrefix(prefix)
                            : NanoAccount.ZERO_ACCOUNT.withPrefix(prefix),
                    prefix, workGenerator);
        }


        /**
         * Builds and returns a {@link StateBlockFactory} based on the configured parameters.
         * @return a BlockFactoryConfig based on the parameters
         */
        public StateBlockFactory buildStateBlockFactory() {
            return new StateBlockFactory(build());
        }

    }

}
