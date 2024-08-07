// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16to1_15_2.packets;

import us.myles.viaversion.libs.opennbt.tag.builtin.LongTag;
import java.util.Iterator;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import us.myles.ViaVersion.api.type.types.UUIDIntArrayType;
import java.util.UUID;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.data.RecipeRewriter1_14;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.types.ShortType;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.storage.InventoryTracker1_16;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;

public class InventoryPackets
{
    public static void register(final Protocol1_16To1_15_2 protocol) {
        final ItemRewriter itemRewriter = new ItemRewriter(protocol, InventoryPackets::toClient, InventoryPackets::toServer);
        ((Protocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.COMPONENT);
                final InventoryTracker1_16 inventoryTracker;
                final int windowId;
                int windowType;
                this.handler(wrapper -> {
                    inventoryTracker = wrapper.user().get(InventoryTracker1_16.class);
                    windowId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    windowType = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (windowType >= 20) {
                        wrapper.set(Type.VAR_INT, 1, ++windowType);
                    }
                    inventoryTracker.setInventory((short)windowId);
                });
            }
        });
        ((Protocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                final InventoryTracker1_16 inventoryTracker;
                this.handler(wrapper -> {
                    inventoryTracker = wrapper.user().get(InventoryTracker1_16.class);
                    inventoryTracker.setInventory((short)(-1));
                });
            }
        });
        ((Protocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.WINDOW_PROPERTY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                final short property;
                short enchantmentId;
                ShortType short1;
                short enchantmentId2;
                final int index;
                this.handler(wrapper -> {
                    property = wrapper.get((Type<Short>)Type.SHORT, 0);
                    if (property >= 4 && property <= 6) {
                        enchantmentId = wrapper.get((Type<Short>)Type.SHORT, 1);
                        if (enchantmentId >= 11) {
                            short1 = Type.SHORT;
                            enchantmentId2 = (short)(enchantmentId + 1);
                            wrapper.set(short1, index, enchantmentId2);
                        }
                    }
                });
            }
        });
        itemRewriter.registerSetCooldown(ClientboundPackets1_15.COOLDOWN);
        itemRewriter.registerWindowItems(ClientboundPackets1_15.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        itemRewriter.registerTradeList(ClientboundPackets1_15.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerSetSlot(ClientboundPackets1_15.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerAdvancements(ClientboundPackets1_15.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((Protocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.ENTITY_EQUIPMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final int slot;
                this.handler(wrapper -> {
                    slot = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.BYTE, (byte)slot);
                    InventoryPackets.toClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                });
            }
        });
        new RecipeRewriter1_14(protocol, InventoryPackets::toClient).registerDefaultHandler(ClientboundPackets1_15.DECLARE_RECIPES);
        itemRewriter.registerClickWindow(ServerboundPackets1_16.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerCreativeInvAction(ServerboundPackets1_16.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((Protocol<C1, C2, S1, ServerboundPackets1_16>)protocol).registerIncoming(ServerboundPackets1_16.CLOSE_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                final InventoryTracker1_16 inventoryTracker;
                this.handler(wrapper -> {
                    inventoryTracker = wrapper.user().get(InventoryTracker1_16.class);
                    inventoryTracker.setInventory((short)(-1));
                });
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_16>)protocol).registerIncoming(ServerboundPackets1_16.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> InventoryPackets.toServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
            }
        });
        itemRewriter.registerSpawnParticle(ClientboundPackets1_15.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
    }
    
    public static void toClient(final Item item) {
        if (item == null) {
            return;
        }
        if (item.getIdentifier() == 771 && item.getTag() != null) {
            final CompoundTag tag = item.getTag();
            final Tag ownerTag = tag.get("SkullOwner");
            if (ownerTag instanceof CompoundTag) {
                final CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
                final Tag idTag = ownerCompundTag.get("Id");
                if (idTag instanceof StringTag) {
                    final UUID id = UUID.fromString((String)idTag.getValue());
                    ownerCompundTag.put(new IntArrayTag("Id", UUIDIntArrayType.uuidToIntArray(id)));
                }
            }
        }
        oldToNewAttributes(item);
        item.setIdentifier(Protocol1_16To1_15_2.MAPPINGS.getNewItemId(item.getIdentifier()));
    }
    
    public static void toServer(final Item item) {
        if (item == null) {
            return;
        }
        item.setIdentifier(Protocol1_16To1_15_2.MAPPINGS.getOldItemId(item.getIdentifier()));
        if (item.getIdentifier() == 771 && item.getTag() != null) {
            final CompoundTag tag = item.getTag();
            final Tag ownerTag = tag.get("SkullOwner");
            if (ownerTag instanceof CompoundTag) {
                final CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
                final Tag idTag = ownerCompundTag.get("Id");
                if (idTag instanceof IntArrayTag) {
                    final UUID id = UUIDIntArrayType.uuidFromIntArray((int[])idTag.getValue());
                    ownerCompundTag.put(new StringTag("Id", id.toString()));
                }
            }
        }
        newToOldAttributes(item);
    }
    
    public static void oldToNewAttributes(final Item item) {
        if (item.getTag() == null) {
            return;
        }
        final ListTag attributes = item.getTag().get("AttributeModifiers");
        if (attributes == null) {
            return;
        }
        for (final Tag tag : attributes) {
            final CompoundTag attribute = (CompoundTag)tag;
            rewriteAttributeName(attribute, "AttributeName", false);
            rewriteAttributeName(attribute, "Name", false);
            final Tag leastTag = attribute.get("UUIDLeast");
            if (leastTag != null) {
                final Tag mostTag = attribute.get("UUIDMost");
                final int[] uuidIntArray = UUIDIntArrayType.bitsToIntArray(((Number)leastTag.getValue()).longValue(), ((Number)mostTag.getValue()).longValue());
                attribute.put(new IntArrayTag("UUID", uuidIntArray));
            }
        }
    }
    
    public static void newToOldAttributes(final Item item) {
        if (item.getTag() == null) {
            return;
        }
        final ListTag attributes = item.getTag().get("AttributeModifiers");
        if (attributes == null) {
            return;
        }
        for (final Tag tag : attributes) {
            final CompoundTag attribute = (CompoundTag)tag;
            rewriteAttributeName(attribute, "AttributeName", true);
            rewriteAttributeName(attribute, "Name", true);
            final IntArrayTag uuidTag = attribute.get("UUID");
            if (uuidTag != null) {
                final UUID uuid = UUIDIntArrayType.uuidFromIntArray(uuidTag.getValue());
                attribute.put(new LongTag("UUIDLeast", uuid.getLeastSignificantBits()));
                attribute.put(new LongTag("UUIDMost", uuid.getMostSignificantBits()));
            }
        }
    }
    
    public static void rewriteAttributeName(final CompoundTag compoundTag, final String entryName, final boolean inverse) {
        final StringTag attributeNameTag = compoundTag.get(entryName);
        if (attributeNameTag == null) {
            return;
        }
        String attributeName = attributeNameTag.getValue();
        if (inverse && !attributeName.startsWith("minecraft:")) {
            attributeName = "minecraft:" + attributeName;
        }
        final String mappedAttribute = (String)(inverse ? Protocol1_16To1_15_2.MAPPINGS.getAttributeMappings().inverse() : Protocol1_16To1_15_2.MAPPINGS.getAttributeMappings()).get((Object)attributeName);
        if (mappedAttribute == null) {
            return;
        }
        attributeNameTag.setValue(mappedAttribute);
    }
}
