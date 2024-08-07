// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.packets;

import nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.Protocol1_13To1_13_1;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;

public class InventoryPackets1_13_1
{
    public static void register(final Protocol protocol) {
        final ItemRewriter itemRewriter = new ItemRewriter(protocol, InventoryPackets1_13_1::toClient, InventoryPackets1_13_1::toServer);
        itemRewriter.registerSetCooldown((ClientboundPacketType)ClientboundPackets1_13.COOLDOWN);
        itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_13.WINDOW_ITEMS, Type.FLAT_ITEM_ARRAY);
        itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_13.SET_SLOT, Type.FLAT_ITEM);
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String channel = (String)wrapper.passthrough(Type.STRING);
                        if (channel.equals("minecraft:trader_list")) {
                            wrapper.passthrough(Type.INT);
                            for (int size = (short)wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                final Item input = (Item)wrapper.passthrough(Type.FLAT_ITEM);
                                InventoryPackets1_13_1.toClient(input);
                                final Item output = (Item)wrapper.passthrough(Type.FLAT_ITEM);
                                InventoryPackets1_13_1.toClient(output);
                                final boolean secondItem = (boolean)wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    final Item second = (Item)wrapper.passthrough(Type.FLAT_ITEM);
                                    InventoryPackets1_13_1.toClient(second);
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
        itemRewriter.registerEntityEquipment((ClientboundPacketType)ClientboundPackets1_13.ENTITY_EQUIPMENT, Type.FLAT_ITEM);
        itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_13.CLICK_WINDOW, Type.FLAT_ITEM);
        itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, Type.FLAT_ITEM);
        itemRewriter.registerSpawnParticle((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PARTICLE, Type.FLAT_ITEM, (Type)Type.FLOAT);
    }
    
    public static void toClient(final Item item) {
        if (item == null) {
            return;
        }
        item.setIdentifier(Protocol1_13To1_13_1.MAPPINGS.getNewItemId(item.getIdentifier()));
    }
    
    public static void toServer(final Item item) {
        if (item == null) {
            return;
        }
        item.setIdentifier(Protocol1_13To1_13_1.MAPPINGS.getOldItemId(item.getIdentifier()));
    }
}
