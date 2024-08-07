// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
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
import java.io.ByteArrayInputStream;
import io.netty.buffer.ByteBuf;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.type.Type;

public class CompressedNBTType extends Type<CompoundTag>
{
    public CompressedNBTType() {
        super((Class)CompoundTag.class);
    }
    
    public CompoundTag read(final ByteBuf buffer) {
        final short length = buffer.readShort();
        if (length <= 0) {
            return null;
        }
        final byte[] compressed = new byte[length];
        buffer.readBytes(compressed);
        final byte[] uncompressed = decompress(compressed);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(uncompressed);
        final DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
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
            final byte[] uncompressed = new byte[buf.readableBytes()];
            buf.readBytes(uncompressed);
            buf.release();
            final byte[] compressed = compress(uncompressed);
            buffer.writeShort(compressed.length);
            buffer.writeBytes(compressed);
        }
    }
    
    public static byte[] compress(final byte[] content) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(content);
            gzipOutputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] decompress(final byte[] contentBytes) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            final GZIPInputStream stream = new GZIPInputStream(new ByteArrayInputStream(contentBytes));
            while (stream.available() > 0) {
                out.write(stream.read());
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }
}
