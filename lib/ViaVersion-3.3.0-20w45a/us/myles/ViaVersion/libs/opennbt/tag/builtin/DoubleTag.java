// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class DoubleTag extends Tag
{
    private double value;
    
    public DoubleTag(final String name) {
        this(name, 0.0);
    }
    
    public DoubleTag(final String name, final double value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Double getValue() {
        return this.value;
    }
    
    public void setValue(final double value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readDouble();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeDouble(this.value);
    }
    
    @Override
    public DoubleTag clone() {
        return new DoubleTag(this.getName(), this.getValue());
    }
}
