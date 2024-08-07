// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class FloatTag extends Tag
{
    private float value;
    
    public FloatTag(final String name) {
        this(name, 0.0f);
    }
    
    public FloatTag(final String name, final float value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Float getValue() {
        return this.value;
    }
    
    public void setValue(final float value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readFloat();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeFloat(this.value);
    }
    
    @Override
    public FloatTag clone() {
        return new FloatTag(this.getName(), this.getValue());
    }
}
