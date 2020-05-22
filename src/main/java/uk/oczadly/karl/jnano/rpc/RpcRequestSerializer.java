package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.internal.JNanoHelper;

public class RpcRequestSerializer {

    public String serialize(RpcRequest<?> request) {
        return JNanoHelper.GSON.toJson(request);
    }

}
