// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import java.io.DataOutput;
import java.io.OutputStream;
import java.io.DataOutputStream;
import io.netty.buffer.ByteBufOutputStream;
import java.io.IOException;
import java.io.DataInput;
import us.myles.viaversion.libs.opennbt.NBTIO;
import java.io.InputStream;
import java.io.DataInputStream;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBuf;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.type.Type;

public class NBTType extends Type<CompoundTag>
{
    public NBTType() {
        super((Class)CompoundTag.class);
    }
    
    public CompoundTag read(final ByteBuf buffer) {
        final short length = buffer.readShort();
        if (length < 0) {
            return null;
        }
        final ByteBufInputStream byteBufInputStream = new ByteBufInputStream(buffer);
        final DataInputStream dataInputStream = new DataInputStream((InputStream)byteBufInputStream);
        try {
            return (CompoundTag)NBTIO.readTag((DataInput)dataInputStream);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            try {
                dataInputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        finally {
            try {
                dataInputStream.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
    
    public void write(final ByteBuf buffer, final CompoundTag nbt) throws Exception {
        if (nbt == null) {
            buffer.writeShort(-1);
        }
        else {
            final ByteBuf buf = buffer.alloc().buffer();
            final ByteBufOutputStream bytebufStream = new ByteBufOutputStream(buf);
            final DataOutputStream dataOutputStream = new DataOutputStream((OutputStream)bytebufStream);
            NBTIO.writeTag((DataOutput)dataOutputStream, (Tag)nbt);
            dataOutputStream.close();
            buffer.writeShort(buf.readableBytes());
            buffer.writeBytes(buf);
            buf.release();
        }
    }
}
