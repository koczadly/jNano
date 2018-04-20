package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.tests.UtilTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(UtilTests.class)
public class WalletUtilTest {
    
    @Test
    public void generateSeed() {
        String seed = WalletUtil.generateNewSeed();
        System.out.println(seed);
        Assert.assertEquals(64, seed.length());
    }

}