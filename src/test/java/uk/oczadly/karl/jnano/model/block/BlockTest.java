/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.*;

/**
 * @author Karl Oczadly
 */
public class BlockTest {
    
    @Test
    public void testHash() {
        Block block = new MockBlock();
        assertEquals("11C0E79B71C3976CCD0C02D1310E2516C08EDC9D8B6F57CCD680D63A4D8E72DA", block.getHash().toHexString());
    }
    
    @Test
    public void testFields() {
        final String SIG = TestConstants.randHex(128);
        final WorkSolution WORK = new WorkSolution(TestConstants.RANDOM.nextLong());
        Block block = new MockBlock(SIG, WORK);
        assertEquals(SIG, block.getSignature().toHexString());
        assertEquals(WORK, block.getWorkSolution());
        assertEquals("test", block.getTypeString());
        assertNull(block.getType());
        assertTrue(block.isComplete());
    }
    
    @Test
    public void testParse() {
        // Valid block (change)
        final String JSON = "{\"type\": \"change\",\n" +
                "    \"previous\": \"279C791486FF3796BDEDF1B85A5349C172B4892F50BA22016C44F836BDC95993\",\n" +
                "    \"representative\": \"nano_1asau6gr8ft5ykynpkauctrq1w37sdasdymuigtxotim6kxoa3rgn3dpenis\",\n" +
                "    \"work\": \"9bbddcc234c0e623\",\n" +
                "    \"signature\": \"1D1A887AECC4A1C581D5CFC64E60B1AFAA6F820EB60FB581FE23D0A2C8AD5DC948311F2E872BC83" +
                "B6D253BDA7FEC4905E0E724006A7E2E4F5A2A6CB671695B09\"}";
        Block parsed = Block.parse(JSON);
        assertNotNull(parsed);
        assertEquals(ChangeBlock.class, parsed.getClass());
        
        // Invalid block (no type field)
        assertThrows(BlockDeserializer.BlockParseException.class,
                () -> Block.parse("{}"));
        // Invalid block (malformed json)
        assertThrows(BlockDeserializer.BlockParseException.class,
                () -> Block.parse("fasofioma"));
    }
    
    
    private static final class MockBlock extends Block {
        protected MockBlock() {
            this(null, null);
        }
    
        protected MockBlock(String signature, WorkSolution workSolution) {
            super("test", signature != null ? new HexData(signature) : null, workSolution);
        }
    
        @Override
        protected byte[][] hashables() {
            return new byte[][] {
                    new byte[] {1}, new byte[] {2}, new byte[] {3}
            };
        }
    
        @Override
        public BlockIntent getIntent() {
            return null;
        }
    
        @Override
        public Block clone() {
            return this;
        }
    }
    
}