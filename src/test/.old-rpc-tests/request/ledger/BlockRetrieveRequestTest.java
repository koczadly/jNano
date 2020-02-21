package uk.oczadly.karl.jnano.rpc.request.ledger;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.model.block.ChangeBlock;
import uk.oczadly.karl.jnano.model.block.OpenBlock;
import uk.oczadly.karl.jnano.model.block.ReceiveBlock;
import uk.oczadly.karl.jnano.model.block.SendBlock;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlock;
import uk.oczadly.karl.jnano.tests.Configuration;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class BlockRetrieveRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void testSendBlock() {
        ResponseBlock res = query(new BlockRetrieveRequest(Configuration.TEST_BLOCK_SEND));
        
        assertTrue(res.getBlock() instanceof SendBlock);
        SendBlock block = (SendBlock)res.getBlock();
    
        assertEquals("7DFEE7769F8BC0A72428E7898FDFDF0660A6E234559B3B284A63B14611783AEC0706FE79368C2816B26363FD65CF900F55DB90E086EA741D718E2758B64F3406", block.getSignature());
        assertEquals("6440a18c6061b71d", block.getWorkSolution());
    
        assertEquals("700DC6DF005DF78706A2C721D3EAA3755CC5209151D4BBD7EEB1D6FF77A068F8", block.getPreviousBlockHash());
        assertEquals("xrb_3deo53mkqduhn6gu55nf4jnmx8dorugsrjfnteywbedcswpsit3zz4u5urg3", block.getDestinationAccount());
        assertEquals("3928845117595383247300999000000", block.getNewBalance().toString());
    }
    
    @Test
    @Category(NodeTests.class)
    public void testReceiveBlock() {
        ResponseBlock res = query(new BlockRetrieveRequest(Configuration.TEST_BLOCK_RECEIVE));
        
        assertTrue(res.getBlock() instanceof ReceiveBlock);
        ReceiveBlock block = (ReceiveBlock)res.getBlock();
    
        assertEquals("406F6A1C5818E0A3625A24AF0A7FDD194B5425C0836E0BE085C18044C19F1A3BBD4D98F5A4B112A38E6208211F3646CD398E437CA974BE1C43656F512693D102", block.getSignature());
        assertEquals("5e05b7f26d1e6563", block.getWorkSolution());
    
        assertEquals("CC3A488D508F816D12D20E72F04BD097C58049C6CF88E972793BDFB5AFB5FE98", block.getPreviousBlockHash());
        assertEquals("4501D8473E3F1F5BD09713B9E6C0F8C3B37CB8E3C0A28D78399EF46006A84AF2", block.getSourceBlockHash());
    }
    
    @Test
    @Category(NodeTests.class)
    public void testOpenBlock() {
        ResponseBlock res = query(new BlockRetrieveRequest(Configuration.TEST_BLOCK_OPEN));
        
        assertTrue(res.getBlock() instanceof OpenBlock);
        OpenBlock block = (OpenBlock)res.getBlock();
    
        assertEquals("0F323D7FEF67152289B288AFD9EE9CD3CD224A3874FA8833E5E2FE60ACC500DADDADE8F37D452088B584C7DB358CA7C79B79A77A3C764ADA964B33DBF34DAB09", block.getSignature());
        assertEquals("a7cf03e595499531", block.getWorkSolution());
    
        assertEquals("78B334ADAD96EE142061121A6C40CE7FC2271257BF11463E18ABE5989E219748", block.getSourceBlockHash());
        assertEquals("xrb_3jybgajxebuj9kby3xusmn4sqiomzu15trmkwb1xyrynnc7axss3qp1yn679", block.getAccountAddress());
        assertEquals("xrb_3dmtrrws3pocycmbqwawk6xs7446qxa36fcncush4s1pejk16ksbmakis78m", block.getRepresentativeAccount());
    }
    
    @Test
    @Category(NodeTests.class)
    public void testChangeBlock() {
        ResponseBlock res = query(new BlockRetrieveRequest(Configuration.TEST_BLOCK_CHANGE));
        
        assertTrue(res.getBlock() instanceof ChangeBlock);
        ChangeBlock block = (ChangeBlock)res.getBlock();
    
        assertEquals("F9490F5C09A2B5B99EBD4D5F50C8095229ACFF7CD823155F14FAA6D17BC87C3EBD8B427A8F2882189D3488640BDA5221A91ED00FB7B72D089037ACA80D30E001", block.getSignature());
        assertEquals("727335966a97f67d", block.getWorkSolution());
    
        assertEquals("D8D494F97BB0519B45B5386157DA7E736381E912A64727522695463040371C25", block.getPreviousBlockHash());
        assertEquals("xrb_3pczxuorp48td8645bs3m6c3xotxd3idskrenmi65rbrga5zmkemzhwkaznh", block.getRepresentativeAccount());
    }
    
}