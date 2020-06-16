package uk.oczadly.karl.jnano.model.block;

import uk.oczadly.karl.jnano.model.NanoAccount;

import java.math.BigInteger;

public final class BlockInterfaces {
    private BlockInterfaces() {}
    
    
    /** This interface is to be implemented by blocks which contain a representative address. */
    public interface Representative {
        /**
         * @return the representative address for this account
         */
        NanoAccount getRepresentative();
    }
    
    /** This interface is to be implemented by blocks which contain a previous block hash. */
    public interface Previous {
        /**
         * @return the hash of the previous block in this account's blockchain
         */
        String getPreviousBlockHash();
    }
    
    /** This interface is to be implemented by blocks which contain the owning account. */
    public interface Account {
        /**
         * @return the account which this block belongs to
         */
        NanoAccount getAccount();
    }
    
    /** This interface is to be implemented by blocks which contain a source block hash. */
    public interface Source extends Link {
        /**
         * @return
         */
        String getSourceBlockHash();
    
        @Override
        default NanoAccount getLinkAsAccount() {
            return NanoAccount.parsePublicKey(getSourceBlockHash());
        }
        
        @Override
        default String getLinkData() {
            return getSourceBlockHash();
        }
    }
    
    /** This interface is to be implemented by blocks which contain the account's balance. */
    public interface Balance {
        /**
         * @return the balance of the account after this transaction
         */
        BigInteger getBalance();
    }
    
    /** This interface is to be implemented by blocks which contain link data. */
    public interface Link {
        /**
         * @return the link data, encoded as a Nano account
         */
        NanoAccount getLinkAsAccount();
    
        /**
         * @return the link data, encoded as a hexadecimal string
         */
        String getLinkData();
    }

}
