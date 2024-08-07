// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.types;

import us.myles.ViaVersion.api.type.types.minecraft.BaseChunkType;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.chunks.BaseChunk;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.ArrayList;
import us.myles.ViaVersion.api.minecraft.Environment;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import java.util.BitSet;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_10to1_9_3.Protocol1_10To1_9_3_4;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.type.PartialType;

public class Chunk1_9_1_2Type extends PartialType<Chunk, ClientWorld>
{
    public Chunk1_9_1_2Type(final ClientWorld clientWorld) {
        super(clientWorld, Chunk.class);
    }
    
    @Override
    public Chunk read(final ByteBuf input, final ClientWorld world) throws Exception {
        final boolean replacePistons = world.getUser().getProtocolInfo().getPipeline().contains(Protocol1_10To1_9_3_4.class) && Via.getConfig().isReplacePistons();
        final int replacementId = Via.getConfig().getPistonReplacementId();
        final int chunkX = input.readInt();
        final int chunkZ = input.readInt();
        final boolean groundUp = input.readBoolean();
        final int primaryBitmask = Type.VAR_INT.readPrimitive(input);
        Type.VAR_INT.readPrimitive(input);
        final BitSet usedSections = new BitSet(16);
        final ChunkSection[] sections = new ChunkSection[16];
        for (int i = 0; i < 16; ++i) {
            if ((primaryBitmask & 1 << i) != 0x0) {
                usedSections.set(i);
            }
        }
        for (int i = 0; i < 16; ++i) {
            if (usedSections.get(i)) {
                final ChunkSection section = Types1_9.CHUNK_SECTION.read(input);
                (sections[i] = section).readBlockLight(input);
                if (world.getEnvironment() == Environment.NORMAL) {
                    section.readSkyLight(input);
                }
                if (replacePistons) {
                    section.replacePaletteEntry(36, replacementId);
                }
            }
        }
        final int[] biomeData = (int[])(groundUp ? new int[256] : null);
        if (groundUp) {
            for (int j = 0; j < 256; ++j) {
                biomeData[j] = (input.readByte() & 0xFF);
            }
        }
        return new BaseChunk(chunkX, chunkZ, groundUp, false, primaryBitmask, sections, biomeData, new ArrayList<CompoundTag>());
    }
    
    @Override
    public void write(final ByteBuf output, final ClientWorld world, final Chunk chunk) throws Exception {
        output.writeInt(chunk.getX());
        output.writeInt(chunk.getZ());
        output.writeBoolean(chunk.isFullChunk());
        Type.VAR_INT.writePrimitive(output, chunk.getBitmask());
        final ByteBuf buf = output.alloc().buffer();
        try {
            for (int i = 0; i < 16; ++i) {
                final ChunkSection section = chunk.getSections()[i];
                if (section != null) {
                    Types1_9.CHUNK_SECTION.write(buf, section);
                    section.writeBlockLight(buf);
                    if (section.hasSkyLight()) {
                        section.writeSkyLight(buf);
                    }
                }
            }
            buf.readerIndex(0);
            Type.VAR_INT.writePrimitive(output, buf.readableBytes() + (chunk.isBiomeData() ? 256 : 0));
            output.writeBytes(buf);
        }
        finally {
            buf.release();
        }
        if (chunk.isBiomeData()) {
            for (final int biome : chunk.getBiomeData()) {
                output.writeByte((int)(byte)biome);
            }
        }
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkType.class;
    }
}
