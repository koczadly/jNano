package uk.oczadly.karl.jnano.rpc.request.ledger;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountBalance;
import uk.oczadly.karl.jnano.rpc.response.ResponseBalance;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class AccountBalanceRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        ResponseBalance res = query(new RequestAccountBalance("xrb_3jwrszth46rk1mu7rmb4rhm54us8yg1gw3ipodftqtikf5yqdyr7471nsg1k"));
        
        assertNotNull(res.getPocketed());
        assertNotNull(res.getPending());
        assertNotNull(res.getTotal());
        assertEquals(res.getPocketed().add(res.getPending()), res.getTotal());
        
        System.out.println(res.getPocketed());
        System.out.println(res.getPending());
        System.out.println(res.getTotal());
    }

}