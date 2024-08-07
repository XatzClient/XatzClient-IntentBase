// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets;

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
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data.MapColorMapping;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import nl.matsv.viabackwards.api.rewriters.LegacyBlockItemRewriter;

public class BlockItemPackets1_12 extends LegacyBlockItemRewriter<Protocol1_11_1To1_12>
{
    public BlockItemPackets1_12(final Protocol1_11_1To1_12 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.MAP_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int count = (int)wrapper.passthrough((Type)Type.VAR_INT), i = 0; i < count * 3; ++i) {
                            wrapper.passthrough(Type.BYTE);
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short columns = (short)wrapper.passthrough(Type.UNSIGNED_BYTE);
                        if (columns <= 0) {
                            return;
                        }
                        final short rows = (short)wrapper.passthrough(Type.UNSIGNED_BYTE);
                        wrapper.passthrough(Type.UNSIGNED_BYTE);
                        wrapper.passthrough(Type.UNSIGNED_BYTE);
                        final byte[] data = (byte[])wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        for (int i = 0; i < data.length; ++i) {
                            short color = (short)(data[i] & 0xFF);
                            if (color > 143) {
                                color = (short)MapColorMapping.getNearestOldColor(color);
                                data[i] = (byte)color;
                            }
                        }
                        wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, (Object)data);
                    }
                });
            }
        });
        final ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
        itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_12.SET_SLOT, Type.ITEM);
        itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_12.WINDOW_ITEMS, Type.ITEM_ARRAY);
        itemRewriter.registerEntityEquipment((ClientboundPacketType)ClientboundPackets1_12.ENTITY_EQUIPMENT, Type.ITEM);
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (((String)wrapper.get(Type.STRING, 0)).equalsIgnoreCase("MC|TrList")) {
                            wrapper.passthrough(Type.INT);
                            for (int size = (short)wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                wrapper.write(Type.ITEM, (Object)BlockItemPackets1_12.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                                wrapper.write(Type.ITEM, (Object)BlockItemPackets1_12.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                                final boolean secondItem = (boolean)wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    wrapper.write(Type.ITEM, (Object)BlockItemPackets1_12.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
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
        ((Protocol1_11_1To1_12)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_9_3.CLICK_WINDOW, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.VAR_INT);
                this.map(Type.ITEM);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if ((int)wrapper.get((Type)Type.VAR_INT, 0) == 1) {
                            wrapper.set(Type.ITEM, 0, (Object)null);
                            final PacketWrapper confirm = wrapper.create(6);
                            confirm.write(Type.BYTE, (Object)((Short)wrapper.get(Type.UNSIGNED_BYTE, 0)).byteValue());
                            confirm.write((Type)Type.SHORT, wrapper.get((Type)Type.SHORT, 1));
                            confirm.write(Type.BOOLEAN, (Object)false);
                            wrapper.sendToServer((Class)Protocol1_11_1To1_12.class, true, true);
                            wrapper.cancel();
                            confirm.sendToServer((Class)Protocol1_11_1To1_12.class, true, true);
                            return;
                        }
                        final Item item = (Item)wrapper.get(Type.ITEM, 0);
                        BlockItemPackets1_12.this.handleItemToServer(item);
                    }
                });
            }
        });
        itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.CHUNK_DATA, (PacketRemapper)new PacketRemapper() {
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
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int idx = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        wrapper.set((Type)Type.VAR_INT, 0, (Object)BlockItemPackets1_12.this.handleBlockID(idx));
                    }
                });
            }
        });
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.MULTI_BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (final BlockChangeRecord record : (BlockChangeRecord[])wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                            record.setBlockId(BlockItemPackets1_12.this.handleBlockID(record.getBlockId()));
                        }
                    }
                });
            }
        });
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.BLOCK_ENTITY_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if ((short)wrapper.get(Type.UNSIGNED_BYTE, 0) == 11) {
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
        final Metadata data;
        ((Protocol1_11_1To1_12)this.protocol).getEntityPackets().registerMetaHandler().handle(e -> {
            data = e.getData();
            if (data.getMetaType().getType().equals(Type.ITEM)) {
                data.setValue((Object)this.handleItemToClient((Item)data.getValue()));
            }
            return data;
        });
        ((Protocol1_11_1To1_12)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_9_3.CLIENT_STATUS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if ((int)wrapper.get((Type)Type.VAR_INT, 0) == 2) {
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
    }
}
