package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.model.block.BlockType;
import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.specific.BlockHistoryResponse;
import in.bigdolph.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class BlockHistoryRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        BlockHistoryResponse res = query(new BlockHistoryRequest(TEST_BLOCK_SEND, 20));
        assertEquals(20, res.getBlocks().size());
        assertEquals(BlockType.SEND, res.getBlocks().iterator().next().getBlockType());
    }
    
}