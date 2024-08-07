// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16;

import us.myles.ViaVersion.api.data.MappingData;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.storage.PlayerSneakStorage;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import java.util.UUID;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.packets.EntityPackets1_16;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.chat.TranslatableRewriter1_16;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.packets.BlockItemPackets1_16;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.data.BackwardsMappings;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_15_2To1_16 extends BackwardsProtocol<ClientboundPackets1_16, ClientboundPackets1_15, ServerboundPackets1_16, ServerboundPackets1_14>
{
    public static final BackwardsMappings MAPPINGS;
    private BlockItemPackets1_16 blockItemPackets;
    private TranslatableRewriter translatableRewriter;
    
    public Protocol1_15_2To1_16() {
        super(ClientboundPackets1_16.class, ClientboundPackets1_15.class, ServerboundPackets1_16.class, ServerboundPackets1_14.class);
    }
    
    protected void registerPackets() {
        this.executeAsyncAfterLoaded((Class<? extends Protocol>)Protocol1_16To1_15_2.class, Protocol1_15_2To1_16.MAPPINGS::load);
        (this.translatableRewriter = new TranslatableRewriter1_16(this)).registerBossBar((ClientboundPacketType)ClientboundPackets1_16.BOSSBAR);
        this.translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_16.COMBAT_EVENT);
        this.translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_16.DISCONNECT);
        this.translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_16.TAB_LIST);
        this.translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_16.TITLE);
        this.translatableRewriter.registerPing();
        (this.blockItemPackets = new BlockItemPackets1_16(this, this.translatableRewriter)).register();
        final EntityPackets1_16 entityPackets = new EntityPackets1_16(this);
        entityPackets.register();
        this.registerOutgoing(State.STATUS, 0, 0, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final String original = (String)wrapper.passthrough(Type.STRING);
                    final JsonObject object = (JsonObject)GsonUtil.getGson().fromJson(original, (Class)JsonObject.class);
                    final JsonElement description = object.get("description");
                    if (description == null) {
                        return;
                    }
                    Protocol1_15_2To1_16.this.translatableRewriter.processText(description);
                    wrapper.set(Type.STRING, 0, (Object)object.toString());
                });
            }
        });
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.CHAT_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> Protocol1_15_2To1_16.this.translatableRewriter.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
                this.map(Type.BYTE);
                this.map(Type.UUID, Type.NOTHING);
            }
        });
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.OPEN_WINDOW, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.handler(wrapper -> Protocol1_15_2To1_16.this.translatableRewriter.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
                this.handler(wrapper -> {
                    int windowType = (int)wrapper.get((Type)Type.VAR_INT, 1);
                    if (windowType == 20) {
                        wrapper.set((Type)Type.VAR_INT, 1, (Object)7);
                    }
                    else if (windowType > 20) {
                        wrapper.set((Type)Type.VAR_INT, 1, (Object)(--windowType));
                    }
                });
            }
        });
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16.SOUND);
        soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16.ENTITY_SOUND);
        soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_16.NAMED_SOUND);
        soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_16.STOP_SOUND);
        this.registerOutgoing(State.LOGIN, 2, 2, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final UUID uuid = (UUID)wrapper.read(Type.UUID_INT_ARRAY);
                    wrapper.write(Type.STRING, (Object)uuid.toString());
                });
            }
        });
        new TagRewriter((Protocol)this, entityPackets::getOldEntityId).register((ClientboundPacketType)ClientboundPackets1_16.TAGS);
        new StatisticsRewriter((Protocol)this, entityPackets::getOldEntityId).register((ClientboundPacketType)ClientboundPackets1_16.STATISTICS);
        this.registerIncoming((ServerboundPacketType)ServerboundPackets1_14.ENTITY_ACTION, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough((Type)Type.VAR_INT);
                    final int action = (int)wrapper.passthrough((Type)Type.VAR_INT);
                    if (action == 0) {
                        ((PlayerSneakStorage)wrapper.user().get((Class)PlayerSneakStorage.class)).setSneaking(true);
                    }
                    else if (action == 1) {
                        ((PlayerSneakStorage)wrapper.user().get((Class)PlayerSneakStorage.class)).setSneaking(false);
                    }
                });
            }
        });
        this.registerIncoming((ServerboundPacketType)ServerboundPackets1_14.INTERACT_ENTITY, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough((Type)Type.VAR_INT);
                    final int action = (int)wrapper.passthrough((Type)Type.VAR_INT);
                    if (action == 0 || action == 2) {
                        if (action == 2) {
                            wrapper.passthrough((Type)Type.FLOAT);
                            wrapper.passthrough((Type)Type.FLOAT);
                            wrapper.passthrough((Type)Type.FLOAT);
                        }
                        wrapper.passthrough((Type)Type.VAR_INT);
                    }
                    wrapper.write(Type.BOOLEAN, (Object)((PlayerSneakStorage)wrapper.user().get((Class)PlayerSneakStorage.class)).isSneaking());
                });
            }
        });
        this.registerIncoming((ServerboundPacketType)ServerboundPackets1_14.PLAYER_ABILITIES, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    byte flags = (byte)wrapper.read(Type.BYTE);
                    flags &= 0x2;
                    wrapper.write(Type.BYTE, (Object)flags);
                    wrapper.read((Type)Type.FLOAT);
                    wrapper.read((Type)Type.FLOAT);
                });
            }
        });
        this.cancelIncoming((ServerboundPacketType)ServerboundPackets1_14.UPDATE_JIGSAW_BLOCK);
    }
    
    public void init(final UserConnection user) {
        if (!user.has((Class)ClientWorld.class)) {
            user.put((StoredObject)new ClientWorld(user));
        }
        if (!user.has((Class)EntityTracker.class)) {
            user.put((StoredObject)new EntityTracker(user));
        }
        user.put((StoredObject)new PlayerSneakStorage(user));
        ((EntityTracker)user.get((Class)EntityTracker.class)).initProtocol(this);
    }
    
    public BlockItemPackets1_16 getBlockItemPackets() {
        return this.blockItemPackets;
    }
    
    public TranslatableRewriter getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_15_2To1_16.MAPPINGS;
    }
    
    static {
        MAPPINGS = new BackwardsMappings();
    }
}
