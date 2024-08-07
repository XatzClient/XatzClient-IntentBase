// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13_1to1_13_2.packets;

import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_13_1to1_13_2.Protocol1_13_1To1_13_2;

public class InventoryPackets1_13_2
{
    public static void register(final Protocol1_13_1To1_13_2 protocol) {
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SET_SLOT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.FLAT_VAR_INT_ITEM, Type.FLAT_ITEM);
            }
        });
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.WINDOW_ITEMS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.FLAT_VAR_INT_ITEM_ARRAY, Type.FLAT_ITEM_ARRAY);
            }
        });
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String channel = (String)wrapper.get(Type.STRING, 0);
                        if (channel.equals("minecraft:trader_list") || channel.equals("trader_list")) {
                            wrapper.passthrough(Type.INT);
                            for (int size = (short)wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                wrapper.write(Type.FLAT_ITEM, wrapper.read(Type.FLAT_VAR_INT_ITEM));
                                wrapper.write(Type.FLAT_ITEM, wrapper.read(Type.FLAT_VAR_INT_ITEM));
                                final boolean secondItem = (boolean)wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    wrapper.write(Type.FLAT_ITEM, wrapper.read(Type.FLAT_VAR_INT_ITEM));
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
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.ENTITY_EQUIPMENT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.map(Type.FLAT_VAR_INT_ITEM, Type.FLAT_ITEM);
            }
        });
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.DECLARE_RECIPES, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int recipesNo = (int)wrapper.passthrough((Type)Type.VAR_INT), i = 0; i < recipesNo; ++i) {
                            wrapper.passthrough(Type.STRING);
                            final String type = (String)wrapper.passthrough(Type.STRING);
                            if (type.equals("crafting_shapeless")) {
                                wrapper.passthrough(Type.STRING);
                                for (int ingredientsNo = (int)wrapper.passthrough((Type)Type.VAR_INT), i2 = 0; i2 < ingredientsNo; ++i2) {
                                    wrapper.write(Type.FLAT_ITEM_ARRAY_VAR_INT, wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT));
                                }
                                wrapper.write(Type.FLAT_ITEM, wrapper.read(Type.FLAT_VAR_INT_ITEM));
                            }
                            else if (type.equals("crafting_shaped")) {
                                final int ingredientsNo = (int)wrapper.passthrough((Type)Type.VAR_INT) * (int)wrapper.passthrough((Type)Type.VAR_INT);
                                wrapper.passthrough(Type.STRING);
                                for (int i2 = 0; i2 < ingredientsNo; ++i2) {
                                    wrapper.write(Type.FLAT_ITEM_ARRAY_VAR_INT, wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT));
                                }
                                wrapper.write(Type.FLAT_ITEM, wrapper.read(Type.FLAT_VAR_INT_ITEM));
                            }
                            else if (type.equals("smelting")) {
                                wrapper.passthrough(Type.STRING);
                                wrapper.write(Type.FLAT_ITEM_ARRAY_VAR_INT, wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT));
                                wrapper.write(Type.FLAT_ITEM, wrapper.read(Type.FLAT_VAR_INT_ITEM));
                                wrapper.passthrough((Type)Type.FLOAT);
                                wrapper.passthrough((Type)Type.VAR_INT);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerIncoming((ServerboundPacketType)ServerboundPackets1_13.CLICK_WINDOW, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.VAR_INT);
                this.map(Type.FLAT_ITEM, Type.FLAT_VAR_INT_ITEM);
            }
        });
        protocol.registerIncoming((ServerboundPacketType)ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.SHORT);
                this.map(Type.FLAT_ITEM, Type.FLAT_VAR_INT_ITEM);
            }
        });
    }
}
