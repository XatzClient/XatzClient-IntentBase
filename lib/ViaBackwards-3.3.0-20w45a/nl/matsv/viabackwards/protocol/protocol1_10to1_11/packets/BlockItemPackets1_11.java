// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets;

import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_11Types;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import nl.matsv.viabackwards.api.data.MappedLegacyBlockItem;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.storage.WindowTracker;
import us.myles.ViaVersion.protocols.protocol1_11to1_10.EntityIdRewriter;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Iterator;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import java.util.Arrays;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import java.util.Optional;
import us.myles.ViaVersion.api.minecraft.item.Item;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.storage.ChestedHorseStorage;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import nl.matsv.viabackwards.api.rewriters.LegacyEnchantmentRewriter;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import nl.matsv.viabackwards.api.rewriters.LegacyBlockItemRewriter;

public class BlockItemPackets1_11 extends LegacyBlockItemRewriter<Protocol1_10To1_11>
{
    private LegacyEnchantmentRewriter enchantmentRewriter;
    
    public BlockItemPackets1_11(final Protocol1_10To1_11 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        final ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SET_SLOT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.ITEM);
                this.handler(itemRewriter.itemToClientHandler(Type.ITEM));
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                            final Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
                            if (!horse.isPresent()) {
                                return;
                            }
                            final ChestedHorseStorage storage = horse.get();
                            int currentSlot = (short)wrapper.get((Type)Type.SHORT, 0);
                            wrapper.set((Type)Type.SHORT, 0, (Object)(currentSlot = BlockItemPackets1_11.this.getNewSlotId(storage, currentSlot)).shortValue());
                            wrapper.set(Type.ITEM, 0, (Object)BlockItemPackets1_11.this.getNewItem(storage, currentSlot, (Item)wrapper.get(Type.ITEM, 0)));
                        }
                    }
                });
            }
        });
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.WINDOW_ITEMS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.ITEM_ARRAY);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        Item[] stacks = (Item[])wrapper.get(Type.ITEM_ARRAY, 0);
                        for (int i = 0; i < stacks.length; ++i) {
                            stacks[i] = BlockItemPackets1_11.this.handleItemToClient(stacks[i]);
                        }
                        if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                            final Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
                            if (!horse.isPresent()) {
                                return;
                            }
                            final ChestedHorseStorage storage = horse.get();
                            stacks = Arrays.copyOf(stacks, storage.isChested() ? 53 : 38);
                            for (int j = stacks.length - 1; j >= 0; --j) {
                                stacks[BlockItemPackets1_11.this.getNewSlotId(storage, j)] = stacks[j];
                                stacks[j] = BlockItemPackets1_11.this.getNewItem(storage, j, stacks[j]);
                            }
                            wrapper.set(Type.ITEM_ARRAY, 0, (Object)stacks);
                        }
                    }
                });
            }
        });
        itemRewriter.registerEntityEquipment((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_EQUIPMENT, Type.ITEM);
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (((String)wrapper.get(Type.STRING, 0)).equalsIgnoreCase("MC|TrList")) {
                            wrapper.passthrough(Type.INT);
                            for (int size = (short)wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                wrapper.write(Type.ITEM, (Object)BlockItemPackets1_11.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                                wrapper.write(Type.ITEM, (Object)BlockItemPackets1_11.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                                final boolean secondItem = (boolean)wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    wrapper.write(Type.ITEM, (Object)BlockItemPackets1_11.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
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
        ((Protocol1_10To1_11)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_9_3.CLICK_WINDOW, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.SHORT);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.VAR_INT);
                this.map(Type.ITEM);
                this.handler(itemRewriter.itemToServerHandler(Type.ITEM));
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                            final Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
                            if (!horse.isPresent()) {
                                return;
                            }
                            final ChestedHorseStorage storage = horse.get();
                            final int clickSlot = (short)wrapper.get((Type)Type.SHORT, 0);
                            final int correctSlot = BlockItemPackets1_11.this.getOldSlotId(storage, clickSlot);
                            wrapper.set((Type)Type.SHORT, 0, (Object)correctSlot.shortValue());
                        }
                    }
                });
            }
        });
        itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.CHUNK_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
                        final Chunk1_9_3_4Type type = new Chunk1_9_3_4Type(clientWorld);
                        final Chunk chunk = (Chunk)wrapper.passthrough((Type)type);
                        LegacyBlockItemRewriter.this.handleChunk(chunk);
                        for (final CompoundTag tag : chunk.getBlockEntities()) {
                            final Tag idTag = tag.get("id");
                            if (!(idTag instanceof StringTag)) {
                                continue;
                            }
                            final String id = (String)idTag.getValue();
                            if (!id.equals("minecraft:sign")) {
                                continue;
                            }
                            ((StringTag)idTag).setValue("Sign");
                        }
                    }
                });
            }
        });
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int idx = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        wrapper.set((Type)Type.VAR_INT, 0, (Object)BlockItemPackets1_11.this.handleBlockID(idx));
                    }
                });
            }
        });
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.MULTI_BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (final BlockChangeRecord record : (BlockChangeRecord[])wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                            record.setBlockId(BlockItemPackets1_11.this.handleBlockID(record.getBlockId()));
                        }
                    }
                });
            }
        });
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.BLOCK_ENTITY_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if ((short)wrapper.get(Type.UNSIGNED_BYTE, 0) == 10) {
                            wrapper.cancel();
                        }
                        if ((short)wrapper.get(Type.UNSIGNED_BYTE, 0) == 1) {
                            final CompoundTag tag = (CompoundTag)wrapper.get(Type.NBT, 0);
                            EntityIdRewriter.toClientSpawner(tag, true);
                        }
                    }
                });
            }
        });
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.OPEN_WINDOW, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.COMPONENT);
                this.map(Type.UNSIGNED_BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        int entityId = -1;
                        if (((String)wrapper.get(Type.STRING, 0)).equals("EntityHorse")) {
                            entityId = (int)wrapper.passthrough(Type.INT);
                        }
                        final String inventory = (String)wrapper.get(Type.STRING, 0);
                        final WindowTracker windowTracker = (WindowTracker)wrapper.user().get((Class)WindowTracker.class);
                        windowTracker.setInventory(inventory);
                        windowTracker.setEntityId(entityId);
                        if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                            wrapper.set(Type.UNSIGNED_BYTE, 1, (Object)17);
                        }
                    }
                });
            }
        });
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.CLOSE_WINDOW, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final WindowTracker windowTracker = (WindowTracker)wrapper.user().get((Class)WindowTracker.class);
                        windowTracker.setInventory(null);
                        windowTracker.setEntityId(-1);
                    }
                });
            }
        });
        ((Protocol1_10To1_11)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_9_3.CLOSE_WINDOW, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final WindowTracker windowTracker = (WindowTracker)wrapper.user().get((Class)WindowTracker.class);
                        windowTracker.setInventory(null);
                        windowTracker.setEntityId(-1);
                    }
                });
            }
        });
        final Metadata data;
        ((Protocol1_10To1_11)this.protocol).getEntityPackets().registerMetaHandler().handle(e -> {
            data = e.getData();
            if (data.getMetaType().getType().equals(Type.ITEM)) {
                data.setValue((Object)this.handleItemToClient((Item)data.getValue()));
            }
            return data;
        });
    }
    
    @Override
    protected void registerRewrites() {
        final MappedLegacyBlockItem data = (MappedLegacyBlockItem)this.replacementData.computeIfAbsent(52, s -> new MappedLegacyBlockItem(52, (short)(-1), null, false));
        data.setBlockEntityHandler((b, tag) -> {
            EntityIdRewriter.toClientSpawner(tag, true);
            return tag;
        });
        (this.enchantmentRewriter = new LegacyEnchantmentRewriter(this.nbtTagName)).registerEnchantment(71, "§cCurse of Vanishing");
        this.enchantmentRewriter.registerEnchantment(10, "§cCurse of Binding");
        this.enchantmentRewriter.setHideLevelForEnchants(71, 10);
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
        EntityIdRewriter.toClientItem(item, true);
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
        EntityIdRewriter.toServerItem(item, true);
        if (tag.contains(this.nbtTagName + "|ench")) {
            this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, false);
        }
        if (tag.contains(this.nbtTagName + "|StoredEnchantments")) {
            this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, true);
        }
        return item;
    }
    
    private boolean isLlama(final UserConnection user) {
        final WindowTracker tracker = (WindowTracker)user.get((Class)WindowTracker.class);
        if (tracker.getInventory() != null && tracker.getInventory().equals("EntityHorse")) {
            final EntityTracker.ProtocolEntityTracker entTracker = ((EntityTracker)user.get((Class)EntityTracker.class)).get(this.getProtocol());
            final EntityTracker.StoredEntity storedEntity = entTracker.getEntity(tracker.getEntityId());
            return storedEntity != null && storedEntity.getType().is((EntityType)Entity1_11Types.EntityType.LIAMA);
        }
        return false;
    }
    
    private Optional<ChestedHorseStorage> getChestedHorse(final UserConnection user) {
        final WindowTracker tracker = (WindowTracker)user.get((Class)WindowTracker.class);
        if (tracker.getInventory() != null && tracker.getInventory().equals("EntityHorse")) {
            final EntityTracker.ProtocolEntityTracker entTracker = ((EntityTracker)user.get((Class)EntityTracker.class)).get(this.getProtocol());
            final EntityTracker.StoredEntity storedEntity = entTracker.getEntity(tracker.getEntityId());
            if (storedEntity != null) {
                return Optional.of((ChestedHorseStorage)storedEntity.get((Class<T>)ChestedHorseStorage.class));
            }
        }
        return Optional.empty();
    }
    
    private int getNewSlotId(final ChestedHorseStorage storage, final int slotId) {
        final int totalSlots = storage.isChested() ? 53 : 38;
        final int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
        final int startNonExistingFormula = 2 + 3 * strength;
        final int offsetForm = 15 - 3 * strength;
        if (slotId >= startNonExistingFormula && totalSlots > slotId + offsetForm) {
            return offsetForm + slotId;
        }
        if (slotId == 1) {
            return 0;
        }
        return slotId;
    }
    
    private int getOldSlotId(final ChestedHorseStorage storage, final int slotId) {
        final int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
        final int startNonExistingFormula = 2 + 3 * strength;
        final int endNonExistingFormula = 2 + 3 * (storage.isChested() ? 5 : 0);
        final int offsetForm = endNonExistingFormula - startNonExistingFormula;
        if (slotId == 1 || (slotId >= startNonExistingFormula && slotId < endNonExistingFormula)) {
            return 0;
        }
        if (slotId >= endNonExistingFormula) {
            return slotId - offsetForm;
        }
        if (slotId == 0) {
            return 1;
        }
        return slotId;
    }
    
    private Item getNewItem(final ChestedHorseStorage storage, final int slotId, final Item current) {
        final int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
        final int startNonExistingFormula = 2 + 3 * strength;
        final int endNonExistingFormula = 2 + 3 * (storage.isChested() ? 5 : 0);
        if (slotId >= startNonExistingFormula && slotId < endNonExistingFormula) {
            return new Item(166, (byte)1, (short)0, this.getNamedTag(ChatColor.RED + "SLOT DISABLED"));
        }
        if (slotId == 1) {
            return null;
        }
        return current;
    }
}
