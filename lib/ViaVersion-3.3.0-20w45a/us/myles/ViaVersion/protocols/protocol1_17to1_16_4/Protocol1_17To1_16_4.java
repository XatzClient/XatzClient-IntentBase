// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_17to1_16_4;

import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.storage.BiomeStorage;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.rewriters.RegistryType;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.SoundRewriter;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.rewriters.IdRewriteFunction;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.packets.WorldPackets;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.packets.InventoryPackets;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_17To1_16_4 extends Protocol<ClientboundPackets1_16_2, ClientboundPackets1_16_2, ServerboundPackets1_16_2, ServerboundPackets1_16_2>
{
    public static final MappingData MAPPINGS;
    private TagRewriter tagRewriter;
    
    public Protocol1_17To1_16_4() {
        super(ClientboundPackets1_16_2.class, ClientboundPackets1_16_2.class, ServerboundPackets1_16_2.class, ServerboundPackets1_16_2.class);
    }
    
    @Override
    protected void registerPackets() {
        InventoryPackets.register(this);
        WorldPackets.register(this);
        (this.tagRewriter = new TagRewriter(this, null)).register(ClientboundPackets1_16_2.TAGS);
        new StatisticsRewriter(this, null).register(ClientboundPackets1_16_2.STATISTICS);
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound(ClientboundPackets1_16_2.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_16_2.ENTITY_SOUND);
        ((Protocol<ClientboundPackets1_16_2, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_16_2.RESOURCE_PACK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough(Type.STRING);
                    wrapper.passthrough(Type.STRING);
                    wrapper.write(Type.BOOLEAN, false);
                });
            }
        });
    }
    
    @Override
    protected void onMappingDataLoaded() {
        this.tagRewriter.addEmptyTags(RegistryType.ITEM, "minecraft:candles", "minecraft:ignored_by_piglin_babies", "minecraft:piglin_food");
        this.tagRewriter.addEmptyTags(RegistryType.BLOCK, "minecraft:crystal_sound_blocks", "minecraft:candle_cakes", "minecraft:candles");
        this.tagRewriter.addTag(RegistryType.BLOCK, "minecraft:cauldrons", 261);
    }
    
    @Override
    public void init(final UserConnection user) {
        user.put(new BiomeStorage(user));
    }
    
    @Nullable
    @Override
    public MappingData getMappingData() {
        return Protocol1_17To1_16_4.MAPPINGS;
    }
    
    static {
        MAPPINGS = new MappingData("1.16.2", "1.17", true);
    }
}
