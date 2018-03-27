package in.bigdolph.jnano.rpc.callback;

import java.net.InetAddress;

public interface BlockCallbackListener {
    
    void onNewBlock(BlockInfo block, String target, InetAddress node);
    
}
