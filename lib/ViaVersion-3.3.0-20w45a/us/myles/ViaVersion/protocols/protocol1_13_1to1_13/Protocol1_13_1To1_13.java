// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13_1to1_13;

import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage.EntityTracker1_13;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.rewriters.IdRewriteFunction;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13_1to1_13.packets.WorldPackets;
import us.myles.ViaVersion.protocols.protocol1_13_1to1_13.packets.InventoryPackets;
import us.myles.ViaVersion.protocols.protocol1_13_1to1_13.packets.EntityPackets;
import us.myles.ViaVersion.protocols.protocol1_13_1to1_13.metadata.MetadataRewriter1_13_1To1_13;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_13_1To1_13 extends Protocol<ClientboundPackets1_13, ClientboundPackets1_13, ServerboundPackets1_13, ServerboundPackets1_13>
{
    public static final MappingData MAPPINGS;
    
    public Protocol1_13_1To1_13() {
        super(ClientboundPackets1_13.class, ClientboundPackets1_13.class, ServerboundPackets1_13.class, ServerboundPackets1_13.class);
    }
    
    @Override
    protected void registerPackets() {
        new MetadataRewriter1_13_1To1_13(this);
        EntityPackets.register(this);
        InventoryPackets.register(this);
        WorldPackets.register(this);
        ((Protocol<C1, C2, S1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.STRING, (ValueTransformer<String, Object>)new ValueTransformer<String, String>(Type.STRING) {
                    @Override
                    public String transform(final PacketWrapper wrapper, final String inputValue) {
                        return inputValue.startsWith("/") ? inputValue.substring(1) : inputValue;
                    }
                });
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLAT_ITEM);
                this.map(Type.BOOLEAN);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item item = wrapper.get(Type.FLAT_ITEM, 0);
                        InventoryPackets.toServer(item);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                        if (hand == 1) {
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_13.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int start = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                        wrapper.set(Type.VAR_INT, 1, start + 1);
                        for (int count = wrapper.get((Type<Integer>)Type.VAR_INT, 3), i = 0; i < count; ++i) {
                            wrapper.passthrough(Type.STRING);
                            final boolean hasTooltip = wrapper.passthrough(Type.BOOLEAN);
                            if (hasTooltip) {
                                wrapper.passthrough(Type.STRING);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_13.BOSSBAR, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (action == 0) {
                            wrapper.passthrough(Type.COMPONENT);
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            short flags = wrapper.read(Type.BYTE);
                            if ((flags & 0x2) != 0x0) {
                                flags |= 0x4;
                            }
                            wrapper.write(Type.UNSIGNED_BYTE, flags);
                        }
                    }
                });
            }
        });
        new TagRewriter(this, null).register(ClientboundPackets1_13.TAGS);
        new StatisticsRewriter(this, null).register(ClientboundPackets1_13.STATISTICS);
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.put(new EntityTracker1_13(userConnection));
        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_13_1To1_13.MAPPINGS;
    }
    
    static {
        MAPPINGS = new MappingData("1.13", "1.13.2", true);
    }
}
