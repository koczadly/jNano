package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ArrayTypeAdapterFactoryFixTest {
    
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(new ArrayTypeAdapterFactoryFix())
            .create();
    
    private static final String JSON_TEST_EMPTY_STR = "{\"array\":\"\",\"list\":\"\",\"map\":\"\"}";
    private static final String JSON_TEST_POPULATED = "{\"array\":[\"420\", \"69\"]," +
            "\"list\":[\"421\", \"70\"],\"map\":{\"1\":\"2\",\"3\":\"4\"}}";
    
    
    @Test
    public void testContainer() {
        Container contEmpty = GSON.fromJson(JSON_TEST_EMPTY_STR, Container.class);
        assertNotNull(contEmpty.array);
        assertNotNull(contEmpty.list);
        assertNotNull(contEmpty.map);
        assertEquals(0, contEmpty.array.length);
        assertEquals(0, contEmpty.list.size());
        assertEquals(0, contEmpty.map.size());
    
        Container contPop = GSON.fromJson(JSON_TEST_POPULATED, Container.class);
        assertNotNull(contPop.array);
        assertNotNull(contPop.list);
        assertNotNull(contPop.map);
        assertArrayEquals(new Integer[] {420, 69},contPop.array);
        assertArrayEquals(new Integer[] {421, 70},contPop.list.toArray());
        assertEquals(2, contPop.map.size());
        assertEquals((Integer)2, contPop.map.get(1));
        assertEquals((Integer) 4, contPop.map.get(3));
    }
    
    
    static class Container {
        Integer[] array;
        List<Integer> list;
        Map<Integer, Integer> map;
    }
    
}