package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHashes;

/**
 * This request class is used to request a list of active election roots.
 * <br>Calls the RPC command {@code confirmation_active}, and returns a {@link ResponseBlockHashes} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#confirmation_active">Official RPC documentation</a>
 */
public class RequestConfirmationActive extends RpcRequest<ResponseBlockHashes> {
    
    @Expose @SerializedName("announcements")
    private final Integer announcements;
    
    
    public RequestConfirmationActive() {
        this(null);
    }
    
    /**
     * @param announcements the minimum announcement threshold
     */
    public RequestConfirmationActive(Integer announcements) {
        super("confirmation_active", ResponseBlockHashes.class);
        this.announcements = announcements;
    }
    
    
    /**
     * @return the requested announcement threshold
     */
    public Integer getAnnouncements() {
        return announcements;
    }
    
}
