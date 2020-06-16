package uk.oczadly.karl.jnano.model.work;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * This class represents an absolute work difficulty.
 */
@JsonAdapter(WorkDifficulty.WorkDifficultyJsonAdapter.class)
public final class WorkDifficulty implements Comparable<WorkDifficulty> {
    
    /**
     * The maximum possible work difficulty, represented as {@code ffffffffffffffff}.
     */
    public static final WorkDifficulty MAX_VALUE = new WorkDifficulty(-1);
    
    /**
     * The minimum possible work difficulty (no work), represented as {@code 0000000000000000}.
     */
    public static final WorkDifficulty MIN_VALUE = new WorkDifficulty(0);
    
    
    private final long longVal;
    private final String hexVal;
    
    /**
     * @param hexVal the absolute work difficulty value, encoded as a hexadecimal string
     */
    public WorkDifficulty(String hexVal) {
        this(Long.parseUnsignedLong(hexVal.startsWith("0x") ? hexVal.substring(2) : hexVal, 16));
    }
    
    /**
     * @param longVal the absolute work difficulty value, encoded as an unsigned long
     */
    public WorkDifficulty(long longVal) {
        this.longVal = longVal;
        
        // Convert hex to 16 char length
        String hex = Long.toHexString(longVal);
        StringBuilder sb = new StringBuilder(16);
        for (int i=0; i<(16-hex.length()); i++)
            sb.append('0');
        sb.append(hex);
        this.hexVal = sb.toString();
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
     * Calculates the difficulty multiplier between the given base difficulty value and this difficulty.
     * @param baseDifficulty the base difficulty value to calculate from
     * @return the difficulty multiplier
     */
    public double calculateMultiplier(WorkDifficulty baseDifficulty) {
        if (baseDifficulty == null)
            throw new IllegalArgumentException("Base difficulty cannot be null.");
        
        if (longVal == baseDifficulty.longVal) return 1;
        return (double)(~baseDifficulty.longVal) / (~longVal);
    }
    
    /**
     * Multiplies this difficulty by the given multiplier.
     * @param multiplier the value to multiply the difficulty by
     * @return the calculated absolute difficulty
     */
    public WorkDifficulty multiply(double multiplier) {
        if (multiplier < 0)
            throw new IllegalArgumentException("Difficulty multiplier cannot be negative.");
        
        if (multiplier == 0) return MIN_VALUE;
        if (multiplier == 1) return this;
        return new WorkDifficulty(~((long)((~longVal) / multiplier)));
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
