// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.data.UserConnection;
import com.google.common.primitives.Ints;
import java.util.List;
import java.util.Collection;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import java.util.Locale;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import nl.matsv.viabackwards.api.rewriters.EnchantmentRewriter;
import java.util.ArrayList;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.BlockIdData;
import us.myles.viaversion.libs.opennbt.conversion.ConverterRegistry;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Iterator;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.FlowerPotHandler;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import us.myles.ViaVersion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.Via;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import java.util.Optional;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.SpawnEggRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import java.util.HashMap;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import java.util.Map;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_13 extends ItemRewriter<Protocol1_12_2To1_13>
{
    private final Map<String, String> enchantmentMappings;
    private final String extraNbtTag;
    
    public BlockItemPackets1_13(final Protocol1_12_2To1_13 protocol) {
        super(protocol, null);
        this.enchantmentMappings = new HashMap<String, String>();
        this.extraNbtTag = "VB|" + protocol.getClass().getSimpleName() + "|2";
    }
    
    public static boolean isDamageable(final int id) {
        return (id >= 256 && id <= 259) || id == 261 || (id >= 267 && id <= 279) || (id >= 283 && id <= 286) || (id >= 290 && id <= 294) || (id >= 298 && id <= 317) || id == 346 || id == 359 || id == 398 || id == 442 || id == 443;
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.COOLDOWN, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        int itemId = (int)wrapper.read((Type)Type.VAR_INT);
                        final int oldId = ((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getItemMappings().get(itemId);
                        if (oldId != -1) {
                            final Optional<String> eggEntityId = (Optional<String>)SpawnEggRewriter.getEntityId(oldId);
                            if (eggEntityId.isPresent()) {
                                itemId = 25100288;
                            }
                            else {
                                itemId = (oldId >> 4 << 16 | (oldId & 0xF));
                            }
                        }
                        wrapper.write((Type)Type.VAR_INT, (Object)itemId);
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.BLOCK_ACTION, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        int blockId = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        if (blockId == 73) {
                            blockId = 25;
                        }
                        else if (blockId == 99) {
                            blockId = 33;
                        }
                        else if (blockId == 92) {
                            blockId = 29;
                        }
                        else if (blockId == 142) {
                            blockId = 54;
                        }
                        else if (blockId == 305) {
                            blockId = 146;
                        }
                        else if (blockId == 249) {
                            blockId = 130;
                        }
                        else if (blockId == 257) {
                            blockId = 138;
                        }
                        else if (blockId == 140) {
                            blockId = 52;
                        }
                        else if (blockId == 472) {
                            blockId = 209;
                        }
                        else if (blockId >= 483 && blockId <= 498) {
                            blockId = blockId - 483 + 219;
                        }
                        wrapper.set((Type)Type.VAR_INT, 0, (Object)blockId);
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.BLOCK_ENTITY_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final BackwardsBlockEntityProvider provider = (BackwardsBlockEntityProvider)Via.getManager().getProviders().get((Class)BackwardsBlockEntityProvider.class);
                        if ((short)wrapper.get(Type.UNSIGNED_BYTE, 0) == 5) {
                            wrapper.cancel();
                        }
                        wrapper.set(Type.NBT, 0, (Object)provider.transform(wrapper.user(), (Position)wrapper.get(Type.POSITION, 0), (CompoundTag)wrapper.get(Type.NBT, 0)));
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.UNLOAD_CHUNK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int chunkMinX = (int)wrapper.passthrough(Type.INT) << 4;
                        final int chunkMinZ = (int)wrapper.passthrough(Type.INT) << 4;
                        final int chunkMaxX = chunkMinX + 15;
                        final int chunkMaxZ = chunkMinZ + 15;
                        final BackwardsBlockStorage blockStorage = (BackwardsBlockStorage)wrapper.user().get((Class)BackwardsBlockStorage.class);
                        final Position position;
                        final int n;
                        final int n2;
                        final int n3;
                        final int n4;
                        blockStorage.getBlocks().entrySet().removeIf(entry -> {
                            position = entry.getKey();
                            return position.getX() >= n && position.getZ() >= n2 && position.getX() <= n3 && position.getZ() <= n4;
                        });
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int blockState = (int)wrapper.read((Type)Type.VAR_INT);
                        final Position position = (Position)wrapper.get(Type.POSITION, 0);
                        final BackwardsBlockStorage storage = (BackwardsBlockStorage)wrapper.user().get((Class)BackwardsBlockStorage.class);
                        storage.checkAndStore(position, blockState);
                        wrapper.write((Type)Type.VAR_INT, (Object)((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(blockState));
                        flowerPotSpecialTreatment(wrapper.user(), blockState, position);
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.MULTI_BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final BackwardsBlockStorage storage = (BackwardsBlockStorage)wrapper.user().get((Class)BackwardsBlockStorage.class);
                        for (final BlockChangeRecord record : (BlockChangeRecord[])wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                            final int chunkX = (int)wrapper.get(Type.INT, 0);
                            final int chunkZ = (int)wrapper.get(Type.INT, 1);
                            final int block = record.getBlockId();
                            final Position position = new Position(record.getSectionX() + chunkX * 16, record.getY(), record.getSectionZ() + chunkZ * 16);
                            storage.checkAndStore(position, block);
                            flowerPotSpecialTreatment(wrapper.user(), block, position);
                            record.setBlockId(((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(block));
                        }
                    }
                });
            }
        });
        final us.myles.ViaVersion.api.rewriters.ItemRewriter itemRewriter = new us.myles.ViaVersion.api.rewriters.ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.WINDOW_ITEMS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.FLAT_ITEM_ARRAY, Type.ITEM_ARRAY);
                this.handler(itemRewriter.itemArrayHandler(Type.ITEM_ARRAY));
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SET_SLOT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.FLAT_ITEM, Type.ITEM);
                this.handler(itemRewriter.itemToClientHandler(Type.ITEM));
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.CHUNK_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final ClientWorld clientWorld = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
                    final Chunk1_9_3_4Type type_old = new Chunk1_9_3_4Type(clientWorld);
                    final Chunk1_13Type type = new Chunk1_13Type(clientWorld);
                    final Chunk chunk = (Chunk)wrapper.read((Type)type);
                    final BackwardsBlockEntityProvider provider = (BackwardsBlockEntityProvider)Via.getManager().getProviders().get((Class)BackwardsBlockEntityProvider.class);
                    final BackwardsBlockStorage storage = (BackwardsBlockStorage)wrapper.user().get((Class)BackwardsBlockStorage.class);
                    for (final CompoundTag tag : chunk.getBlockEntities()) {
                        final Tag idTag = tag.get("id");
                        if (idTag == null) {
                            continue;
                        }
                        final String id = (String)idTag.getValue();
                        if (!provider.isHandled(id)) {
                            continue;
                        }
                        final int sectionIndex = (int)tag.get("y").getValue() >> 4;
                        final ChunkSection section = chunk.getSections()[sectionIndex];
                        final int x = (int)tag.get("x").getValue();
                        final int y = (int)tag.get("y").getValue();
                        final int z = (int)tag.get("z").getValue();
                        final Position position = new Position(x, (short)y, z);
                        final int block = section.getFlatBlock(x & 0xF, y & 0xF, z & 0xF);
                        storage.checkAndStore(position, block);
                        provider.transform(wrapper.user(), position, tag);
                    }
                    for (int i = 0; i < chunk.getSections().length; ++i) {
                        final ChunkSection section2 = chunk.getSections()[i];
                        if (section2 != null) {
                            for (int y2 = 0; y2 < 16; ++y2) {
                                for (int z2 = 0; z2 < 16; ++z2) {
                                    for (int x2 = 0; x2 < 16; ++x2) {
                                        final int block2 = section2.getFlatBlock(x2, y2, z2);
                                        if (FlowerPotHandler.isFlowah(block2)) {
                                            final Position pos = new Position(x2 + (chunk.getX() << 4), (short)(y2 + (i << 4)), z2 + (chunk.getZ() << 4));
                                            storage.checkAndStore(pos, block2);
                                            final CompoundTag nbt = provider.transform(wrapper.user(), pos, "minecraft:flower_pot");
                                            chunk.getBlockEntities().add(nbt);
                                        }
                                    }
                                }
                            }
                            for (int p = 0; p < section2.getPaletteSize(); ++p) {
                                final int old = section2.getPaletteEntry(p);
                                if (old != 0) {
                                    final int oldId = ((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(old);
                                    section2.setPaletteEntry(p, oldId);
                                }
                            }
                        }
                    }
                    if (chunk.isBiomeData()) {
                        for (int i = 0; i < 256; ++i) {
                            final int biome = chunk.getBiomeData()[i];
                            int newId = -1;
                            switch (biome) {
                                case 40:
                                case 41:
                                case 42:
                                case 43: {
                                    newId = 9;
                                    break;
                                }
                                case 47:
                                case 48:
                                case 49: {
                                    newId = 24;
                                    break;
                                }
                                case 50: {
                                    newId = 10;
                                    break;
                                }
                                case 44:
                                case 45:
                                case 46: {
                                    newId = 0;
                                    break;
                                }
                            }
                            if (newId != -1) {
                                chunk.getBiomeData()[i] = newId;
                            }
                        }
                    }
                    wrapper.write((Type)type_old, (Object)chunk);
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.EFFECT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = (int)wrapper.get(Type.INT, 0);
                        int data = (int)wrapper.get(Type.INT, 1);
                        if (id == 1010) {
                            wrapper.set(Type.INT, 1, (Object)(((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getItemMappings().get(data) >> 4));
                        }
                        else if (id == 2001) {
                            data = ((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(data);
                            final int blockId = data >> 4;
                            final int blockData = data & 0xF;
                            wrapper.set(Type.INT, 1, (Object)((blockId & 0xFFF) | blockData << 12));
                        }
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.MAP_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int iconCount = (int)wrapper.passthrough((Type)Type.VAR_INT), i = 0; i < iconCount; ++i) {
                            final int type = (int)wrapper.read((Type)Type.VAR_INT);
                            final byte x = (byte)wrapper.read(Type.BYTE);
                            final byte z = (byte)wrapper.read(Type.BYTE);
                            final byte direction = (byte)wrapper.read(Type.BYTE);
                            if (wrapper.read(Type.BOOLEAN)) {
                                wrapper.read(Type.COMPONENT);
                            }
                            if (type > 9) {
                                wrapper.set((Type)Type.VAR_INT, 1, (Object)((int)wrapper.get((Type)Type.VAR_INT, 1) - 1));
                            }
                            else {
                                wrapper.write(Type.BYTE, (Object)(byte)(type << 4 | (direction & 0xF)));
                                wrapper.write(Type.BYTE, (Object)x);
                                wrapper.write(Type.BYTE, (Object)z);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.ENTITY_EQUIPMENT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.map(Type.FLAT_ITEM, Type.ITEM);
                this.handler(itemRewriter.itemToClientHandler(Type.ITEM));
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.WINDOW_PROPERTY, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.handler(wrapper -> {
                    final short property = (short)wrapper.get((Type)Type.SHORT, 0);
                    if (property >= 4 && property <= 6) {
                        final short oldId = (short)wrapper.get((Type)Type.SHORT, 1);
                        wrapper.set((Type)Type.SHORT, 1, (Object)(short)((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getEnchantmentMappings().getNewId((int)oldId));
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.CREATIVE_INVENTORY_ACTION, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.SHORT);
                this.map(Type.ITEM, Type.FLAT_ITEM);
                this.handler(itemRewriter.itemToServerHandler(Type.FLAT_ITEM));
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.CLICK_WINDOW, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.VAR_INT);
                this.map(Type.ITEM, Type.FLAT_ITEM);
                this.handler(itemRewriter.itemToServerHandler(Type.FLAT_ITEM));
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.enchantmentMappings.put("minecraft:loyalty", "§7Loyalty");
        this.enchantmentMappings.put("minecraft:impaling", "§7Impaling");
        this.enchantmentMappings.put("minecraft:riptide", "§7Riptide");
        this.enchantmentMappings.put("minecraft:channeling", "§7Channeling");
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        final int originalId = item.getIdentifier();
        Integer rawId = null;
        boolean gotRawIdFromTag = false;
        CompoundTag tag = item.getTag();
        final Tag originalIdTag;
        if (tag != null && (originalIdTag = tag.remove(this.extraNbtTag)) != null) {
            rawId = (Integer)originalIdTag.getValue();
            gotRawIdFromTag = true;
        }
        if (rawId == null) {
            super.handleItemToClient(item);
            if (item.getIdentifier() == -1) {
                if (originalId == 362) {
                    rawId = 15007744;
                }
                else {
                    if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                        ViaBackwards.getPlatform().getLogger().warning("Failed to get 1.12 item for " + originalId);
                    }
                    rawId = 65536;
                }
            }
            else {
                if (tag == null) {
                    tag = item.getTag();
                }
                rawId = this.itemIdToRaw(item.getIdentifier(), item, tag);
            }
        }
        item.setIdentifier(rawId >> 16);
        item.setData((short)(rawId & 0xFFFF));
        if (tag != null) {
            if (isDamageable(item.getIdentifier())) {
                final Tag damageTag = tag.remove("Damage");
                if (!gotRawIdFromTag && damageTag instanceof IntTag) {
                    item.setData((short)(int)damageTag.getValue());
                }
            }
            if (item.getIdentifier() == 358) {
                final Tag mapTag = tag.remove("map");
                if (!gotRawIdFromTag && mapTag instanceof IntTag) {
                    item.setData((short)(int)mapTag.getValue());
                }
            }
            this.invertShieldAndBannerId(item, tag);
            final CompoundTag display = (CompoundTag)tag.get("display");
            if (display != null) {
                final StringTag name = (StringTag)display.get("Name");
                if (name instanceof StringTag) {
                    display.put((Tag)new StringTag(this.extraNbtTag + "|Name", name.getValue()));
                    name.setValue(ChatRewriter.jsonTextToLegacy(name.getValue()));
                }
            }
            this.rewriteEnchantmentsToClient(tag, false);
            this.rewriteEnchantmentsToClient(tag, true);
            this.rewriteCanPlaceToClient(tag, "CanPlaceOn");
            this.rewriteCanPlaceToClient(tag, "CanDestroy");
        }
        return item;
    }
    
    private int itemIdToRaw(final int oldId, final Item item, CompoundTag tag) {
        final Optional<String> eggEntityId = (Optional<String>)SpawnEggRewriter.getEntityId(oldId);
        if (eggEntityId.isPresent()) {
            if (tag == null) {
                item.setTag(tag = new CompoundTag("tag"));
            }
            if (!tag.contains("EntityTag")) {
                final CompoundTag entityTag = new CompoundTag("EntityTag");
                entityTag.put((Tag)new StringTag("id", (String)eggEntityId.get()));
                tag.put((Tag)entityTag);
            }
            return 25100288;
        }
        return oldId >> 4 << 16 | (oldId & 0xF);
    }
    
    private void rewriteCanPlaceToClient(final CompoundTag tag, final String tagName) {
        if (!(tag.get(tagName) instanceof ListTag)) {
            return;
        }
        final ListTag blockTag = (ListTag)tag.get(tagName);
        if (blockTag == null) {
            return;
        }
        final ListTag newCanPlaceOn = new ListTag(tagName, (Class)StringTag.class);
        tag.put(ConverterRegistry.convertToTag(this.extraNbtTag + "|" + tagName, ConverterRegistry.convertToValue((Tag)blockTag)));
        for (final Tag oldTag : blockTag) {
            final Object value = oldTag.getValue();
            final String[] newValues = (value instanceof String) ? BlockIdData.fallbackReverseMapping.get(((String)value).replace("minecraft:", "")) : null;
            if (newValues != null) {
                for (final String newValue : newValues) {
                    newCanPlaceOn.add((Tag)new StringTag("", newValue));
                }
            }
            else {
                newCanPlaceOn.add(oldTag);
            }
        }
        tag.put((Tag)newCanPlaceOn);
    }
    
    private void rewriteEnchantmentsToClient(final CompoundTag tag, final boolean storedEnch) {
        final String key = storedEnch ? "StoredEnchantments" : "Enchantments";
        final ListTag enchantments = (ListTag)tag.get(key);
        if (enchantments == null) {
            return;
        }
        final ListTag noMapped = new ListTag(this.extraNbtTag + "|" + key, (Class)CompoundTag.class);
        final ListTag newEnchantments = new ListTag(storedEnch ? key : "ench", (Class)CompoundTag.class);
        final List<Tag> lore = new ArrayList<Tag>();
        boolean hasValidEnchants = false;
        for (final Tag enchantmentEntryTag : enchantments.clone()) {
            final CompoundTag enchantmentEntry = (CompoundTag)enchantmentEntryTag;
            final String newId = (String)enchantmentEntry.get("id").getValue();
            final Number levelValue = (Number)enchantmentEntry.get("lvl").getValue();
            final int intValue = levelValue.intValue();
            final short level = (short)((intValue < 32767) ? levelValue.shortValue() : 32767);
            final String mappedEnchantmentId = this.enchantmentMappings.get(newId);
            if (mappedEnchantmentId != null) {
                lore.add((Tag)new StringTag("", mappedEnchantmentId + " " + EnchantmentRewriter.getRomanNumber(level)));
                noMapped.add((Tag)enchantmentEntry);
            }
            else {
                if (newId.isEmpty()) {
                    continue;
                }
                Short oldId = (Short)Protocol1_13To1_12_2.MAPPINGS.getOldEnchantmentsIds().inverse().get((Object)newId);
                if (oldId == null) {
                    if (!newId.startsWith("viaversion:legacy/")) {
                        noMapped.add((Tag)enchantmentEntry);
                        if (ViaBackwards.getConfig().addCustomEnchantsToLore()) {
                            String name = newId;
                            final int index = name.indexOf(58) + 1;
                            if (index != 0 && index != name.length()) {
                                name = name.substring(index);
                            }
                            name = "§7" + Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase(Locale.ENGLISH);
                            lore.add((Tag)new StringTag("", name + " " + EnchantmentRewriter.getRomanNumber(level)));
                        }
                        if (Via.getManager().isDebug()) {
                            ViaBackwards.getPlatform().getLogger().warning("Found unknown enchant: " + newId);
                            continue;
                        }
                        continue;
                    }
                    else {
                        oldId = Short.valueOf(newId.substring(18));
                    }
                }
                if (level != 0) {
                    hasValidEnchants = true;
                }
                final CompoundTag newEntry = new CompoundTag("");
                newEntry.put((Tag)new ShortTag("id", (short)oldId));
                newEntry.put((Tag)new ShortTag("lvl", level));
                newEnchantments.add((Tag)newEntry);
            }
        }
        if (!storedEnch && !hasValidEnchants) {
            IntTag hideFlags = (IntTag)tag.get("HideFlags");
            if (hideFlags == null) {
                hideFlags = new IntTag("HideFlags");
                tag.put((Tag)new ByteTag(this.extraNbtTag + "|DummyEnchant"));
            }
            else {
                tag.put((Tag)new IntTag(this.extraNbtTag + "|OldHideFlags", (int)hideFlags.getValue()));
            }
            if (newEnchantments.size() == 0) {
                final CompoundTag enchEntry = new CompoundTag("");
                enchEntry.put((Tag)new ShortTag("id", (short)0));
                enchEntry.put((Tag)new ShortTag("lvl", (short)0));
                newEnchantments.add((Tag)enchEntry);
            }
            final int value = hideFlags.getValue() | 0x1;
            hideFlags.setValue(value);
            tag.put((Tag)hideFlags);
        }
        if (noMapped.size() != 0) {
            tag.put((Tag)noMapped);
            if (!lore.isEmpty()) {
                CompoundTag display = (CompoundTag)tag.get("display");
                if (display == null) {
                    tag.put((Tag)(display = new CompoundTag("display")));
                }
                ListTag loreTag = (ListTag)display.get("Lore");
                if (loreTag == null) {
                    display.put((Tag)(loreTag = new ListTag("Lore", (Class)StringTag.class)));
                    tag.put((Tag)new ByteTag(this.extraNbtTag + "|DummyLore"));
                }
                else if (loreTag.size() != 0) {
                    final ListTag oldLore = new ListTag(this.extraNbtTag + "|OldLore", (Class)StringTag.class);
                    for (final Tag value2 : loreTag) {
                        oldLore.add(value2.clone());
                    }
                    tag.put((Tag)oldLore);
                    lore.addAll(loreTag.getValue());
                }
                loreTag.setValue((List)lore);
            }
        }
        tag.remove("Enchantments");
        tag.put((Tag)newEnchantments);
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        CompoundTag tag = item.getTag();
        final int originalId = item.getIdentifier() << 16 | (item.getData() & 0xFFFF);
        int rawId = item.getIdentifier() << 4 | (item.getData() & 0xF);
        if (isDamageable(item.getIdentifier())) {
            if (tag == null) {
                item.setTag(tag = new CompoundTag("tag"));
            }
            tag.put((Tag)new IntTag("Damage", (int)item.getData()));
        }
        if (item.getIdentifier() == 358) {
            if (tag == null) {
                item.setTag(tag = new CompoundTag("tag"));
            }
            tag.put((Tag)new IntTag("map", (int)item.getData()));
        }
        if (tag != null) {
            this.invertShieldAndBannerId(item, tag);
            final Tag display = tag.get("display");
            if (display instanceof CompoundTag) {
                final CompoundTag displayTag = (CompoundTag)display;
                final StringTag name = (StringTag)displayTag.get("Name");
                if (name instanceof StringTag) {
                    final StringTag via = (StringTag)displayTag.remove(this.extraNbtTag + "|Name");
                    name.setValue((via != null) ? via.getValue() : ChatRewriter.legacyTextToJson(name.getValue()).toString());
                }
            }
            this.rewriteEnchantmentsToServer(tag, false);
            this.rewriteEnchantmentsToServer(tag, true);
            this.rewriteCanPlaceToServer(tag, "CanPlaceOn");
            this.rewriteCanPlaceToServer(tag, "CanDestroy");
            if (item.getIdentifier() == 383) {
                final CompoundTag entityTag = (CompoundTag)tag.get("EntityTag");
                final StringTag identifier;
                if (entityTag != null && (identifier = (StringTag)entityTag.get("id")) != null) {
                    rawId = SpawnEggRewriter.getSpawnEggId(identifier.getValue());
                    if (rawId == -1) {
                        rawId = 25100288;
                    }
                    else {
                        entityTag.remove("id");
                        if (entityTag.isEmpty()) {
                            tag.remove("EntityTag");
                        }
                    }
                }
                else {
                    rawId = 25100288;
                }
            }
            if (tag.isEmpty()) {
                item.setTag(tag = null);
            }
        }
        final int identifier2 = item.getIdentifier();
        item.setIdentifier(rawId);
        super.handleItemToServer(item);
        if (item.getIdentifier() != rawId && item.getIdentifier() != -1) {
            return item;
        }
        item.setIdentifier(identifier2);
        int newId = -1;
        if (!((Protocol1_12_2To1_13)this.protocol).getMappingData().getItemMappings().inverse().containsKey(rawId)) {
            if (!isDamageable(item.getIdentifier()) && item.getIdentifier() != 358) {
                if (tag == null) {
                    item.setTag(tag = new CompoundTag("tag"));
                }
                tag.put((Tag)new IntTag(this.extraNbtTag, originalId));
            }
            if (item.getIdentifier() == 229) {
                newId = 362;
            }
            else if (item.getIdentifier() == 31 && item.getData() == 0) {
                rawId = 512;
            }
            else if (((Protocol1_12_2To1_13)this.protocol).getMappingData().getItemMappings().inverse().containsKey(rawId & 0xFFFFFFF0)) {
                rawId &= 0xFFFFFFF0;
            }
            else {
                if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                    ViaBackwards.getPlatform().getLogger().warning("Failed to get 1.13 item for " + item.getIdentifier());
                }
                rawId = 16;
            }
        }
        if (newId == -1) {
            newId = ((Protocol1_12_2To1_13)this.protocol).getMappingData().getItemMappings().inverse().get(rawId);
        }
        item.setIdentifier(newId);
        item.setData((short)0);
        return item;
    }
    
    private void rewriteCanPlaceToServer(final CompoundTag tag, final String tagName) {
        if (!(tag.get(tagName) instanceof ListTag)) {
            return;
        }
        ListTag blockTag = (ListTag)tag.remove(this.extraNbtTag + "|" + tagName);
        if (blockTag != null) {
            tag.put(ConverterRegistry.convertToTag(tagName, ConverterRegistry.convertToValue((Tag)blockTag)));
        }
        else if ((blockTag = (ListTag)tag.get(tagName)) != null) {
            final ListTag newCanPlaceOn = new ListTag(tagName, (Class)StringTag.class);
            for (final Tag oldTag : blockTag) {
                final Object value = oldTag.getValue();
                String oldId = value.toString().replace("minecraft:", "");
                final String numberConverted = (String)BlockIdData.numberIdToString.get((Object)Ints.tryParse(oldId));
                if (numberConverted != null) {
                    oldId = numberConverted;
                }
                final String lowerCaseId = oldId.toLowerCase(Locale.ROOT);
                final String[] newValues = BlockIdData.blockIdMapping.get(lowerCaseId);
                if (newValues != null) {
                    for (final String newValue : newValues) {
                        newCanPlaceOn.add((Tag)new StringTag("", newValue));
                    }
                }
                else {
                    newCanPlaceOn.add((Tag)new StringTag("", lowerCaseId));
                }
            }
            tag.put((Tag)newCanPlaceOn);
        }
    }
    
    private void rewriteEnchantmentsToServer(final CompoundTag tag, final boolean storedEnch) {
        final String key = storedEnch ? "StoredEnchantments" : "Enchantments";
        final ListTag enchantments = (ListTag)tag.get(storedEnch ? key : "ench");
        if (enchantments == null) {
            return;
        }
        final ListTag newEnchantments = new ListTag(key, (Class)CompoundTag.class);
        boolean dummyEnchant = false;
        if (!storedEnch) {
            final IntTag hideFlags = (IntTag)tag.remove(this.extraNbtTag + "|OldHideFlags");
            if (hideFlags != null) {
                tag.put((Tag)new IntTag("HideFlags", (int)hideFlags.getValue()));
                dummyEnchant = true;
            }
            else if (tag.remove(this.extraNbtTag + "|DummyEnchant") != null) {
                tag.remove("HideFlags");
                dummyEnchant = true;
            }
        }
        for (final Tag enchEntry : enchantments) {
            final CompoundTag enchantmentEntry = new CompoundTag("");
            final short oldId = ((Number)((CompoundTag)enchEntry).get("id").getValue()).shortValue();
            final short level = ((Number)((CompoundTag)enchEntry).get("lvl").getValue()).shortValue();
            if (dummyEnchant && oldId == 0 && level == 0) {
                continue;
            }
            String newId = (String)Protocol1_13To1_12_2.MAPPINGS.getOldEnchantmentsIds().get((Object)oldId);
            if (newId == null) {
                newId = "viaversion:legacy/" + oldId;
            }
            enchantmentEntry.put((Tag)new StringTag("id", newId));
            enchantmentEntry.put((Tag)new ShortTag("lvl", level));
            newEnchantments.add((Tag)enchantmentEntry);
        }
        final ListTag noMapped = (ListTag)tag.remove(this.extraNbtTag + "|Enchantments");
        if (noMapped != null) {
            for (final Tag value : noMapped) {
                newEnchantments.add(value);
            }
        }
        CompoundTag display = (CompoundTag)tag.get("display");
        if (display == null) {
            tag.put((Tag)(display = new CompoundTag("display")));
        }
        final ListTag oldLore = (ListTag)tag.remove(this.extraNbtTag + "|OldLore");
        if (oldLore != null) {
            ListTag lore = (ListTag)display.get("Lore");
            if (lore == null) {
                tag.put((Tag)(lore = new ListTag("Lore")));
            }
            lore.setValue(oldLore.getValue());
        }
        else if (tag.remove(this.extraNbtTag + "|DummyLore") != null) {
            display.remove("Lore");
            if (display.isEmpty()) {
                tag.remove("display");
            }
        }
        if (!storedEnch) {
            tag.remove("ench");
        }
        tag.put((Tag)newEnchantments);
    }
    
    private void invertShieldAndBannerId(final Item item, final CompoundTag tag) {
        if (item.getIdentifier() != 442 && item.getIdentifier() != 425) {
            return;
        }
        final Tag blockEntityTag = tag.get("BlockEntityTag");
        if (!(blockEntityTag instanceof CompoundTag)) {
            return;
        }
        final CompoundTag blockEntityCompoundTag = (CompoundTag)blockEntityTag;
        final Tag base = blockEntityCompoundTag.get("Base");
        if (base instanceof IntTag) {
            final IntTag baseTag = (IntTag)base;
            baseTag.setValue(15 - baseTag.getValue());
        }
        final Tag patterns = blockEntityCompoundTag.get("Patterns");
        if (patterns instanceof ListTag) {
            final ListTag patternsTag = (ListTag)patterns;
            for (final Tag pattern : patternsTag) {
                if (!(pattern instanceof CompoundTag)) {
                    continue;
                }
                final IntTag colorTag = (IntTag)((CompoundTag)pattern).get("Color");
                colorTag.setValue(15 - colorTag.getValue());
            }
        }
    }
    
    private static void flowerPotSpecialTreatment(final UserConnection user, final int blockState, final Position position) throws Exception {
        if (FlowerPotHandler.isFlowah(blockState)) {
            final BackwardsBlockEntityProvider beProvider = (BackwardsBlockEntityProvider)Via.getManager().getProviders().get((Class)BackwardsBlockEntityProvider.class);
            final CompoundTag nbt = beProvider.transform(user, position, "minecraft:flower_pot");
            final PacketWrapper blockUpdateRemove = new PacketWrapper(11, (ByteBuf)null, user);
            blockUpdateRemove.write(Type.POSITION, (Object)position);
            blockUpdateRemove.write((Type)Type.VAR_INT, (Object)0);
            blockUpdateRemove.send((Class)Protocol1_12_2To1_13.class, true);
            final PacketWrapper blockCreate = new PacketWrapper(11, (ByteBuf)null, user);
            blockCreate.write(Type.POSITION, (Object)position);
            blockCreate.write((Type)Type.VAR_INT, (Object)Protocol1_12_2To1_13.MAPPINGS.getNewBlockStateId(blockState));
            blockCreate.send((Class)Protocol1_12_2To1_13.class, true);
            final PacketWrapper wrapper = new PacketWrapper(9, (ByteBuf)null, user);
            wrapper.write(Type.POSITION, (Object)position);
            wrapper.write(Type.UNSIGNED_BYTE, (Object)5);
            wrapper.write(Type.NBT, (Object)nbt);
            wrapper.send((Class)Protocol1_12_2To1_13.class, true);
        }
    }
}
