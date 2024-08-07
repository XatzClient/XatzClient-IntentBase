// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.packets;

import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ItemRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.InventoryTracker;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_8.ClientboundPackets1_8;
import us.myles.ViaVersion.api.protocol.Protocol;

public class InventoryPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(ClientboundPackets1_8.WINDOW_PROPERTY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short windowId = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        final short property = wrapper.get((Type<Short>)Type.SHORT, 0);
                        final short value = wrapper.get((Type<Short>)Type.SHORT, 1);
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        if (inventoryTracker.getInventory() != null && inventoryTracker.getInventory().equalsIgnoreCase("minecraft:enchanting_table") && property > 3 && property < 7) {
                            final short level = (short)(value >> 8);
                            final short enchantID = (short)(value & 0xFF);
                            wrapper.create(wrapper.getId(), new ValueCreator() {
                                @Override
                                public void write(final PacketWrapper wrapper) throws Exception {
                                    wrapper.write(Type.UNSIGNED_BYTE, windowId);
                                    wrapper.write(Type.SHORT, property);
                                    wrapper.write(Type.SHORT, enchantID);
                                }
                            }).send(Protocol1_9To1_8.class);
                            wrapper.set(Type.SHORT, 0, (short)(property + 3));
                            wrapper.set(Type.SHORT, 1, level);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String inventory = wrapper.get(Type.STRING, 0);
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        inventoryTracker.setInventory(inventory);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String inventory = wrapper.get(Type.STRING, 0);
                        if (inventory.equals("minecraft:brewing_stand")) {
                            wrapper.set(Type.UNSIGNED_BYTE, 1, (short)(wrapper.get(Type.UNSIGNED_BYTE, 1) + 1));
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.SET_SLOT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.ITEM);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item stack = wrapper.get(Type.ITEM, 0);
                        ItemRewriter.toClient(stack);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        final short slotID = wrapper.get((Type<Short>)Type.SHORT, 0);
                        if (inventoryTracker.getInventory() != null && inventoryTracker.getInventory().equals("minecraft:brewing_stand") && slotID >= 4) {
                            wrapper.set(Type.SHORT, 0, (short)(slotID + 1));
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.WINDOW_ITEMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.ITEM_ARRAY);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item[] array;
                        final Item[] stacks = array = wrapper.get(Type.ITEM_ARRAY, 0);
                        for (final Item stack : array) {
                            ItemRewriter.toClient(stack);
                        }
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        if (inventoryTracker.getInventory() != null && inventoryTracker.getInventory().equals("minecraft:brewing_stand")) {
                            final Item[] oldStack = wrapper.get(Type.ITEM_ARRAY, 0);
                            final Item[] newStack = new Item[oldStack.length + 1];
                            for (int i = 0; i < newStack.length; ++i) {
                                if (i > 4) {
                                    newStack[i] = oldStack[i - 1];
                                }
                                else if (i != 4) {
                                    newStack[i] = oldStack[i];
                                }
                            }
                            wrapper.set(Type.ITEM_ARRAY, 0, newStack);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        inventoryTracker.setInventory(null);
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.MAP_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.create(new ValueCreator() {
                    @Override
                    public void write(final PacketWrapper wrapper) {
                        wrapper.write(Type.BOOLEAN, true);
                    }
                });
            }
        });
        protocol.registerIncoming(ServerboundPackets1_9.CREATIVE_INVENTORY_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.SHORT);
                this.map(Type.ITEM);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item stack = wrapper.get(Type.ITEM, 0);
                        ItemRewriter.toServer(stack);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short slot = wrapper.get((Type<Short>)Type.SHORT, 0);
                        final boolean throwItem = slot == 45;
                        if (throwItem) {
                            wrapper.create(22, new ValueCreator() {
                                @Override
                                public void write(final PacketWrapper wrapper) throws Exception {
                                    wrapper.write(Type.BYTE, (Byte)0);
                                    wrapper.write(Type.SHORT, slot);
                                    wrapper.write(Type.ITEM, null);
                                }
                            }).send(Protocol1_9To1_8.class);
                            wrapper.set(Type.SHORT, 0, (Short)(-999));
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(ServerboundPackets1_9.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.VAR_INT, Type.BYTE);
                this.map(Type.ITEM);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item stack = wrapper.get(Type.ITEM, 0);
                        ItemRewriter.toServer(stack);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short windowID = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        final short slot = wrapper.get((Type<Short>)Type.SHORT, 0);
                        boolean throwItem = slot == 45 && windowID == 0;
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        if (inventoryTracker.getInventory() != null && inventoryTracker.getInventory().equals("minecraft:brewing_stand")) {
                            if (slot == 4) {
                                throwItem = true;
                            }
                            if (slot > 4) {
                                wrapper.set(Type.SHORT, 0, (short)(slot - 1));
                            }
                        }
                        if (throwItem) {
                            wrapper.create(22, new ValueCreator() {
                                @Override
                                public void write(final PacketWrapper wrapper) throws Exception {
                                    wrapper.write(Type.BYTE, (byte)windowID);
                                    wrapper.write(Type.SHORT, slot);
                                    wrapper.write(Type.ITEM, null);
                                }
                            }).send(Protocol1_9To1_8.class);
                            wrapper.set(Type.BYTE, 0, (Byte)0);
                            wrapper.set(Type.BYTE, 1, (Byte)0);
                            wrapper.set(Type.SHORT, 0, (Short)(-999));
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(ServerboundPackets1_9.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        inventoryTracker.setInventory(null);
                    }
                });
            }
        });
        protocol.registerIncoming(ServerboundPackets1_9.HELD_ITEM_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final EntityTracker1_9 entityTracker = wrapper.user().get(EntityTracker1_9.class);
                        if (entityTracker.isBlocking()) {
                            entityTracker.setBlocking(false);
                            entityTracker.setSecondHand(null);
                        }
                    }
                });
            }
        });
    }
}
