package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountHistoryResponse;
import in.bigdolph.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class AccountHistoryRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        AccountHistoryResponse response = query(new AccountHistoryRequest(TEST_ACCOUNT, 1000));
        assertNotNull(response.getHistory());
        
        assertEquals(1, response.getHistory().size());
        
        for(AccountHistoryResponse.AccountHistory history : response.getHistory()) {
            assertNotNull(history.getBlockHash());
            assertNotNull(history.getBlockType());
            assertNotNull(history.getAccountAddress());
            assertNotNull(history.getAmount());
        }
        
        
        //Test empty account
        response = query(new AccountHistoryRequest(TEST_ACCOUNT, 0));
        assertNotNull(response.getHistory());
        assertEquals(0, response.getHistory().size());
    }
    
}