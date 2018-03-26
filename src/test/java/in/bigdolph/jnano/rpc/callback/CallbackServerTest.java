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
            System.out.println("NEW BLOCK FROM " + nodeAddress.getHostAddress() + " at " + context);
            System.out.println(block.getBlock().getType() + " - " + block.getBlockHash() + ": " + (block.getTransactionalAmount() == null ? "N/A" : block.getTransactionalAmount().toString()));
        }
        
    }

}