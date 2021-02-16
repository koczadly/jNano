/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.*;

/**
 * @author Karl Oczadly
 */
public class WorkCacheTest {
    
    @Test
    public void testCacheGet() {
        HexData root1 = new HexData("60B420BB3851D9D47ACB933DBE70399BF6C92DA33AF01D4FB770E98C0325F41D");
        WorkSolution work1 = new WorkSolution("7a3388034b63c165");
        HexData root2 = new HexData("3EBAF8986DA712C82BCD4D554BF0B54023C29B624DE9EF9C2F931EFC580F9AFB");
        WorkDifficulty diff1 = new WorkDifficulty("fffffe0000000000");
        WorkDifficulty diff2 = new WorkDifficulty("fffffffff0000000");
        
        WorkCache cache = new WorkCache(5);
        cache.store(root1, work1);
        
        // Root 1
        assertTrue(cache.get(root1, WorkDifficulty.MIN_VALUE).isPresent());
        assertTrue(cache.get(root1, diff1).isPresent());
        assertFalse(cache.get(root1, diff2).isPresent());
        assertEquals(work1, cache.get(root1, WorkDifficulty.MIN_VALUE).get());
        
        // Root 2 (not stored)
        assertFalse(cache.get(root2, WorkDifficulty.MIN_VALUE).isPresent());
        assertFalse(cache.get(root2, diff1).isPresent());
        assertFalse(cache.get(root2, diff2).isPresent());
    }
    
    @Test
    public void testHigherDiffOverwrite() {
        // Ensure the cache prefers the higher difficulty work
        HexData root = new HexData("60B420BB3851D9D47ACB933DBE70399BF6C92DA33AF01D4FB770E98C0325F41D");
        WorkSolution work1 = new WorkSolution("7a3388034b63c165"); // Lower diff
        WorkSolution work2 = new WorkSolution("b11ebc4f400a0e67"); // Higher diff
        
        WorkCache cache = new WorkCache(5);
        cache.store(root, work1);
        cache.store(root, work2);
        cache.store(root, work1);
        
        assertEquals(1, cache.getCacheSize());
        assertEquals(work2, cache.get(root, WorkDifficulty.MIN_VALUE).get());
    }
    
    @Test
    public void testCacheLimit() {
        WorkCache cache = new WorkCache(2);
        
        cache.store(new HexData("60B420BB3851D9D47ACB933DBE70399BF6C92DA33AF01D4FB770E98C0325F41D"),
                new WorkSolution("7a3388034b63c165"));
        cache.store(new HexData("3EBAF8986DA712C82BCD4D554BF0B54023C29B624DE9EF9C2F931EFC580F9AFB"),
                new WorkSolution("e9bfc25aaf7f7f7a"));
        cache.store(new HexData("081B12E107B1E805F2B4F5F0F1D00C2D0F62634670921C505867FF20F6A8335E"),
                new WorkSolution("7ab1268112920c1d"));
        
        assertEquals(2, cache.getCacheSize());
    }
    
}