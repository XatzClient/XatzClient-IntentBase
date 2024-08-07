// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class ByteArrayTag extends Tag
{
    private byte[] value;
    
    public ByteArrayTag(final String name) {
        this(name, new byte[0]);
    }
    
    public ByteArrayTag(final String name, final byte[] value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public byte[] getValue() {
        return this.value.clone();
    }
    
    public void setValue(final byte[] value) {
        if (value == null) {
            return;
        }
        this.value = value.clone();
    }
    
    public byte getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final byte value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        in.readFully(this.value = new byte[in.readInt()]);
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeInt(this.value.length);
        out.write(this.value);
    }
    
    @Override
    public ByteArrayTag clone() {
        return new ByteArrayTag(this.getName(), this.getValue());
    }
}
