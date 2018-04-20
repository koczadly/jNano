package uk.oczadly.karl.jnano.rpc.request.ledger;

import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.response.BlockHistoryResponse;
import uk.oczadly.karl.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.tests.Configuration;

import static org.junit.Assert.*;

public class BlockHistoryRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        BlockHistoryResponse res = query(new BlockHistoryRequest(Configuration.TEST_BLOCK_SEND, 20));
        assertEquals(20, res.getBlocks().size());
        assertEquals(BlockType.SEND, res.getBlocks().iterator().next().getBlockType());
    }
    
}