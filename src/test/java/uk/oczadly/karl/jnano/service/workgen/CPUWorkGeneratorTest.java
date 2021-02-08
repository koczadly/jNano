/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.service.workgen;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

import static org.junit.Assert.fail;

/**
 * @author Karl Oczadly
 */
public class CPUWorkGeneratorTest {
    
    @Test
    public void testGeneration() throws Exception {
        WorkGenerator workGen = new CPUWorkGenerator();
        WorkDifficulty threshold = new WorkDifficulty("fff0000000000000");
        HexData root = TestConstants.randHash();
    
        System.out.println("Generating 2500 works...");
        
        for (int i = 0; i < 2500; i++) {
            GeneratedWork work = workGen.generate(root, threshold).get();
            if (work.getDifficulty().compareTo(threshold) < 0)
                fail("Generated work was below the requested threshold.");
        }
    }
    
}