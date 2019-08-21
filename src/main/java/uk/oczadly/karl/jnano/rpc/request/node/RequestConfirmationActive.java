package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHashes;

public class RequestConfirmationActive extends RpcRequest<ResponseBlockHashes> {
    
    @Expose @SerializedName("announcements")
    private Integer announcements;
    
    
    public RequestConfirmationActive() {
        this(null);
    }
    
    public RequestConfirmationActive(Integer announcements) {
        super("confirmation_active", ResponseBlockHashes.class);
        this.announcements = announcements;
    }
    
    
    public Integer getAnnouncements() {
        return announcements;
    }
    
}
