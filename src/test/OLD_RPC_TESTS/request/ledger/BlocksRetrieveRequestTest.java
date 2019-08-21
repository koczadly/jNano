package uk.oczadly.karl.jnano.rpc.request.ledger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.model.block.SendBlock;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.request.node.RequestRetrieveMultiBlocks;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlocksMap;
import uk.oczadly.karl.jnano.tests.Configuration;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class BlocksRetrieveRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        ResponseBlocksMap res = query(new RequestRetrieveMultiBlocks(Configuration.TEST_BLOCK_SEND, Configuration.TEST_BLOCK_CHANGE, Configuration.TEST_BLOCK_RECEIVE));
        assertNotNull(res.getBlocks());
        assertEquals(3, res.getBlocks().size());
        
        Assert.assertEquals("3928845117595383247300999000000", ((SendBlock)res.getBlock(Configuration.TEST_BLOCK_SEND)).getNewBalance().toString()); //Ensure balance is accurate
    }
    
}