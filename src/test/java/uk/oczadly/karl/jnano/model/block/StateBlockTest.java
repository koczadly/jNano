package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class StateBlockTest {
    
    @Test
    public void testCalcHash() {
        StateBlock block = new StateBlock(null, null, null, null, null, NanoAccount.parse(
                "nano_3dmtrrws3pocycmbqwawk6xs7446qxa36fcncush4s1pejk16ksbmakis78m"),
                "90204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488",
                NanoAccount.parse("nano_34prihdxwz3u4ps8qjnn14p7ujyewkoxkwyxm3u665it8rg5rdqw84qrypzk"),
                new BigInteger("1234567"),
                "80204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488",
                null);
        
        // Hash
        assertEquals("AC762C4D4E8501026152DA37FBFB00D5A5FB55CDD85835CA4A2354717512203C",
                block.getHash());
        
        // Account link
        assertEquals(NanoAccount.parse("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"),
                block.getLinkAsAccount());
    }
    
    @Test
    public void testCalcLinkAccount() {
        StateBlock block = new StateBlock(null, null, null, null, null, NanoAccount.parse(
                "nano_3dmtrrws3pocycmbqwawk6xs7446qxa36fcncush4s1pejk16ksbmakis78m"),
                "90204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488",
                NanoAccount.parse("nano_34prihdxwz3u4ps8qjnn14p7ujyewkoxkwyxm3u665it8rg5rdqw84qrypzk"),
                new BigInteger("1234567"),
                "80204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488",
                null);
        
        // Account link
        assertEquals(NanoAccount.parse("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"),
                block.getLinkAsAccount());
    }
    
    @Test
    public void testCalcLinkData() {
        StateBlock block = new StateBlock(null, null, null, null, null, NanoAccount.parse(
                "nano_3dmtrrws3pocycmbqwawk6xs7446qxa36fcncush4s1pejk16ksbmakis78m"),
                "90204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488",
                NanoAccount.parse("nano_34prihdxwz3u4ps8qjnn14p7ujyewkoxkwyxm3u665it8rg5rdqw84qrypzk"),
                new BigInteger("1234567"),
                null,
                NanoAccount.parse("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"));
        
        // Account link
        assertEquals("80204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488", block.getLinkData());
    }
    
}