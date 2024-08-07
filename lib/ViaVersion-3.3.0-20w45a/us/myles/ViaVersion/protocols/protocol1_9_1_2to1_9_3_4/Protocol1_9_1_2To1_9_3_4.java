// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9_1_2to1_9_3_4;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_9_1_2to1_9_3_4.chunks.BlockEntity;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.types.Chunk1_9_1_2Type;
import us.myles.ViaVersion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_9_1_2To1_9_3_4 extends Protocol<ClientboundPackets1_9_3, ClientboundPackets1_9, ServerboundPackets1_9_3, ServerboundPackets1_9>
{
    public Protocol1_9_1_2To1_9_3_4() {
        super(ClientboundPackets1_9_3.class, ClientboundPackets1_9.class, ServerboundPackets1_9_3.class, ServerboundPackets1_9.class);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol<ClientboundPackets1_9_3, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9_3.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (wrapper.get(Type.UNSIGNED_BYTE, 0) == 9) {
                            final Position position = wrapper.get(Type.POSITION, 0);
                            final CompoundTag tag = wrapper.get(Type.NBT, 0);
                            wrapper.clearPacket();
                            wrapper.setId(ClientboundPackets1_9.UPDATE_SIGN.ordinal());
                            wrapper.write(Type.POSITION, position);
                            for (int i = 1; i < 5; ++i) {
                                wrapper.write(Type.STRING, (String)tag.get("Text" + i).getValue());
                            }
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_9_3, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9_3.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final Chunk1_9_3_4Type newType = new Chunk1_9_3_4Type(clientWorld);
                        final Chunk1_9_1_2Type oldType = new Chunk1_9_1_2Type(clientWorld);
                        final Chunk chunk = wrapper.read((Type<Chunk>)newType);
                        wrapper.write((Type<Chunk>)oldType, chunk);
                        BlockEntity.handle(chunk.getBlockEntities(), wrapper.user());
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_9_3, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9_3.JOIN_GAME, new PacketRemapper() {
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
        ((Protocol<ClientboundPackets1_9_3, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9_3.RESPAWN, new PacketRemapper() {
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
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
    }
}
