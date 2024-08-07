// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_14to1_14_1.packets;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import java.util.List;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.entities.Entity1_14Types;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import nl.matsv.viabackwards.protocol.protocol1_14to1_14_1.Protocol1_14To1_14_1;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_14_1 extends LegacyEntityRewriter<Protocol1_14To1_14_1>
{
    public EntityPackets1_14_1(final Protocol1_14To1_14_1 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_14.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_14Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_14.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_14Types.EntityType.LIGHTNING_BOLT);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PAINTING, (EntityType)Entity1_14Types.EntityType.PAINTING);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PLAYER, (EntityType)Entity1_14Types.EntityType.PLAYER);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_14.JOIN_GAME, (EntityType)Entity1_14Types.EntityType.PLAYER, Type.INT);
        this.registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_14.DESTROY_ENTITIES);
        ((Protocol1_14To1_14_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_ENTITY, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT);
                this.handler(EntityRewriterBase.this.getTrackerHandler());
            }
        });
        ((Protocol1_14To1_14_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_MOB, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map(Types1_14.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        final int type = (int)wrapper.get((Type)Type.VAR_INT, 1);
                        EntityRewriterBase.this.addTrackedEntity(wrapper, entityId, (EntityType)Entity1_14Types.getTypeFromId(type));
                        final MetaStorage storage = new MetaStorage((List<Metadata>)wrapper.get(Types1_14.METADATA_LIST, 0));
                        EntityRewriterBase.this.handleMeta(wrapper.user(), entityId, storage);
                    }
                });
            }
        });
        this.registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_14.ENTITY_METADATA, (Type<List<Metadata>>)Types1_14.METADATA_LIST);
    }
    
    @Override
    protected void registerRewrites() {
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.VILLAGER, 15).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.VILLAGER, 16).handleIndexChange(15);
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.WANDERING_TRADER, 15).removed();
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_14Types.getTypeFromId(typeId);
    }
}
