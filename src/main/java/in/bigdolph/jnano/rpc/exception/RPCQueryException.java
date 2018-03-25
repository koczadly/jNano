package in.bigdolph.jnano.rpc.exception;

public class RPCQueryException extends Exception {
    
    public RPCQueryException() {
        super();
    }
    
    public RPCQueryException(String message) {
        super(message);
    }
    
    public RPCQueryException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RPCQueryException(Throwable cause) {
        super(cause);
    }
    
    public RPCQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
