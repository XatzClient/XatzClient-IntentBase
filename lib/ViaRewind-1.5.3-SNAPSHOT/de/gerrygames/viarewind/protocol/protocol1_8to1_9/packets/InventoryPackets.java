// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import us.myles.ViaVersion.api.type.types.ShortType;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ItemRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonElement;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Windows;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class InventoryPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 17, 50);
        protocol.registerOutgoing(State.PLAY, 18, 46, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowsId = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        ((Windows)packetWrapper.user().get((Class)Windows.class)).remove(windowsId);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 19, 45, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.COMPONENT);
                this.map(Type.UNSIGNED_BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final String type = (String)packetWrapper.get(Type.STRING, 0);
                        if (type.equals("EntityHorse")) {
                            packetWrapper.passthrough(Type.INT);
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowId = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        final String windowType = (String)packetWrapper.get(Type.STRING, 0);
                        ((Windows)packetWrapper.user().get((Class)Windows.class)).put(windowId, windowType);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        String type = (String)packetWrapper.get(Type.STRING, 0);
                        if (type.equalsIgnoreCase("minecraft:shulker_box")) {
                            packetWrapper.set(Type.STRING, 0, (Object)(type = "minecraft:container"));
                        }
                        final String name = ((JsonElement)packetWrapper.get(Type.COMPONENT, 0)).toString();
                        if (name.equalsIgnoreCase("{\"translate\":\"container.shulkerBox\"}")) {
                            packetWrapper.set(Type.COMPONENT, 0, (Object)GsonUtil.getJsonParser().parse("{\"text\":\"Shulker Box\"}"));
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 20, 48, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowId = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        Item[] items = (Item[])packetWrapper.read(Type.ITEM_ARRAY);
                        for (int i = 0; i < items.length; ++i) {
                            items[i] = ItemRewriter.toClient(items[i]);
                        }
                        if (windowId == 0 && items.length == 46) {
                            final Item[] old = items;
                            items = new Item[45];
                            System.arraycopy(old, 0, items, 0, 45);
                        }
                        else {
                            final String type = ((Windows)packetWrapper.user().get((Class)Windows.class)).get(windowId);
                            if (type != null && type.equalsIgnoreCase("minecraft:brewing_stand")) {
                                System.arraycopy(items, 0, ((Windows)packetWrapper.user().get((Class)Windows.class)).getBrewingItems(windowId), 0, 4);
                                Windows.updateBrewingStand(packetWrapper.user(), items[4], windowId);
                                final Item[] old2 = items;
                                items = new Item[old2.length - 1];
                                System.arraycopy(old2, 0, items, 0, 4);
                                System.arraycopy(old2, 5, items, 4, old2.length - 5);
                            }
                        }
                        packetWrapper.write(Type.ITEM_ARRAY, (Object)items);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 21, 49);
        protocol.registerOutgoing(State.PLAY, 22, 47, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.ITEM);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.set(Type.ITEM, 0, (Object)ItemRewriter.toClient((Item)packetWrapper.get(Type.ITEM, 0)));
                        final byte windowId = (byte)packetWrapper.get(Type.BYTE, 0);
                        short slot = (short)packetWrapper.get((Type)Type.SHORT, 0);
                        if (windowId == 0 && slot == 45) {
                            packetWrapper.cancel();
                            return;
                        }
                        final String type = ((Windows)packetWrapper.user().get((Class)Windows.class)).get(windowId);
                        if (type == null) {
                            return;
                        }
                        if (type.equalsIgnoreCase("minecraft:brewing_stand")) {
                            if (slot > 4) {
                                final ShortType short1 = Type.SHORT;
                                final int n = 0;
                                --slot;
                                packetWrapper.set((Type)short1, n, (Object)slot);
                            }
                            else {
                                if (slot == 4) {
                                    packetWrapper.cancel();
                                    Windows.updateBrewingStand(packetWrapper.user(), (Item)packetWrapper.get(Type.ITEM, 0), windowId);
                                    return;
                                }
                                ((Windows)packetWrapper.user().get((Class)Windows.class)).getBrewingItems(windowId)[slot] = (Item)packetWrapper.get(Type.ITEM, 0);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 8, 13, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowsId = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        ((Windows)packetWrapper.user().get((Class)Windows.class)).remove(windowsId);
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 7, 14, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.BYTE, (Type)Type.VAR_INT);
                this.map(Type.ITEM);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.set(Type.ITEM, 0, (Object)ItemRewriter.toServer((Item)packetWrapper.get(Type.ITEM, 0)));
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowId = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        final Windows windows = (Windows)packetWrapper.user().get((Class)Windows.class);
                        final String type = windows.get(windowId);
                        if (type == null) {
                            return;
                        }
                        if (type.equalsIgnoreCase("minecraft:brewing_stand")) {
                            short slot = (short)packetWrapper.get((Type)Type.SHORT, 0);
                            if (slot > 3) {
                                final ShortType short1 = Type.SHORT;
                                final int n = 0;
                                ++slot;
                                packetWrapper.set((Type)short1, n, (Object)slot);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 24, 16, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.SHORT);
                this.map(Type.ITEM);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.set(Type.ITEM, 0, (Object)ItemRewriter.toServer((Item)packetWrapper.get(Type.ITEM, 0)));
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 6, 17);
    }
}
