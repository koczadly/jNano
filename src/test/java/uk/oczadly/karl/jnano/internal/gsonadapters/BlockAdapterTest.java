/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.*;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.*;

public class BlockAdapterTest {
    
    private Gson gson = new GsonBuilder().create();
    
    public Block deserialize(String json) {
        Block block = gson.fromJson(json, Block.class);
        assertNotNull(block);
        return block;
    }
    
    private static String encapsulateStrings(String json) {
        return "\"" + json.replaceAll("\"", "\\\\\"") + "\"";
    }
    
    
    
    @Test
    public void testSend() {
        String json = "{\"type\":\"send\",\n" +
                "\"previous\":\"700DC6DF005DF78706A2C721D3EAA3755CC5209151D4BBD7EEB1D6FF77A068F8\",\n" +
                "  \"destination\":\"nano_3deo53mkqduhn6gu55nf4jnmx8dorugsrjfnteywbedcswpsit3zz4u5urg3\",\n" +
                "  \"balance\": \"1036304679954330940296402126272\",  \n" +
                "  \"work\": \"6440a18c6061b71d\",  \n" +
                "  \"signature\": \"7DFEE7769F8BC0A72428E7898FDFDF0660A6E234559B3B284A63B14611783AEC0706FE79368C2816B" +
                "26363FD65CF900F55DB90E086EA741D718E2758B64F3406\"}";
    
        testSend(deserialize(json)); //Regular block
        // String encapsulated block
        testSend(deserialize(encapsulateStrings(json)));
    }
    
    public void testSend(Block rawBlock) {
        assertTrue(rawBlock instanceof SendBlock);
        assertEquals(BlockType.SEND, rawBlock.getType());
        
        SendBlock block = (SendBlock)rawBlock;
        assertEquals("7DFEE7769F8BC0A72428E7898FDFDF0660A6E234559B3B284A63B14611783AEC0706FE79368C2816B26363F" +
                "D65CF900F55DB90E086EA741D718E2758B64F3406", block.getSignature().toHexString());
        assertEquals(new WorkSolution("6440a18c6061b71d"), block.getWorkSolution());
        assertNotNull(block.toJsonString());
        
        assertEquals("700DC6DF005DF78706A2C721D3EAA3755CC5209151D4BBD7EEB1D6FF77A068F8",
                block.getPreviousBlockHash().toHexString());
        assertEquals("nano_3deo53mkqduhn6gu55nf4jnmx8dorugsrjfnteywbedcswpsit3zz4u5urg3",
                block.getDestinationAccount().toAddress());
        assertEquals("1036304679954330940296402126272", block.getBalance().getAsRaw().toString());
    }
    
    
    
    @Test
    public void testReceive() {
        String json = "{\n    \"type\": \"receive\",\n    \"previous\": \"CC3A488D508F816D12D20E72F04BD097C58049C6CF" +
                "88E972793BDFB5AFB5FE98\",\n    \"source\": \"4501D8473E3F1F5BD09713B9E6C0F8C3B37CB8E3C0A28D78399EF46" +
                "006A84AF2\",\n    \"work\": \"5e05b7f26d1e6563\",\n    \"signature\": \"406F6A1C5818E0A3625A24AF0A7F" +
                "DD194B5425C0836E0BE085C18044C19F1A3BBD4D98F5A4B112A38E6208211F3646CD398E437CA974BE1C43656F512693D1" +
                "02\"\n}";
        
        testReceive(deserialize(json)); // Regular block
        testReceive(deserialize(encapsulateStrings(json))); // String encapsulated block
    }
    
