// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class LongTag extends Tag
{
    private long value;
    
    public LongTag(final String name) {
        this(name, 0L);
    }
    
    public LongTag(final String name, final long value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Long getValue() {
        return this.value;
    }
    
    public void setValue(final long value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readLong();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeLong(this.value);
    }
    
    @Override
    public LongTag clone() {
        return new LongTag(this.getName(), this.getValue());
    }
}
