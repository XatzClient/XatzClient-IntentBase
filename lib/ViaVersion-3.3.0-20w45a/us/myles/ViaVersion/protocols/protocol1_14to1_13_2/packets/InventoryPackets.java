// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_14to1_13_2.packets;

import us.myles.viaversion.libs.gson.JsonObject;
import com.google.common.collect.Sets;
import java.util.Iterator;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import us.myles.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.concurrent.ThreadLocalRandom;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.api.rewriters.RecipeRewriter;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.RecipeRewriter1_13_2;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import us.myles.ViaVersion.api.Via;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.ComponentRewriter;
import java.util.Set;

public class InventoryPackets
{
    private static final String NBT_TAG_NAME;
    private static final Set<String> REMOVED_RECIPE_TYPES;
    private static final ComponentRewriter COMPONENT_REWRITER;
    
    public static void register(final Protocol protocol) {
        final ItemRewriter itemRewriter = new ItemRewriter(protocol, InventoryPackets::toClient, InventoryPackets::toServer);
        itemRewriter.registerSetCooldown(ClientboundPackets1_13.COOLDOWN);
        itemRewriter.registerAdvancements(ClientboundPackets1_13.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        protocol.registerOutgoing(ClientboundPackets1_13.OPEN_WINDOW, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Short windowsId = wrapper.read(Type.UNSIGNED_BYTE);
                        final String type = wrapper.read(Type.STRING);
                        final JsonElement title = wrapper.read(Type.COMPONENT);
                        InventoryPackets.COMPONENT_REWRITER.processText(title);
                        final Short slots = wrapper.read(Type.UNSIGNED_BYTE);
                        if (type.equals("EntityHorse")) {
                            wrapper.setId(31);
                            final int entityId = wrapper.read(Type.INT);
                            wrapper.write(Type.UNSIGNED_BYTE, windowsId);
                            wrapper.write(Type.VAR_INT, (int)slots);
                            wrapper.write(Type.INT, entityId);
                        }
                        else {
                            wrapper.setId(46);
                            wrapper.write(Type.VAR_INT, (int)windowsId);
                            int typeId = -1;
                            final String s = type;
                            switch (s) {
                                case "minecraft:container":
                                case "minecraft:chest": {
                                    typeId = slots / 9 - 1;
                                    break;
                                }
                                case "minecraft:crafting_table": {
                                    typeId = 11;
                                    break;
                                }
                                case "minecraft:furnace": {
                                    typeId = 13;
                                    break;
                                }
                                case "minecraft:dropper":
                                case "minecraft:dispenser": {
                                    typeId = 6;
                                    break;
                                }
                                case "minecraft:enchanting_table": {
                                    typeId = 12;
                                    break;
                                }
                                case "minecraft:brewing_stand": {
                                    typeId = 10;
                                    break;
                                }
                                case "minecraft:villager": {
                                    typeId = 18;
                                    break;
                                }
                                case "minecraft:beacon": {
                                    typeId = 8;
                                    break;
                                }
                                case "minecraft:anvil": {
                                    typeId = 7;
                                    break;
                                }
                                case "minecraft:hopper": {
                                    typeId = 15;
                                    break;
                                }
                                case "minecraft:shulker_box": {
                                    typeId = 19;
                                    break;
                                }
                            }
                            if (typeId == -1) {
                                Via.getPlatform().getLogger().warning("Can't open inventory for 1.14 player! Type: " + type + " Size: " + slots);
                            }
                            wrapper.write(Type.VAR_INT, typeId);
                            wrapper.write(Type.COMPONENT, title);
                        }
                    }
                });
            }
        });
        itemRewriter.registerWindowItems(ClientboundPackets1_13.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        itemRewriter.registerSetSlot(ClientboundPackets1_13.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        protocol.registerOutgoing(ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String channel = wrapper.get(Type.STRING, 0);
                        if (channel.equals("minecraft:trader_list") || channel.equals("trader_list")) {
                            wrapper.setId(39);
                            wrapper.resetReader();
                            wrapper.read(Type.STRING);
                            final int windowId = wrapper.read(Type.INT);
                            wrapper.user().get(EntityTracker1_14.class).setLatestTradeWindowId(windowId);
                            wrapper.write(Type.VAR_INT, windowId);
                            for (int size = wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                InventoryPackets.toClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                                InventoryPackets.toClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                                final boolean secondItem = wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    InventoryPackets.toClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                                }
                                wrapper.passthrough(Type.BOOLEAN);
                                wrapper.passthrough(Type.INT);
                                wrapper.passthrough(Type.INT);
                                wrapper.write(Type.INT, 0);
                                wrapper.write(Type.INT, 0);
                                wrapper.write(Type.FLOAT, 0.0f);
                            }
                            wrapper.write(Type.VAR_INT, 0);
                            wrapper.write(Type.VAR_INT, 0);
                            wrapper.write(Type.BOOLEAN, false);
                        }
                        else if (channel.equals("minecraft:book_open") || channel.equals("book_open")) {
                            final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                            wrapper.clearPacket();
                            wrapper.setId(45);
                            wrapper.write(Type.VAR_INT, hand);
                        }
                    }
                });
            }
        });
        itemRewriter.registerEntityEquipment(ClientboundPackets1_13.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
        final RecipeRewriter recipeRewriter = new RecipeRewriter1_13_2(protocol, InventoryPackets::toClient);
        protocol.registerOutgoing(ClientboundPackets1_13.DECLARE_RECIPES, new PacketRemapper() {
            @Override
            public void registerMap() {
                final RecipeRewriter val$recipeRewriter;
                final int size;
                int deleted;
                int i;
                String id;
                String type;
                this.handler(wrapper -> {
                    val$recipeRewriter = recipeRewriter;
                    size = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    deleted = 0;
                    for (i = 0; i < size; ++i) {
                        id = wrapper.read(Type.STRING);
                        type = wrapper.read(Type.STRING);
                        if (InventoryPackets.REMOVED_RECIPE_TYPES.contains(type)) {
                            ++deleted;
                        }
                        else {
                            wrapper.write(Type.STRING, type);
                            wrapper.write(Type.STRING, id);
                            val$recipeRewriter.handle(wrapper, type);
                        }
                    }
                    wrapper.set(Type.VAR_INT, 0, size - deleted);
                });
            }
        });
        itemRewriter.registerClickWindow(ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        protocol.registerIncoming(ServerboundPackets1_14.SELECT_TRADE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final PacketWrapper resyncPacket = wrapper.create(8);
                        resyncPacket.write(Type.UNSIGNED_BYTE, (short)wrapper.user().get(EntityTracker1_14.class).getLatestTradeWindowId());
                        resyncPacket.write(Type.SHORT, (Short)(-999));
                        resyncPacket.write(Type.BYTE, (Byte)2);
                        resyncPacket.write(Type.SHORT, (short)ThreadLocalRandom.current().nextInt());
                        resyncPacket.write(Type.VAR_INT, 5);
                        final CompoundTag tag = new CompoundTag("");
                        tag.put(new DoubleTag("force_resync", Double.NaN));
                        resyncPacket.write(Type.FLAT_VAR_INT_ITEM, new Item(1, (byte)1, (short)0, tag));
                        resyncPacket.sendToServer(Protocol1_14To1_13_2.class, true, false);
                    }
                });
            }
        });
        itemRewriter.registerCreativeInvAction(ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerSpawnParticle(ClientboundPackets1_13.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.FLOAT);
    }
    
    public static void toClient(final Item item) {
        if (item == null) {
            return;
        }
        item.setIdentifier(Protocol1_14To1_13_2.MAPPINGS.getNewItemId(item.getIdentifier()));
        if (item.getTag() == null) {
            return;
        }
        final Tag displayTag = item.getTag().get("display");
        if (displayTag instanceof CompoundTag) {
            final CompoundTag display = (CompoundTag)displayTag;
            final Tag loreTag = display.get("Lore");
            if (loreTag instanceof ListTag) {
                final ListTag lore = (ListTag)loreTag;
                display.put(new ListTag(InventoryPackets.NBT_TAG_NAME + "|Lore", lore.clone().getValue()));
                for (final Tag loreEntry : lore) {
                    if (loreEntry instanceof StringTag) {
                        final String jsonText = ChatRewriter.fromLegacyTextAsString(((StringTag)loreEntry).getValue(), ChatColor.WHITE, true);
                        ((StringTag)loreEntry).setValue(jsonText);
                    }
                }
            }
        }
    }
    
    public static void toServer(final Item item) {
        if (item == null) {
            return;
        }
        item.setIdentifier(Protocol1_14To1_13_2.MAPPINGS.getOldItemId(item.getIdentifier()));
        if (item.getTag() == null) {
            return;
        }
        final Tag displayTag = item.getTag().get("display");
        if (displayTag instanceof CompoundTag) {
            final CompoundTag display = (CompoundTag)displayTag;
            final Tag loreTag = display.get("Lore");
            if (loreTag instanceof ListTag) {
                final ListTag lore = (ListTag)loreTag;
                final ListTag savedLore = display.remove(InventoryPackets.NBT_TAG_NAME + "|Lore");
                if (savedLore != null) {
                    display.put(new ListTag("Lore", savedLore.getValue()));
                }
                else {
                    for (final Tag loreEntry : lore) {
                        if (loreEntry instanceof StringTag) {
                            ((StringTag)loreEntry).setValue(ChatRewriter.jsonTextToLegacy(((StringTag)loreEntry).getValue()));
                        }
                    }
                }
            }
        }
    }
    
    static {
        NBT_TAG_NAME = "ViaVersion|" + Protocol1_14To1_13_2.class.getSimpleName();
        REMOVED_RECIPE_TYPES = Sets.newHashSet((Object[])new String[] { "crafting_special_banneraddpattern", "crafting_special_repairitem" });
        COMPONENT_REWRITER = new ComponentRewriter() {
            @Override
            protected void handleTranslate(final JsonObject object, final String translate) {
                super.handleTranslate(object, translate);
                if (translate.startsWith("block.") && translate.endsWith(".name")) {
                    object.addProperty("translate", translate.substring(0, translate.length() - 5));
                }
            }
        };
    }
}
