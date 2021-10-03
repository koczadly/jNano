/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.factory;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountInfo;

import java.util.Objects;

/**
 * Represents an immutable account state.
 */
public class AccountState {
    
    /** A universal constant state representing an unopened account. */
    public static final AccountState UNOPENED = new AccountState();
    
    private final NanoAccount representative;
    private final NanoAmount balance;
    private final HexData frontier;
    
    private AccountState() {
        this.representative = null;
        this.balance = NanoAmount.ZERO;
        this.frontier = null;
    }
    
    /**
     * Creates an account state from the given data.
     * @param frontier       the hash of the frontier block
     * @param balance        the balance of the account
     * @param representative the current representative
     * @see #UNOPENED
     */
    public AccountState(HexData frontier, NanoAmount balance, NanoAccount representative) {
        if (representative == null) throw new IllegalArgumentException("Representative cannot be null.");
        if (balance == null) throw new IllegalArgumentException("Balance cannot be null.");
        if (frontier == null) throw new IllegalArgumentException("Frontier hash cannot be null.");
        this.representative = representative;
        this.balance = balance;
        this.frontier = frontier;
    }
    
    
    /**
     * @return the balance of the account (zero if unopened)
     */
    public final NanoAmount getBalance() {
        return balance;
    }
    
    /**
     * @return the current representative, or null if not opened
     */
    public final NanoAccount getRepresentative() {
        return representative;
    }
    
    /**
     * @return the current frontier block hash, or null if not opened
     */
    public final HexData getFrontierHash() {
        return frontier;
    }
    
    /**
     * @return true if the account has been opened
     */
    public final boolean isOpened() {
        return frontier != null;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountState)) return false;
        AccountState that = (AccountState)o;
        return Objects.equals(representative, that.representative)
                && Objects.equals(balance, that.balance)
                && Objects.equals(frontier, that.frontier);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(frontier);
    }
    
    /**
     * Constructs an {@link AccountState} from a {@link ResponseAccountInfo} response object. This uses the
     * <em>unconfirmed</em> frontier and balance values.
     *
     * @param info the response data, or null if unopened
     * @return the account state represented by the response
     */
    public static AccountState ofAccountInfo(ResponseAccountInfo info) {
        if (info == null) return UNOPENED;
        return new AccountState(info.getFrontierBlockHash(), info.getBalance(), info.getRepresentativeAccount());
    }
    
}
