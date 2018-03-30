package in.bigdolph.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.UnitConversionResponse;

import java.math.BigInteger;

public class UnitConversionRequest extends RpcRequest<UnitConversionResponse> {
    
    @Expose
    @SerializedName("amount")
    private BigInteger amount;
    
    private final Conversion conversion;
    
    
    public UnitConversionRequest(Conversion conversion, long amount) {
        this(conversion, BigInteger.valueOf(amount));
    }
    
    public UnitConversionRequest(Conversion conversion, BigInteger amount) {
        super(conversion.name().toLowerCase(), UnitConversionResponse.class);
        this.conversion = conversion;
        this.amount = amount;
    }
    
    
    
    public BigInteger getAmount() {
        return amount;
    }
    
    public final Conversion getConversionType() {
        return conversion;
    }
    
    
    
    public enum Conversion {
        MRAI_FROM_RAW,
        MRAI_TO_RAW,
        KRAI_FROM_RAW,
        KRAI_TO_RAW,
        RAI_FROM_RAW,
        RAI_TO_RAW;
    }
    
}
