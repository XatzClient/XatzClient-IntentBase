// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.PartialType;

public class CustomStringType extends PartialType<String[], Integer>
{
    public CustomStringType(final Integer param) {
        super((Object)param, (Class)String[].class);
    }
    
    public String[] read(final ByteBuf buffer, final Integer size) throws Exception {
        if (buffer.readableBytes() < size / 4) {
            throw new RuntimeException("Readable bytes does not match expected!");
        }
        final String[] array = new String[(int)size];
        for (int i = 0; i < size; ++i) {
            array[i] = (String)Type.STRING.read(buffer);
        }
        return array;
    }
    
    public void write(final ByteBuf buffer, final Integer size, final String[] strings) throws Exception {
        for (final String s : strings) {
            Type.STRING.write(buffer, (Object)s);
        }
    }
}
