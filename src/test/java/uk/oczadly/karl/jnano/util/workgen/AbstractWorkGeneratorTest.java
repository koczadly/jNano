/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.workgen.policy.ConstantDifficultyPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author Karl Oczadly
 */
public class AbstractWorkGeneratorTest {
    
    @Test
    public void testQueue() throws Exception {
        TestGenerator generator = new TestGenerator();
    
        // Send to process (async)
        Future<GeneratedWork> req1 = generator.generate(TestConstants.randHash());
        Future<GeneratedWork> req2 = generator.generate(TestConstants.randHash());
        Future<GeneratedWork> req3 = generator.generate(TestConstants.randHash());
        
        // Wait for results
        WorkSolution work1 = req1.get().getWork();
        WorkSolution work2 = req2.get().getWork();
        WorkSolution work3 = req3.get().getWork();
    
        // Compare
        assertSame(generator.requestLog.get(0).result, work1);
        assertSame(generator.requestLog.get(1).result, work2);
        assertSame(generator.requestLog.get(2).result, work3);
    }
    
    @Test
    public void testGeneration() throws Exception {
        TestGenerator generator = new TestGenerator();
        
        HexData root1 = TestConstants.randHash();
        HexData root2 = TestConstants.randHash();
        GeneratedWork work1 = generator.generate(root1).get();                          // Standard policy
        GeneratedWork work2 = generator.generate(root2, new WorkDifficulty(420)).get(); // Specified difficulty
        
        // Assert requests
        assertEquals(generator.requestLog.get(0).root, root1);                              // Root hash
        assertEquals(generator.requestLog.get(0).difficulty, new WorkDifficulty(1337));     // Difficulty (policy)
        assertSame(generator.requestLog.get(0).result, work1.getWork());                    // Work
        
        assertEquals(generator.requestLog.get(1).root, root2);                              // Root hash
        assertEquals(generator.requestLog.get(1).difficulty, new WorkDifficulty(420));      // Difficulty (manual)
        assertSame(generator.requestLog.get(1).result, work2.getWork());                    // Work
    
    }
    
    
    
    static class TestGenerator extends AbstractWorkGenerator {
        final List<Request> requestLog = new ArrayList<>();
        
        protected TestGenerator() {
            super(new ConstantDifficultyPolicy(new WorkDifficulty(1337)));
        }
    
        @Override
        protected WorkSolution generateWork(HexData root, WorkDifficulty difficulty, RequestContext context) {
            WorkSolution work = new WorkSolution(0);
            requestLog.add(new Request(root, difficulty, work));
            return work;
        }
        
        static class Request {
            final HexData root;
            final WorkDifficulty difficulty;
            final WorkSolution result;
    
            public Request(HexData root, WorkDifficulty difficulty, WorkSolution result) {
                this.root = root;
                this.difficulty = difficulty;
                this.result = result;
            }
        }
    }

}