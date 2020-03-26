package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class StateBlockBuilderTest {
    
    private static final String NULL_ZERO_STRING = "0000000000000000000000000000000000000000000000000000000000000000";
    
    public static StateBlockBuilder newBuilder() {
        return new StateBlockBuilder(
                StateBlockSubType.EPOCH,
                "ACCOUNT",
                "PREVHASH",
                "REP",
                new BigInteger("1337"));
    }
    

    @Test
    public void testBuildMultiple() {
        StateBlockBuilder builder = newBuilder();
    
        StateBlock b1 = builder.build();
        StateBlock b2 = builder.build();
        
        assertNotNull(b1);
        assertNotNull(b2);
        assertNotSame(b1, b2);
    }
    
    @Test
    public void testAllValues() {
        StateBlock block = newBuilder()
                .setHash("HASH")
                .setLinkData("LINK", StateBlockBuilder.LinkFormat.RAW_HEX)
                .setSignature("SIG")
                .setWorkSolution("WORK")
                .build();
    
        assertEquals(BlockType.STATE, block.getType());
    
        assertEquals(new BigInteger("1337"), block.getBalance());
        assertEquals(StateBlockSubType.EPOCH, block.getSubType());
        assertEquals("HASH", block.getHash());
        assertEquals("ACCOUNT", block.getAccountAddress());
        assertEquals("PREVHASH", block.getPreviousBlockHash());
        assertEquals("REP", block.getRepresentativeAddress());
        assertEquals("LINK", block.getLinkData());
        assertEquals("SIG", block.getSignature());
        assertEquals("WORK", block.getWorkSolution());
        
        //TODO test JSON
        assertNotNull(block.getJsonObject());
        assertNotNull(block.getJsonString());
    }
    
    @Test
    public void testLinkFormats() {
        // Hex format
        assertEquals("LINK", newBuilder()
                .setLinkData("LINK", StateBlockBuilder.LinkFormat.RAW_HEX)
                .build().getLinkData());
        assertEquals("LINK", newBuilder().setLinkData("LINK", null).build().getLinkData());
        
        // Account format
        assertEquals("LINK", newBuilder()
                .setLinkData("LINK", StateBlockBuilder.LinkFormat.ACCOUNT)
                .build().getLinkAsAccount());
        
        // Null should default to 000000...
        assertEquals(NULL_ZERO_STRING, newBuilder().build().getLinkData());
        assertEquals(NULL_ZERO_STRING, newBuilder()
                .setLinkData(null, null).build().getLinkData());
    }
    
}