    public void testReceive(Block rawBlock) {
        assertTrue(rawBlock instanceof ReceiveBlock);
        assertEquals(BlockType.RECEIVE, rawBlock.getType());
    
        ReceiveBlock block = (ReceiveBlock)rawBlock;
        assertEquals("406F6A1C5818E0A3625A24AF0A7FDD194B5425C0836E0BE085C18044C19F1A3BBD4D98F5A4B112A38E6208" +
                "211F3646CD398E437CA974BE1C43656F512693D102", block.getSignature().toHexString());
        assertEquals(new WorkSolution("5e05b7f26d1e6563"), block.getWorkSolution());
        assertNotNull(block.toJsonString());
        
        assertEquals("CC3A488D508F816D12D20E72F04BD097C58049C6CF88E972793BDFB5AFB5FE98",
                block.getPreviousBlockHash().toHexString());
        assertEquals("4501D8473E3F1F5BD09713B9E6C0F8C3B37CB8E3C0A28D78399EF46006A84AF2",
                block.getSourceBlockHash().toHexString());
    }
    
    
    
    @Test
    public void testOpen() {
        String json = "{\n    \"type\": \"open\",\n    \"source\": \"78B334ADAD96EE142061121A6C40CE7FC2271257BF114" +
                "63E18ABE5989E219748\",\n    \"representative\": \"nano_3dmtrrws3pocycmbqwawk6xs7446qxa36fcncush4s1pe" +
                "jk16ksbmakis78m\",\n    \"account\": \"nano_3jybgajxebuj9kby3xusmn4sqiomzu15trmkwb1xyrynnc7axss3qp1yn" +
                "679\",\n    \"work\": \"a7cf03e595499531\",\n    \"signature\": \"0F323D7FEF67152289B288AFD9EE9CD3CD" +
                "224A3874FA8833E5E2FE60ACC500DADDADE8F37D452088B584C7DB358CA7C79B79A77A3C764ADA964B33DBF34DAB09\"\n}";
        
        testOpen(deserialize(json)); // Regular block
        testOpen(deserialize(encapsulateStrings(json))); // String encapsulated block
    }
    
    public void testOpen(Block rawBlock) {
        assertTrue(rawBlock instanceof OpenBlock);
        assertEquals(BlockType.OPEN, rawBlock.getType());
        
        OpenBlock block = (OpenBlock)rawBlock;
        assertEquals("0F323D7FEF67152289B288AFD9EE9CD3CD224A3874FA8833E5E2FE60ACC500DADDADE8F37D452088B584C" +
                "7DB358CA7C79B79A77A3C764ADA964B33DBF34DAB09", block.getSignature().toHexString());
        assertEquals(new WorkSolution("a7cf03e595499531"), block.getWorkSolution());
        assertNotNull(block.toJsonString());
        
        assertEquals("78B334ADAD96EE142061121A6C40CE7FC2271257BF11463E18ABE5989E219748",
                block.getSourceBlockHash().toHexString());
        assertEquals("nano_3jybgajxebuj9kby3xusmn4sqiomzu15trmkwb1xyrynnc7axss3qp1yn679",
                block.getAccount().toAddress());
        assertEquals("nano_3dmtrrws3pocycmbqwawk6xs7446qxa36fcncush4s1pejk16ksbmakis78m",
                block.getRepresentative().toAddress());
    }
    
    
    
    @Test
    public void testChange() {
        String json = "{\n    \"type\": \"change\",\n    \"previous\": \"D8D494F97BB0519B45B5386157DA7E736381E912A647" +
                "27522695463040371C25\",\n    \"representative\": \"nano_3pczxuorp48td8645bs3m6c3xotxd3idskrenmi65rbr" +
                "ga5zmkemzhwkaznh\",\n    \"work\": \"727335966a97f67d\",\n    \"signature\": \"F9490F5C09A2B5B99EBD" +
                "4D5F50C8095229ACFF7CD823155F14FAA6D17BC87C3EBD8B427A8F2882189D3488640BDA5221A91ED00FB7B72D089037ACA" +
                "80D30E001\"\n}";
        
        testChange(deserialize(json)); // Regular block
        testChange(deserialize(encapsulateStrings(json))); // String encapsulated block
    }
    
