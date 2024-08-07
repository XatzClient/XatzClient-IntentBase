// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.version;

import java.nio.ByteOrder;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.type.Type;

public class ChunkSectionType1_8 extends Type<ChunkSection>
{
    public ChunkSectionType1_8() {
        super("Chunk Section Type", ChunkSection.class);
    }
    
    @Override
    public ChunkSection read(final ByteBuf buffer) throws Exception {
        final ChunkSection chunkSection = new ChunkSection();
        chunkSection.addPaletteEntry(0);
        final ByteBuf littleEndianView = buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 4096; ++i) {
            final int mask = littleEndianView.readShort();
            final int type = mask >> 4;
            final int data = mask & 0xF;
            chunkSection.setBlock(i, type, data);
        }
        return chunkSection;
    }
    
    @Override
    public void write(final ByteBuf buffer, final ChunkSection chunkSection) throws Exception {
        throw new UnsupportedOperationException();
    }
}
