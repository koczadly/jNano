package uk.oczadly.karl.jnano.rpc.request.ledger;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.request.node.RequestMultiAccountsPending;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountsPending;
import uk.oczadly.karl.jnano.tests.Configuration;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class RequestMultiAccountsPendingTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        ResponseMultiAccountsPending res = query(new RequestMultiAccountsPending(new String[] {Configuration.TEST_ACCOUNT}, 100));
        assertNotNull(res.getPendingBlocks());
        assertNotNull(res.getPendingBlocks(Configuration.TEST_ACCOUNT));
        assertEquals(1, res.getPendingBlocks().keySet().size());
        assertEquals(1, res.getPendingBlockHashes(Configuration.TEST_ACCOUNT).size());
    
        res = query(new RequestMultiAccountsPending(new String[] {}, 1));
        assertNotNull(res.getPendingBlocks());
        assertEquals(0, res.getPendingBlocks().keySet().size());
    }
    
}