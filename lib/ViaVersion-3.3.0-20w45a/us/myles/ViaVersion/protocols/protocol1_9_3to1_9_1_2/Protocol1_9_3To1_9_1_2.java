// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import java.util.List;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.chunks.FakeTileEntity;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.types.Chunk1_9_1_2Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_9_3To1_9_1_2 extends Protocol<ClientboundPackets1_9, ClientboundPackets1_9_3, ServerboundPackets1_9, ServerboundPackets1_9_3>
{
    public static final ValueTransformer<Short, Short> ADJUST_PITCH;
    
    public Protocol1_9_3To1_9_1_2() {
        super(ClientboundPackets1_9.class, ClientboundPackets1_9_3.class, ServerboundPackets1_9.class, ServerboundPackets1_9_3.class);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol<ClientboundPackets1_9, ClientboundPackets1_9_3, S1, S2>)this).registerOutgoing(ClientboundPackets1_9.UPDATE_SIGN, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Position position = wrapper.read(Type.POSITION);
                        final JsonElement[] lines = new JsonElement[4];
                        for (int i = 0; i < 4; ++i) {
                            lines[i] = wrapper.read(Type.COMPONENT);
                        }
                        wrapper.clearInputBuffer();
                        wrapper.setId(9);
                        wrapper.write(Type.POSITION, position);
                        wrapper.write(Type.UNSIGNED_BYTE, (Short)9);
                        final CompoundTag tag = new CompoundTag("");
                        tag.put(new StringTag("id", "Sign"));
                        tag.put(new IntTag("x", position.getX()));
                        tag.put(new IntTag("y", position.getY()));
                        tag.put(new IntTag("z", position.getZ()));
                        for (int j = 0; j < lines.length; ++j) {
                            tag.put(new StringTag("Text" + (j + 1), lines[j].toString()));
                        }
                        wrapper.write(Type.NBT, tag);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_9, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final Chunk1_9_1_2Type type = new Chunk1_9_1_2Type(clientWorld);
                        final Chunk chunk = wrapper.passthrough((Type<Chunk>)type);
                        final List<CompoundTag> tags = chunk.getBlockEntities();
                        for (int i = 0; i < chunk.getSections().length; ++i) {
                            final ChunkSection section = chunk.getSections()[i];
                            if (section != null) {
                                for (int y = 0; y < 16; ++y) {
                                    for (int z = 0; z < 16; ++z) {
                                        for (int x = 0; x < 16; ++x) {
                                            final int block = section.getBlockId(x, y, z);
                                            if (FakeTileEntity.hasBlock(block)) {
                                                tags.add(FakeTileEntity.getFromBlock(x + (chunk.getX() << 4), y + (i << 4), z + (chunk.getZ() << 4), block));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        wrapper.write(Type.NBT_ARRAY, chunk.getBlockEntities().toArray(new CompoundTag[0]));
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_9, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9.JOIN_GAME, new PacketRemapper() {
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
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_9, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final int dimensionId = wrapper.get(Type.INT, 0);
                        clientWorld.setEnvironment(dimensionId);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_9, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9.SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.FLOAT);
                this.map(Protocol1_9_3To1_9_1_2.ADJUST_PITCH);
            }
        });
    }
    
    @Override
    public void init(final UserConnection user) {
        if (!user.has(ClientWorld.class)) {
            user.put(new ClientWorld(user));
        }
    }
    
    static {
        ADJUST_PITCH = new ValueTransformer<Short, Short>(Type.UNSIGNED_BYTE, Type.UNSIGNED_BYTE) {
            @Override
            public Short transform(final PacketWrapper wrapper, final Short inputValue) throws Exception {
                return (short)Math.round(inputValue / 63.5f * 63.0f);
            }
        };
    }
}
