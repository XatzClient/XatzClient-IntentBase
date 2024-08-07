// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class StringTag extends Tag
{
    private String value;
    
    public StringTag(final String name) {
        this(name, "");
    }
    
    public StringTag(final String name, final String value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readUTF();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeUTF(this.value);
    }
    
    @Override
    public StringTag clone() {
        return new StringTag(this.getName(), this.getValue());
    }
}
