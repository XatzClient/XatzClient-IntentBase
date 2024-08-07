// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_15to1_14_4.packets;

import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.storage.EntityTracker1_15;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.entities.Entity1_15Types;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.metadata.MetadataRewriter1_15To1_14_4;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;

public class EntityPackets
{
    public static void register(final Protocol1_15To1_14_4 protocol) {
        final MetadataRewriter1_15To1_14_4 metadataRewriter = protocol.get(MetadataRewriter1_15To1_14_4.class);
        metadataRewriter.registerSpawnTrackerWithData(ClientboundPackets1_14.SPAWN_ENTITY, Entity1_15Types.EntityType.FALLING_BLOCK);
        ((Protocol<ClientboundPackets1_14, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_14.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(metadataRewriter.getTracker());
                final MetadataRewriter val$metadataRewriter;
                final int entityId;
                final List<Metadata> metadata;
                final PacketWrapper metadataUpdate;
                this.handler(wrapper -> {
                    val$metadataRewriter = metadataRewriter;
                    entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    metadata = wrapper.read(Types1_14.METADATA_LIST);
                    val$metadataRewriter.handleMetadata(entityId, metadata, wrapper.user());
                    metadataUpdate = wrapper.create(68);
                    metadataUpdate.write(Type.VAR_INT, entityId);
                    metadataUpdate.write(Types1_14.METADATA_LIST, metadata);
                    metadataUpdate.send(Protocol1_15To1_14_4.class);
                });
            }
        });
        ((Protocol<ClientboundPackets1_14, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_14.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                final MetadataRewriter val$metadataRewriter;
                final int entityId;
                final Entity1_15Types.EntityType entityType;
                final List<Metadata> metadata;
                final PacketWrapper metadataUpdate;
                this.handler(wrapper -> {
                    val$metadataRewriter = metadataRewriter;
                    entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    entityType = Entity1_15Types.EntityType.PLAYER;
                    wrapper.user().get(EntityTracker1_15.class).addEntity(entityId, entityType);
                    metadata = wrapper.read(Types1_14.METADATA_LIST);
                    val$metadataRewriter.handleMetadata(entityId, metadata, wrapper.user());
                    metadataUpdate = wrapper.create(68);
                    metadataUpdate.write(Type.VAR_INT, entityId);
                    metadataUpdate.write(Types1_14.METADATA_LIST, metadata);
                    metadataUpdate.send(Protocol1_15To1_14_4.class);
                });
            }
        });
        metadataRewriter.registerMetadataRewriter(ClientboundPackets1_14.ENTITY_METADATA, Types1_14.METADATA_LIST);
        metadataRewriter.registerEntityDestroy(ClientboundPackets1_14.DESTROY_ENTITIES);
    }
    
    public static int getNewEntityId(final int oldId) {
        return (oldId >= 4) ? (oldId + 1) : oldId;
    }
}
