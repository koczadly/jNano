package uk.oczadly.karl.jnano.model.work;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * This class represents a proof-of-work solution.
 */
@JsonAdapter(WorkSolution.WorkSolutionJsonAdapter.class)
public class WorkSolution {

    private final long longVal;
    private final String hexVal;
    
    public WorkSolution(String hexVal) {
        this(Long.parseUnsignedLong(hexVal.startsWith("0x") ? hexVal.substring(2) : hexVal, 16));
    }
    
    public WorkSolution(long longVal) {
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
        if (!(o instanceof WorkSolution)) return false;
        WorkSolution that = (WorkSolution)o;
        return longVal == that.longVal;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(longVal);
    }
    
    
    static class WorkSolutionJsonAdapter implements JsonSerializer<WorkSolution>,
            JsonDeserializer<WorkSolution> {
        @Override
        public WorkSolution deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new WorkSolution(json.getAsString());
        }
        
        @Override
        public JsonElement serialize(WorkSolution src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getAsHexadecimal());
        }
    }
    
}
