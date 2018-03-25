package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.model.block.BlockSend;
import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.generic.BlocksResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlocksRetrieveRequestTest extends QueryBaseTest {
    
    @Test
    public void test() {
        BlocksResponse res = query(new BlocksRetrieveRequest(TEST_BLOCK_SEND, TEST_BLOCK_CHANGE, TEST_BLOCK_RECEIVE));
        assertNotNull(res.getBlocks());
        assertEquals(3, res.getBlocks().size());
        
        assertEquals("3928845117595383247300999000000", ((BlockSend)res.getBlock(TEST_BLOCK_SEND)).getNewBalance().toString()); //Ensure balance is accurate
    }
    
}