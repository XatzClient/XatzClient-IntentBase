// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_11to1_11_1.packets;

import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
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
import nl.matsv.viabackwards.api.rewriters.LegacyEnchantmentRewriter;
import nl.matsv.viabackwards.protocol.protocol1_11to1_11_1.Protocol1_11To1_11_1;
import nl.matsv.viabackwards.api.rewriters.LegacyBlockItemRewriter;

public class ItemPackets1_11_1 extends LegacyBlockItemRewriter<Protocol1_11To1_11_1>
{
    private LegacyEnchantmentRewriter enchantmentRewriter;
    
    public ItemPackets1_11_1(final Protocol1_11To1_11_1 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        final ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
        itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_9_3.SET_SLOT, Type.ITEM);
        itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_9_3.WINDOW_ITEMS, Type.ITEM_ARRAY);
        itemRewriter.registerEntityEquipment((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_EQUIPMENT, Type.ITEM);
        ((Protocol1_11To1_11_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (((String)wrapper.get(Type.STRING, 0)).equalsIgnoreCase("MC|TrList")) {
                            wrapper.passthrough(Type.INT);
                            for (int size = (short)wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                wrapper.write(Type.ITEM, (Object)ItemPackets1_11_1.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                                wrapper.write(Type.ITEM, (Object)ItemPackets1_11_1.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                                final boolean secondItem = (boolean)wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    wrapper.write(Type.ITEM, (Object)ItemPackets1_11_1.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
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
        itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_9_3.CLICK_WINDOW, Type.ITEM);
        itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
        final Metadata data;
        ((Protocol1_11To1_11_1)this.protocol).getEntityPackets().registerMetaHandler().handle(e -> {
            data = e.getData();
            if (data.getMetaType().getType().equals(Type.ITEM)) {
                data.setValue((Object)this.handleItemToClient((Item)data.getValue()));
            }
            return data;
        });
    }
    
    @Override
    protected void registerRewrites() {
        (this.enchantmentRewriter = new LegacyEnchantmentRewriter(this.nbtTagName)).registerEnchantment(22, "§7Sweeping Edge");
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToClient(item);
        final CompoundTag tag = item.getTag();
        if (tag == null) {
            return item;
        }
        if (tag.get("ench") instanceof ListTag) {
            this.enchantmentRewriter.rewriteEnchantmentsToClient(tag, false);
        }
        if (tag.get("StoredEnchantments") instanceof ListTag) {
            this.enchantmentRewriter.rewriteEnchantmentsToClient(tag, true);
        }
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToServer(item);
        final CompoundTag tag = item.getTag();
        if (tag == null) {
            return item;
        }
        if (tag.contains(this.nbtTagName + "|ench")) {
            this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, false);
        }
        if (tag.contains(this.nbtTagName + "|StoredEnchantments")) {
            this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, true);
        }
        return item;
    }
}
