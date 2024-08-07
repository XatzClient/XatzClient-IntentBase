// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.storage.EntityTracker1_16_2;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.rewriters.RegistryType;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.SoundRewriter;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.packets.InventoryPackets;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.packets.WorldPackets;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.packets.EntityPackets;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.metadata.MetadataRewriter1_16_2To1_16_1;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.data.MappingData;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_16_2To1_16_1 extends Protocol<ClientboundPackets1_16, ClientboundPackets1_16_2, ServerboundPackets1_16, ServerboundPackets1_16_2>
{
    public static final MappingData MAPPINGS;
    private TagRewriter tagRewriter;
    
    public Protocol1_16_2To1_16_1() {
        super(ClientboundPackets1_16.class, ClientboundPackets1_16_2.class, ServerboundPackets1_16.class, ServerboundPackets1_16_2.class);
    }
    
    @Override
    protected void registerPackets() {
        final MetadataRewriter metadataRewriter = new MetadataRewriter1_16_2To1_16_1(this);
        EntityPackets.register(this);
        WorldPackets.register(this);
        InventoryPackets.register(this);
        (this.tagRewriter = new TagRewriter(this, metadataRewriter::getNewEntityId)).register(ClientboundPackets1_16.TAGS);
        new StatisticsRewriter(this, metadataRewriter::getNewEntityId).register(ClientboundPackets1_16.STATISTICS);
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound(ClientboundPackets1_16.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_16.ENTITY_SOUND);
        ((Protocol<C1, C2, S1, ServerboundPackets1_16_2>)this).registerIncoming(ServerboundPackets1_16_2.RECIPE_BOOK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int recipeType;
                final boolean open;
                final boolean filter;
                this.handler(wrapper -> {
                    recipeType = wrapper.read((Type<Integer>)Type.VAR_INT);
                    open = wrapper.read(Type.BOOLEAN);
                    filter = wrapper.read(Type.BOOLEAN);
                    wrapper.write(Type.VAR_INT, 1);
                    wrapper.write(Type.BOOLEAN, recipeType == 0);
                    wrapper.write(Type.BOOLEAN, filter);
                    wrapper.write(Type.BOOLEAN, recipeType == 1);
                    wrapper.write(Type.BOOLEAN, filter);
                    wrapper.write(Type.BOOLEAN, recipeType == 2);
                    wrapper.write(Type.BOOLEAN, filter);
                    wrapper.write(Type.BOOLEAN, recipeType == 3);
                    wrapper.write(Type.BOOLEAN, filter);
                });
            }
        });
        ((Protocol<C1, C2, ServerboundPackets1_16, ServerboundPackets1_16_2>)this).registerIncoming(ServerboundPackets1_16_2.SEEN_RECIPE, ServerboundPackets1_16.RECIPE_BOOK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                final String recipe;
                this.handler(wrapper -> {
                    recipe = wrapper.read(Type.STRING);
                    wrapper.write(Type.VAR_INT, 0);
                    wrapper.write(Type.STRING, recipe);
                });
            }
        });
    }
    
    @Override
    protected void onMappingDataLoaded() {
        this.tagRewriter.addTag(RegistryType.ITEM, "minecraft:stone_crafting_materials", 14, 962);
        this.tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:mushroom_grow_block");
        this.tagRewriter.addEmptyTags(RegistryType.ITEM, "minecraft:soul_fire_base_blocks", "minecraft:furnace_materials", "minecraft:crimson_stems", "minecraft:gold_ores", "minecraft:piglin_loved", "minecraft:piglin_repellents", "minecraft:creeper_drop_music_discs", "minecraft:logs_that_burn", "minecraft:stone_tool_materials", "minecraft:warped_stems");
        this.tagRewriter.addEmptyTags(RegistryType.BLOCK, "minecraft:infiniburn_nether", "minecraft:crimson_stems", "minecraft:wither_summon_base_blocks", "minecraft:infiniburn_overworld", "minecraft:piglin_repellents", "minecraft:hoglin_repellents", "minecraft:prevent_mob_spawning_inside", "minecraft:wart_blocks", "minecraft:stone_pressure_plates", "minecraft:nylium", "minecraft:gold_ores", "minecraft:pressure_plates", "minecraft:logs_that_burn", "minecraft:strider_warm_blocks", "minecraft:warped_stems", "minecraft:infiniburn_end", "minecraft:base_stone_nether", "minecraft:base_stone_overworld");
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.put(new EntityTracker1_16_2(userConnection));
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_16_2To1_16_1.MAPPINGS;
    }
    
    static {
        MAPPINGS = new MappingData();
    }
}
