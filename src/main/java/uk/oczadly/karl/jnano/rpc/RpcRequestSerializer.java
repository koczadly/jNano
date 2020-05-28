package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;

public class RpcRequestSerializer {

    public String serialize(RpcRequest<?> request) {
        return JNanoHelper.GSON.toJson(request);
    }

}
