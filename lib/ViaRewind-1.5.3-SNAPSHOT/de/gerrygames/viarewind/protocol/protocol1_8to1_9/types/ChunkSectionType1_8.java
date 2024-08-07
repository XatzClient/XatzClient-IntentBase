// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.types;

import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.type.Type;

public class ChunkSectionType1_8 extends Type<ChunkSection>
{
    public ChunkSectionType1_8() {
        super("Chunk Section Type", (Class)ChunkSection.class);
    }
    
    public ChunkSection read(final ByteBuf buffer) throws Exception {
        final ChunkSection chunkSection = new ChunkSection();
        final byte[] blockData = new byte[8192];
        buffer.readBytes(blockData);
        final ShortBuffer blockBuf = ByteBuffer.wrap(blockData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        for (int i = 0; i < 4096; ++i) {
            final int mask = blockBuf.get();
            final int type = mask >> 4;
            final int data = mask & 0xF;
            chunkSection.setBlock(i, type, data);
        }
        return chunkSection;
    }
    
    public void write(final ByteBuf buffer, final ChunkSection chunkSection) throws Exception {
        for (int y = 0; y < 16; ++y) {
            for (int z = 0; z < 16; ++z) {
                for (int x = 0; x < 16; ++x) {
                    final int block = chunkSection.getFlatBlock(x, y, z);
                    buffer.writeByte(block);
                    buffer.writeByte(block >> 8);
                }
            }
        }
    }
}
