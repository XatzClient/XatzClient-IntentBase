// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14;

import us.myles.ViaVersion.api.data.MappingData;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets.SoundPackets1_14;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets.PlayerPackets1_14;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets.EntityPackets1_14;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets.BlockItemPackets1_14;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_13_2To1_14 extends BackwardsProtocol<ClientboundPackets1_14, ClientboundPackets1_13, ServerboundPackets1_14, ServerboundPackets1_13>
{
    public static final BackwardsMappings MAPPINGS;
    private BlockItemPackets1_14 blockItemPackets;
    private EntityPackets1_14 entityPackets;
    
    public Protocol1_13_2To1_14() {
        super(ClientboundPackets1_14.class, ClientboundPackets1_13.class, ServerboundPackets1_14.class, ServerboundPackets1_13.class);
    }
    
    protected void registerPackets() {
        this.executeAsyncAfterLoaded((Class<? extends Protocol>)Protocol1_14To1_13_2.class, Protocol1_13_2To1_14.MAPPINGS::load);
        final TranslatableRewriter translatableRewriter = new TranslatableRewriter(this);
        translatableRewriter.registerBossBar((ClientboundPacketType)ClientboundPackets1_14.BOSSBAR);
        translatableRewriter.registerChatMessage((ClientboundPacketType)ClientboundPackets1_14.CHAT_MESSAGE);
        translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_14.COMBAT_EVENT);
        translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_14.DISCONNECT);
        translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_14.TAB_LIST);
        translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_14.TITLE);
        translatableRewriter.registerPing();
        (this.blockItemPackets = new BlockItemPackets1_14(this, translatableRewriter)).register();
        (this.entityPackets = new EntityPackets1_14(this)).register();
        new PlayerPackets1_14(this).register();
        new SoundPackets1_14(this).register();
        new StatisticsRewriter((Protocol)this, this.entityPackets::getOldEntityId).register((ClientboundPacketType)ClientboundPackets1_14.STATISTICS);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_14.UPDATE_VIEW_POSITION);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_14.UPDATE_VIEW_DISTANCE);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_14.ACKNOWLEDGE_PLAYER_DIGGING);
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.TAGS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int blockTagsSize = (int)wrapper.passthrough((Type)Type.VAR_INT), i = 0; i < blockTagsSize; ++i) {
                            wrapper.passthrough(Type.STRING);
                            final int[] blockIds = (int[])wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                            for (int j = 0; j < blockIds.length; ++j) {
                                final int id = blockIds[j];
                                final int blockId = Protocol1_13_2To1_14.this.getMappingData().getNewBlockId(id);
                                blockIds[j] = blockId;
                            }
                        }
                        for (int itemTagsSize = (int)wrapper.passthrough((Type)Type.VAR_INT), k = 0; k < itemTagsSize; ++k) {
                            wrapper.passthrough(Type.STRING);
                            final int[] itemIds = (int[])wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                            for (int l = 0; l < itemIds.length; ++l) {
                                final int itemId = itemIds[l];
                                final int oldId = Protocol1_13_2To1_14.this.getMappingData().getItemMappings().get(itemId);
                                itemIds[l] = oldId;
                            }
                        }
                        for (int fluidTagsSize = (int)wrapper.passthrough((Type)Type.VAR_INT), m = 0; m < fluidTagsSize; ++m) {
                            wrapper.passthrough(Type.STRING);
                            wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                        }
                        for (int entityTagsSize = (int)wrapper.read((Type)Type.VAR_INT), i2 = 0; i2 < entityTagsSize; ++i2) {
                            wrapper.read(Type.STRING);
                            wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                        }
                    }
                });
            }
        });
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.UPDATE_LIGHT, (ClientboundPacketType)null, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int x = (int)wrapper.read((Type)Type.VAR_INT);
                        final int z = (int)wrapper.read((Type)Type.VAR_INT);
                        final int skyLightMask = (int)wrapper.read((Type)Type.VAR_INT);
                        final int blockLightMask = (int)wrapper.read((Type)Type.VAR_INT);
                        final int emptySkyLightMask = (int)wrapper.read((Type)Type.VAR_INT);
                        final int emptyBlockLightMask = (int)wrapper.read((Type)Type.VAR_INT);
                        final byte[][] skyLight = new byte[16][];
                        if (this.isSet(skyLightMask, 0)) {
                            wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        }
                        for (int i = 0; i < 16; ++i) {
                            if (this.isSet(skyLightMask, i + 1)) {
                                skyLight[i] = (byte[])wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                            }
                            else if (this.isSet(emptySkyLightMask, i + 1)) {
                                skyLight[i] = ChunkLightStorage.EMPTY_LIGHT;
                            }
                        }
                        if (this.isSet(skyLightMask, 17)) {
                            wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        }
                        final byte[][] blockLight = new byte[16][];
                        if (this.isSet(blockLightMask, 0)) {
                            wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        }
                        for (int j = 0; j < 16; ++j) {
                            if (this.isSet(blockLightMask, j + 1)) {
                                blockLight[j] = (byte[])wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                            }
                            else if (this.isSet(emptyBlockLightMask, j + 1)) {
                                blockLight[j] = ChunkLightStorage.EMPTY_LIGHT;
                            }
                        }
                        if (this.isSet(blockLightMask, 17)) {
                            wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        }
                        ((ChunkLightStorage)wrapper.user().get((Class)ChunkLightStorage.class)).setStoredLight(skyLight, blockLight, x, z);
                        wrapper.cancel();
                    }
                    
                    private boolean isSet(final int mask, final int i) {
                        return (mask & 1 << i) != 0x0;
                    }
                });
            }
        });
    }
    
    public void init(final UserConnection user) {
        if (!user.has((Class)ClientWorld.class)) {
            user.put((StoredObject)new ClientWorld(user));
        }
        if (!user.has((Class)EntityTracker.class)) {
            user.put((StoredObject)new EntityTracker(user));
        }
        ((EntityTracker)user.get((Class)EntityTracker.class)).initProtocol(this);
        if (!user.has((Class)ChunkLightStorage.class)) {
            user.put((StoredObject)new ChunkLightStorage(user));
        }
    }
    
    public BlockItemPackets1_14 getBlockItemPackets() {
        return this.blockItemPackets;
    }
    
    public EntityPackets1_14 getEntityPackets() {
        return this.entityPackets;
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_13_2To1_14.MAPPINGS;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.14", "1.13.2", (Class<? extends Protocol>)Protocol1_14To1_13_2.class, true);
    }
}
