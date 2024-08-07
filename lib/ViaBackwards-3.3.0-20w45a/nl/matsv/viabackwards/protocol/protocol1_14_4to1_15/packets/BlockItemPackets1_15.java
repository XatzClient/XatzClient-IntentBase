// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.data.RecipeRewriter1_14;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.protocol.Protocol;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.Protocol1_14_4To1_15;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_15 extends ItemRewriter<Protocol1_14_4To1_15>
{
    public BlockItemPackets1_15(final Protocol1_14_4To1_15 protocol, final TranslatableRewriter translatableRewriter) {
        super(protocol, translatableRewriter);
    }
    
    @Override
    protected void registerPackets() {
        final us.myles.ViaVersion.api.rewriters.ItemRewriter itemRewriter = new us.myles.ViaVersion.api.rewriters.ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
        final BlockRewriter blockRewriter = new BlockRewriter((Protocol)this.protocol, Type.POSITION1_14);
        new RecipeRewriter1_14((Protocol)this.protocol, this::handleItemToClient).registerDefaultHandler((ClientboundPacketType)ClientboundPackets1_15.DECLARE_RECIPES);
        ((Protocol1_14_4To1_15)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_14.EDIT_BOOK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> BlockItemPackets1_15.this.handleItemToServer((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
            }
        });
        itemRewriter.registerSetCooldown((ClientboundPacketType)ClientboundPackets1_15.COOLDOWN);
        itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_15.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_15.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerTradeList((ClientboundPacketType)ClientboundPackets1_15.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerEntityEquipment((ClientboundPacketType)ClientboundPackets1_15.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerAdvancements((ClientboundPacketType)ClientboundPackets1_15.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        blockRewriter.registerAcknowledgePlayerDigging((ClientboundPacketType)ClientboundPackets1_15.ACKNOWLEDGE_PLAYER_DIGGING);
        blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_15.BLOCK_ACTION);
        blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_15.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange((ClientboundPacketType)ClientboundPackets1_15.MULTI_BLOCK_CHANGE);
        ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.CHUNK_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Chunk chunk = (Chunk)wrapper.read((Type)new Chunk1_15Type());
                        wrapper.write((Type)new Chunk1_14Type(), (Object)chunk);
                        if (chunk.isFullChunk()) {
                            final int[] biomeData = chunk.getBiomeData();
                            final int[] newBiomeData = new int[256];
                            for (int i = 0; i < 4; ++i) {
                                for (int j = 0; j < 4; ++j) {
                                    final int x = j << 2;
                                    final int z = i << 2;
                                    final int newIndex = z << 4 | x;
                                    final int oldIndex = i << 2 | j;
                                    final int biome = biomeData[oldIndex];
                                    for (int k = 0; k < 4; ++k) {
                                        final int offX = newIndex + (k << 4);
                                        for (int l = 0; l < 4; ++l) {
                                            newBiomeData[offX + l] = biome;
                                        }
                                    }
                                }
                            }
                            chunk.setBiomeData(newBiomeData);
                        }
                        for (int m = 0; m < chunk.getSections().length; ++m) {
                            final ChunkSection section = chunk.getSections()[m];
                            if (section != null) {
                                for (int j2 = 0; j2 < section.getPaletteSize(); ++j2) {
                                    final int old = section.getPaletteEntry(j2);
                                    final int newId = ((Protocol1_14_4To1_15)BlockItemPackets1_15.this.protocol).getMappingData().getNewBlockStateId(old);
                                    section.setPaletteEntry(j2, newId);
                                }
                            }
                        }
                    }
                });
            }
        });
        blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_15.EFFECT, 1010, 2001);
        ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.SPAWN_PARTICLE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.DOUBLE, (Type)Type.FLOAT);
                this.map(Type.DOUBLE, (Type)Type.FLOAT);
                this.map(Type.DOUBLE, (Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = (int)wrapper.get(Type.INT, 0);
                        if (id == 3 || id == 23) {
                            final int data = (int)wrapper.passthrough((Type)Type.VAR_INT);
                            wrapper.set((Type)Type.VAR_INT, 0, (Object)((Protocol1_14_4To1_15)BlockItemPackets1_15.this.protocol).getMappingData().getNewBlockStateId(data));
                        }
                        else if (id == 32) {
                            final Item item = BlockItemPackets1_15.this.handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM));
                            wrapper.write(Type.FLAT_VAR_INT_ITEM, (Object)item);
                        }
                        final int mappedId = ((Protocol1_14_4To1_15)BlockItemPackets1_15.this.protocol).getMappingData().getNewParticleId(id);
                        if (id != mappedId) {
                            wrapper.set(Type.INT, 0, (Object)mappedId);
                        }
                    }
                });
            }
        });
    }
}
