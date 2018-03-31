package in.bigdolph.jnano.rpc.callback;

import in.bigdolph.jnano.rpc.callback.server.BlockCallbackServer;
import in.bigdolph.jnano.tests.FunctionalityTests;
import in.bigdolph.jnano.util.CurrencyDivisor;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;
import java.net.InetAddress;

public class BlockCallbackServerTest {

    private BlockCallbackServer server;
    
    public BlockCallbackServerTest() throws Exception {
        this.server = new BlockCallbackServer(8080);
    }
    
    
    
    
    @Test
    @Category(FunctionalityTests.class)
    public void testStartStop() {
        this.server.start();
        this.server.stop();
        this.server.start();
        this.server.stop();
    }
    
    
    
    @Test @Ignore
    public void testFunction() throws Exception {
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
                BigDecimal value = CurrencyDivisor.BASE_UNIT.convert(new BigDecimal(block.getTransactionalAmount()), CurrencyDivisor.RAW);
                System.out.println("Value: " + value.toString() + " " + CurrencyDivisor.BASE_UNIT.getDisplayName()); //Outputs associated value
            }
            System.out.println("Node IP: " + node.getHostAddress() + ", target: " + target);
            System.out.println("");
        }
        
    }

}