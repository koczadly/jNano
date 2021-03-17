/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.blockproducer;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountInfo;

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
    
    public AccountState(HexData frontier, NanoAmount balance, NanoAccount representative) {
        if (representative == null) throw new IllegalArgumentException("Representative cannot be null.");
        if (balance == null) throw new IllegalArgumentException("Balance cannot be null.");
        if (frontier == null) throw new IllegalArgumentException("Frontier hash cannot be null.");
        this.representative = representative;
        this.balance = balance;
        this.frontier = frontier;
    }
    
    
    /**
     * @return the balance of the account
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
    
    
    /**
     * Constructs an {@link AccountState} from a {@link ResponseAccountInfo} response object.
     * @param info the response data
     * @return the account state
     */
    public static AccountState fromAccountInfo(ResponseAccountInfo info) {
        if (info == null) return UNOPENED;
        return new AccountState(info.getFrontierBlockHash(), info.getBalance(), info.getRepresentativeAccount());
    }
    
    /**
     * Constructs an {@link AccountState} from a {@link StateBlock}.
     * @param block the block
     * @return the account state
     */
    public static AccountState fromBlock(StateBlock block) {
        if (block == null) return AccountState.UNOPENED;
        return new AccountState(block.getHash(), block.getBalance(), block.getRepresentative());
    }
    
}
