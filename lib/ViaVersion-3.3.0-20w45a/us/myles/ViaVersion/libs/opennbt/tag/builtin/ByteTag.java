// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class ByteTag extends Tag
{
    private byte value;
    
    public ByteTag(final String name) {
        this(name, (byte)0);
    }
    
    public ByteTag(final String name, final byte value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Byte getValue() {
        return this.value;
    }
    
    public void setValue(final byte value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readByte();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeByte(this.value);
    }
    
    @Override
    public ByteTag clone() {
        return new ByteTag(this.getName(), this.getValue());
    }
}
