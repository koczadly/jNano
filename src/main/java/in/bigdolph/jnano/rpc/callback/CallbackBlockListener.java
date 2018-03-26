package in.bigdolph.jnano.rpc.callback;

import java.net.InetAddress;

public interface CallbackBlockListener {
    
    void onNewBlock(InetAddress nodeAddress, String context, CallbackBlock block);
    
}
