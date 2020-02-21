package uk.oczadly.karl.jnano.rpc.request.compute;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.request.conversion.RequestConvertUnits;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;
import uk.oczadly.karl.jnano.tests.NodeTests;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class RequestConvertUnitsTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        //1 MRAI to RAW
        ResponseAmount res = query(new RequestConvertUnits(RequestConvertUnits.NanoDenomination.MRAI_TO_RAW, BigInteger.ONE));
        assertEquals("1000000000000000000000000000000", res.getConvertedAmount().toString());
    
        //1000000000000000000000000000000 RAW to MRAI
        res = query(new RequestConvertUnits(RequestConvertUnits.NanoDenomination.MRAI_FROM_RAW, new BigInteger("1000000000000000000000000000000")));
        assertEquals("1", res.getConvertedAmount().toString());
    }
    
}