package uk.oczadly.karl.jnano.util.wallet;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;

import java.util.Optional;
import java.util.Set;

public interface ControllableWalletAccountProvider<B extends Block> extends ReadOnlyWalletAccountProvider {

    NanoAmount DEFAULT_THRESHOLD = NanoAmount.valueOfRawExponent(24);


    /**
     * Sends the specified amount of funds to an account.
     *
     * <p>Calling this method will construct and sign a new block, generate the appropriate work for it, and publish the
     * block to the network via RPC.</p>
     *
     * @param destination the account where the funds should be sent
     * @param amount      the amount of funds to send
     * @return the generated and published {@code send} block
     * @throws WalletActionException if an error occurs with the RPC query, work generation, block processing, or if
     *                               there are not enough funds available in the account
     */
    B send(NanoAccount destination, NanoAmount amount) throws WalletActionException;

    /**
     * Attempts to send the entire balance to the specified account, returning an empty value if there are no remaining
     * funds to send (account has zero balance).
     *
     * <p>This method will not receive/send any pending blocks; that can be done by calling {@link #receiveAll()}
     * prior to sending funds.</p>
     *
     * <p>Calling this method will construct and sign a new block, generate the appropriate work for it, and publish the
     * block to the network via RPC.</p>
     *
     * @param destination the account where the funds should be sent
     * @return the generated and published {@code send} block, or empty if the account has no funds to send
     * @throws WalletActionException if an error occurs with the RPC query, work generation or block processing
     */
    Optional<B> sendAll(NanoAccount destination) throws WalletActionException;

    /**
     * Receives the specified pending {@code send} block.
     *
     * <p>Calling this method will construct and sign a new block, generate the appropriate work for it, and publish the
     * block to the network via RPC.</p>
     *
     * @param sourceHash the hash of the pending {@code send} block
     * @return the generated and published {@code receive} block
     * @throws WalletActionException if an error occurs with the RPC queries, work generation, block processing, or the
     *                               specified block could not be found in the ledger
     *
     * @see #receiveAll()
     * @see #receiveBatch(int)
     */
    B receive(HexData sourceHash) throws WalletActionException;

    /**
     * Attempts to receive a batch of pending blocks of at least {@code 0.000001 NANO} in value, receiving no more than
     * {@code count} blocks (highest valued blocks are processed first).
     *
     * <p>Calling this method will construct and sign a set of new blocks, generate the appropriate work for them, and
     * publish the blocks to the network via RPC.</p>
     *
     * @param count the maximum number of blocks to receive in this batch
     * @return a set containing the generated and published {@code receive} blocks
     * @throws WalletActionException if an error occurs with the RPC queries, work generation or block processing
     *
     * @see #receiveAll()
     */
    default Set<B> receiveBatch(int count) throws WalletActionException {
        return receiveBatch(count, DEFAULT_THRESHOLD);
    }

    /**
     * Attempts to receive a batch of pending blocks of a value greater than or equal to the specified threshold
     * amount, receiving no more than {@code count} blocks (highest valued blocks are processed first).
     *
     * <p>Calling this method will construct and sign a set of new blocks, generate the appropriate work for them, and
     * publish the blocks to the network via RPC.</p>
     *
     * @param count     the maximum number of blocks to receive in this batch
     * @param threshold the minimum amount threshold
     * @return a set containing the generated and published {@code receive} blocks
     * @throws WalletActionException if an error occurs with the RPC queries, work generation or block processing
     *
     * @see #receiveAll(NanoAmount)
     */
    Set<B> receiveBatch(int count, NanoAmount threshold) throws WalletActionException;

    /**
     * Attempts to receive all pending blocks of at least {@code 0.000001 NANO} in value.
     *
     * <p>Calling this method will construct and sign a set of new blocks, generate the appropriate work for them, and
     * publish the blocks to the network via RPC.</p>
     *
     * <p><strong>Note:</strong> if a large number of transactions are pending, or an attacker continues to send funds
     * to this account, this method may block and continue indefinitely. Receive operations are performed in small
     * batches to allow other operations to proceed between each batch.</p>
     *
     * @return a set containing the generated and published {@code receive} blocks
     * @throws WalletActionException if an error occurs with the RPC queries, work generation or block processing
     *
     * @see #receiveBatch(int)
     */
    default Set<B> receiveAll() throws WalletActionException {
        return receiveAll(DEFAULT_THRESHOLD);
    }

    /**
     * Attempts to receive all pending blocks of a value greater than or equal to the specified threshold amount.
     *
     * <p>Calling this method will construct and sign a set of new blocks, generate the appropriate work for them, and
     * publish the blocks to the network via RPC.</p>
     *
     * <p><strong>Note:</strong> if a large number of transactions are pending, or an attacker continues to send funds
     * to this account, this method may block and continue indefinitely. Receive operations are performed in small
     * batches to allow other operations to proceed between each batch.</p>
     *
     * @param threshold the minimum amount threshold
     * @return a set containing the generated and published {@code receive} blocks
     * @throws WalletActionException if an error occurs with the RPC queries, work generation or block processing
     *
     * @see #receiveBatch(int, NanoAmount)
     */
    Set<B> receiveAll(NanoAmount threshold) throws WalletActionException;

    /**
     * Changes the representative of the account to the specified representative address.
     *
     * <p>Calling this method will construct and sign a new block, generate the appropriate work for it, and publish the
     * block to the network via RPC.</p>
     *
     * @param representative the new representative
     * @return the generated and published representative change block, or empty if the representative is already set
     *         to the specified account
     * @throws WalletActionException if an error occurs with the RPC query, work generation or block processing
     */
    Optional<B> changeRepresentative(NanoAccount representative) throws WalletActionException;

}
