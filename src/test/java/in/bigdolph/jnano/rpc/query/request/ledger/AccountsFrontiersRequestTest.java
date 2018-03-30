package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.AccountFrontiersResponse;
import in.bigdolph.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class AccountsFrontiersRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        AccountFrontiersResponse res = query(new AccountsFrontiersRequest(TEST_ACCOUNT));
        assertNotNull(res.getFrontiers());
        assertEquals(1, res.getFrontiers().size());
        assertEquals("B5738AB45581CE752F90F3B921119D5AABE01ED568042317F9146B1FEB5E924C", res.getFrontierBlockHash(TEST_ACCOUNT));
    
        res = query(new AccountsFrontiersRequest());
        assertNotNull(res.getFrontiers());
        assertEquals(0, res.getFrontiers().size());
    }
    
}