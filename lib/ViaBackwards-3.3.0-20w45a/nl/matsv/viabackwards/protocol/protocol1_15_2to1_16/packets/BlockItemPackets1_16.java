// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.packets.InventoryPackets;
import java.util.UUID;
import us.myles.ViaVersion.api.type.types.UUIDIntArrayType;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.api.minecraft.Position;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.data.MapColorRewriter;
import us.myles.ViaVersion.api.type.types.ShortType;
import java.util.Iterator;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.util.CompactArrayUtil;
import us.myles.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import java.util.List;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.api.minecraft.item.Item;
import java.util.ArrayList;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.data.RecipeRewriter1_14;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.protocol.Protocol;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.api.rewriters.EnchantmentRewriter;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_16 extends ItemRewriter<Protocol1_15_2To1_16>
{
    private EnchantmentRewriter enchantmentRewriter;
    
    public BlockItemPackets1_16(final Protocol1_15_2To1_16 protocol, final TranslatableRewriter translatableRewriter) {
        super(protocol, translatableRewriter);
    }
    
    @Override
    protected void registerPackets() {
        final us.myles.ViaVersion.api.rewriters.ItemRewriter itemRewriter = new us.myles.ViaVersion.api.rewriters.ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
        final BlockRewriter blockRewriter = new BlockRewriter((Protocol)this.protocol, Type.POSITION1_14);
        final RecipeRewriter1_14 recipeRewriter = new RecipeRewriter1_14((Protocol)this.protocol, this::handleItemToClient);
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.DECLARE_RECIPES, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    int newSize;
                    for (int size = newSize = (int)wrapper.passthrough((Type)Type.VAR_INT), i = 0; i < size; ++i) {
                        final String originalType = (String)wrapper.read(Type.STRING);
                        final String type = originalType.replace("minecraft:", "");
                        if (type.equals("smithing")) {
                            --newSize;
                            wrapper.read(Type.STRING);
                            wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                            wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                            wrapper.read(Type.FLAT_VAR_INT_ITEM);
                        }
                        else {
                            wrapper.write(Type.STRING, (Object)originalType);
                            final String id = (String)wrapper.passthrough(Type.STRING);
                            recipeRewriter.handle(wrapper, type);
                        }
                    }
                    wrapper.set((Type)Type.VAR_INT, 0, (Object)newSize);
                });
            }
        });
        itemRewriter.registerSetCooldown((ClientboundPacketType)ClientboundPackets1_16.COOLDOWN);
        itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_16.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_16.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerTradeList((ClientboundPacketType)ClientboundPackets1_16.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerAdvancements((ClientboundPacketType)ClientboundPackets1_16.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        blockRewriter.registerAcknowledgePlayerDigging((ClientboundPacketType)ClientboundPackets1_16.ACKNOWLEDGE_PLAYER_DIGGING);
        blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_16.BLOCK_ACTION);
        blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_16.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange((ClientboundPacketType)ClientboundPackets1_16.MULTI_BLOCK_CHANGE);
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.ENTITY_EQUIPMENT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final int entityId = (int)wrapper.passthrough((Type)Type.VAR_INT);
                    final List<EquipmentData> equipmentData = new ArrayList<EquipmentData>();
                    byte slot;
                    do {
                        slot = (byte)wrapper.read(Type.BYTE);
                        final Item item = BlockItemPackets1_16.this.handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM));
                        final int rawSlot = slot & 0x7F;
                        equipmentData.add(new EquipmentData(rawSlot, item));
                    } while ((slot & 0xFFFFFF80) != 0x0);
                    final EquipmentData firstData = equipmentData.get(0);
                    wrapper.write((Type)Type.VAR_INT, (Object)firstData.slot);
                    wrapper.write(Type.FLAT_VAR_INT_ITEM, (Object)firstData.item);
                    for (int i = 1; i < equipmentData.size(); ++i) {
                        final PacketWrapper equipmentPacket = wrapper.create(ClientboundPackets1_15.ENTITY_EQUIPMENT.ordinal());
                        final EquipmentData data = equipmentData.get(i);
                        equipmentPacket.write((Type)Type.VAR_INT, (Object)entityId);
                        equipmentPacket.write((Type)Type.VAR_INT, (Object)data.slot);
                        equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, (Object)data.item);
                        equipmentPacket.send((Class)Protocol1_15_2To1_16.class);
                    }
                });
            }
        });
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.UPDATE_LIGHT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.map(Type.BOOLEAN, Type.NOTHING);
            }
        });
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.CHUNK_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final Chunk chunk = (Chunk)wrapper.read((Type)new Chunk1_16Type());
                    wrapper.write((Type)new Chunk1_15Type(), (Object)chunk);
                    for (int j = 0; j < chunk.getSections().length; ++j) {
                        final ChunkSection section = chunk.getSections()[j];
                        if (section != null) {
                            for (int k = 0; k < section.getPaletteSize(); ++k) {
                                final int old = section.getPaletteEntry(k);
                                section.setPaletteEntry(k, ((Protocol1_15_2To1_16)BlockItemPackets1_16.this.protocol).getMappingData().getNewBlockStateId(old));
                            }
                        }
                    }
                    final CompoundTag heightMaps = chunk.getHeightMap();
                    for (final Tag heightMapTag : heightMaps) {
                        final LongArrayTag heightMap = (LongArrayTag)heightMapTag;
                        final int[] heightMapData = new int[256];
                        CompactArrayUtil.iterateCompactArrayWithPadding(9, heightMapData.length, heightMap.getValue(), (i, v) -> heightMapData[i] = v);
                        heightMap.setValue(CompactArrayUtil.createCompactArray(9, heightMapData.length, i -> heightMapData[i]));
                    }
                    if (chunk.isBiomeData()) {
                        for (int l = 0; l < 1024; ++l) {
                            final int biome = chunk.getBiomeData()[l];
                            switch (biome) {
                                case 170:
                                case 171:
                                case 172:
                                case 173: {
                                    chunk.getBiomeData()[l] = 8;
                                    break;
                                }
                            }
                        }
                    }
                    if (chunk.getBlockEntities() == null) {
                        return;
                    }
                    for (final CompoundTag blockEntity : chunk.getBlockEntities()) {
                        BlockItemPackets1_16.this.handleBlockEntity(blockEntity);
                    }
                });
            }
        });
        blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_16.EFFECT, 1010, 2001);
        itemRewriter.registerSpawnParticle((ClientboundPacketType)ClientboundPackets1_16.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.WINDOW_PROPERTY, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.handler(wrapper -> {
                    final short property = (short)wrapper.get((Type)Type.SHORT, 0);
                    if (property >= 4 && property <= 6) {
                        short enchantmentId = (short)wrapper.get((Type)Type.SHORT, 1);
                        if (enchantmentId > 11) {
                            final ShortType short1 = Type.SHORT;
                            final int n = 1;
                            --enchantmentId;
                            wrapper.set((Type)short1, n, (Object)enchantmentId);
                        }
                        else if (enchantmentId == 11) {
                            wrapper.set((Type)Type.SHORT, 1, (Object)9);
                        }
                    }
                });
            }
        });
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.MAP_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.handler(wrapper -> {
                    for (int iconCount = (int)wrapper.passthrough((Type)Type.VAR_INT), i = 0; i < iconCount; ++i) {
                        wrapper.passthrough((Type)Type.VAR_INT);
                        wrapper.passthrough(Type.BYTE);
                        wrapper.passthrough(Type.BYTE);
                        wrapper.passthrough(Type.BYTE);
                        if (wrapper.passthrough(Type.BOOLEAN)) {
                            wrapper.passthrough(Type.COMPONENT);
                        }
                    }
                    final short columns = (short)wrapper.passthrough(Type.UNSIGNED_BYTE);
                    if (columns < 1) {
                        return;
                    }
                    wrapper.passthrough(Type.UNSIGNED_BYTE);
                    wrapper.passthrough(Type.UNSIGNED_BYTE);
                    wrapper.passthrough(Type.UNSIGNED_BYTE);
                    final byte[] data = (byte[])wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                    for (int j = 0; j < data.length; ++j) {
                        final int color = data[j] & 0xFF;
                        final int mappedColor = MapColorRewriter.getMappedColor(color);
                        if (mappedColor != -1) {
                            data[j] = (byte)mappedColor;
                        }
                    }
                });
            }
        });
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.BLOCK_ENTITY_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final Position position = (Position)wrapper.passthrough(Type.POSITION1_14);
                    final short action = (short)wrapper.passthrough(Type.UNSIGNED_BYTE);
                    final CompoundTag tag = (CompoundTag)wrapper.passthrough(Type.NBT);
                    BlockItemPackets1_16.this.handleBlockEntity(tag);
                });
            }
        });
        itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((Protocol1_15_2To1_16)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_14.EDIT_BOOK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> BlockItemPackets1_16.this.handleItemToServer((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
            }
        });
    }
    
    private void handleBlockEntity(final CompoundTag tag) {
        final StringTag idTag = (StringTag)tag.get("id");
        if (idTag == null) {
            return;
        }
        final String id = idTag.getValue();
        if (id.equals("minecraft:conduit")) {
            final Tag targetUuidTag = tag.remove("Target");
            if (!(targetUuidTag instanceof IntArrayTag)) {
                return;
            }
            final UUID targetUuid = UUIDIntArrayType.uuidFromIntArray((int[])targetUuidTag.getValue());
            tag.put((Tag)new StringTag("target_uuid", targetUuid.toString()));
        }
        else if (id.equals("minecraft:skull")) {
            final Tag skullOwnerTag = tag.remove("SkullOwner");
            if (!(skullOwnerTag instanceof CompoundTag)) {
                return;
            }
            final CompoundTag skullOwnerCompoundTag = (CompoundTag)skullOwnerTag;
            final Tag ownerUuidTag = skullOwnerCompoundTag.remove("Id");
            if (ownerUuidTag instanceof IntArrayTag) {
                final UUID ownerUuid = UUIDIntArrayType.uuidFromIntArray((int[])ownerUuidTag.getValue());
                skullOwnerCompoundTag.put((Tag)new StringTag("Id", ownerUuid.toString()));
            }
            final CompoundTag ownerTag = new CompoundTag("Owner");
            for (final Tag t : skullOwnerCompoundTag) {
                ownerTag.put(t);
            }
            tag.put((Tag)ownerTag);
        }
    }
    
    @Override
    protected void registerRewrites() {
        (this.enchantmentRewriter = new EnchantmentRewriter(this.nbtTagName)).registerEnchantment("minecraft:soul_speed", "§7Soul Speed");
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToClient(item);
        final CompoundTag tag = item.getTag();
        if (item.getIdentifier() == 771 && tag != null) {
            final Tag ownerTag = tag.get("SkullOwner");
            if (ownerTag instanceof CompoundTag) {
                final CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
                final Tag idTag = ownerCompundTag.get("Id");
                if (idTag instanceof IntArrayTag) {
                    final UUID ownerUuid = UUIDIntArrayType.uuidFromIntArray((int[])idTag.getValue());
                    ownerCompundTag.put((Tag)new StringTag("Id", ownerUuid.toString()));
                }
            }
        }
        InventoryPackets.newToOldAttributes(item);
        this.enchantmentRewriter.handleToClient(item);
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        final int identifier = item.getIdentifier();
        super.handleItemToServer(item);
        final CompoundTag tag = item.getTag();
        if (identifier == 771 && tag != null) {
            final Tag ownerTag = tag.get("SkullOwner");
            if (ownerTag instanceof CompoundTag) {
                final CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
                final Tag idTag = ownerCompundTag.get("Id");
                if (idTag instanceof StringTag) {
                    final UUID ownerUuid = UUID.fromString((String)idTag.getValue());
                    ownerCompundTag.put((Tag)new IntArrayTag("Id", UUIDIntArrayType.uuidToIntArray(ownerUuid)));
                }
            }
        }
        InventoryPackets.oldToNewAttributes(item);
        this.enchantmentRewriter.handleToServer(item);
        return item;
    }
    
    private static final class EquipmentData
    {
        private final int slot;
        private final Item item;
        
        private EquipmentData(final int slot, final Item item) {
            this.slot = slot;
            this.item = item;
        }
    }
}
