// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.version;

import us.myles.ViaVersion.util.CompactArrayUtil;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.type.Type;

public class ChunkSectionType1_16 extends Type<ChunkSection>
{
    private static final int GLOBAL_PALETTE = 15;
    
    public ChunkSectionType1_16() {
        super("Chunk Section Type", ChunkSection.class);
    }
    
    @Override
    public ChunkSection read(final ByteBuf buffer) throws Exception {
        final int originalBitsPerBlock;
        int bitsPerBlock = originalBitsPerBlock = buffer.readUnsignedByte();
        if (bitsPerBlock == 0 || bitsPerBlock > 8) {
            bitsPerBlock = 15;
        }
        ChunkSection chunkSection;
        if (bitsPerBlock != 15) {
            final int paletteLength = Type.VAR_INT.readPrimitive(buffer);
            chunkSection = new ChunkSection(paletteLength);
            for (int i = 0; i < paletteLength; ++i) {
                chunkSection.addPaletteEntry(Type.VAR_INT.readPrimitive(buffer));
            }
        }
        else {
            chunkSection = new ChunkSection();
        }
        final long[] blockData = new long[Type.VAR_INT.readPrimitive(buffer)];
        if (blockData.length > 0) {
            final char valuesPerLong = (char)(64 / bitsPerBlock);
            final int expectedLength = ('\u1000' + valuesPerLong - 1) / valuesPerLong;
            if (blockData.length != expectedLength) {
                throw new IllegalStateException("Block data length (" + blockData.length + ") does not match expected length (" + expectedLength + ")! bitsPerBlock=" + bitsPerBlock + ", originalBitsPerBlock=" + originalBitsPerBlock);
            }
            for (int j = 0; j < blockData.length; ++j) {
                blockData[j] = buffer.readLong();
            }
            CompactArrayUtil.iterateCompactArrayWithPadding(bitsPerBlock, 4096, blockData, (bitsPerBlock == 15) ? chunkSection::setFlatBlock : chunkSection::setPaletteIndex);
        }
        return chunkSection;
    }
    
    @Override
    public void write(final ByteBuf buffer, final ChunkSection chunkSection) throws Exception {
        int bitsPerBlock;
        for (bitsPerBlock = 4; chunkSection.getPaletteSize() > 1 << bitsPerBlock; ++bitsPerBlock) {}
        if (bitsPerBlock > 8) {
            bitsPerBlock = 15;
        }
        buffer.writeByte(bitsPerBlock);
        if (bitsPerBlock != 15) {
            Type.VAR_INT.writePrimitive(buffer, chunkSection.getPaletteSize());
            for (int i = 0; i < chunkSection.getPaletteSize(); ++i) {
                Type.VAR_INT.writePrimitive(buffer, chunkSection.getPaletteEntry(i));
            }
        }
        final long[] data = CompactArrayUtil.createCompactArrayWithPadding(bitsPerBlock, 4096, (bitsPerBlock == 15) ? chunkSection::getFlatBlock : chunkSection::getPaletteIndex);
        Type.VAR_INT.writePrimitive(buffer, data.length);
        for (final long l : data) {
            buffer.writeLong(l);
        }
    }
}
