package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountHistoryResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountHistoryRequestTest extends QueryBaseTest {
    
    @Test
    public void test() {
        AccountHistoryResponse response = query(new AccountHistoryRequest("xrb_3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xtoncuohr3", 1000));
        assertNotNull(response.getHistory());
        
        assertEquals(42, response.getHistory().length);
        
        for(AccountHistoryResponse.AccountHistory history : response.getHistory()) {
            assertNotNull(history.getBlockHash());
            assertNotNull(history.getBlockType());
            assertNotNull(history.getAccountAddress());
            assertNotNull(history.getAmount());
        }
        
        
        //Test empty account
        response = query(new AccountHistoryRequest("xrb_3jxpteeg47a8ofi5h91mazey9ggeas3xais387rejb5413gaicbax9bq9mgr", 0));
        assertNotNull(response.getHistory());
        assertEquals(0, response.getHistory().length);
    }
    
}