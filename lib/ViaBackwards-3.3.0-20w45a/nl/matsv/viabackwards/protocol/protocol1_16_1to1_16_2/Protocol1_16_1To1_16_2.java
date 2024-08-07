// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2;

import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.data.StoredObject;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.packets.EntityPackets1_16_2;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.packets.BlockItemPackets1_16_2;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_16_1To1_16_2 extends BackwardsProtocol<ClientboundPackets1_16_2, ClientboundPackets1_16, ServerboundPackets1_16_2, ServerboundPackets1_16>
{
    public static final BackwardsMappings MAPPINGS;
    private BlockItemPackets1_16_2 blockItemPackets;
    private TranslatableRewriter translatableRewriter;
    
    public Protocol1_16_1To1_16_2() {
        super(ClientboundPackets1_16_2.class, ClientboundPackets1_16.class, ServerboundPackets1_16_2.class, ServerboundPackets1_16.class);
    }
    
    protected void registerPackets() {
        this.executeAsyncAfterLoaded((Class<? extends Protocol>)Protocol1_16_2To1_16_1.class, Protocol1_16_1To1_16_2.MAPPINGS::load);
        (this.translatableRewriter = new TranslatableRewriter(this)).registerBossBar((ClientboundPacketType)ClientboundPackets1_16_2.BOSSBAR);
        this.translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_16_2.COMBAT_EVENT);
        this.translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_16_2.DISCONNECT);
        this.translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_16_2.TAB_LIST);
        this.translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_16_2.TITLE);
        this.translatableRewriter.registerOpenWindow((ClientboundPacketType)ClientboundPackets1_16_2.OPEN_WINDOW);
        this.translatableRewriter.registerPing();
        (this.blockItemPackets = new BlockItemPackets1_16_2(this, this.translatableRewriter)).register();
        final EntityPackets1_16_2 entityPackets = new EntityPackets1_16_2(this);
        entityPackets.register();
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16_2.SOUND);
        soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16_2.ENTITY_SOUND);
        soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_16_2.NAMED_SOUND);
        soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_16_2.STOP_SOUND);
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.CHAT_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final JsonElement message = (JsonElement)wrapper.passthrough(Type.COMPONENT);
                    Protocol1_16_1To1_16_2.this.translatableRewriter.processText(message);
                    final byte position = (byte)wrapper.passthrough(Type.BYTE);
                    if (position == 2) {
                        wrapper.clearPacket();
                        wrapper.setId(ClientboundPackets1_16.TITLE.ordinal());
                        wrapper.write((Type)Type.VAR_INT, (Object)2);
                        wrapper.write(Type.COMPONENT, (Object)message);
                    }
                });
            }
        });
        this.registerIncoming((ServerboundPacketType)ServerboundPackets1_16.RECIPE_BOOK_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = (int)wrapper.read((Type)Type.VAR_INT);
                        if (type == 0) {
                            wrapper.passthrough(Type.STRING);
                            wrapper.setId(ServerboundPackets1_16_2.SEEN_RECIPE.ordinal());
                        }
                        else {
                            wrapper.cancel();
                            for (int i = 0; i < 3; ++i) {
                                this.sendSeenRecipePacket(i, wrapper);
                            }
                        }
                    }
                    
                    private void sendSeenRecipePacket(final int recipeType, final PacketWrapper wrapper) throws Exception {
                        final boolean open = (boolean)wrapper.read(Type.BOOLEAN);
                        final boolean filter = (boolean)wrapper.read(Type.BOOLEAN);
                        final PacketWrapper newPacket = wrapper.create(ServerboundPackets1_16_2.RECIPE_BOOK_DATA.ordinal());
                        newPacket.write((Type)Type.VAR_INT, (Object)recipeType);
                        newPacket.write(Type.BOOLEAN, (Object)open);
                        newPacket.write(Type.BOOLEAN, (Object)filter);
                        newPacket.sendToServer((Class)Protocol1_16_1To1_16_2.class);
                    }
                });
            }
        });
        new TagRewriter((Protocol)this, entityPackets::getOldEntityId).register((ClientboundPacketType)ClientboundPackets1_16_2.TAGS);
        new StatisticsRewriter((Protocol)this, entityPackets::getOldEntityId).register((ClientboundPacketType)ClientboundPackets1_16_2.STATISTICS);
    }
    
    public void init(final UserConnection user) {
        if (!user.has((Class)EntityTracker.class)) {
            user.put((StoredObject)new EntityTracker(user));
        }
        ((EntityTracker)user.get((Class)EntityTracker.class)).initProtocol(this);
    }
    
    public BlockItemPackets1_16_2 getBlockItemPackets() {
        return this.blockItemPackets;
    }
    
    public TranslatableRewriter getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_16_1To1_16_2.MAPPINGS;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.16.2", "1.16", (Class<? extends Protocol>)Protocol1_16_2To1_16_1.class, true);
    }
}
