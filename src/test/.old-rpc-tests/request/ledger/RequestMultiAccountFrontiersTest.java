package uk.oczadly.karl.jnano.rpc.request.ledger;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.request.node.RequestMultiAccountFrontiers;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountFrontiers;
import uk.oczadly.karl.jnano.tests.Configuration;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class RequestMultiAccountFrontiersTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        ResponseMultiAccountFrontiers res = query(new RequestMultiAccountFrontiers(Configuration.TEST_ACCOUNT));
        assertNotNull(res.getFrontiers());
        assertEquals(1, res.getFrontiers().size());
        assertEquals("B5738AB45581CE752F90F3B921119D5AABE01ED568042317F9146B1FEB5E924C", res.getFrontierBlockHash(Configuration.TEST_ACCOUNT));
    
        res = query(new RequestMultiAccountFrontiers());
        assertNotNull(res.getFrontiers());
        assertEquals(0, res.getFrontiers().size());
    }
    
}