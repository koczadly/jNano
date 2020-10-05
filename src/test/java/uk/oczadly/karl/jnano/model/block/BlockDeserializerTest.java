/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import org.junit.Test;
import uk.oczadly.karl.jnano.TestBlock;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.*;

/**
 * @author Karl Oczadly
 */
public class BlockDeserializerTest {
    
    @Test
    public void testRegister() {
        // From BlockType
        BlockDeserializer des1 = BlockDeserializer.withNone();
        des1.registerDeserializer(BlockType.STATE);
        assertSame(BlockType.STATE.getDeserializerFunction(), des1.getDeserializer(BlockType.STATE));
        assertSame(BlockType.STATE.getDeserializerFunction(), des1.getDeserializer("state"));
        assertSame(BlockType.STATE.getDeserializerFunction(), des1.getDeserializer("utx"));
        // From name
        BlockDeserializer des2 = BlockDeserializer.withNone();
        des2.registerDeserializer("slug", TestBlock.DESERIALIZER);
        assertSame(TestBlock.DESERIALIZER, des2.getDeserializer("slug"));
        // Unregistered
        BlockDeserializer des3 = BlockDeserializer.withNone();
        assertNull(des3.getDeserializer(BlockType.STATE));
        assertNull(des3.getDeserializer("slug"));
    }
    
    @Test
    public void testDeserialize() {
        final String SIG = TestConstants.randHex(128);
        final WorkSolution WORK = new WorkSolution(TestConstants.RANDOM.nextLong());
        final String VAL = "12345";
        
        BlockDeserializer des = BlockDeserializer.withNone();
        des.registerDeserializer("test", TestBlock.DESERIALIZER);
        // Valid block
        JsonObject json1 = new JsonObject();
        json1.addProperty("type", "test");
        json1.addProperty("signature", SIG);
        json1.addProperty("work", WORK.getAsHexadecimal());
        json1.addProperty("val", VAL);
        Block block1 = des.deserialize(json1);
        assertNotNull(block1);
        assertTrue(block1 instanceof TestBlock);
        TestBlock tb = (TestBlock)block1;
        assertEquals(SIG, tb.getSignature().toHexString());
        assertEquals(WORK, tb.getWorkSolution());
        assertEquals(VAL, tb.getVal());
        // Invalid block type
        JsonObject json2 = new JsonObject();
        json2.addProperty("type", "slug");
        json2.addProperty("signature", SIG);
        json2.addProperty("work", WORK.getAsHexadecimal());
        json2.addProperty("val", VAL);
        assertThrows(BlockDeserializer.BlockParseException.class, () -> des.deserialize(json2));
        // Empty json
        assertThrows(BlockDeserializer.BlockParseException.class, () -> des.deserialize(new JsonObject()));
    }
    
}