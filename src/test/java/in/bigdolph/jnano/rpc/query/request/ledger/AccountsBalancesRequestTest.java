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
                    "xrb_3jwrszth46rk1mu7rmb4rhm54us8yg1gw3ipodftqtikf5yqdyr7471nsg1k",
                    "xrb_3jybgajxebuj9kby3xusmn4sqiomzu15trmkwb1xyrynnc7axss3qp1yn679"));
    
        System.out.println(response.getBalance("xrb_3jwrszth46rk1mu7rmb4rhm54us8yg1gw3ipodftqtikf5yqdyr7471nsg1k").getTotal());
        System.out.println(response.getBalance("xrb_3jwrszth46rk1mu7rmb4rhm54us8yg1gw3ipodftqtikf5yqdyr7471nsg1k").getPocketed());
        
        assertNotNull(response.getBalance("xrb_3jwrszth46rk1mu7rmb4rhm54us8yg1gw3ipodftqtikf5yqdyr7471nsg1k"));
        assertNotNull(response.getBalance("xrb_3jybgajxebuj9kby3xusmn4sqiomzu15trmkwb1xyrynnc7axss3qp1yn679"));
    
        assertNotNull(response.getBalance("xrb_3jwrszth46rk1mu7rmb4rhm54us8yg1gw3ipodftqtikf5yqdyr7471nsg1k").getPocketed());
        assertNotNull(response.getBalance("xrb_3jwrszth46rk1mu7rmb4rhm54us8yg1gw3ipodftqtikf5yqdyr7471nsg1k").getPending());
        
        //Test empty
        response = query(new AccountsBalancesRequest());
        assertNotNull(response.getBalances());
        assertTrue(response.getBalances().isEmpty());
        
    }
    
}