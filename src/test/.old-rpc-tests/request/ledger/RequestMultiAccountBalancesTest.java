package uk.oczadly.karl.jnano.rpc.request.ledger;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.request.node.RequestMultiAccountBalances;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountBalances;
import uk.oczadly.karl.jnano.tests.Configuration;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class RequestMultiAccountBalancesTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        ResponseMultiAccountBalances response = query(
                new RequestMultiAccountBalances(
                    Configuration.TEST_ACCOUNT,
                    "xrb_3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xtoncuohr3"));
        
        System.out.println(response.getBalance(Configuration.TEST_ACCOUNT).getTotal());
        System.out.println(response.getBalance(Configuration.TEST_ACCOUNT).getPocketed());
        
        assertNotNull(response.getBalance(Configuration.TEST_ACCOUNT));
        assertNotNull(response.getBalance("xrb_3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xtoncuohr3"));
        
        assertEquals("219000000000000000000000000", response.getBalance(Configuration.TEST_ACCOUNT).getPocketed().toString());
        assertEquals("97000000000000000000000000", response.getBalance(Configuration.TEST_ACCOUNT).getPending().toString());
        assertEquals("316000000000000000000000000", response.getBalance(Configuration.TEST_ACCOUNT).getTotal().toString());
        
        //Test empty
        response = query(new RequestMultiAccountBalances());
        assertNotNull(response.getBalances());
        assertTrue(response.getBalances().isEmpty());
        
    }
    
}