package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.generic.BalanceResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountBalanceRequestTest extends QueryBaseTest {
    
    @Test
    public void test() {
        BalanceResponse res = query(new AccountBalanceRequest("xrb_3jwrszth46rk1mu7rmb4rhm54us8yg1gw3ipodftqtikf5yqdyr7471nsg1k"));
        
        assertNotNull(res.getPocketed());
        assertNotNull(res.getPending());
        assertNotNull(res.getTotal());
        assertEquals(res.getPocketed().add(res.getPending()), res.getTotal());
        
        System.out.println(res.getPocketed());
        System.out.println(res.getPending());
        System.out.println(res.getTotal());
    }

}