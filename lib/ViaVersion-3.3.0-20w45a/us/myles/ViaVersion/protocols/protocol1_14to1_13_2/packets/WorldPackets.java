// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_14to1_13_2.packets;

import java.util.Arrays;
import us.myles.ViaVersion.api.minecraft.chunks.NibbleArray;
import us.myles.ViaVersion.api.minecraft.BlockFace;
import us.myles.ViaVersion.util.CompactArrayUtil;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_14Types;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import us.myles.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;

public class WorldPackets
{
    public static final int SERVERSIDE_VIEW_DISTANCE = 64;
    private static final byte[] FULL_LIGHT;
    public static int air;
    public static int voidAir;
    public static int caveAir;
    
    public static void register(final Protocol1_14To1_13_2 protocol) {
        final BlockRewriter blockRewriter = new BlockRewriter(protocol, null);
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.BLOCK_BREAK_ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map(Type.BYTE);
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.BLOCK_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.set(Type.VAR_INT, 0, protocol.getMappingData().getNewBlockId(wrapper.get((Type<Integer>)Type.VAR_INT, 0)));
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        wrapper.set(Type.VAR_INT, 0, protocol.getMappingData().getNewBlockStateId(id));
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.SERVER_DIFFICULTY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.create(new ValueCreator() {
                    @Override
                    public void write(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.BOOLEAN, false);
                    }
                });
            }
        });
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_13.MULTI_BLOCK_CHANGE);
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.EXPLOSION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int i = 0; i < 3; ++i) {
                            float coord = wrapper.get((Type<Float>)Type.FLOAT, i);
                            if (coord < 0.0f) {
                                coord = (float)(int)coord;
                                wrapper.set(Type.FLOAT, i, coord);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_13Type(clientWorld));
                        wrapper.write(new Chunk1_14Type(), chunk);
                        final int[] motionBlocking = new int[256];
                        final int[] worldSurface = new int[256];
                        for (int s = 0; s < 16; ++s) {
                            final ChunkSection section = chunk.getSections()[s];
                            if (section != null) {
                                boolean hasBlock = false;
                                for (int i = 0; i < section.getPaletteSize(); ++i) {
                                    final int old = section.getPaletteEntry(i);
                                    final int newId = protocol.getMappingData().getNewBlockStateId(old);
                                    if (!hasBlock && newId != WorldPackets.air && newId != WorldPackets.voidAir && newId != WorldPackets.caveAir) {
                                        hasBlock = true;
                                    }
                                    section.setPaletteEntry(i, newId);
                                }
                                if (!hasBlock) {
                                    section.setNonAirBlocksCount(0);
                                }
                                else {
                                    int nonAirBlockCount = 0;
                                    for (int x = 0; x < 16; ++x) {
                                        for (int y = 0; y < 16; ++y) {
                                            for (int z = 0; z < 16; ++z) {
                                                final int id = section.getFlatBlock(x, y, z);
                                                if (id != WorldPackets.air && id != WorldPackets.voidAir && id != WorldPackets.caveAir) {
                                                    ++nonAirBlockCount;
                                                    worldSurface[x + z * 16] = y + s * 16 + 1;
                                                }
                                                if (protocol.getMappingData().getMotionBlocking().contains(id)) {
                                                    motionBlocking[x + z * 16] = y + s * 16 + 1;
                                                }
                                                if (Via.getConfig().isNonFullBlockLightFix() && protocol.getMappingData().getNonFullBlocks().contains(id)) {
                                                    setNonFullLight(chunk, section, s, x, y, z);
                                                }
                                            }
                                        }
                                    }
                                    section.setNonAirBlocksCount(nonAirBlockCount);
                                }
                            }
                        }
                        final CompoundTag heightMap = new CompoundTag("");
                        heightMap.put(new LongArrayTag("MOTION_BLOCKING", encodeHeightMap(motionBlocking)));
                        heightMap.put(new LongArrayTag("WORLD_SURFACE", encodeHeightMap(worldSurface)));
                        chunk.setHeightMap(heightMap);
                        final PacketWrapper lightPacket = wrapper.create(36);
                        lightPacket.write(Type.VAR_INT, chunk.getX());
                        lightPacket.write(Type.VAR_INT, chunk.getZ());
                        int skyLightMask = chunk.isFullChunk() ? 262143 : 0;
                        int blockLightMask = 0;
                        for (int j = 0; j < chunk.getSections().length; ++j) {
                            final ChunkSection sec = chunk.getSections()[j];
                            if (sec != null) {
                                if (!chunk.isFullChunk() && sec.hasSkyLight()) {
                                    skyLightMask |= 1 << j + 1;
                                }
                                blockLightMask |= 1 << j + 1;
                            }
                        }
                        lightPacket.write(Type.VAR_INT, skyLightMask);
                        lightPacket.write(Type.VAR_INT, blockLightMask);
                        lightPacket.write(Type.VAR_INT, 0);
                        lightPacket.write(Type.VAR_INT, 0);
                        if (chunk.isFullChunk()) {
                            lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, WorldPackets.FULL_LIGHT);
                        }
                        for (final ChunkSection section2 : chunk.getSections()) {
                            if (section2 == null || !section2.hasSkyLight()) {
                                if (chunk.isFullChunk()) {
                                    lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, WorldPackets.FULL_LIGHT);
                                }
                            }
                            else {
                                lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, section2.getSkyLight());
                            }
                        }
                        if (chunk.isFullChunk()) {
                            lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, WorldPackets.FULL_LIGHT);
                        }
                        for (final ChunkSection section2 : chunk.getSections()) {
                            if (section2 != null) {
                                lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, section2.getBlockLight());
                            }
                        }
                        final EntityTracker1_14 entityTracker = wrapper.user().get(EntityTracker1_14.class);
                        final int diffX = Math.abs(entityTracker.getChunkCenterX() - chunk.getX());
                        final int diffZ = Math.abs(entityTracker.getChunkCenterZ() - chunk.getZ());
                        if (entityTracker.isForceSendCenterChunk() || diffX >= 64 || diffZ >= 64) {
                            final PacketWrapper fakePosLook = wrapper.create(64);
                            fakePosLook.write(Type.VAR_INT, chunk.getX());
                            fakePosLook.write(Type.VAR_INT, chunk.getZ());
                            fakePosLook.send(Protocol1_14To1_13_2.class, true, true);
                            entityTracker.setChunkCenterX(chunk.getX());
                            entityTracker.setChunkCenterZ(chunk.getZ());
                        }
                        lightPacket.send(Protocol1_14To1_13_2.class, true, true);
                    }
                    
                    private Byte[] fromPrimitiveArray(final byte[] bytes) {
                        final Byte[] newArray = new Byte[bytes.length];
                        for (int i = 0; i < bytes.length; ++i) {
                            newArray[i] = bytes[i];
                        }
                        return newArray;
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get(Type.INT, 0);
                        final int data = wrapper.get(Type.INT, 1);
                        if (id == 1010) {
                            wrapper.set(Type.INT, 1, protocol.getMappingData().getNewItemId(data));
                        }
                        else if (id == 2001) {
                            wrapper.set(Type.INT, 1, protocol.getMappingData().getNewBlockStateId(data));
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientChunks = wrapper.user().get(ClientWorld.class);
                        final int dimensionId = wrapper.get(Type.INT, 1);
                        clientChunks.setEnvironment(dimensionId);
                        final int entityId = wrapper.get(Type.INT, 0);
                        final Entity1_14Types.EntityType entType = Entity1_14Types.EntityType.PLAYER;
                        final EntityTracker1_14 tracker = wrapper.user().get(EntityTracker1_14.class);
                        tracker.addEntity(entityId, entType);
                        tracker.setClientEntityId(entityId);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short difficulty = wrapper.read(Type.UNSIGNED_BYTE);
                        final PacketWrapper difficultyPacket = wrapper.create(13);
                        difficultyPacket.write(Type.UNSIGNED_BYTE, difficulty);
                        difficultyPacket.write(Type.BOOLEAN, false);
                        difficultyPacket.send(protocol.getClass());
                        wrapper.passthrough(Type.UNSIGNED_BYTE);
                        wrapper.passthrough(Type.STRING);
                        wrapper.write(Type.VAR_INT, 64);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.MAP_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.create(new ValueCreator() {
                    @Override
                    public void write(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.BOOLEAN, false);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final int dimensionId = wrapper.get(Type.INT, 0);
                        clientWorld.setEnvironment(dimensionId);
                        final EntityTracker1_14 entityTracker = wrapper.user().get(EntityTracker1_14.class);
                        entityTracker.setForceSendCenterChunk(true);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short difficulty = wrapper.read(Type.UNSIGNED_BYTE);
                        final PacketWrapper difficultyPacket = wrapper.create(13);
                        difficultyPacket.write(Type.UNSIGNED_BYTE, difficulty);
                        difficultyPacket.write(Type.BOOLEAN, false);
                        difficultyPacket.send(protocol.getClass());
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.SPAWN_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
    }
    
    private static long[] encodeHeightMap(final int[] heightMap) {
        return CompactArrayUtil.createCompactArray(9, heightMap.length, i -> heightMap[i]);
    }
    
    private static void setNonFullLight(final Chunk chunk, final ChunkSection section, int ySection, final int x, final int y, final int z) {
        int skyLight = 0;
        int blockLight = 0;
        for (final BlockFace blockFace : BlockFace.values()) {
            NibbleArray skyLightArray = section.getSkyLightNibbleArray();
            NibbleArray blockLightArray = section.getBlockLightNibbleArray();
            final int neighbourX = x + blockFace.getModX();
            int neighbourY = y + blockFace.getModY();
            final int neighbourZ = z + blockFace.getModZ();
            Label_0335: {
                if (blockFace.getModX() != 0) {
                    if (neighbourX == 16) {
                        break Label_0335;
                    }
                    if (neighbourX == -1) {
                        break Label_0335;
                    }
                }
                else if (blockFace.getModY() != 0) {
                    if (neighbourY == 16 || neighbourY == -1) {
                        if (neighbourY == 16) {
                            ++ySection;
                            neighbourY = 0;
                        }
                        else {
                            --ySection;
                            neighbourY = 15;
                        }
                        if (ySection == 16) {
                            break Label_0335;
                        }
                        if (ySection == -1) {
                            break Label_0335;
                        }
                        final ChunkSection newSection = chunk.getSections()[ySection];
                        if (newSection == null) {
                            break Label_0335;
                        }
                        skyLightArray = newSection.getSkyLightNibbleArray();
                        blockLightArray = newSection.getBlockLightNibbleArray();
                    }
                }
                else if (blockFace.getModZ() != 0) {
                    if (neighbourZ == 16) {
                        break Label_0335;
                    }
                    if (neighbourZ == -1) {
                        break Label_0335;
                    }
                }
                if (blockLightArray != null && blockLight != 15) {
                    final int neighbourBlockLight = blockLightArray.get(neighbourX, neighbourY, neighbourZ);
                    if (neighbourBlockLight == 15) {
                        blockLight = 14;
                    }
                    else if (neighbourBlockLight > blockLight) {
                        blockLight = neighbourBlockLight - 1;
                    }
                }
                if (skyLightArray != null && skyLight != 15) {
                    final int neighbourSkyLight = skyLightArray.get(neighbourX, neighbourY, neighbourZ);
                    if (neighbourSkyLight == 15) {
                        if (blockFace.getModY() == 1) {
                            skyLight = 15;
                        }
                        else {
                            skyLight = 14;
                        }
                    }
                    else if (neighbourSkyLight > skyLight) {
                        skyLight = neighbourSkyLight - 1;
                    }
                }
            }
        }
        if (skyLight != 0) {
            if (!section.hasSkyLight()) {
                final byte[] newSkyLight = new byte[2028];
                section.setSkyLight(newSkyLight);
            }
            section.getSkyLightNibbleArray().set(x, y, z, skyLight);
        }
        if (blockLight != 0) {
            section.getBlockLightNibbleArray().set(x, y, z, blockLight);
        }
    }
    
    private static long getChunkIndex(final int x, final int z) {
        return ((long)x & 0x3FFFFFFL) << 38 | ((long)z & 0x3FFFFFFL);
    }
    
    static {
        Arrays.fill(FULL_LIGHT = new byte[2048], (byte)(-1));
    }
}
