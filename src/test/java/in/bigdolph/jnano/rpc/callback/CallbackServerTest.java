package in.bigdolph.jnano.rpc.callback;

import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;

public class CallbackServerTest {

    private CallbackServer server;
    
    public CallbackServerTest() throws Exception {
        this.server = new CallbackServer(8080);
    }
    
    
    
    @Test @Ignore
    public void test() throws Exception {
        server.registerListener(new TestListener());
        server.start();
        while(true); //Keep main thread alive
    }
    
    
    
    public class TestListener implements CallbackBlockListener {
        
        @Override
        public void onNewBlock(InetAddress nodeAddress, String context, CallbackBlock block) {
            System.out.println("New block received: " + block.getBlockHash()); //Outputs the block hash
            System.out.println("Type: " + block.getBlock().getType().toString()); //Outputs block type
            if(block.getTransactionalAmount() != null) {
                System.out.println("Value: " + block.getTransactionalAmount().toString()); //Outputs associated value
            }
        }
        
    }

}