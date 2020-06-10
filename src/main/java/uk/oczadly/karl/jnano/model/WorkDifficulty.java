package uk.oczadly.karl.jnano.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * This class represents an absolute work difficulty.
 */
@JsonAdapter(WorkDifficulty.WorkDifficultyJsonAdapter.class)
public final class WorkDifficulty {
    
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
    
    
    public BigInteger getIntVal() {
        return intVal;
    }
    
    public String getHexVal() {
        return hexVal;
    }
    
    @Override
    public String toString() {
        return getHexVal();
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
            return new JsonPrimitive(src.getHexVal());
        }
    }
    
}
