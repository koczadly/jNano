/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockIntent;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Objects;
import java.util.function.Function;

/**
 * Block type "{@code test}", has additional string value "{@code val}".
 *
 * @author Karl Oczadly
 */
public class TestBlock extends Block {
    
    public static final Function<JsonObject, TestBlock> DESERIALIZER = json -> new TestBlock(
            JNH.getJson(json, "signature", HexData::new),
            JNH.getJson(json, "work", WorkSolution::new),
            JNH.getJson(json, "val"));
    
    
    
    @Expose @SerializedName("val")
    private final String val;
    
    
    public TestBlock() {
        this(null);
    }
    
    public TestBlock(String val) {
        this(null, null, val);
    }
    
    public TestBlock(HexData signature, WorkSolution workSolution, String val) {
        super("test", signature, workSolution);
        this.val = val;
    }
    
    
    public String getVal() {
        return val;
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                (getVal() != null ? getVal().getBytes() : new byte[0])
        };
    }
    
    @Override
    public BlockIntent getIntent() {
        return BlockIntent.ALL_UNKNOWN;
    }
    
    @Override
    public boolean contentEquals(Block block) {
        if (!(block instanceof TestBlock)) return false;
        TestBlock tb = (TestBlock)block;
        return super.contentEquals(tb)
                && Objects.equals(getVal(), tb.getVal());
    }
    
}
