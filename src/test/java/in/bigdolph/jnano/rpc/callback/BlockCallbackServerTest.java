package in.bigdolph.jnano.rpc.callback;

import in.bigdolph.jnano.rpc.callback.server.BlockCallbackServer;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;

public class BlockCallbackServerTest {

    private BlockCallbackServer server;
    
    public BlockCallbackServerTest() throws Exception {
        this.server = new BlockCallbackServer(8080);
    }
    
    
    
    @Test @Ignore
    public void test() throws Exception {
        server.registerListener(new TestListener());
        server.start();
        while(true); //Keep main thread alive
    }
    
    
    
    public class TestListener implements BlockCallbackListener {
        
        @Override
        public void onNewBlock(BlockInfo block, String target, InetAddress node) {
            System.out.println("New block received: " + block.getBlockHash()); //Outputs the block hash
            System.out.println("Type: " + block.getBlock().getType().toString()); //Outputs block type
            if(block.getTransactionalAmount() != null) {
                System.out.println("Value: " + block.getTransactionalAmount().toString()); //Outputs associated value
            }
        }
        
    }

}