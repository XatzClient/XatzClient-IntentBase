// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_9_4to1_10.packets;

import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import nl.matsv.viabackwards.protocol.protocol1_9_4to1_10.Protocol1_9_4To1_10;
import nl.matsv.viabackwards.api.rewriters.LegacyBlockItemRewriter;

public class BlockItemPackets1_10 extends LegacyBlockItemRewriter<Protocol1_9_4To1_10>
{
    public BlockItemPackets1_10(final Protocol1_9_4To1_10 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        final ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
        itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_9_3.SET_SLOT, Type.ITEM);
        itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_9_3.WINDOW_ITEMS, Type.ITEM_ARRAY);
        itemRewriter.registerEntityEquipment((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_EQUIPMENT, Type.ITEM);
        ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (((String)wrapper.get(Type.STRING, 0)).equalsIgnoreCase("MC|TrList")) {
                            wrapper.passthrough(Type.INT);
                            for (int size = (short)wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                wrapper.write(Type.ITEM, (Object)BlockItemPackets1_10.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                                wrapper.write(Type.ITEM, (Object)BlockItemPackets1_10.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                                final boolean secondItem = (boolean)wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    wrapper.write(Type.ITEM, (Object)BlockItemPackets1_10.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                                }
                                wrapper.passthrough(Type.BOOLEAN);
                                wrapper.passthrough(Type.INT);
                                wrapper.passthrough(Type.INT);
                            }
                        }
                    }
                });
            }
        });
        itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_9_3.CLICK_WINDOW, Type.ITEM);
        itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
        ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.CHUNK_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
                        final Chunk1_9_3_4Type type = new Chunk1_9_3_4Type(clientWorld);
                        final Chunk chunk = (Chunk)wrapper.passthrough((Type)type);
                        LegacyBlockItemRewriter.this.handleChunk(chunk);
                    }
                });
            }
        });
        ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int idx = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        wrapper.set((Type)Type.VAR_INT, 0, (Object)BlockItemPackets1_10.this.handleBlockID(idx));
                    }
                });
            }
        });
        ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.MULTI_BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (final BlockChangeRecord record : (BlockChangeRecord[])wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                            record.setBlockId(BlockItemPackets1_10.this.handleBlockID(record.getBlockId()));
                        }
                    }
                });
            }
        });
        final Metadata data;
        ((Protocol1_9_4To1_10)this.protocol).getEntityPackets().registerMetaHandler().handle(e -> {
            data = e.getData();
            if (data.getMetaType().getType().equals(Type.ITEM)) {
                data.setValue((Object)this.handleItemToClient((Item)data.getValue()));
            }
            return data;
        });
        ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PARTICLE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = (int)wrapper.get(Type.INT, 0);
                        if (id == 46) {
                            wrapper.set(Type.INT, 0, (Object)38);
                        }
                    }
                });
            }
        });
    }
}
