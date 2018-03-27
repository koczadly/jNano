package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountsPendingResponse;
import in.bigdolph.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class AccountsPendingRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        AccountsPendingResponse res = query(new AccountsPendingRequest(new String[] {TEST_ACCOUNT}, 100));
        assertNotNull(res.getPendingBlocks());
        assertNotNull(res.getPendingBlocks(TEST_ACCOUNT));
        assertEquals(1, res.getPendingBlocks().keySet().size());
        assertEquals(1, res.getPendingBlockHashes(TEST_ACCOUNT).size());
    
        res = query(new AccountsPendingRequest(new String[] {}, 1));
        assertNotNull(res.getPendingBlocks());
        assertEquals(0, res.getPendingBlocks().keySet().size());
    }
    
}