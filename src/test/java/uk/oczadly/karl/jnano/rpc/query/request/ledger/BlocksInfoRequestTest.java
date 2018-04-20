package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import uk.oczadly.karl.jnano.model.block.SendBlock;
import uk.oczadly.karl.jnano.rpc.query.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.query.response.BlocksInfoResponse;
import uk.oczadly.karl.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.tests.Configuration;

import static org.junit.Assert.*;

public class BlocksInfoRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        BlocksInfoResponse res = query(new BlocksInfoRequest(Configuration.TEST_BLOCK_SEND, Configuration.TEST_BLOCK_CHANGE, Configuration.TEST_BLOCK_RECEIVE));
        assertNotNull(res.getBlocks());
        assertNotNull(res.getBlock(Configuration.TEST_BLOCK_SEND));
        assertEquals(3, res.getBlocks().size());
        assertTrue(res.getBlock(Configuration.TEST_BLOCK_SEND).getBlock() instanceof SendBlock);
    }
    
}