// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.Type;

public class RemainingBytesType extends Type<byte[]>
{
    public RemainingBytesType() {
        super(byte[].class);
    }
    
    @Override
    public byte[] read(final ByteBuf buffer) {
        final byte[] array = new byte[buffer.readableBytes()];
        buffer.readBytes(array);
        return array;
    }
    
    @Override
    public void write(final ByteBuf buffer, final byte[] object) {
        buffer.writeBytes(object);
    }
}
