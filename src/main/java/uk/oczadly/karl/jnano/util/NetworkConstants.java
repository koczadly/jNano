package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.OpenBlock;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

public final class NetworkConstants {
    
    private final OpenBlock genesisBlock;
    private final NanoAccount burnAddress;
    private final String addressPrefix;
    
    NetworkConstants(String burnAddressSegment, String addressPrefix, String genBlockSig, WorkSolution genBlockWork,
                     String genBlockSource, String genBlockAccountSeg) {
        this.burnAddress = NanoAccount.parseSegment(burnAddressSegment, addressPrefix);
        this.addressPrefix = addressPrefix;
        NanoAccount genesisAccount = NanoAccount.parseSegment(genBlockAccountSeg, addressPrefix);
        this.genesisBlock = new OpenBlock(genBlockSig, genBlockWork, genBlockSource, genesisAccount, genesisAccount);
    }
    
    
    /**
     * @return the genesis block which created all existing nano units
     */
    public OpenBlock getGenesisBlock() {
        return genesisBlock;
    }
    
    /**
     * @return the account which created the genesis block
     */
    public NanoAccount getGenesisAccount() {
        return getGenesisBlock().getAccount();
    }
    
    /**
     * @return the officially designated burn address, where coins are made irretrievable
     */
    public NanoAccount getBurnAddress() {
        return burnAddress;
    }
    
    /**
     * @return the prefix of account addresses
     */
    public String getAddressPrefix() {
        return addressPrefix;
    }
    
}
