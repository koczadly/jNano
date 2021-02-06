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
import uk.oczadly.karl.jnano.util.workgen.policy.ConstantDifficultyPolicyV1;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author Karl Oczadly
 */
public class WorkGeneratorTest {

    @Test
    public void testGeneration() throws Exception {
        TestGenerator generator = new TestGenerator(new ConstantDifficultyPolicyV1(new WorkDifficulty(1337)));
        
        HexData root1 = TestConstants.randHash();
        HexData root2 = TestConstants.randHash();
        Future<GeneratedWork> fwork1 = generator.generate(root1);                          // Standard policy
        Future<GeneratedWork> fwork2 = generator.generate(root2, new WorkDifficulty(420)); // Specified difficulty
    
        GeneratedWork work1 = fwork1.get();
        GeneratedWork work2 = fwork2.get();
        
        // Assert results
        assertSame(generator.requests.get(0).result, work1.getWork());
        assertSame(generator.requests.get(1).result, work2.getWork());
        
        // Assert requests
        assertEquals(generator.requests.get(0).root, root1);
        assertEquals(generator.requests.get(0).difficulty, new WorkDifficulty(1337));
        assertEquals(generator.requests.get(1).root, root2);
        assertEquals(generator.requests.get(1).difficulty, new WorkDifficulty(420));
    }
    
    
    
    static class TestGenerator extends WorkGenerator {
        final List<Request> requests = new ArrayList<>();
        
        protected TestGenerator(WorkDifficultyPolicy policy) {
            super(policy);
        }
    
        @Override
        protected WorkSolution generateWork(HexData root, WorkDifficulty difficulty) {
            WorkSolution work = new WorkSolution(0);
            requests.add(new Request(root, difficulty, work));
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