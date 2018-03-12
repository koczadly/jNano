package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountsFrontiersResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountsFrontiersRequestTest extends QueryBaseTest {
    
    @Test
    public void test() {
        AccountsFrontiersResponse res = query(new AccountsFrontiersRequest(TEST_ACCOUNT));
        assertNotNull(res.getFrontiers());
        assertEquals(1, res.getFrontiers().size());
        assertEquals("B5738AB45581CE752F90F3B921119D5AABE01ED568042317F9146B1FEB5E924C", res.getFrontierBlockHash(TEST_ACCOUNT));
    
        res = query(new AccountsFrontiersRequest());
        assertNotNull(res.getFrontiers());
        assertEquals(0, res.getFrontiers().size());
    }
    
}