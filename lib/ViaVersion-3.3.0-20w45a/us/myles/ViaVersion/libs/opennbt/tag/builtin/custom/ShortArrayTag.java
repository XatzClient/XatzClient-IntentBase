// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin.custom;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class ShortArrayTag extends Tag
{
    private short[] value;
    
    public ShortArrayTag(final String name) {
        this(name, new short[0]);
    }
    
    public ShortArrayTag(final String name, final short[] value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public short[] getValue() {
        return this.value.clone();
    }
    
    public void setValue(final short[] value) {
        if (value == null) {
            return;
        }
        this.value = value.clone();
    }
    
    public short getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final short value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = new short[in.readInt()];
        for (int index = 0; index < this.value.length; ++index) {
            this.value[index] = in.readShort();
        }
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeInt(this.value.length);
        for (int index = 0; index < this.value.length; ++index) {
            out.writeShort(this.value[index]);
        }
    }
    
    @Override
    public ShortArrayTag clone() {
        return new ShortArrayTag(this.getName(), this.getValue());
    }
}
