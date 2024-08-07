// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord1_8;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import java.util.Iterator;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.types.Chunk1_16_2Type;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.data.RecipeRewriter1_16;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.protocol.Protocol;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.Protocol1_16_1To1_16_2;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_16_2 extends ItemRewriter<Protocol1_16_1To1_16_2>
{
    public BlockItemPackets1_16_2(final Protocol1_16_1To1_16_2 protocol, final TranslatableRewriter translatableRewriter) {
        super(protocol, translatableRewriter);
    }
    
    @Override
    protected void registerPackets() {
        final us.myles.ViaVersion.api.rewriters.ItemRewriter itemRewriter = new us.myles.ViaVersion.api.rewriters.ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
        final BlockRewriter blockRewriter = new BlockRewriter((Protocol)this.protocol, Type.POSITION1_14);
        new RecipeRewriter1_16((Protocol)this.protocol, this::handleItemToClient).registerDefaultHandler((ClientboundPacketType)ClientboundPackets1_16_2.DECLARE_RECIPES);
        itemRewriter.registerSetCooldown((ClientboundPacketType)ClientboundPackets1_16_2.COOLDOWN);
        itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_16_2.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_16_2.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerEntityEquipmentArray((ClientboundPacketType)ClientboundPackets1_16_2.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerTradeList((ClientboundPacketType)ClientboundPackets1_16_2.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerAdvancements((ClientboundPacketType)ClientboundPackets1_16_2.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.UNLOCK_RECIPES, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough((Type)Type.VAR_INT);
                    wrapper.passthrough(Type.BOOLEAN);
                    wrapper.passthrough(Type.BOOLEAN);
                    wrapper.passthrough(Type.BOOLEAN);
                    wrapper.passthrough(Type.BOOLEAN);
                    wrapper.read(Type.BOOLEAN);
                    wrapper.read(Type.BOOLEAN);
                    wrapper.read(Type.BOOLEAN);
                    wrapper.read(Type.BOOLEAN);
                });
            }
        });
        blockRewriter.registerAcknowledgePlayerDigging((ClientboundPacketType)ClientboundPackets1_16_2.ACKNOWLEDGE_PLAYER_DIGGING);
        blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_16_2.BLOCK_ACTION);
        blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_16_2.BLOCK_CHANGE);
        ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.CHUNK_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final Chunk chunk = (Chunk)wrapper.read((Type)new Chunk1_16_2Type());
                    wrapper.write((Type)new Chunk1_16Type(), (Object)chunk);
                    chunk.setIgnoreOldLightData(true);
                    for (int i = 0; i < chunk.getSections().length; ++i) {
                        final ChunkSection section = chunk.getSections()[i];
                        if (section != null) {
                            for (int j = 0; j < section.getPaletteSize(); ++j) {
                                final int old = section.getPaletteEntry(j);
                                section.setPaletteEntry(j, ((Protocol1_16_1To1_16_2)BlockItemPackets1_16_2.this.protocol).getMappingData().getNewBlockStateId(old));
                            }
                        }
                    }
                    for (final CompoundTag blockEntity : chunk.getBlockEntities()) {
                        if (blockEntity == null) {
                            continue;
                        }
                        final IntTag x = (IntTag)blockEntity.get("x");
                        final IntTag y = (IntTag)blockEntity.get("y");
                        final IntTag z = (IntTag)blockEntity.get("z");
                        if (x == null || y == null || z == null) {
                            continue;
                        }
                        BlockItemPackets1_16_2.this.handleBlockEntity(blockEntity, new Position((int)x.getValue(), y.getValue().shortValue(), (int)z.getValue()));
                    }
                });
            }
        });
        ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.BLOCK_ENTITY_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final Position position = (Position)wrapper.passthrough(Type.POSITION1_14);
                    wrapper.passthrough(Type.UNSIGNED_BYTE);
                    BlockItemPackets1_16_2.this.handleBlockEntity((CompoundTag)wrapper.passthrough(Type.NBT), position);
                });
            }
        });
        ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.MULTI_BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final long chunkPosition = (long)wrapper.read(Type.LONG);
                    wrapper.read(Type.BOOLEAN);
                    final int chunkX = (int)(chunkPosition >> 42);
                    final int chunkY = (int)(chunkPosition << 44 >> 44);
                    final int chunkZ = (int)(chunkPosition << 22 >> 42);
                    wrapper.write(Type.INT, (Object)chunkX);
                    wrapper.write(Type.INT, (Object)chunkZ);
                    final BlockChangeRecord[] blockChangeRecord = (BlockChangeRecord[])wrapper.read(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY);
                    wrapper.write(Type.BLOCK_CHANGE_RECORD_ARRAY, (Object)blockChangeRecord);
                    for (int i = 0; i < blockChangeRecord.length; ++i) {
                        final BlockChangeRecord record = blockChangeRecord[i];
                        final int blockId = ((Protocol1_16_1To1_16_2)BlockItemPackets1_16_2.this.protocol).getMappingData().getNewBlockStateId(record.getBlockId());
                        blockChangeRecord[i] = (BlockChangeRecord)new BlockChangeRecord1_8(record.getSectionX(), record.getY(chunkY), record.getSectionZ(), blockId);
                    }
                });
            }
        });
        blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_16_2.EFFECT, 1010, 2001);
        itemRewriter.registerSpawnParticle((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
        itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_16.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_16.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((Protocol1_16_1To1_16_2)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_16.EDIT_BOOK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> BlockItemPackets1_16_2.this.handleItemToServer((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
            }
        });
    }
    
    private void handleBlockEntity(final CompoundTag tag, final Position position) {
        final StringTag idTag = (StringTag)tag.get("id");
        if (idTag == null) {
            return;
        }
        if (idTag.getValue().equals("minecraft:skull")) {
            final Tag skullOwnerTag = tag.get("SkullOwner");
            if (!(skullOwnerTag instanceof CompoundTag)) {
                return;
            }
            final CompoundTag skullOwnerCompoundTag = (CompoundTag)skullOwnerTag;
            if (!skullOwnerCompoundTag.contains("Id")) {
                return;
            }
            final CompoundTag properties = (CompoundTag)skullOwnerCompoundTag.get("Properties");
            if (properties == null) {
                return;
            }
            final ListTag textures = (ListTag)properties.get("textures");
            if (textures == null) {
                return;
            }
            final CompoundTag first = (textures.size() > 0) ? ((CompoundTag)textures.get(0)) : null;
            if (first == null) {
                return;
            }
            final int hashCode = first.get("Value").getValue().hashCode();
            final int[] uuidIntArray = { hashCode, 0, 0, 0 };
            skullOwnerCompoundTag.put((Tag)new IntArrayTag("Id", uuidIntArray));
        }
    }
}
