// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13to1_13_1;

import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.data.StoredObject;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.IdRewriteFunction;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.packets.WorldPackets1_13_1;
import nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.packets.InventoryPackets1_13_1;
import nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.packets.EntityPackets1_13_1;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_13To1_13_1 extends BackwardsProtocol<ClientboundPackets1_13, ClientboundPackets1_13, ServerboundPackets1_13, ServerboundPackets1_13>
{
    public static final BackwardsMappings MAPPINGS;
    
    public Protocol1_13To1_13_1() {
        super(ClientboundPackets1_13.class, ClientboundPackets1_13.class, ServerboundPackets1_13.class, ServerboundPackets1_13.class);
    }
    
    protected void registerPackets() {
        this.executeAsyncAfterLoaded((Class<? extends Protocol>)Protocol1_13_1To1_13.class, Protocol1_13To1_13_1.MAPPINGS::load);
        new EntityPackets1_13_1(this).register();
        InventoryPackets1_13_1.register(this);
        WorldPackets1_13_1.register(this);
        final TranslatableRewriter translatableRewriter = new TranslatableRewriter(this);
        translatableRewriter.registerChatMessage((ClientboundPacketType)ClientboundPackets1_13.CHAT_MESSAGE);
        translatableRewriter.registerLegacyOpenWindow((ClientboundPacketType)ClientboundPackets1_13.OPEN_WINDOW);
        translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_13.COMBAT_EVENT);
        translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_13.DISCONNECT);
        translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_13.TAB_LIST);
        translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_13.TITLE);
        translatableRewriter.registerPing();
        this.registerIncoming((ServerboundPacketType)ServerboundPackets1_13.TAB_COMPLETE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.STRING, (ValueTransformer)new ValueTransformer<String, String>(Type.STRING) {
                    public String transform(final PacketWrapper wrapper, final String inputValue) {
                        return inputValue.startsWith("/") ? inputValue : ("/" + inputValue);
                    }
                });
            }
        });
        this.registerIncoming((ServerboundPacketType)ServerboundPackets1_13.EDIT_BOOK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.FLAT_ITEM);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        InventoryPackets1_13_1.toServer((Item)wrapper.get(Type.FLAT_ITEM, 0));
                        wrapper.write((Type)Type.VAR_INT, (Object)0);
                    }
                });
            }
        });
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.TAB_COMPLETE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int start = (int)wrapper.get((Type)Type.VAR_INT, 1);
                        wrapper.set((Type)Type.VAR_INT, 1, (Object)(start - 1));
                        for (int count = (int)wrapper.get((Type)Type.VAR_INT, 3), i = 0; i < count; ++i) {
                            wrapper.passthrough(Type.STRING);
                            final boolean hasTooltip = (boolean)wrapper.passthrough(Type.BOOLEAN);
                            if (hasTooltip) {
                                wrapper.passthrough(Type.STRING);
                            }
                        }
                    }
                });
            }
        });
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.BOSSBAR, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int action = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        if (action == 0 || action == 3) {
                            translatableRewriter.processText((JsonElement)wrapper.passthrough(Type.COMPONENT));
                            if (action == 0) {
                                wrapper.passthrough((Type)Type.FLOAT);
                                wrapper.passthrough((Type)Type.VAR_INT);
                                wrapper.passthrough((Type)Type.VAR_INT);
                                short flags = (short)wrapper.read(Type.UNSIGNED_BYTE);
                                if ((flags & 0x4) != 0x0) {
                                    flags |= 0x2;
                                }
                                wrapper.write(Type.UNSIGNED_BYTE, (Object)flags);
                            }
                        }
                    }
                });
            }
        });
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.ADVANCEMENTS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough(Type.BOOLEAN);
                        for (int size = (int)wrapper.passthrough((Type)Type.VAR_INT), i = 0; i < size; ++i) {
                            wrapper.passthrough(Type.STRING);
                            if (wrapper.passthrough(Type.BOOLEAN)) {
                                wrapper.passthrough(Type.STRING);
                            }
                            if (wrapper.passthrough(Type.BOOLEAN)) {
                                wrapper.passthrough(Type.COMPONENT);
                                wrapper.passthrough(Type.COMPONENT);
                                final Item icon = (Item)wrapper.passthrough(Type.FLAT_ITEM);
                                InventoryPackets1_13_1.toClient(icon);
                                wrapper.passthrough((Type)Type.VAR_INT);
                                final int flags = (int)wrapper.passthrough(Type.INT);
                                if ((flags & 0x1) != 0x0) {
                                    wrapper.passthrough(Type.STRING);
                                }
                                wrapper.passthrough((Type)Type.FLOAT);
                                wrapper.passthrough((Type)Type.FLOAT);
                            }
                            wrapper.passthrough(Type.STRING_ARRAY);
                            for (int arrayLength = (int)wrapper.passthrough((Type)Type.VAR_INT), array = 0; array < arrayLength; ++array) {
                                wrapper.passthrough(Type.STRING_ARRAY);
                            }
                        }
                    }
                });
            }
        });
        new TagRewriter((Protocol)this, (IdRewriteFunction)null).register((ClientboundPacketType)ClientboundPackets1_13.TAGS);
        new StatisticsRewriter((Protocol)this, (IdRewriteFunction)null).register((ClientboundPacketType)ClientboundPackets1_13.STATISTICS);
    }
    
    public void init(final UserConnection user) {
        if (!user.has((Class)EntityTracker.class)) {
            user.put((StoredObject)new EntityTracker(user));
        }
        ((EntityTracker)user.get((Class)EntityTracker.class)).initProtocol(this);
        if (!user.has((Class)ClientWorld.class)) {
            user.put((StoredObject)new ClientWorld(user));
        }
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_13To1_13_1.MAPPINGS;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.13.2", "1.13", (Class<? extends Protocol>)Protocol1_13_1To1_13.class, true);
    }
}
