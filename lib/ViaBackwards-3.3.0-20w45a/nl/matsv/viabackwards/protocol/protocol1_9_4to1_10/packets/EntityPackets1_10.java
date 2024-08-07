// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_9_4to1_10.packets;

import us.myles.ViaVersion.api.data.UserConnection;
import java.util.function.Function;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import java.util.List;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.entities.ObjectType;
import nl.matsv.viabackwards.utils.Block;
import java.util.Optional;
import us.myles.ViaVersion.api.entities.Entity1_12Types;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.entities.Entity1_11Types;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import nl.matsv.viabackwards.protocol.protocol1_9_4to1_10.Protocol1_9_4To1_10;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_10 extends LegacyEntityRewriter<Protocol1_9_4To1_10>
{
    public EntityPackets1_10(final Protocol1_9_4To1_10 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_ENTITY, (PacketRemapper)new PacketRemapper() {
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
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Optional<Entity1_12Types.ObjectType> type = (Optional<Entity1_12Types.ObjectType>)Entity1_12Types.ObjectType.findById((int)(byte)wrapper.get(Type.BYTE, 0));
                        if (type.isPresent() && type.get() == Entity1_12Types.ObjectType.FALLING_BLOCK) {
                            final int objectData = (int)wrapper.get(Type.INT, 0);
                            final int objType = objectData & 0xFFF;
                            final int data = objectData >> 12 & 0xF;
                            final Block block = EntityPackets1_10.this.getProtocol().getBlockItemPackets().handleBlock(objType, data);
                            if (block == null) {
                                return;
                            }
                            wrapper.set(Type.INT, 0, (Object)(block.getId() | block.getData() << 12));
                        }
                    }
                });
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_10Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_10Types.EntityType.WEATHER);
        ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_MOB, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.UNSIGNED_BYTE);
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
                this.handler(EntityRewriterBase.this.getTrackerHandler(Type.UNSIGNED_BYTE, 0));
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        final EntityType type = EntityRewriterBase.this.getEntityType(wrapper.user(), entityId);
                        final MetaStorage storage = new MetaStorage((List<Metadata>)wrapper.get(Types1_9.METADATA_LIST, 0));
                        EntityRewriterBase.this.handleMeta(wrapper.user(), (int)wrapper.get((Type)Type.VAR_INT, 0), storage);
                        final EntityData entityData = EntityRewriterBase.this.getEntityData(type);
                        if (entityData != null) {
                            wrapper.set(Type.UNSIGNED_BYTE, 0, (Object)(short)entityData.getReplacementId());
                            if (entityData.hasBaseMeta()) {
                                entityData.getDefaultMeta().createMeta(storage);
                            }
                        }
                        wrapper.set(Types1_9.METADATA_LIST, 0, (Object)storage.getMetaDataList());
                    }
                });
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PAINTING, (EntityType)Entity1_10Types.EntityType.PAINTING);
        this.registerJoinGame((ClientboundPacketType)ClientboundPackets1_9_3.JOIN_GAME, (EntityType)Entity1_10Types.EntityType.PLAYER);
        this.registerRespawn((ClientboundPacketType)ClientboundPackets1_9_3.RESPAWN);
        ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PLAYER, (PacketRemapper)new PacketRemapper() {
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
        this.mapEntity((EntityType)Entity1_10Types.EntityType.POLAR_BEAR, (EntityType)Entity1_10Types.EntityType.SHEEP).mobName("Polar Bear");
        final Metadata data;
        final boolean b;
        this.registerMetaHandler().filter((EntityType)Entity1_10Types.EntityType.POLAR_BEAR, 13).handle(e -> {
            data = e.getData();
            b = (boolean)data.getValue();
            data.setMetaType((MetaType)MetaType1_9.Byte);
            data.setValue((Object)(byte)(b ? 14 : 0));
            return data;
        });
        final Metadata data2;
        this.registerMetaHandler().filter((EntityType)Entity1_10Types.EntityType.ZOMBIE, 13).handle(e -> {
            data2 = e.getData();
            if ((int)data2.getValue() == 6) {
                data2.setValue((Object)0);
            }
            return data2;
        });
        final Metadata data3;
        this.registerMetaHandler().filter((EntityType)Entity1_10Types.EntityType.SKELETON, 12).handle(e -> {
            data3 = e.getData();
            if ((int)data3.getValue() == 2) {
                data3.setValue((Object)0);
            }
            return data3;
        });
        final Metadata data4;
        this.registerMetaHandler().handle(e -> {
            data4 = e.getData();
            if (data4.getId() == 5) {
                throw RemovedValueException.EX;
            }
            else {
                if (data4.getId() >= 5) {
                    data4.setId(data4.getId() - 1);
                }
                return data4;
            }
        });
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_10Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return (EntityType)Entity1_10Types.getTypeFromId(typeId, true);
    }
}
