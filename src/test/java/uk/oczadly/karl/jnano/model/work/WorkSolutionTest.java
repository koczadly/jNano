package uk.oczadly.karl.jnano.model.work;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorkSolutionTest {

    @Test
    public void testDiffCalculation() {
        WorkSolution solution = new WorkSolution("6277aa854f897e6f");
        assertEquals(new WorkDifficulty("fffffff4bc8f0cbf"),
                solution.calculateDifficulty("133D48F43EC826CF0B66C78B4B3DDF0D8E57550B0F6119186DB4CB1B5D8ACC35"));
    }
    
}