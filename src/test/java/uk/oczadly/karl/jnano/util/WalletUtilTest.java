package uk.oczadly.karl.jnano.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.tests.UtilTests;

@Category(UtilTests.class)
public class WalletUtilTest {
    
    @Test
    public void generateSeed() {
        String seed = WalletUtil.generateRandomSeed();
        Assert.assertEquals(64, seed.length());
    }

}