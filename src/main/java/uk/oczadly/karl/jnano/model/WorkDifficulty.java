package uk.oczadly.karl.jnano.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * This class represents an absolute work difficulty.
 */
@JsonAdapter(WorkDifficulty.WorkDifficultyJsonAdapter.class)
public final class WorkDifficulty implements Comparable<WorkDifficulty> {
    
    private static final BigInteger BIGINT_2_64 = new BigInteger("18446744073709551616"); // 2^64
    
    public static final WorkDifficulty MAX_VALUE = new WorkDifficulty("ffffffffffffffff");
    
    private final BigInteger intVal;
    private final String hexVal;
    
    /**
     * @param hexVal the hexadecimal absolute work difficulty value
     */
    public WorkDifficulty(String hexVal) {
        this(new BigInteger(hexVal, 16));
    }
    
    /**
     * @param intVal the absolute work difficulty value as an integer
     */
    public WorkDifficulty(BigInteger intVal) {
        if (intVal.compareTo(BigInteger.ZERO) < 0)
            throw new IllegalArgumentException("Absolute work difficulty value cannot be negative.");
        if (MAX_VALUE != null && intVal.compareTo(MAX_VALUE.intVal) > 0)
            throw new IllegalArgumentException("Absolute work difficulty value is above the maximum possible value.");
        
        this.intVal = intVal;
        this.hexVal = intVal.toString(16);
    }
    
    
    public BigInteger getAsInteger() {
        return intVal;
    }
    
    public String getAsHexadecimal() {
        return hexVal;
    }
    
    @Override
    public String toString() {
        return getAsHexadecimal();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkDifficulty that = (WorkDifficulty)o;
        return Objects.equals(intVal, that.intVal);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(intVal);
    }
    
    /**
     * {@inheritDoc}
     * @return -1, 0 or 1 as this WorkDifficulty is numerically less than, equal to, or greater than val.
     */
    @Override
    public int compareTo(WorkDifficulty val) {
        return intVal.compareTo(val.intVal);
    }
    
    /**
     * Calculates the difficulty multiplier between the given base difficulty value and this difficulty.
     * @param baseDifficulty the base difficulty value to calculate from
     * @return the difficulty multiplier
     */
    public double calculateMultiplier(WorkDifficulty baseDifficulty) {
        if (baseDifficulty == null)
            throw new IllegalArgumentException("Minimum difficulty argument cannot be null.");
        
        return new BigDecimal(BIGINT_2_64.subtract(baseDifficulty.intVal))
                .divide(new BigDecimal(BIGINT_2_64.subtract(this.intVal)), 12, RoundingMode.HALF_UP)
                .doubleValue();
    }
    
    /**
     * Multiplies the difficulty by the given multiplier, or returns the {@link #MAX_VALUE} if the value overflows.
     * @param multiplier the multiplier
     * @return the calculated absolute difficulty
     */
    public WorkDifficulty multiply(double multiplier) {
        return multiply(new BigDecimal(multiplier));
    }
    
    /**
     * Multiplies the difficulty by the given multiplier, or returns the {@link #MAX_VALUE} if the value overflows.
     * @param multiplier the multiplier
     * @return the calculated absolute difficulty
     */
    public WorkDifficulty multiply(BigDecimal multiplier) {
        if (multiplier == null)
            throw new IllegalArgumentException("Multiplier argument cannot be null.");
        if (multiplier.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Multiplier argument cannot be negative.");
        
        if (multiplier.equals(BigDecimal.ZERO))
            return new WorkDifficulty(BigInteger.ZERO);
        
        BigInteger newDiff = BIGINT_2_64.subtract(
                new BigDecimal(BIGINT_2_64.subtract(this.intVal))
                .divide(multiplier, 0, RoundingMode.FLOOR)
                .toBigInteger());
        
        return newDiff.compareTo(MAX_VALUE.intVal) >= 0 ? MAX_VALUE : new WorkDifficulty(newDiff);
    }
    
    
    
    static class WorkDifficultyJsonAdapter implements JsonSerializer<WorkDifficulty>,
            JsonDeserializer<WorkDifficulty> {
        @Override
        public WorkDifficulty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new WorkDifficulty(json.getAsString());
        }
    
        @Override
        public JsonElement serialize(WorkDifficulty src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getAsHexadecimal());
        }
    }
    
}
