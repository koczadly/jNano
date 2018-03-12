package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountsBalanceResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountsBalancesRequestTest extends QueryBaseTest {
    
    @Test
    public void test() {
        AccountsBalanceResponse response = query(
                new AccountsBalancesRequest(
                    TEST_ACCOUNT,
                    "xrb_3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xtoncuohr3"));
        
        System.out.println(response.getBalance(TEST_ACCOUNT).getTotal());
        System.out.println(response.getBalance(TEST_ACCOUNT).getPocketed());
        
        assertNotNull(response.getBalance(TEST_ACCOUNT));
        assertNotNull(response.getBalance("xrb_3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xtoncuohr3"));
        
        assertEquals("219000000000000000000000000", response.getBalance(TEST_ACCOUNT).getPocketed().toString());
        assertEquals("97000000000000000000000000", response.getBalance(TEST_ACCOUNT).getPending().toString());
        assertEquals("316000000000000000000000000", response.getBalance(TEST_ACCOUNT).getTotal().toString());
        
        //Test empty
        response = query(new AccountsBalancesRequest());
        assertNotNull(response.getBalances());
        assertTrue(response.getBalances().isEmpty());
        
    }
    
}