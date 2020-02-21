package uk.oczadly.karl.jnano.rpc.request.compute;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkValidate;
import uk.oczadly.karl.jnano.rpc.response.ResponseValidation;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class WorkValidateRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        ResponseValidation res = query(new RequestWorkValidate("2bf29ef00786a6bc", "718CC2121C3E641059BC1C2CFC45666C99E8AE922F7A807B7D07B62C995D79E2"));
        assertTrue(res.isValid());
    
        res = query(new RequestWorkValidate("2bf29ef00786a6bd", "652BA0461666145EEC3375389E85EE840B6C75CCB47EACB0F4DEDBD46F6BEB3C"));
        assertFalse(res.isValid());
    }
    
}