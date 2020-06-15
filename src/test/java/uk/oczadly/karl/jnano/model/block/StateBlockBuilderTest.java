package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class StateBlockBuilderTest {
    
    private static final String NULL_ZERO_STRING = "0000000000000000000000000000000000000000000000000000000000000000";
    private static final String DATA = "8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97";
    private static final NanoAccount ACCOUNT =
            NanoAccount.parse("nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz");
    
    public static StateBlockBuilder newBuilder() {
        return new StateBlockBuilder(
                ACCOUNT,
                "PREVHASH",
                ACCOUNT,
                new BigInteger("1337"))
                .setSubtype(StateBlockSubType.EPOCH);
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
                .setLinkData(DATA)
                .setSignature("SIG")
                .setWorkSolution("WORK")
                .build();
    
        assertEquals(BlockType.STATE, block.getType());
    
        assertEquals(new BigInteger("1337"), block.getBalance());
        assertEquals(StateBlockSubType.EPOCH, block.getSubType());
        assertEquals("HASH", block.getHash());
        assertEquals(ACCOUNT, block.getAccountAddress());
        assertEquals("PREVHASH", block.getPreviousBlockHash());
        assertEquals(ACCOUNT, block.getRepresentativeAddress());
        assertEquals(DATA, block.getLinkData());
        assertEquals("SIG", block.getSignature());
        assertEquals("WORK", block.getWorkSolution());
        
        //TODO test JSON
        assertNotNull(block.getJsonObject());
        assertNotNull(block.getJsonString());
    }
    
    @Test
    public void testLinkFormats() {
        // Data
        StateBlock b1 = newBuilder().setLinkAccount(ACCOUNT).setLinkData(DATA).build();
        assertEquals(DATA, b1.getLinkData());
        assertEquals(b1.getLinkAsAccount(), ACCOUNT);
    
        // Account
        StateBlock b2 = newBuilder().setLinkData(DATA).setLinkAccount(ACCOUNT).build();
        assertEquals(ACCOUNT, b2.getLinkAsAccount());
        assertEquals(b2.getLinkData(), DATA);
        
        // Null should default to 000000...
        assertEquals(NULL_ZERO_STRING, newBuilder().build().getLinkData());
        assertEquals(NULL_ZERO_STRING, newBuilder().setLinkData(null).build().getLinkData());
    }
    
}