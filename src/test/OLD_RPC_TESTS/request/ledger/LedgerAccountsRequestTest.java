package uk.oczadly.karl.jnano.rpc.request.ledger;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.request.node.RequestLedger;
import uk.oczadly.karl.jnano.rpc.response.ResponseLedger;
import uk.oczadly.karl.jnano.tests.Configuration;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class LedgerAccountsRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        ResponseLedger res = query(new RequestLedger(Configuration.TEST_ACCOUNT, 20));
        assertEquals(20, res.getAccounts().size());
        ResponseLedger.AccountInfo info = res.getAccount(Configuration.TEST_ACCOUNT);
        assertNotNull(info);
        assertEquals("B5738AB45581CE752F90F3B921119D5AABE01ED568042317F9146B1FEB5E924C", info.getOpenBlockHash());
        assertEquals("219000000000000000000000000", info.getBalance().toString());
        assertEquals("97000000000000000000000000", info.getBalancePending().toString());
    }
    
}