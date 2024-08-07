// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.TypeConverter;
import us.myles.ViaVersion.api.type.Type;

public class ByteType extends Type<Byte> implements TypeConverter<Byte>
{
    public ByteType() {
        super(Byte.class);
    }
    
    @Override
    public Byte read(final ByteBuf buffer) {
        return buffer.readByte();
    }
    
    @Override
    public void write(final ByteBuf buffer, final Byte object) {
        buffer.writeByte((int)object);
    }
    
    @Override
    public Byte from(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).byteValue();
        }
        if (o instanceof Boolean) {
            return (byte)(((boolean)o) ? 1 : 0);
        }
        return (Byte)o;
    }
}
