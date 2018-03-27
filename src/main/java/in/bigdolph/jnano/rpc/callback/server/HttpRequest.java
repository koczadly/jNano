package in.bigdolph.jnano.rpc.callback.server;

import java.net.InetAddress;

public class HttpRequest {
    
    private InetAddress clientAddr;
    private final String path;
    private final int contentLength;
    private final String body;
    
    public HttpRequest(InetAddress clientAddr, String path, int contentLength, String body) {
        this.clientAddr = clientAddr;
        this.path = path;
        this.contentLength = contentLength;
        this.body = body;
    }
    
    
    
    public InetAddress getClientAddr() {
        return clientAddr;
    }
    
    public String getPath() {
        return path;
    }
    
    public int getContentLength() {
        return contentLength;
    }
    
    public String getBody() {
        return body;
    }
    
}
