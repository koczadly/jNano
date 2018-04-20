package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import uk.oczadly.karl.jnano.rpc.query.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.query.response.AccountResponse;
import uk.oczadly.karl.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.tests.Configuration;

import static org.junit.Assert.*;

public class BlockAccountRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        AccountResponse res = query(new BlockAccountRequest(Configuration.TEST_BLOCK_SEND));
        assertEquals("xrb_3jybgajxebuj9kby3xusmn4sqiomzu15trmkwb1xyrynnc7axss3qp1yn679", res.getAccountAddress());
    }
    
}