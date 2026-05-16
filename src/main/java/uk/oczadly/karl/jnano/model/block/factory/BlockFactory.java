package uk.oczadly.karl.jnano.model.block.factory;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.currency.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;

import java.util.Optional;

/**
 * This class can be used to create and sign new blocks based on an existing account state.
 *
 * @param <B> the base block type
 *
 * @see StateBlockFactory
 * @see LegacyBlockFactory
 */
public interface BlockFactory<B extends Block> {


    /**
     * @return the specification for this block factory
     */
    BlockFactoryConfig getConfig();


    /**
     * Constructs and signs a new block which sends a specified amount of funds from the account.
     *
     * @param privateKey  the private key of the account
     * @param state       the state of the account prior to the block
     * @param destination the destination account where the funds will be sent
     * @param amount      the amount to send
     * @return the constructed block and new account state
     * @throws BlockCreationException if the block couldn't be constructed, work couldn't be generated, or the account
     *                                state doesn't match the arguments (eg. not enough funds)
     */
    BlockAndState<? extends B> createSendBlock(HexData privateKey, AccountState state, NanoAccount destination, NanoAmount amount);

    /**
     * Constructs and signs a new block which receives a pending block.
     *
     * @param privateKey the private key of the account
     * @param state      the state of the account prior to the block
     * @param sourceHash the hash of the pending {@code send} block
     * @param amount     the amount of the pending send block
     * @return the constructed block and new account state
     * @throws BlockCreationException if the block couldn't be constructed, work couldn't be generated, or the account
     *                                state doesn't match the arguments (eg. receiving too many funds)
     */
    BlockAndState<? extends B> createReceiveBlock(HexData privateKey, AccountState state, HexData sourceHash, NanoAmount amount);

    /**
     * Constructs and signs a new block which changes the account's representative. An empty optional will be returned
     * if the representative is already set to the one given.
     *
     * @param privateKey     the private key of the account
     * @param state          the state of the account prior to the block
     * @param representative the representative account
     * @return the constructed block and new account state, or empty if the representative is already set
     * @throws BlockCreationException if the block couldn't be constructed, or work couldn't be generated
     */
    Optional<BlockAndState<? extends B>> createChangeBlock(HexData privateKey, AccountState state, NanoAccount representative);



    class BlockCreationException extends RuntimeException {
        public BlockCreationException(String message) {
            super(message);
        }

        public BlockCreationException(Throwable cause) {
            super(cause);
        }

        public BlockCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
