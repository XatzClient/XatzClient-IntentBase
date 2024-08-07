// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.TypeConverter;
import us.myles.ViaVersion.api.type.Type;

public class UnsignedByteType extends Type<Short> implements TypeConverter<Short>
{
    public UnsignedByteType() {
        super("Unsigned Byte", Short.class);
    }
    
    @Override
    public Short read(final ByteBuf buffer) {
        return buffer.readUnsignedByte();
    }
    
    @Override
    public void write(final ByteBuf buffer, final Short object) {
        buffer.writeByte((int)object);
    }
    
    @Override
    public Short from(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).shortValue();
        }
        if (o instanceof Boolean) {
            return (short)(((boolean)o) ? 1 : 0);
        }
        return (Short)o;
    }
}
