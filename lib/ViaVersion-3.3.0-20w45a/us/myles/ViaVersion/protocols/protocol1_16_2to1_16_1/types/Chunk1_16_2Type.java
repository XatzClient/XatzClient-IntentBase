// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.types;

import us.myles.ViaVersion.api.type.types.minecraft.BaseChunkType;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.chunks.BaseChunk;
import us.myles.ViaVersion.api.Via;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import us.myles.ViaVersion.api.type.types.version.Types1_16;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import io.netty.buffer.ByteBuf;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.type.Type;

public class Chunk1_16_2Type extends Type<Chunk>
{
    private static final CompoundTag[] EMPTY_COMPOUNDS;
    
    public Chunk1_16_2Type() {
        super(Chunk.class);
    }
    
    @Override
    public Chunk read(final ByteBuf input) throws Exception {
        final int chunkX = input.readInt();
        final int chunkZ = input.readInt();
        final boolean fullChunk = input.readBoolean();
        final int primaryBitmask = Type.VAR_INT.readPrimitive(input);
        final CompoundTag heightMap = Type.NBT.read(input);
        int[] biomeData = null;
        if (fullChunk) {
            biomeData = Type.VAR_INT_ARRAY_PRIMITIVE.read(input);
        }
        Type.VAR_INT.readPrimitive(input);
        final ChunkSection[] sections = new ChunkSection[16];
        for (int i = 0; i < 16; ++i) {
            if ((primaryBitmask & 1 << i) != 0x0) {
                final short nonAirBlocksCount = input.readShort();
                final ChunkSection section = Types1_16.CHUNK_SECTION.read(input);
                section.setNonAirBlocksCount(nonAirBlocksCount);
                sections[i] = section;
            }
        }
        final List<CompoundTag> nbtData = new ArrayList<CompoundTag>(Arrays.asList((CompoundTag[])Type.NBT_ARRAY.read(input)));
        if (input.readableBytes() > 0) {
            final byte[] array = Type.REMAINING_BYTES.read(input);
            if (Via.getManager().isDebug()) {
                Via.getPlatform().getLogger().warning("Found " + array.length + " more bytes than expected while reading the chunk: " + chunkX + "/" + chunkZ);
            }
        }
        return new BaseChunk(chunkX, chunkZ, fullChunk, false, primaryBitmask, sections, biomeData, heightMap, nbtData);
    }
    
    @Override
    public void write(final ByteBuf output, final Chunk chunk) throws Exception {
        output.writeInt(chunk.getX());
        output.writeInt(chunk.getZ());
        output.writeBoolean(chunk.isFullChunk());
        Type.VAR_INT.writePrimitive(output, chunk.getBitmask());
        Type.NBT.write(output, chunk.getHeightMap());
        if (chunk.isBiomeData()) {
            Type.VAR_INT_ARRAY_PRIMITIVE.write(output, chunk.getBiomeData());
        }
        final ByteBuf buf = output.alloc().buffer();
        try {
            for (int i = 0; i < 16; ++i) {
                final ChunkSection section = chunk.getSections()[i];
                if (section != null) {
                    buf.writeShort(section.getNonAirBlocksCount());
                    Types1_16.CHUNK_SECTION.write(buf, section);
                }
            }
            buf.readerIndex(0);
            Type.VAR_INT.writePrimitive(output, buf.readableBytes());
            output.writeBytes(buf);
        }
        finally {
            buf.release();
        }
        Type.NBT_ARRAY.write(output, chunk.getBlockEntities().toArray(Chunk1_16_2Type.EMPTY_COMPOUNDS));
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkType.class;
    }
    
    static {
        EMPTY_COMPOUNDS = new CompoundTag[0];
    }
}
