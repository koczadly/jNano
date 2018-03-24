package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.specific.BlocksRetrieveResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlocksRetrieveRequestTest extends QueryBaseTest {
    
    @Test
    public void test() {
        BlocksRetrieveResponse res = query(new BlocksRetrieveRequest(TEST_BLOCK_SEND, TEST_BLOCK_CHANGE, TEST_BLOCK_RECEIVE));
        assertNotNull(res.getBlocks());
        assertEquals(3, res.getBlocks().size());
    }
    
}