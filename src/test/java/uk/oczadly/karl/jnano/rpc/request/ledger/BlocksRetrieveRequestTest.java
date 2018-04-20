package uk.oczadly.karl.jnano.rpc.request.ledger;

import uk.oczadly.karl.jnano.model.block.SendBlock;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.response.BlocksResponse;
import uk.oczadly.karl.jnano.tests.NodeTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.tests.Configuration;

import static org.junit.Assert.*;

public class BlocksRetrieveRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        BlocksResponse res = query(new BlocksRetrieveRequest(Configuration.TEST_BLOCK_SEND, Configuration.TEST_BLOCK_CHANGE, Configuration.TEST_BLOCK_RECEIVE));
        assertNotNull(res.getBlocks());
        assertEquals(3, res.getBlocks().size());
        
        Assert.assertEquals("3928845117595383247300999000000", ((SendBlock)res.getBlock(Configuration.TEST_BLOCK_SEND)).getNewBalance().toString()); //Ensure balance is accurate
    }
    
}