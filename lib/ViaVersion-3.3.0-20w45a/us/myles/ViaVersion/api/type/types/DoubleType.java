// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.TypeConverter;
import us.myles.ViaVersion.api.type.Type;

public class DoubleType extends Type<Double> implements TypeConverter<Double>
{
    public DoubleType() {
        super(Double.class);
    }
    
    @Override
    public Double read(final ByteBuf buffer) {
        return buffer.readDouble();
    }
    
    @Override
    public void write(final ByteBuf buffer, final Double object) {
        buffer.writeDouble((double)object);
    }
    
    @Override
    public Double from(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).doubleValue();
        }
        if (o instanceof Boolean) {
            return o ? 1.0 : 0.0;
        }
        return (Double)o;
    }
}
