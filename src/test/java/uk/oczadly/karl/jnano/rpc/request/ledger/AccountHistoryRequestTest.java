package uk.oczadly.karl.jnano.rpc.request.ledger;

import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.response.BlocksResponse;
import uk.oczadly.karl.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.tests.Configuration;

import static org.junit.Assert.*;

public class AccountHistoryRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        BlocksResponse response = query(new AccountHistoryRequest(Configuration.TEST_ACCOUNT, 1000));
        assertNotNull(response.getHistory());
        
        assertEquals(1, response.getHistory().size());
        
        for(Block block : response.getHistory()) {
            assertNotNull(block.getHash());
            assertNotNull(block.getType());
        }
        
        //Test empty account
        response = query(new AccountHistoryRequest(Configuration.TEST_ACCOUNT, 0));
        assertNotNull(response.getHistory());
        assertEquals(0, response.getHistory().size());
    }
    
}