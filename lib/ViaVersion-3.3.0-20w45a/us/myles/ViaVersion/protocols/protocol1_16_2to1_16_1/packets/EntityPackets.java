// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.packets;

import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.Via;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.storage.EntityTracker1_16_2;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.entities.Entity1_16_2Types;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.metadata.MetadataRewriter1_16_2To1_16_1;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;

public class EntityPackets
{
    public static void register(final Protocol1_16_2To1_16_1 protocol) {
        final MetadataRewriter1_16_2To1_16_1 metadataRewriter = protocol.get(MetadataRewriter1_16_2To1_16_1.class);
        metadataRewriter.registerSpawnTrackerWithData(ClientboundPackets1_16.SPAWN_ENTITY, Entity1_16_2Types.EntityType.FALLING_BLOCK);
        metadataRewriter.registerTracker(ClientboundPackets1_16.SPAWN_MOB);
        metadataRewriter.registerTracker(ClientboundPackets1_16.SPAWN_PLAYER, Entity1_16_2Types.EntityType.PLAYER);
        metadataRewriter.registerMetadataRewriter(ClientboundPackets1_16.ENTITY_METADATA, Types1_14.METADATA_LIST);
        metadataRewriter.registerEntityDestroy(ClientboundPackets1_16.DESTROY_ENTITIES);
        ((Protocol<ClientboundPackets1_16, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_16.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                final short gamemode;
                final short gamemode2;
                this.handler(wrapper -> {
                    gamemode = wrapper.read(Type.UNSIGNED_BYTE);
                    wrapper.write(Type.BOOLEAN, (gamemode & 0x8) != 0x0);
                    gamemode2 = (short)(gamemode & 0xFFFFFFF7);
                    wrapper.write(Type.UNSIGNED_BYTE, gamemode2);
                    return;
                });
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                final Protocol1_16_2To1_16_1 val$protocol;
                final String dimensionType;
                this.handler(wrapper -> {
                    val$protocol = protocol;
                    wrapper.read(Type.NBT);
                    wrapper.write(Type.NBT, val$protocol.getMappingData().getDimensionRegistry());
                    dimensionType = wrapper.read(Type.STRING);
                    wrapper.write(Type.NBT, EntityPackets.getDimensionData(dimensionType));
                    return;
                });
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE, Type.VAR_INT);
                this.handler(wrapper -> wrapper.user().get(EntityTracker1_16_2.class).addEntity(wrapper.get(Type.INT, 0), Entity1_16_2Types.EntityType.PLAYER));
            }
        });
        ((Protocol<ClientboundPackets1_16, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_16.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                final String dimensionType;
                this.handler(wrapper -> {
                    dimensionType = wrapper.read(Type.STRING);
                    wrapper.write(Type.NBT, EntityPackets.getDimensionData(dimensionType));
                });
            }
        });
    }
    
    public static CompoundTag getDimensionData(final String dimensionType) {
        final CompoundTag tag = Protocol1_16_2To1_16_1.MAPPINGS.getDimensionDataMap().get(dimensionType);
        if (tag == null) {
            Via.getPlatform().getLogger().severe("Could not get dimension data of " + dimensionType);
            throw new NullPointerException("Dimension data for " + dimensionType + " is null!");
        }
        return tag;
    }
}
