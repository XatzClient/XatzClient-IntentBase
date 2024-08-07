// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_11to1_11_1.packets;

import java.util.function.Function;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.entities.ObjectType;
import us.myles.ViaVersion.api.entities.Entity1_11Types;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import nl.matsv.viabackwards.protocol.protocol1_11to1_11_1.Protocol1_11To1_11_1;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_11_1 extends LegacyEntityRewriter<Protocol1_11To1_11_1>
{
    public EntityPackets1_11_1(final Protocol1_11To1_11_1 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_11To1_11_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_ENTITY, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler(LegacyEntityRewriter.this.getObjectTrackerHandler());
                this.handler(LegacyEntityRewriter.this.getObjectRewriter(id -> Entity1_11Types.ObjectType.findById((int)id).orElse(null)));
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_11Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_11Types.EntityType.WEATHER);
        ((Protocol1_11To1_11_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_MOB, (PacketRemapper)new PacketRemapper() {
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
                this.map(Types1_9.METADATA_LIST);
                this.handler(EntityRewriterBase.this.getTrackerHandler());
                this.handler(LegacyEntityRewriter.this.getMobSpawnRewriter((Type<List<Metadata>>)Types1_9.METADATA_LIST));
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PAINTING, (EntityType)Entity1_11Types.EntityType.PAINTING);
        this.registerJoinGame((ClientboundPacketType)ClientboundPackets1_9_3.JOIN_GAME, (EntityType)Entity1_11Types.EntityType.PLAYER);
        this.registerRespawn((ClientboundPacketType)ClientboundPackets1_9_3.RESPAWN);
        ((Protocol1_11To1_11_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PLAYER, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_9.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler((Type<List<Metadata>>)Types1_9.METADATA_LIST, (EntityType)Entity1_11Types.EntityType.PLAYER));
            }
        });
        this.registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_9_3.DESTROY_ENTITIES);
        this.registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_METADATA, (Type<List<Metadata>>)Types1_9.METADATA_LIST);
    }
    
    @Override
    protected void registerRewrites() {
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.FIREWORK, 7).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.PIG, 14).removed();
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_11Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return (EntityType)Entity1_11Types.getTypeFromId(typeId, true);
    }
}
