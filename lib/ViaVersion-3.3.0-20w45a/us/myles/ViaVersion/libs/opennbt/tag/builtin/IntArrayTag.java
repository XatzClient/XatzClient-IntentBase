// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class IntArrayTag extends Tag
{
    private int[] value;
    
    public IntArrayTag(final String name) {
        this(name, new int[0]);
    }
    
    public IntArrayTag(final String name, final int[] value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public int[] getValue() {
        return this.value.clone();
    }
    
    public void setValue(final int[] value) {
        if (value == null) {
            return;
        }
        this.value = value.clone();
    }
    
    public int getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final int value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = new int[in.readInt()];
        for (int index = 0; index < this.value.length; ++index) {
            this.value[index] = in.readInt();
        }
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeInt(this.value.length);
        for (int index = 0; index < this.value.length; ++index) {
            out.writeInt(this.value[index]);
        }
    }
    
    @Override
    public IntArrayTag clone() {
        return new IntArrayTag(this.getName(), this.getValue());
    }
}
