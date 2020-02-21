package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class StateBlockBuilderTest {
    
    public StateBlockBuilder newBuilder() {
        return new StateBlockBuilder(
                "ACCOUNT",
                "PREVHASH",
                "REP",
                BigInteger.ZERO);
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
        
        assertEquals("HASH", block.getHash());
        assertEquals("ACCOUNT", block.getAccountAddress());
        assertEquals("PREVHASH", block.getPreviousBlockHash());
        assertEquals("REP", block.getRepresentativeAddress());
        assertEquals("LINK", block.getLinkData());
        assertEquals("SIG", block.getSignature());
        assertEquals("WORK", block.getWorkSolution());
    }
    
    @Test
    public void testLinkFormats() {
        // Hex
        assertEquals("LINK", newBuilder()
                .setLinkData("LINK", StateBlockBuilder.LinkFormat.RAW_HEX).build().getLinkData());
        
        // Account
        assertEquals("LINK", newBuilder()
                .setLinkData("LINK", StateBlockBuilder.LinkFormat.ACCOUNT).build().getLinkAsAccount());
        
        // Null
        assertNotNull(newBuilder().setLinkData(null, null).build().getLinkData());
        assertNotNull(newBuilder().build().getLinkData());
    }
    
}