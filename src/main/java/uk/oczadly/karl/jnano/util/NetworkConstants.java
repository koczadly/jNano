package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.OpenBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

public final class NetworkConstants {
    
    private final String networkName, addressPrefix;
    private final OpenBlock genesisBlock;
    private final NanoAccount burnAddress;
    private MinimumWorkDifficulty workDifficulty;
    
    @SuppressWarnings("deprecation")
    NetworkConstants(String networkName, String addressPrefix, String burnAddressSegment, String genBlockSig,
                     WorkSolution genBlockWork, String genBlockAccountSeg, MinimumWorkDifficulty workDifficulty) {
        this.networkName = networkName;
        this.addressPrefix = addressPrefix;
        this.burnAddress = NanoAccount.parseAddressSegment(burnAddressSegment, addressPrefix);
        NanoAccount genesisAccount = NanoAccount.parseAddressSegment(genBlockAccountSeg, addressPrefix);
        this.genesisBlock = new OpenBlock(genBlockSig, genBlockWork, genesisAccount.toPublicKey(), genesisAccount,
                genesisAccount);
        this.workDifficulty = workDifficulty;
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
     * @return the network identifier string (genesis block hash)
     */
    public String getNetworkIdentifier() {
        return getGenesisBlock().getHash();
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
    
    /**
     * @param blockType the block type
     * @return the work minimum difficulty threshold for the given block type
     */
    public WorkDifficulty getWorkDifficultyThreshold(StateBlockSubType blockType) {
        return workDifficulty.getWorkThreshold(blockType);
    }
    
    
    @Override
    public String toString() {
        return getNetworkName();
    }
    
    
    static class MinimumWorkDifficulty {
        private WorkDifficulty send, receive;
        
        MinimumWorkDifficulty(WorkDifficulty send, WorkDifficulty receive) {
            this.send = send;
            this.receive = receive;
        }
        
        public WorkDifficulty getWorkThreshold(StateBlockSubType subtype) {
            switch (subtype) {
                case SEND:
                case CHANGE_REP:
                    return send;
                default:
                    return receive;
            }
        }
    }
    
}
