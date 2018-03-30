package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.LedgerAccountsResponse;
import in.bigdolph.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class LedgerAccountsRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        LedgerAccountsResponse res = query(new LedgerAccountsRequest(TEST_ACCOUNT, 20));
        assertEquals(20, res.getAccounts().size());
        LedgerAccountsResponse.AccountInfo info = res.getAccount(TEST_ACCOUNT);
        assertNotNull(info);
        assertEquals("B5738AB45581CE752F90F3B921119D5AABE01ED568042317F9146B1FEB5E924C", info.getOpenBlockHash());
        assertEquals("219000000000000000000000000", info.getBalance().toString());
        assertEquals("97000000000000000000000000", info.getBalancePending().toString());
    }
    
}