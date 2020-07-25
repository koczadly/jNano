package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.OpenBlock;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

public final class NetworkConstants {
    
    private final String networkName, addressPrefix;
    private final OpenBlock genesisBlock;
    private final NanoAccount burnAddress;
    
    @SuppressWarnings("deprecation")
    NetworkConstants(String networkName, String addressPrefix, String burnAddressSegment, String genBlockSig,
                     WorkSolution genBlockWork, String genBlockAccountSeg) {
        this.networkName = networkName;
        this.addressPrefix = addressPrefix;
        this.burnAddress = NanoAccount.parseAddressSegment(burnAddressSegment, addressPrefix);
        NanoAccount genesisAccount = NanoAccount.parseAddressSegment(genBlockAccountSeg, addressPrefix);
        this.genesisBlock = new OpenBlock(genBlockSig, genBlockWork, genesisAccount.toPublicKey(), genesisAccount,
                genesisAccount);
    }
    
    
    /**
     * @return the name of this network
     */
    public String getNetworkName() {
        return networkName;
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
    
    
    @Override
    public String toString() {
        return getNetworkName();
    }
    
}
