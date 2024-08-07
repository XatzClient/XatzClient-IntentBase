// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_12to1_11_1;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.storage.EntityTracker1_12;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.providers.InventoryQuickMoveProvider;
import us.myles.ViaVersion.api.platform.providers.ViaProviders;
import us.myles.ViaVersion.api.rewriters.SoundRewriter;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.types.version.Types1_12;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.packets.InventoryPackets;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.metadata.MetadataRewriter1_12To1_11_1;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_12To1_11_1 extends Protocol<ClientboundPackets1_9_3, ClientboundPackets1_12, ServerboundPackets1_9_3, ServerboundPackets1_12>
{
    public Protocol1_12To1_11_1() {
        super(ClientboundPackets1_9_3.class, ClientboundPackets1_12.class, ServerboundPackets1_9_3.class, ServerboundPackets1_12.class);
    }
    
    @Override
    protected void registerPackets() {
        final MetadataRewriter metadataRewriter = new MetadataRewriter1_12To1_11_1(this);
        InventoryPackets.register(this);
        ((Protocol<ClientboundPackets1_9_3, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9_3.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.BYTE);
                this.handler(metadataRewriter.getObjectTracker());
            }
        });
        ((Protocol<ClientboundPackets1_9_3, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9_3.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Types1_12.METADATA_LIST);
                this.handler(metadataRewriter.getTrackerAndRewriter(Types1_12.METADATA_LIST));
            }
        });
        ((Protocol<ClientboundPackets1_9_3, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9_3.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (!Via.getConfig().is1_12NBTArrayFix()) {
                            return;
                        }
                        try {
                            final JsonElement obj = Protocol1_9To1_8.FIX_JSON.transform(null, wrapper.passthrough(Type.COMPONENT).toString());
                            TranslateRewriter.toClient(obj, wrapper.user());
                            ChatItemRewriter.toClient(obj, wrapper.user());
                            wrapper.set(Type.COMPONENT, 0, obj);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
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
                        final Chunk1_9_3_4Type type = new Chunk1_9_3_4Type(clientWorld);
                        final Chunk chunk = wrapper.passthrough((Type<Chunk>)type);
                        for (int i = 0; i < chunk.getSections().length; ++i) {
                            final ChunkSection section = chunk.getSections()[i];
                            if (section != null) {
                                for (int y = 0; y < 16; ++y) {
                                    for (int z = 0; z < 16; ++z) {
                                        for (int x = 0; x < 16; ++x) {
                                            final int block = section.getBlockId(x, y, z);
                                            if (block == 26) {
                                                final CompoundTag tag = new CompoundTag("");
                                                tag.put(new IntTag("color", 14));
                                                tag.put(new IntTag("x", x + (chunk.getX() << 4)));
                                                tag.put(new IntTag("y", y + (i << 4)));
                                                tag.put(new IntTag("z", z + (chunk.getZ() << 4)));
                                                tag.put(new StringTag("id", "minecraft:bed"));
                                                chunk.getBlockEntities().add(tag);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
        metadataRewriter.registerEntityDestroy(ClientboundPackets1_9_3.DESTROY_ENTITIES);
        metadataRewriter.registerMetadataRewriter(ClientboundPackets1_9_3.ENTITY_METADATA, Types1_12.METADATA_LIST);
        ((Protocol<ClientboundPackets1_9_3, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9_3.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                final ClientWorld clientChunks;
                final int dimensionId;
                this.handler(wrapper -> {
                    clientChunks = wrapper.user().get(ClientWorld.class);
                    dimensionId = wrapper.get(Type.INT, 1);
                    clientChunks.setEnvironment(dimensionId);
                });
            }
        });
        ((Protocol<ClientboundPackets1_9_3, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9_3.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                final ClientWorld clientWorld;
                final int dimensionId;
                this.handler(wrapper -> {
                    clientWorld = wrapper.user().get(ClientWorld.class);
                    dimensionId = wrapper.get(Type.INT, 0);
                    clientWorld.setEnvironment(dimensionId);
                });
            }
        });
        new SoundRewriter(this, this::getNewSoundId).registerSound(ClientboundPackets1_9_3.SOUND);
        ((Protocol<C1, C2, S1, ServerboundPackets1_12>)this).cancelIncoming(ServerboundPackets1_12.PREPARE_CRAFTING_GRID);
        ((Protocol<C1, C2, S1, ServerboundPackets1_12>)this).registerIncoming(ServerboundPackets1_12.CLIENT_SETTINGS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String locale = wrapper.get(Type.STRING, 0);
                        if (locale.length() > 7) {
                            wrapper.set(Type.STRING, 0, locale.substring(0, 7));
                        }
                    }
                });
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_12>)this).cancelIncoming(ServerboundPackets1_12.RECIPE_BOOK_DATA);
        ((Protocol<C1, C2, S1, ServerboundPackets1_12>)this).cancelIncoming(ServerboundPackets1_12.ADVANCEMENT_TAB);
    }
    
    private int getNewSoundId(final int id) {
        int newId = id;
        if (id >= 26) {
            newId += 2;
        }
        if (id >= 70) {
            newId += 4;
        }
        if (id >= 74) {
            ++newId;
        }
        if (id >= 143) {
            newId += 3;
        }
        if (id >= 185) {
            ++newId;
        }
        if (id >= 263) {
            newId += 7;
        }
        if (id >= 301) {
            newId += 33;
        }
        if (id >= 317) {
            newId += 2;
        }
        if (id >= 491) {
            newId += 3;
        }
        return newId;
    }
    
    @Override
    protected void register(final ViaProviders providers) {
        providers.register(InventoryQuickMoveProvider.class, new InventoryQuickMoveProvider());
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.put(new EntityTracker1_12(userConnection));
        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
    }
}
