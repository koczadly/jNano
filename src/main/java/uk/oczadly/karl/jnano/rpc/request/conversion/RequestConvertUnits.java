package uk.oczadly.karl.jnano.rpc.request.conversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;
import uk.oczadly.karl.jnano.util.CurrencyDivisor;

import java.math.BigInteger;

/**
 * This request class is used to convert between the various currency units.
 * <br>Calls the appropriate RPC command, and returns a {@link ResponseAmount} data object.
 *
 * @see CurrencyDivisor
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#unit-conversion-rpcs">Official RPC documentation</a>
 *
 * @deprecated Use the native {@link CurrencyDivisor} utility class instead for efficiency
 */
@Deprecated
public class RequestConvertUnits extends RpcRequest<ResponseAmount> {
    
    @Expose @SerializedName("amount")
    private final BigInteger amount;
    
    private final Conversion conversion;
    
    
    /**
     * @param conversion    the units of currency to convert between
     * @param amount        the quantity of the origin currency
     */
    public RequestConvertUnits(Conversion conversion, long amount) {
        this(conversion, BigInteger.valueOf(amount));
    }
    
    /**
     * @param conversion    the units of currency to convert between
     * @param amount        the quantity of the origin currency
     */
    public RequestConvertUnits(Conversion conversion, BigInteger amount) {
        super(conversion.name().toLowerCase(), ResponseAmount.class);
        this.conversion = conversion;
        this.amount = amount;
    }
    
    
    /**
     * @return the requested amount
     */
    public BigInteger getAmount() {
        return amount;
    }
    
    /**
     * @return the requested conversion method
     */
    public final Conversion getConversionType() {
        return conversion;
    }
    
    
    
    public enum Conversion {
        /** Divide a raw amount down by the Mrai ratio. */
        MRAI_FROM_RAW,
    
        /** Multiply an Mrai amount by the Mrai ratio. */
        MRAI_TO_RAW,
    
        /** Divide a raw amount down by the krai ratio. */
        KRAI_FROM_RAW,
    
        /** Multiply an krai amount by the krai ratio. */
        KRAI_TO_RAW,
    
        /** Divide a raw amount down by the rai ratio. */
        RAI_FROM_RAW,
    
        /** Multiply an rai amount by the rai ratio. */
        RAI_TO_RAW
    }
    
}
