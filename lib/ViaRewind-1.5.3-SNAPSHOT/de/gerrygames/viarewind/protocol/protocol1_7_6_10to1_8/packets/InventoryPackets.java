// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import java.util.UUID;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.utils.ChatUtil;
import us.myles.viaversion.libs.gson.JsonElement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Windows;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class InventoryPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 45, 45, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowId = (short)packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                        final String windowType = (String)packetWrapper.read(Type.STRING);
                        final short windowtypeId = (short)Windows.getInventoryType(windowType);
                        ((Windows)packetWrapper.user().get((Class)Windows.class)).types.put(windowId, windowtypeId);
                        packetWrapper.write(Type.UNSIGNED_BYTE, (Object)windowtypeId);
                        final JsonElement titleComponent = (JsonElement)packetWrapper.read(Type.COMPONENT);
                        String title = ChatUtil.jsonToLegacy(titleComponent);
                        title = ChatUtil.removeUnusedColor(title, '8');
                        if (title.length() > 32) {
                            title = title.substring(0, 32);
                        }
                        packetWrapper.write(Type.STRING, (Object)title);
                        packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                        packetWrapper.write(Type.BOOLEAN, (Object)true);
                        if (windowtypeId == 11) {
                            packetWrapper.passthrough(Type.INT);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 46, 46, (PacketRemapper)new PacketRemapper() {
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
        protocol.registerOutgoing(State.PLAY, 47, 47, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowId = (byte)packetWrapper.read(Type.BYTE);
                        final short windowType = ((Windows)packetWrapper.user().get((Class)Windows.class)).get(windowId);
                        packetWrapper.write(Type.BYTE, (Object)(byte)windowId);
                        short slot = (short)packetWrapper.read((Type)Type.SHORT);
                        if (windowType == 4) {
                            if (slot == 1) {
                                packetWrapper.cancel();
                                return;
                            }
                            if (slot >= 2) {
                                --slot;
                            }
                        }
                        packetWrapper.write((Type)Type.SHORT, (Object)slot);
                    }
                });
                this.map(Type.ITEM, (Type)Types1_7_6_10.COMPRESSED_NBT_ITEM);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Item item = (Item)packetWrapper.get((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                        ItemRewriter.toClient(item);
                        packetWrapper.set((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, 0, (Object)item);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowId = (byte)packetWrapper.get(Type.BYTE, 0);
                        if (windowId != 0) {
                            return;
                        }
                        final short slot = (short)packetWrapper.get((Type)Type.SHORT, 0);
                        if (slot < 5 || slot > 8) {
                            return;
                        }
                        final Item item = (Item)packetWrapper.get((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final UUID uuid = ((ProtocolInfo)packetWrapper.user().get((Class)ProtocolInfo.class)).getUuid();
                        Item[] equipment = tracker.getPlayerEquipment(uuid);
                        if (equipment == null) {
                            tracker.setPlayerEquipment(uuid, equipment = new Item[5]);
                        }
                        equipment[9 - slot] = item;
                        if (tracker.getGamemode() == 3) {
                            packetWrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 48, 48, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowId = (short)packetWrapper.read(Type.UNSIGNED_BYTE);
                        final short windowType = ((Windows)packetWrapper.user().get((Class)Windows.class)).get(windowId);
                        packetWrapper.write(Type.UNSIGNED_BYTE, (Object)windowId);
                        Item[] items = (Item[])packetWrapper.read(Type.ITEM_ARRAY);
                        if (windowType == 4) {
                            final Item[] old = items;
                            items = new Item[old.length - 1];
                            items[0] = old[0];
                            System.arraycopy(old, 2, items, 1, old.length - 3);
                        }
                        for (int i = 0; i < items.length; ++i) {
                            items[i] = ItemRewriter.toClient(items[i]);
                        }
                        packetWrapper.write((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM_ARRAY, (Object)items);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowId = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (windowId != 0) {
                            return;
                        }
                        final Item[] items = (Item[])packetWrapper.get((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM_ARRAY, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final UUID uuid = ((ProtocolInfo)packetWrapper.user().get((Class)ProtocolInfo.class)).getUuid();
                        Item[] equipment = tracker.getPlayerEquipment(uuid);
                        if (equipment == null) {
                            tracker.setPlayerEquipment(uuid, equipment = new Item[5]);
                        }
                        for (int i = 5; i < 9; ++i) {
                            equipment[9 - i] = items[i];
                            if (tracker.getGamemode() == 3) {
                                items[i] = null;
                            }
                        }
                        if (tracker.getGamemode() == 3) {
                            final GameProfileStorage.GameProfile profile = ((GameProfileStorage)packetWrapper.user().get((Class)GameProfileStorage.class)).get(uuid);
                            if (profile != null) {
                                items[5] = profile.getSkull();
                            }
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 49, 49, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowId = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        final Windows windows = (Windows)packetWrapper.user().get((Class)Windows.class);
                        final short windowType = windows.get(windowId);
                        final short property = (short)packetWrapper.get((Type)Type.SHORT, 0);
                        short value = (short)packetWrapper.get((Type)Type.SHORT, 1);
                        if (windowType == -1) {
                            return;
                        }
                        if (windowType == 2) {
                            final Windows.Furnace furnace = windows.furnace.computeIfAbsent(windowId, x -> new Windows.Furnace());
                            if (property == 0 || property == 1) {
                                if (property == 0) {
                                    furnace.setFuelLeft(value);
                                }
                                else {
                                    furnace.setMaxFuel(value);
                                }
                                if (furnace.getMaxFuel() == 0) {
                                    packetWrapper.cancel();
                                    return;
                                }
                                value = (short)(200 * furnace.getFuelLeft() / furnace.getMaxFuel());
                                packetWrapper.set((Type)Type.SHORT, 0, (Object)1);
                                packetWrapper.set((Type)Type.SHORT, 1, (Object)value);
                            }
                            else if (property == 2 || property == 3) {
                                if (property == 2) {
                                    furnace.setProgress(value);
                                }
                                else {
                                    furnace.setMaxProgress(value);
                                }
                                if (furnace.getMaxProgress() == 0) {
                                    packetWrapper.cancel();
                                    return;
                                }
                                value = (short)(200 * furnace.getProgress() / furnace.getMaxProgress());
                                packetWrapper.set((Type)Type.SHORT, 0, (Object)0);
                                packetWrapper.set((Type)Type.SHORT, 1, (Object)value);
                            }
                        }
                        else if (windowType == 4) {
                            if (property > 2) {
                                packetWrapper.cancel();
                            }
                        }
                        else if (windowType == 8) {
                            windows.levelCost = value;
                            windows.anvilId = windowId;
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 13, 13, (PacketRemapper)new PacketRemapper() {
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
        protocol.registerIncoming(State.PLAY, 14, 14, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short windowId = (byte)packetWrapper.read(Type.BYTE);
                        packetWrapper.write(Type.UNSIGNED_BYTE, (Object)windowId);
                        final short windowType = ((Windows)packetWrapper.user().get((Class)Windows.class)).get(windowId);
                        short slot = (short)packetWrapper.read((Type)Type.SHORT);
                        if (windowType == 4 && slot > 0) {
                            ++slot;
                        }
                        packetWrapper.write((Type)Type.SHORT, (Object)slot);
                    }
                });
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.BYTE);
                this.map((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, Type.ITEM);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Item item = (Item)packetWrapper.get(Type.ITEM, 0);
                        ItemRewriter.toServer(item);
                        packetWrapper.set(Type.ITEM, 0, (Object)item);
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 15, 15, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int action = (short)packetWrapper.get((Type)Type.SHORT, 0);
                        if (action == -89) {
                            packetWrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 16, 16, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.SHORT);
                this.map((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, Type.ITEM);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Item item = (Item)packetWrapper.get(Type.ITEM, 0);
                        ItemRewriter.toServer(item);
                        packetWrapper.set(Type.ITEM, 0, (Object)item);
                    }
                });
            }
        });
    }
}
