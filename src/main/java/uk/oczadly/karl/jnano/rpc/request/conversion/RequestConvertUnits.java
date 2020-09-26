/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.conversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;
import uk.oczadly.karl.jnano.util.NanoUnit;

import java.math.BigInteger;

/**
 * This request class is used to convert between the various currency units.
 *
 * <p>Calls the appropriate RPC command, and returns a {@link ResponseAmount} data object.</p>
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#unit-conversion-rpcs">Official RPC documentation</a>
 * @see NanoAmount
 * @see NanoUnit
 * @deprecated Use the built-in {@link NanoAmount} or {@link NanoUnit} utility classes instead for efficiency.
 */
@Deprecated
public class RequestConvertUnits extends RpcRequest<ResponseAmount> {
    
    @Expose @SerializedName("amount")
    private final BigInteger amount;
    
    private final transient Conversion conversion;
    
    
    /**
     * @param conversion the units of currency to convert between
     * @param amount     the quantity of the origin currency
     */
    public RequestConvertUnits(Conversion conversion, long amount) {
        this(conversion, BigInteger.valueOf(amount));
    }
    
    /**
     * @param conversion the units of currency to convert between
     * @param amount     the quantity of the origin currency
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
        /**
         * Divide a raw amount down by the Mrai ratio.
         */
        MRAI_FROM_RAW,
        
        /**
         * Multiply an Mrai amount by the Mrai ratio.
         */
        MRAI_TO_RAW,
        
        /**
         * Divide a raw amount down by the krai ratio.
         */
        KRAI_FROM_RAW,
        
        /**
         * Multiply an krai amount by the krai ratio.
         */
        KRAI_TO_RAW,
        
        /**
         * Divide a raw amount down by the rai ratio.
         */
        RAI_FROM_RAW,
        
        /**
         * Multiply an rai amount by the rai ratio.
         */
        RAI_TO_RAW
    }
    
}
