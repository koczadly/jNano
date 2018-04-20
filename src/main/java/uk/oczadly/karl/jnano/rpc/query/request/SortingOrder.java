package uk.oczadly.karl.jnano.rpc.query.request;

import com.google.gson.annotations.SerializedName;

public enum SortingOrder {
    
    @SerializedName("0")
    ASCENDING,
    
    @SerializedName("1")
    DESCENDING;
    
    
    public static final SortingOrder DEFAULT = ASCENDING;
    
}
