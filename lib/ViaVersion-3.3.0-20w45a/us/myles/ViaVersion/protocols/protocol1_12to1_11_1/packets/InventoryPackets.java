// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_12to1_11_1.packets;

import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.providers.InventoryQuickMoveProvider;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.BedRewriter;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.Protocol1_12To1_11_1;

public class InventoryPackets
{
    public static void register(final Protocol1_12To1_11_1 protocol) {
        final ItemRewriter itemRewriter = new ItemRewriter(protocol, BedRewriter::toClientItem, BedRewriter::toServerItem);
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
                                BedRewriter.toClientItem(wrapper.passthrough(Type.ITEM));
                                BedRewriter.toClientItem(wrapper.passthrough(Type.ITEM));
                                final boolean secondItem = wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    BedRewriter.toClientItem(wrapper.passthrough(Type.ITEM));
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
        ((Protocol<C1, C2, S1, ServerboundPackets1_12>)protocol).registerIncoming(ServerboundPackets1_12.CLICK_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.VAR_INT);
                this.map(Type.ITEM);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item item = wrapper.get(Type.ITEM, 0);
                        if (!Via.getConfig().is1_12QuickMoveActionFix()) {
                            BedRewriter.toServerItem(item);
                            return;
                        }
                        final byte button = wrapper.get(Type.BYTE, 0);
                        final int mode = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (mode == 1 && button == 0 && item == null) {
                            final short windowId = wrapper.get(Type.UNSIGNED_BYTE, 0);
                            final short slotId = wrapper.get((Type<Short>)Type.SHORT, 0);
                            final short actionId = wrapper.get((Type<Short>)Type.SHORT, 1);
                            final InventoryQuickMoveProvider provider = Via.getManager().getProviders().get(InventoryQuickMoveProvider.class);
                            final boolean succeed = provider.registerQuickMoveAction(windowId, slotId, actionId, wrapper.user());
                            if (succeed) {
                                wrapper.cancel();
                            }
                        }
                        else {
                            BedRewriter.toServerItem(item);
                        }
                    }
                });
            }
        });
        itemRewriter.registerCreativeInvAction(ServerboundPackets1_12.CREATIVE_INVENTORY_ACTION, Type.ITEM);
    }
}
