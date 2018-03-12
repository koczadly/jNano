package in.bigdolph.jnano.rpc.query.request.wallet;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.generic.AccountsResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountsCreateRequestTest extends QueryBaseTest {
    
    @Test
    public void test() {
        AccountsResponse res = query(new AccountsCreateRequest(TEST_WALLET, 3, false));
        
        assertNotNull(res.getAccounts());
        assertEquals(3, res.getAccounts().size());
    }
    
}