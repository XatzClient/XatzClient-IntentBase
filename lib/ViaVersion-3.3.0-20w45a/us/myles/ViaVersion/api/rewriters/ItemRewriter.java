// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.rewriters;

import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.data.ParticleMappings;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;

public class ItemRewriter
{
    private final Protocol protocol;
    private final RewriteFunction toClient;
    private final RewriteFunction toServer;
    
    public ItemRewriter(final Protocol protocol, final RewriteFunction toClient, final RewriteFunction toServer) {
        this.protocol = protocol;
        this.toClient = toClient;
        this.toServer = toServer;
    }
    
    public void registerWindowItems(final ClientboundPacketType packetType, final Type<Item[]> type) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(type);
                this.handler(ItemRewriter.this.itemArrayHandler(type));
            }
        });
    }
    
    public void registerSetSlot(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(type);
                this.handler(ItemRewriter.this.itemToClientHandler(type));
            }
        });
    }
    
    public void registerEntityEquipment(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(type);
                this.handler(ItemRewriter.this.itemToClientHandler(type));
            }
        });
    }
    
    public void registerEntityEquipmentArray(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final Type<Item> val$type;
                byte slot;
                this.handler(wrapper -> {
                    val$type = type;
                    do {
                        slot = wrapper.passthrough(Type.BYTE);
                        ItemRewriter.this.toClient.rewrite(wrapper.passthrough(val$type));
                    } while ((slot & 0xFFFFFF80) != 0x0);
                });
            }
        });
    }
    
    public void registerCreativeInvAction(final ServerboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerIncoming(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.SHORT);
                this.map(type);
                this.handler(ItemRewriter.this.itemToServerHandler(type));
            }
        });
    }
    
    public void registerClickWindow(final ServerboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerIncoming(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.VAR_INT);
                this.map(type);
                this.handler(ItemRewriter.this.itemToServerHandler(type));
            }
        });
    }
    
    public void registerSetCooldown(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int itemId;
                this.handler(wrapper -> {
                    itemId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.VAR_INT, ItemRewriter.this.protocol.getMappingData().getNewItemId(itemId));
                });
            }
        });
    }
    
    public void registerTradeList(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Type<Item> val$type;
                int size;
                int i;
                this.handler(wrapper -> {
                    val$type = type;
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    for (size = wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                        ItemRewriter.this.toClient.rewrite(wrapper.passthrough(val$type));
                        ItemRewriter.this.toClient.rewrite(wrapper.passthrough(val$type));
                        if (wrapper.passthrough(Type.BOOLEAN)) {
                            ItemRewriter.this.toClient.rewrite(wrapper.passthrough(val$type));
                        }
                        wrapper.passthrough(Type.BOOLEAN);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough((Type<Object>)Type.FLOAT);
                        wrapper.passthrough(Type.INT);
                    }
                });
            }
        });
    }
    
    public void registerAdvancements(final ClientboundPacketType packetType, final Type<Item> type) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Type<Item> val$type;
                int size;
                int i;
                int flags;
                int arrayLength;
                int array;
                this.handler(wrapper -> {
                    val$type = type;
                    wrapper.passthrough(Type.BOOLEAN);
                    for (size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        wrapper.passthrough(Type.STRING);
                        if (wrapper.passthrough(Type.BOOLEAN)) {
                            wrapper.passthrough(Type.STRING);
                        }
                        if (wrapper.passthrough(Type.BOOLEAN)) {
                            wrapper.passthrough(Type.COMPONENT);
                            wrapper.passthrough(Type.COMPONENT);
                            ItemRewriter.this.toClient.rewrite(wrapper.passthrough(val$type));
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            flags = wrapper.passthrough(Type.INT);
                            if ((flags & 0x1) != 0x0) {
                                wrapper.passthrough(Type.STRING);
                            }
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                        }
                        wrapper.passthrough(Type.STRING_ARRAY);
                        for (arrayLength = wrapper.passthrough((Type<Integer>)Type.VAR_INT), array = 0; array < arrayLength; ++array) {
                            wrapper.passthrough(Type.STRING_ARRAY);
                        }
                    }
                });
            }
        });
    }
    
    public void registerSpawnParticle(final ClientboundPacketType packetType, final Type<Item> itemType, final Type<?> coordType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(coordType);
                this.map(coordType);
                this.map(coordType);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                final Type<Item> val$itemType;
                final int id;
                ParticleMappings mappings;
                int data;
                int newId;
                this.handler(wrapper -> {
                    val$itemType = itemType;
                    id = wrapper.get(Type.INT, 0);
                    if (id != -1) {
                        mappings = ItemRewriter.this.protocol.getMappingData().getParticleMappings();
                        if (id == mappings.getBlockId() || id == mappings.getFallingDustId()) {
                            data = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                            wrapper.set(Type.VAR_INT, 0, ItemRewriter.this.protocol.getMappingData().getNewBlockStateId(data));
                        }
                        else if (id == mappings.getItemId()) {
                            ItemRewriter.this.toClient.rewrite(wrapper.passthrough(val$itemType));
                        }
                        newId = ItemRewriter.this.protocol.getMappingData().getNewParticleId(id);
                        if (newId != id) {
                            wrapper.set(Type.INT, 0, newId);
                        }
                    }
                });
            }
        });
    }
    
    public PacketHandler itemArrayHandler(final Type<Item[]> type) {
        final Item[] array;
        final Item[] items;
        int length;
        int i = 0;
        Item item;
        return wrapper -> {
            items = (array = wrapper.get(type, 0));
            for (length = array.length; i < length; ++i) {
                item = array[i];
                this.toClient.rewrite(item);
            }
        };
    }
    
    public PacketHandler itemToClientHandler(final Type<Item> type) {
        return wrapper -> this.toClient.rewrite(wrapper.get(type, 0));
    }
    
    public PacketHandler itemToServerHandler(final Type<Item> type) {
        return wrapper -> this.toServer.rewrite(wrapper.get(type, 0));
    }
    
    @FunctionalInterface
    public interface RewriteFunction
    {
        void rewrite(final Item p0);
    }
}
