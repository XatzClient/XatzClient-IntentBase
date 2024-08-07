// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class IntTag extends Tag
{
    private int value;
    
    public IntTag(final String name) {
        this(name, 0);
    }
    
    public IntTag(final String name, final int value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Integer getValue() {
        return this.value;
    }
    
    public void setValue(final int value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readInt();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeInt(this.value);
    }
    
    @Override
    public IntTag clone() {
        return new IntTag(this.getName(), this.getValue());
    }
}