    public void testChange(Block rawBlock) {
        assertTrue(rawBlock instanceof ChangeBlock);
        assertEquals(BlockType.CHANGE, rawBlock.getType());
        
        ChangeBlock block = (ChangeBlock)rawBlock;
        assertEquals("F9490F5C09A2B5B99EBD4D5F50C8095229ACFF7CD823155F14FAA6D17BC87C3EBD8B427A8F2882189D34886" +
                "40BDA5221A91ED00FB7B72D089037ACA80D30E001", block.getSignature().toHexString());
        assertEquals(new WorkSolution("727335966a97f67d"), block.getWorkSolution());
        assertNotNull(block.toJsonString());
        
        assertEquals("D8D494F97BB0519B45B5386157DA7E736381E912A64727522695463040371C25",
                block.getPreviousBlockHash().toHexString());
        assertEquals("nano_3pczxuorp48td8645bs3m6c3xotxd3idskrenmi65rbrga5zmkemzhwkaznh",
                block.getRepresentative().toAddress());
    }
    
    
    @Test
    public void testState() {
        String json = "{\"type\":\"state\",\"representative\":\"nano_3rw4un6ys57hrb39sy1qx8qy5wukst1iiponztrz9qiz6" +
                "qqa55kxzx4491or\",\"link\":\"C798CFF4F1131204F65C4D22C3E6316F26F380EE0616AADBABEA1268FD75FB05\"," +
                "\"balance\":\"420\",\"previous\":\"707CAA0DBEB16C486EE37C03409D663ACE501D2985CC72ACD6903CECACF31" +
                "89C\",\"subtype\":\"send\",\"account\":\"nano_3jwrszth46rk1mu7rmb4rhm54us8yg1gw3ipodftqtikf5yqdyr74" +
                "71nsg1k\",\"hash\":\"D7B1B764399B3417BC1220C602A9608D9C883CF2064EA481E14152813F3A6B9E\",\"work\":\"b7b524ffbff9f517\",\"signature\":\"A41A889F0FF68" +
                "4A9A167F1EEDD2D389DBFA30B254A263EA1A79B438CBEFEDE2CBEDD1655AB7A2B10CC7B2ACAC94DE2AD670A64A8D314C756B" +
                "3A8241437C48702\"}";
    
        testState(deserialize(json)); // Regular block
        testState(deserialize(encapsulateStrings(json))); // String encapsulated block
    }
    
    public void testState(Block rawBlock) {
        assertTrue(rawBlock instanceof StateBlock);
        assertEquals(BlockType.STATE, rawBlock.getType());
        
        StateBlock block = (StateBlock)rawBlock;
        assertEquals("A41A889F0FF684A9A167F1EEDD2D389DBFA30B254A263EA1A79B438CBEFEDE2CBEDD1655AB7A2B10CC7B2ACAC" +
                "94DE2AD670A64A8D314C756B3A8241437C48702", block.getSignature().toHexString());
        assertEquals(new WorkSolution("b7b524ffbff9f517"), block.getWorkSolution());
        assertNotNull(block.toJsonString());
        
        assertEquals("707CAA0DBEB16C486EE37C03409D663ACE501D2985CC72ACD6903CECACF3189C",
                block.getPreviousBlockHash().toHexString());
        assertEquals("nano_3rw4un6ys57hrb39sy1qx8qy5wukst1iiponztrz9qiz6qqa55kxzx4491or",
                block.getRepresentative().toAddress());
        assertEquals("nano_3jwrszth46rk1mu7rmb4rhm54us8yg1gw3ipodftqtikf5yqdyr7471nsg1k",
                block.getAccount().toAddress());
        assertEquals("C798CFF4F1131204F65C4D22C3E6316F26F380EE0616AADBABEA1268FD75FB05",
                block.getLinkData().toHexString());
        assertEquals(NanoAmount.valueOfRaw("420"),
                block.getBalance());
        assertEquals(StateBlockSubType.SEND, block.getSubType());
    }
    
}