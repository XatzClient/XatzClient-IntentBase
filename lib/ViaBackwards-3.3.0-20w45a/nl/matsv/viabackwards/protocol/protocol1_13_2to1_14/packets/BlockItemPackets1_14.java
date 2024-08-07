// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets;

import java.util.Iterator;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.conversion.ConverterRegistry;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.minecraft.Environment;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import us.myles.ViaVersion.api.rewriters.RecipeRewriter;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.RecipeRewriter1_13_2;
import java.util.List;
import us.myles.ViaVersion.api.type.types.version.Types1_13;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_13_2;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.ArrayList;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_14Types;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.viaversion.libs.gson.JsonElement;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.api.rewriters.EnchantmentRewriter;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_14 extends ItemRewriter<Protocol1_13_2To1_14>
{
    private EnchantmentRewriter enchantmentRewriter;
    
    public BlockItemPackets1_14(final Protocol1_13_2To1_14 protocol, final TranslatableRewriter translatableRewriter) {
        super(protocol, translatableRewriter);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.EDIT_BOOK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> BlockItemPackets1_14.this.handleItemToServer((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.OPEN_WINDOW, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int windowId = (int)wrapper.read((Type)Type.VAR_INT);
                        wrapper.write(Type.UNSIGNED_BYTE, (Object)(short)windowId);
                        final int type = (int)wrapper.read((Type)Type.VAR_INT);
                        String stringType = null;
                        String containerTitle = null;
                        int slotSize = 0;
                        if (type < 6) {
                            if (type == 2) {
                                containerTitle = "Barrel";
                            }
                            stringType = "minecraft:container";
                            slotSize = (type + 1) * 9;
                        }
                        else {
                            switch (type) {
                                case 11: {
                                    stringType = "minecraft:crafting_table";
                                    break;
                                }
                                case 9:
                                case 13:
                                case 14:
                                case 20: {
                                    if (type == 9) {
                                        containerTitle = "Blast Furnace";
                                    }
                                    else if (type == 20) {
                                        containerTitle = "Smoker";
                                    }
                                    else if (type == 14) {
                                        containerTitle = "Grindstone";
                                    }
                                    stringType = "minecraft:furnace";
                                    slotSize = 3;
                                    break;
                                }
                                case 6: {
                                    stringType = "minecraft:dropper";
                                    slotSize = 9;
                                    break;
                                }
                                case 12: {
                                    stringType = "minecraft:enchanting_table";
                                    break;
                                }
                                case 10: {
                                    stringType = "minecraft:brewing_stand";
                                    slotSize = 5;
                                    break;
                                }
                                case 18: {
                                    stringType = "minecraft:villager";
                                    break;
                                }
                                case 8: {
                                    stringType = "minecraft:beacon";
                                    slotSize = 1;
                                    break;
                                }
                                case 7:
                                case 21: {
                                    if (type == 21) {
                                        containerTitle = "Cartography Table";
                                    }
                                    stringType = "minecraft:anvil";
                                    break;
                                }
                                case 15: {
                                    stringType = "minecraft:hopper";
                                    slotSize = 5;
                                    break;
                                }
                                case 19: {
                                    stringType = "minecraft:shulker_box";
                                    slotSize = 27;
                                    break;
                                }
                            }
                        }
                        if (stringType == null) {
                            ViaBackwards.getPlatform().getLogger().warning("Can't open inventory for 1.13 player! Type: " + type);
                            wrapper.cancel();
                            return;
                        }
                        wrapper.write(Type.STRING, (Object)stringType);
                        JsonElement title = (JsonElement)wrapper.read(Type.COMPONENT);
                        final JsonObject object;
                        if (containerTitle != null && title.isJsonObject() && (object = title.getAsJsonObject()).has("translate") && (type != 2 || object.getAsJsonPrimitive("translate").getAsString().equals("container.barrel"))) {
                            title = ChatRewriter.legacyTextToJson(containerTitle);
                        }
                        wrapper.write(Type.COMPONENT, (Object)title);
                        wrapper.write(Type.UNSIGNED_BYTE, (Object)(short)slotSize);
                    }
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.OPEN_HORSE_WINDOW, (ClientboundPacketType)ClientboundPackets1_13.OPEN_WINDOW, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough(Type.UNSIGNED_BYTE);
                        wrapper.write(Type.STRING, (Object)"EntityHorse");
                        final JsonObject object = new JsonObject();
                        object.addProperty("translate", "minecraft.horse");
                        wrapper.write(Type.COMPONENT, (Object)object);
                        wrapper.write(Type.UNSIGNED_BYTE, (Object)((Integer)wrapper.read((Type)Type.VAR_INT)).shortValue());
                        wrapper.passthrough(Type.INT);
                    }
                });
            }
        });
        final us.myles.ViaVersion.api.rewriters.ItemRewriter itemRewriter = new us.myles.ViaVersion.api.rewriters.ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
        final BlockRewriter blockRewriter = new BlockRewriter((Protocol)this.protocol, Type.POSITION);
        itemRewriter.registerSetCooldown((ClientboundPacketType)ClientboundPackets1_14.COOLDOWN);
        itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_14.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_14.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerAdvancements((ClientboundPacketType)ClientboundPackets1_14.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.TRADE_LIST, (ClientboundPacketType)ClientboundPackets1_13.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.STRING, (Object)"minecraft:trader_list");
                        final int windowId = (int)wrapper.read((Type)Type.VAR_INT);
                        wrapper.write(Type.INT, (Object)windowId);
                        for (int size = (short)wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                            Item input = (Item)wrapper.read(Type.FLAT_VAR_INT_ITEM);
                            input = BlockItemPackets1_14.this.handleItemToClient(input);
                            wrapper.write(Type.FLAT_VAR_INT_ITEM, (Object)input);
                            Item output = (Item)wrapper.read(Type.FLAT_VAR_INT_ITEM);
                            output = BlockItemPackets1_14.this.handleItemToClient(output);
                            wrapper.write(Type.FLAT_VAR_INT_ITEM, (Object)output);
                            final boolean secondItem = (boolean)wrapper.passthrough(Type.BOOLEAN);
                            if (secondItem) {
                                Item second = (Item)wrapper.read(Type.FLAT_VAR_INT_ITEM);
                                second = BlockItemPackets1_14.this.handleItemToClient(second);
                                wrapper.write(Type.FLAT_VAR_INT_ITEM, (Object)second);
                            }
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough(Type.INT);
                            wrapper.read(Type.INT);
                            wrapper.read(Type.INT);
                            wrapper.read((Type)Type.FLOAT);
                        }
                        wrapper.read((Type)Type.VAR_INT);
                        wrapper.read((Type)Type.VAR_INT);
                        wrapper.read(Type.BOOLEAN);
                    }
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.OPEN_BOOK, (ClientboundPacketType)ClientboundPackets1_13.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.STRING, (Object)"minecraft:book_open");
                        wrapper.passthrough((Type)Type.VAR_INT);
                    }
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_EQUIPMENT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.map(Type.FLAT_VAR_INT_ITEM);
                this.handler(itemRewriter.itemToClientHandler(Type.FLAT_VAR_INT_ITEM));
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        final EntityType entityType = ((EntityTracker)wrapper.user().get((Class)EntityTracker.class)).get(BlockItemPackets1_14.this.getProtocol()).getEntityType(entityId);
                        if (entityType == null) {
                            return;
                        }
                        if (entityType.isOrHasParent((EntityType)Entity1_14Types.EntityType.ABSTRACT_HORSE)) {
                            wrapper.setId(63);
                            wrapper.resetReader();
                            wrapper.passthrough((Type)Type.VAR_INT);
                            wrapper.read((Type)Type.VAR_INT);
                            final Item item = (Item)wrapper.read(Type.FLAT_VAR_INT_ITEM);
                            final int armorType = (item == null || item.getIdentifier() == 0) ? 0 : (item.getIdentifier() - 726);
                            if (armorType < 0 || armorType > 3) {
                                ViaBackwards.getPlatform().getLogger().warning("Received invalid horse armor: " + item);
                                wrapper.cancel();
                                return;
                            }
                            final List<Metadata> metadataList = new ArrayList<Metadata>();
                            metadataList.add(new Metadata(16, (MetaType)MetaType1_13_2.VarInt, (Object)armorType));
                            wrapper.write(Types1_13.METADATA_LIST, (Object)metadataList);
                        }
                    }
                });
            }
        });
        final RecipeRewriter recipeHandler = (RecipeRewriter)new RecipeRewriter1_13_2((Protocol)this.protocol, this::handleItemToClient);
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.DECLARE_RECIPES, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    private final Set<String> removedTypes = ImmutableSet.of((Object)"crafting_special_suspiciousstew", (Object)"blasting", (Object)"smoking", (Object)"campfire_cooking", (Object)"stonecutting");
                    
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int size = (int)wrapper.passthrough((Type)Type.VAR_INT);
                        int deleted = 0;
                        for (int i = 0; i < size; ++i) {
                            String type = (String)wrapper.read(Type.STRING);
                            final String id = (String)wrapper.read(Type.STRING);
                            type = type.replace("minecraft:", "");
                            if (this.removedTypes.contains(type)) {
                                final String s = type;
                                switch (s) {
                                    case "blasting":
                                    case "smoking":
                                    case "campfire_cooking": {
                                        wrapper.read(Type.STRING);
                                        wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                                        wrapper.read(Type.FLAT_VAR_INT_ITEM);
                                        wrapper.read((Type)Type.FLOAT);
                                        wrapper.read((Type)Type.VAR_INT);
                                        break;
                                    }
                                    case "stonecutting": {
                                        wrapper.read(Type.STRING);
                                        wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                                        wrapper.read(Type.FLAT_VAR_INT_ITEM);
                                        break;
                                    }
                                }
                                ++deleted;
                            }
                            else {
                                wrapper.write(Type.STRING, (Object)id);
                                wrapper.write(Type.STRING, (Object)type);
                                recipeHandler.handle(wrapper, type);
                            }
                        }
                        wrapper.set((Type)Type.VAR_INT, 0, (Object)(size - deleted));
                    }
                });
            }
        });
        itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_13.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.BLOCK_BREAK_ANIMATION, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.BYTE);
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.BLOCK_ENTITY_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.BLOCK_ACTION, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int mappedId = ((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockId((int)wrapper.get((Type)Type.VAR_INT, 0));
                    if (mappedId == -1) {
                        wrapper.cancel();
                        return;
                    }
                    wrapper.set((Type)Type.VAR_INT, 0, (Object)mappedId);
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        wrapper.set((Type)Type.VAR_INT, 0, (Object)((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockStateId(id));
                    }
                });
            }
        });
        blockRewriter.registerMultiBlockChange((ClientboundPacketType)ClientboundPackets1_14.MULTI_BLOCK_CHANGE);
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.EXPLOSION, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int i = 0; i < 3; ++i) {
                            float coord = (float)wrapper.get((Type)Type.FLOAT, i);
                            if (coord < 0.0f) {
                                coord = (float)Math.floor(coord);
                                wrapper.set((Type)Type.FLOAT, i, (Object)coord);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.CHUNK_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
                        final Chunk chunk = (Chunk)wrapper.read((Type)new Chunk1_14Type());
                        wrapper.write((Type)new Chunk1_13Type(clientWorld), (Object)chunk);
                        final ChunkLightStorage.ChunkLight chunkLight = ((ChunkLightStorage)wrapper.user().get((Class)ChunkLightStorage.class)).getStoredLight(chunk.getX(), chunk.getZ());
                        for (int i = 0; i < chunk.getSections().length; ++i) {
                            final ChunkSection section = chunk.getSections()[i];
                            if (section != null) {
                                if (chunkLight == null) {
                                    section.setBlockLight(ChunkLightStorage.FULL_LIGHT);
                                    if (clientWorld.getEnvironment() == Environment.NORMAL) {
                                        section.setSkyLight(ChunkLightStorage.FULL_LIGHT);
                                    }
                                }
                                else {
                                    final byte[] blockLight = chunkLight.getBlockLight()[i];
                                    section.setBlockLight((blockLight != null) ? blockLight : ChunkLightStorage.FULL_LIGHT);
                                    if (clientWorld.getEnvironment() == Environment.NORMAL) {
                                        final byte[] skyLight = chunkLight.getSkyLight()[i];
                                        section.setSkyLight((skyLight != null) ? skyLight : ChunkLightStorage.FULL_LIGHT);
                                    }
                                }
                                if (Via.getConfig().isNonFullBlockLightFix() && section.getNonAirBlocksCount() != 0 && section.hasBlockLight()) {
                                    for (int x = 0; x < 16; ++x) {
                                        for (int y = 0; y < 16; ++y) {
                                            for (int z = 0; z < 16; ++z) {
                                                final int id = section.getFlatBlock(x, y, z);
                                                if (Protocol1_14To1_13_2.MAPPINGS.getNonFullBlocks().contains(id)) {
                                                    section.getBlockLightNibbleArray().set(x, y, z, 0);
                                                }
                                            }
                                        }
                                    }
                                }
                                for (int j = 0; j < section.getPaletteSize(); ++j) {
                                    final int old = section.getPaletteEntry(j);
                                    final int newId = ((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockStateId(old);
                                    section.setPaletteEntry(j, newId);
                                }
                            }
                        }
                    }
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.UNLOAD_CHUNK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int x = (int)wrapper.passthrough(Type.INT);
                        final int z = (int)wrapper.passthrough(Type.INT);
                        ((ChunkLightStorage)wrapper.user().get((Class)ChunkLightStorage.class)).unloadChunk(x, z);
                    }
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.EFFECT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = (int)wrapper.get(Type.INT, 0);
                        final int data = (int)wrapper.get(Type.INT, 1);
                        if (id == 1010) {
                            wrapper.set(Type.INT, 1, (Object)((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewItemId(data));
                        }
                        else if (id == 2001) {
                            wrapper.set(Type.INT, 1, (Object)((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockStateId(data));
                        }
                    }
                });
            }
        });
        itemRewriter.registerSpawnParticle((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, (Type)Type.FLOAT);
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.MAP_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN, Type.NOTHING);
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_POSITION, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        (this.enchantmentRewriter = new EnchantmentRewriter(this.nbtTagName, false)).registerEnchantment("minecraft:multishot", "§7Multishot");
        this.enchantmentRewriter.registerEnchantment("minecraft:quick_charge", "§7Quick Charge");
        this.enchantmentRewriter.registerEnchantment("minecraft:piercing", "§7Piercing");
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToClient(item);
        final CompoundTag tag = item.getTag();
        if (tag != null) {
            if (tag.get("display") instanceof CompoundTag) {
                final CompoundTag display = (CompoundTag)tag.get("display");
                if (((CompoundTag)tag.get("display")).get("Lore") instanceof ListTag) {
                    final ListTag lore = (ListTag)display.get("Lore");
                    final ListTag via = (ListTag)display.remove(this.nbtTagName + "|Lore");
                    if (via != null) {
                        display.put(ConverterRegistry.convertToTag("Lore", ConverterRegistry.convertToValue((Tag)via)));
                    }
                    else {
                        for (final Tag loreEntry : lore) {
                            if (!(loreEntry instanceof StringTag)) {
                                continue;
                            }
                            final String value = ((StringTag)loreEntry).getValue();
                            if (value == null || value.isEmpty()) {
                                continue;
                            }
                            ((StringTag)loreEntry).setValue(ChatRewriter.jsonTextToLegacy(value));
                        }
                    }
                }
            }
            this.enchantmentRewriter.handleToClient(item);
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
        if (tag != null) {
            if (tag.get("display") instanceof CompoundTag) {
                final CompoundTag display = (CompoundTag)tag.get("display");
                if (display.get("Lore") instanceof ListTag) {
                    final ListTag lore = (ListTag)display.get("Lore");
                    display.put(ConverterRegistry.convertToTag(this.nbtTagName + "|Lore", ConverterRegistry.convertToValue((Tag)lore)));
                    for (final Tag loreEntry : lore) {
                        if (loreEntry instanceof StringTag) {
                            ((StringTag)loreEntry).setValue(ChatRewriter.legacyTextToJson(((StringTag)loreEntry).getValue()).toString());
                        }
                    }
                }
            }
            this.enchantmentRewriter.handleToServer(item);
        }
        return item;
    }
}
