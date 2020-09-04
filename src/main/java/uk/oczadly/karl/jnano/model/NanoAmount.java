/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.util.NanoConstants;
import uk.oczadly.karl.jnano.util.NanoUnit;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * This class is used to represent a quantity of Nano, for use as a transactional amount or account balance.
 *
 * @author Karl Oczadly
 */
@JsonAdapter(NanoAmount.JsonAdapter.class)
public final class NanoAmount implements Comparable<NanoAmount> {
    
    /** A zero-value amount. */
    public static final NanoAmount ZERO = new NanoAmount(BigInteger.ZERO);
    
    /** The maximum possible balance value. */
    public static final NanoAmount MAX_VALUE = new NanoAmount(NanoConstants.MAX_BALANCE_RAW);
    
    
    private final BigInteger rawValue;
    
    
    /**
     * Creates a NanoAmount from a given {@code raw} value.
     * @param rawValue the raw value, as a string
     */
    public NanoAmount(String rawValue) {
        this(new BigInteger(rawValue));
    }
    
    /**
     * Creates a NanoAmount from a given value and unit.
     * @param value the value
     * @param unit  the unit of the value
     */
    public NanoAmount(BigInteger value, NanoUnit unit) {
        this(NanoUnit.RAW.convertFromInt(unit, value));
    }
    
    /**
     * Creates a NanoAmount from a given value and unit.
     * @param value the value
     * @param unit  the unit of the value
     */
    public NanoAmount(BigDecimal value, NanoUnit unit) {
        this(NanoUnit.RAW.convertFromInt(unit, value));
    }
    
    /**
     * Creates a NanoAmount from a given {@code raw} value.
     * @param rawValue the raw value
     */
    public NanoAmount(BigInteger rawValue) {
        if (rawValue == null)
            throw new IllegalArgumentException("Raw value cannot be null.");
        if (!JNH.isBalanceValid(rawValue))
            throw new IllegalArgumentException("Raw value is out of the possible range.");
        
        this.rawValue = rawValue;
    }
    
    
    /**
     * Returns the value of this amount in the {@code raw} unit.
     * @return the value, in raw units
     */
    public BigInteger getAsRaw() {
        return rawValue;
    }
    
    /**
     * Returns the value of this amount in the standard base unit.
     * @return the value, in the base unit
     */
    public BigDecimal getAsNano() {
        return getAs(NanoUnit.BASE_UNIT);
    }
    
    /**
     * Returns the value of this amount in the provided unit.
     * @param unit the unit to convert to
     * @return the value, in the given unit
     */
    public BigDecimal getAs(NanoUnit unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null.");
        
        return unit.convertFrom(NanoUnit.RAW, rawValue);
    }
    
    
    @Override
    public String toString() {
        return NanoUnit.toFriendlyString(rawValue);
    }
    
    /**
     * @return this value in raw, as a string
     */
    public String toRawString() {
        return rawValue.toString();
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NanoAmount)) return false;
        NanoAmount that = (NanoAmount)o;
        return Objects.equals(rawValue, that.rawValue);
    }
    
    @Override
    public int hashCode() {
        return rawValue.hashCode();
    }
    
    @Override
    public int compareTo(NanoAmount o) {
        return rawValue.compareTo(o.rawValue);
    }
    
    
    static class JsonAdapter implements JsonSerializer<NanoAmount>, JsonDeserializer<NanoAmount> {
        @Override
        public NanoAmount deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new NanoAmount(json.getAsString());
        }
        
        @Override
        public JsonElement serialize(NanoAmount src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.rawValue.toString());
        }
    }
    
}
