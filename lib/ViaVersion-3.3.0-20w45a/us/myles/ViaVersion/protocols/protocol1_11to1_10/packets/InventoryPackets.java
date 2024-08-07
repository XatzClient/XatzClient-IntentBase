// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_11to1_10.packets;

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
import us.myles.ViaVersion.protocols.protocol1_11to1_10.EntityIdRewriter;
import us.myles.ViaVersion.protocols.protocol1_11to1_10.Protocol1_11To1_10;

public class InventoryPackets
{
    public static void register(final Protocol1_11To1_10 protocol) {
        final ItemRewriter itemRewriter = new ItemRewriter(protocol, EntityIdRewriter::toClientItem, EntityIdRewriter::toServerItem);
        itemRewriter.registerSetSlot(ClientboundPackets1_9_3.SET_SLOT, Type.ITEM);
        itemRewriter.registerWindowItems(ClientboundPackets1_9_3.WINDOW_ITEMS, Type.ITEM_ARRAY);
        itemRewriter.registerEntityEquipment(ClientboundPackets1_9_3.ENTITY_EQUIPMENT, Type.ITEM);
        ((Protocol<ClientboundPackets1_9_3, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_9_3.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (wrapper.get(Type.STRING, 0).equalsIgnoreCase("MC|TrList")) {
                            wrapper.passthrough(Type.INT);
                            for (int size = wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                EntityIdRewriter.toClientItem(wrapper.passthrough(Type.ITEM));
                                EntityIdRewriter.toClientItem(wrapper.passthrough(Type.ITEM));
                                final boolean secondItem = wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    EntityIdRewriter.toClientItem(wrapper.passthrough(Type.ITEM));
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
        itemRewriter.registerClickWindow(ServerboundPackets1_9_3.CLICK_WINDOW, Type.ITEM);
        itemRewriter.registerCreativeInvAction(ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
    }
}
