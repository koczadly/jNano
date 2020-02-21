package uk.oczadly.karl.jnano.util;

import org.junit.Assert;
import org.junit.Test;

public class WalletUtilTest {
    
    @Test
    public void generateSeed() {
        String seed = WalletUtil.generateRandomSeed();
        Assert.assertEquals(64, seed.length());
    }

}