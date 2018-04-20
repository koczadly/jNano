package uk.oczadly.karl.jnano.callback;

import java.net.InetAddress;

public interface BlockCallbackListener {
    
    /**
     * When registered to a callback server, this method will be executed with every new block arrival.
     *
     * @param block     the block's information and contents
     * @param target    the HTTP target address
     * @param node      the IP address of the node that issued the callback
     */
    void onNewBlock(BlockInfo block, String target, InetAddress node);
    
}
