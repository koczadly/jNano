package uk.oczadly.karl.jnano.util.wallet;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.currency.NanoAmount;
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountInfo;

import java.util.Optional;

public interface ReadOnlyWalletAccountProvider {

    /**
     * Returns the account which this wallet represents.
     *
     * @return the account of this wallet
     */
    NanoAccount getAccount();

    /**
     * Returns the current balance of this account, not include any pending amounts. A value of zero if the account hasn't yet been opened.
     *
     * <p>Note that this balance may include unconfirmed amounts. This shouldn't be an issue, as only people with
     * access to this account's private key may reverse these transactions with a fork block.</p>
     *
     * @return the current balance of the account
     * @throws WalletActionException if an error occurs with the RPC query when retrieving the account state
     */
    NanoAmount getBalance() throws WalletActionException;

    /**
     * Returns the hash of the current frontier block of this account, or an empty value if the account hasn't been opened yet.
     *
     * <p>Note that this may be an unconfirmed block. This shouldn't be an issue, as only people with access to this
     * account's private key may reverse this block with another fork block.</p>
     *
     * @return the current account frontier block hash, or empty if unopened
     * @throws WalletActionException if an error occurs with the RPC query when retrieving the account state
     */
    Optional<HexData> getFrontierHash() throws WalletActionException;

    /**
     * Forcefully refreshes the internal cached state of the account by calling the {@link RequestAccountInfo} RPC query.
     *
     * <p>Most implementations should never have to call this method, as the state will automatically be retrieved or
     * updated when necessary through the other methods.</p>
     *
     * @return true if the internally cached state was externally updated and has changed, false if it remains the same or is the first time
     * retrieving the state
     * @throws WalletActionException if an error occurs with the RPC query
     */
    boolean refreshState() throws WalletActionException;

}
