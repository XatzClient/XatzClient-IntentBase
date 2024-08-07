// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.type.Type;

public class Position1_14Type extends Type<Position>
{
    public Position1_14Type() {
        super(Position.class);
    }
    
    @Override
    public Position read(final ByteBuf buffer) {
        final long val = buffer.readLong();
        final long x = val >> 38;
        final long y = val << 52 >> 52;
        final long z = val << 26 >> 38;
        return new Position((int)x, (short)y, (int)z);
    }
    
    @Override
    public void write(final ByteBuf buffer, final Position object) {
        buffer.writeLong(((long)object.getX() & 0x3FFFFFFL) << 38 | (long)(object.getY() & 0xFFF) | ((long)object.getZ() & 0x3FFFFFFL) << 12);
    }
}
