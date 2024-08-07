// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.TypeConverter;
import us.myles.ViaVersion.api.type.Type;

public class LongType extends Type<Long> implements TypeConverter<Long>
{
    public LongType() {
        super(Long.class);
    }
    
    @Override
    public Long read(final ByteBuf buffer) {
        return buffer.readLong();
    }
    
    @Override
    public void write(final ByteBuf buffer, final Long object) {
        buffer.writeLong((long)object);
    }
    
    @Override
    public Long from(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).longValue();
        }
        if (o instanceof Boolean) {
            return (long)(((boolean)o) ? 1 : 0);
        }
        return (Long)o;
    }
}
