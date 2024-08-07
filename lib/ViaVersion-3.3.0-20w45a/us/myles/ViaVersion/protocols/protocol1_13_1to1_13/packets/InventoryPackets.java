// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13_1to1_13.packets;

import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.api.rewriters.RecipeRewriter;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.RecipeRewriter1_13_2;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;

public class InventoryPackets
{
    public static void register(final Protocol1_13_1To1_13 protocol) {
        final ItemRewriter itemRewriter = new ItemRewriter(protocol, InventoryPackets::toClient, InventoryPackets::toServer);
        itemRewriter.registerSetSlot(ClientboundPackets1_13.SET_SLOT, Type.FLAT_ITEM);
        itemRewriter.registerWindowItems(ClientboundPackets1_13.WINDOW_ITEMS, Type.FLAT_ITEM_ARRAY);
        itemRewriter.registerAdvancements(ClientboundPackets1_13.ADVANCEMENTS, Type.FLAT_ITEM);
        itemRewriter.registerSetCooldown(ClientboundPackets1_13.COOLDOWN);
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String channel = wrapper.get(Type.STRING, 0);
                        if (channel.equals("minecraft:trader_list") || channel.equals("trader_list")) {
                            wrapper.passthrough(Type.INT);
                            for (int size = wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                InventoryPackets.toClient(wrapper.passthrough(Type.FLAT_ITEM));
                                InventoryPackets.toClient(wrapper.passthrough(Type.FLAT_ITEM));
                                final boolean secondItem = wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    InventoryPackets.toClient(wrapper.passthrough(Type.FLAT_ITEM));
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
        itemRewriter.registerEntityEquipment(ClientboundPackets1_13.ENTITY_EQUIPMENT, Type.FLAT_ITEM);
        final RecipeRewriter recipeRewriter = new RecipeRewriter1_13_2(protocol, InventoryPackets::toClient);
        ((Protocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_13.DECLARE_RECIPES, new PacketRemapper() {
            @Override
            public void registerMap() {
                final RecipeRewriter val$recipeRewriter;
                int size;
                int i;
                String id;
                String type;
                this.handler(wrapper -> {
                    val$recipeRewriter = recipeRewriter;
                    for (size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        id = wrapper.passthrough(Type.STRING);
                        type = wrapper.passthrough(Type.STRING).replace("minecraft:", "");
                        val$recipeRewriter.handle(wrapper, type);
                    }
                });
            }
        });
        itemRewriter.registerClickWindow(ServerboundPackets1_13.CLICK_WINDOW, Type.FLAT_ITEM);
        itemRewriter.registerCreativeInvAction(ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, Type.FLAT_ITEM);
        itemRewriter.registerSpawnParticle(ClientboundPackets1_13.SPAWN_PARTICLE, Type.FLAT_ITEM, Type.FLOAT);
    }
    
    public static void toClient(final Item item) {
        if (item == null) {
            return;
        }
        item.setIdentifier(Protocol1_13_1To1_13.MAPPINGS.getNewItemId(item.getIdentifier()));
    }
    
    public static void toServer(final Item item) {
        if (item == null) {
            return;
        }
        item.setIdentifier(Protocol1_13_1To1_13.MAPPINGS.getOldItemId(item.getIdentifier()));
    }
}
