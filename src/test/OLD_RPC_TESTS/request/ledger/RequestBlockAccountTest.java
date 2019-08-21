package uk.oczadly.karl.jnano.rpc.request.ledger;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.request.node.RequestBlockAccount;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;
import uk.oczadly.karl.jnano.tests.Configuration;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class RequestBlockAccountTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        ResponseAccount res = query(new RequestBlockAccount(Configuration.TEST_BLOCK_SEND));
        assertEquals("xrb_3jybgajxebuj9kby3xusmn4sqiomzu15trmkwb1xyrynnc7axss3qp1yn679", res.getAccountAddress());
    }
    
}