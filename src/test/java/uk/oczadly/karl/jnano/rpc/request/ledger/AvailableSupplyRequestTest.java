package uk.oczadly.karl.jnano.rpc.request.ledger;

import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.response.AvailableSupplyResponse;
import uk.oczadly.karl.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class AvailableSupplyRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        AvailableSupplyResponse res = query(new AvailableSupplyRequest());
        System.out.println(res.getSupply().toString());
        
        assertNotNull(res.getSupply());
        assertEquals("133248289218203497353846153999000000001", res.getSupply().toString());
    }
    
}