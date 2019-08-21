package uk.oczadly.karl.jnano.rpc.request.conversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;
import uk.oczadly.karl.jnano.util.CurrencyDivisor;

import java.math.BigInteger;

/**
 * This request class is used to convert between the various currency units.
 *
 * @deprecated Use native {@link CurrencyDivisor} utility class instead for efficiency
 */
@Deprecated
public class RequestConvertUnits extends RpcRequest<ResponseAmount> {
    
    @Expose
    @SerializedName("amount")
    private BigInteger amount;
    
    private final NanoDenomination conversion;
    
    
    /**
     * @param conversion    the units of currency to convert between
     * @param amount        the quantity of the origin currency
     */
    public RequestConvertUnits(NanoDenomination conversion, long amount) {
        this(conversion, BigInteger.valueOf(amount));
    }
    
    /**
     * @param conversion    the units of currency to convert between
     * @param amount        the quantity of the origin currency
     */
    public RequestConvertUnits(NanoDenomination conversion, BigInteger amount) {
        super(conversion.name().toLowerCase(), ResponseAmount.class);
        this.conversion = conversion;
        this.amount = amount;
    }
    
    
    
    public BigInteger getAmount() {
        return amount;
    }
    
    public final NanoDenomination getConversionType() {
        return conversion;
    }
    
    
    
    public enum NanoDenomination {
        MRAI_FROM_RAW,
        MRAI_TO_RAW,
        KRAI_FROM_RAW,
        KRAI_TO_RAW,
        RAI_FROM_RAW,
        RAI_TO_RAW;
    }
    
}
