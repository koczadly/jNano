package in.bigdolph.jnano.rpc.query;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.net.MalformedURLException;


public abstract class QueryBaseTest {
    
    public static final String TEST_ACCOUNT =       "xrb_365md53cciaquuerrp97psfrsg1bd4rw5d8cyfpyefgofsopiuzozibdnbmx";
    public static final String TEST_ACCOUNT_BLANK = "xrb_3jxpteeg47a8ofi5h91mazey9ggeas3xais387rejb5413gaicbax9bq9mgr";
    
    public static final String TEST_WALLET =        "69F429CC16E115E0F6BE5698A3AAAA57318A967F45FC50A5E18E1304ED4D2D1C";
    
    public static final String TEST_BLOCK_SEND =    "652BA0461666145EEC3375389E85EE840B6C75CCB47EACB0F4DEDBD46F6BEB3C";
    public static final String TEST_BLOCK_RECEIVE = "85D771106D26D73B4C804E44BC9CFC158D147DF8D6F4B65B09FC046E353C6CB6";
    public static final String TEST_BLOCK_OPEN =    "A1F42D1995AAACC49E8BEDBB451D56AEC269A843D259DB36E977ED3326372D2F";
    public static final String TEST_BLOCK_CHANGE =  "0680AEFDAA578F6935FEFFAE4020D44F075B5D330F63C9D26F6968EDB6569A47";
    
    private TestNode node;
    
    
    @Before
    public void setUp() throws MalformedURLException {
        this.node = new TestNode();
    }
    
    
    
    public TestNode getNode() {
        return node;
    }
    
    
    public <R extends RPCResponse> R query(RPCRequest<R> req) {
        try {
            R response = this.node.processRequest(req);
            Assert.assertNotNull(response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public <R extends RPCResponse> R query(String responseJson, Class<R> responseClass) {
        return this.node.deserializeResponseFromJSON(responseJson, responseClass);
    }
    
}