/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.work;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.JNH;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * This class represents an absolute work difficulty.
 */
@JsonAdapter(WorkDifficulty.WorkDifficultyJsonAdapter.class)
public final class WorkDifficulty implements Comparable<WorkDifficulty> {
    
    /** The maximum possible work difficulty, represented as {@code ffffffffffffffff}. */
    public static final WorkDifficulty MAX_VALUE = new WorkDifficulty(-1);
    
    /** The minimum possible work difficulty ({@code 1}), represented as {@code 0000000000000001}. */
    public static final WorkDifficulty MIN_VALUE = new WorkDifficulty(1);
    
    
    private final long longVal;
    private final String hexVal;
    
    /**
     * @param hexVal the absolute work difficulty, encoded as a hexadecimal string, eg: {@code fffffff800000000}
     */
    public WorkDifficulty(String hexVal) {
        if (hexVal.length() != 16)
            throw new IllegalArgumentException("Difficulty must be a 16-character hex string.");
        this.longVal = Long.parseUnsignedLong(hexVal, 16);
        this.hexVal = hexVal.toLowerCase();
    }
    
    /**
     * @param longVal the absolute work difficulty value, encoded as an unsigned long
     */
    public WorkDifficulty(long longVal) {
        if (longVal == 0) throw new IllegalArgumentException("Work difficulty cannot be zero.");
        this.longVal = longVal;
        this.hexVal = JNH.leftPadString(Long.toHexString(longVal), 16, '0');
    }
    
    
    /**
     * @return the absolute difficulty value, encoded as an unsigned long
     */
    public long getAsLong() {
        return longVal;
    }
    
    /**
     * @return the absolute difficulty value in hexadecimal format
     */
    public String getAsHexadecimal() {
        return hexVal;
    }
    
    @Override
    public String toString() {
        return getAsHexadecimal();
    }
    
    /**
     * Returns whether or not this difficulty is equal to or above the provided threshold difficulty.
     * @param threshold the minimum threshold difficulty to compare
     * @return true if this difficulty is equal to or greater than the provided threshold
     */
    public boolean isValid(WorkDifficulty threshold) {
        return compareTo(threshold) >= 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkDifficulty that = (WorkDifficulty)o;
        return Objects.equals(longVal, that.longVal);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(longVal);
    }
    
    /**
     * {@inheritDoc}
     * @return -1, 0 or 1 as this WorkDifficulty is numerically less than, equal to, or greater than val.
     */
    @Override
    public int compareTo(WorkDifficulty val) {
        return Long.compareUnsigned(longVal, val.longVal);
    }
    
    
    /**
     * Calculates the difficulty multiplier of this difficulty compared to the provided base difficulty.
     *
     * <p>The base difficulty is the reference point that this difficulty is compared to. If this difficulty is
     * larger ("<i>more difficult</i>") than the supplied {@code baseDiff}, then the result will be larger than
     * {@code 1}.</p>
     *
     * <p>The returned difficulty multiplier is based on the required time to compute a work value. A multiplier of
     * {@code 3} would mean that on average, it statistically took 3 times longer to compute the work solution than a
     * solution at the base difficulty.</p>
     *
     * @param baseDiff the base difficulty value to calculate from
     * @return the difficulty multiplier
     */
    public double calculateMultiplier(WorkDifficulty baseDiff) {
        if (baseDiff == null) throw new IllegalArgumentException("Base difficulty cannot be null.");
        if (compareTo(baseDiff) == 0) return 1;
        return baseDiff.invertedDoubleVal() / invertedDoubleVal();
    }
    
    /**
     * Returns a {@code WorkDifficulty} which is this difficulty multiplied by the given multiplier.
     *
     * <p>The difficulty multiplier is based on the required time to compute a work value. Multiplying by {@code 3}
     * would mean that on average, it should take 3 times longer to compute the work solution.</p>
     *
     * @param multiplier the value to multiply the difficulty by
     * @return the calculated absolute difficulty
     */
    public WorkDifficulty multiply(double multiplier) {
        if (multiplier <= 0) throw new IllegalArgumentException("Difficulty multiplier must be positive.");
        if (multiplier == 1) return this;
        return new WorkDifficulty(~(long)(invertedDoubleVal() / multiplier));
    }
    
    /** Returns the value as an inverted unsigned double. */
    private double invertedDoubleVal() {
        long val = ~longVal;
        double dval = (double)(val & 0x7fffffffffffffffL);
        if (val < 0) dval += 0x1.0p63;
        return dval;
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
