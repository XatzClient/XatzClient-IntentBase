// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class ShortTag extends Tag
{
    private short value;
    
    public ShortTag(final String name) {
        this(name, (short)0);
    }
    
    public ShortTag(final String name, final short value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Short getValue() {
        return this.value;
    }
    
    public void setValue(final short value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readShort();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeShort(this.value);
    }
    
    @Override
    public ShortTag clone() {
        return new ShortTag(this.getName(), this.getValue());
    }
}
