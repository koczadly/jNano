package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.generic.AccountResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockAccountRequestTest extends QueryBaseTest {
    
    @Test
    public void test() {
        AccountResponse res = query(new BlockAccountRequest(TEST_BLOCK_SEND));
        assertEquals("xrb_3jybgajxebuj9kby3xusmn4sqiomzu15trmkwb1xyrynnc7axss3qp1yn679", res.getAccountAddress());
    }
    
}