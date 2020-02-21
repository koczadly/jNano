package uk.oczadly.karl.jnano.callback;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.tests.NodeTests;
import uk.oczadly.karl.jnano.util.CurrencyDivisor;

import java.math.BigDecimal;
import java.net.InetAddress;

public class BlockCallbackServerTest {

    private BlockCallbackServer server;
    
    public BlockCallbackServerTest() throws Exception {
        this.server = new BlockCallbackServer(8080);
    }
    
    
    
    @Test
    @Category(NodeTests.class)
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
        public void onNewBlock(BlockData block, String target, InetAddress node) {
            System.out.println("New block received: " + block.getBlockHash()); //Outputs the block hash
            System.out.println("Type: " + block.getBlockContents().getType().toString()); //Outputs block type
            if(block.getTransactionalAmount() != null) {
                BigDecimal value = CurrencyDivisor.BASE_UNIT.convert(new BigDecimal(block.getTransactionalAmount()), CurrencyDivisor.RAW);
                System.out.println("Value: " + value.toPlainString() + " " + CurrencyDivisor.BASE_UNIT.getDisplayName()); //Outputs associated value
            }
            System.out.println("Node IP: " + node.getHostAddress() + ", target: " + target);
            System.out.println("");
        }
        
    }

}