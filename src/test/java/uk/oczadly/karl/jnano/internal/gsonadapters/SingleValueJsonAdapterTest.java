package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SingleValueJsonAdapterTest {
    
    Gson gson = new Gson();
    
    
    @Test
    public void testParseSingleField() {
        ContainerA a = gson.fromJson("{\"value\":69}", ContainerA.class);
        assertEquals(a.value, 69);
    }
    
    @Test
    public void testParseMultipleFields() {
        ContainerB a = gson.fromJson("{\"value\":69}", ContainerB.class);
        assertEquals(a.value, 69);
        assertEquals(a.value2, 0);
    }
    
    @Test
    public void testParseMissingKey() {
        ContainerA a = gson.fromJson("{}", ContainerA.class);
        assertEquals(a.value, 0);
    }
    
    @Test(expected = JsonParseException.class)
    public void testFailMultipleFieldsDeclared() {
        gson.fromJson("{\"value\":69}", ContainerC.class);
    }
    
    @Test(expected = JsonParseException.class)
    public void testFailMultipleKeysDeclared() {
        gson.fromJson("{\"value\":69, \"value2\":70}", ContainerA.class);
    }
    
    
    @JsonAdapter(SingleValueJsonAdapter.class)
    static class ContainerA {
        @Expose int value;
    }
    
    @JsonAdapter(SingleValueJsonAdapter.class)
    static class ContainerB {
        @Expose int value;
        int value2;
    }
    
    @JsonAdapter(SingleValueJsonAdapter.class)
    static class ContainerC {
        int value, value2;
    }
    
}