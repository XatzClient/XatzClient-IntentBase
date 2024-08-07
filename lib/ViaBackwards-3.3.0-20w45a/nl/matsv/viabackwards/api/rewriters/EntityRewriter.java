// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.rewriters;

import nl.matsv.viabackwards.api.entities.storage.EntityData;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public abstract class EntityRewriter<T extends BackwardsProtocol> extends EntityRewriterBase<T>
{
    protected EntityRewriter(final T protocol) {
        this(protocol, (MetaType)MetaType1_14.OptChat, (MetaType)MetaType1_14.Boolean);
    }
    
    protected EntityRewriter(final T protocol, final MetaType displayType, final MetaType displayVisibilityType) {
        super(protocol, displayType, 2, displayVisibilityType, 3);
    }
    
    public void registerSpawnTrackerWithData(final ClientboundPacketType packetType, final EntityType fallingBlockType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final EntityType entityType = EntityRewriter.this.setOldEntityId(wrapper);
                    if (entityType == fallingBlockType) {
                        final int blockState = (int)wrapper.get(Type.INT, 0);
                        wrapper.set(Type.INT, 0, (Object)EntityRewriter.this.protocol.getMappingData().getNewBlockStateId(blockState));
                    }
                });
            }
        });
    }
    
    public void registerSpawnTracker(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT);
                this.handler(wrapper -> EntityRewriter.this.setOldEntityId(wrapper));
            }
        });
    }
    
    private EntityType setOldEntityId(final PacketWrapper wrapper) throws Exception {
        final int typeId = (int)wrapper.get((Type)Type.VAR_INT, 1);
        final EntityType entityType = this.getTypeFromId(typeId);
        this.addTrackedEntity(wrapper, (int)wrapper.get((Type)Type.VAR_INT, 0), entityType);
        final int oldTypeId = this.getOldEntityId(entityType.getId());
        if (typeId != oldTypeId) {
            wrapper.set((Type)Type.VAR_INT, 1, (Object)oldTypeId);
        }
        return entityType;
    }
    
    protected void registerMetadataRewriter(final ClientboundPacketType packetType, final Type<List<Metadata>> oldMetaType, final Type<List<Metadata>> newMetaType) {
        this.getProtocol().registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                if (oldMetaType != null) {
                    this.map(oldMetaType, newMetaType);
                }
                else {
                    this.map(newMetaType);
                }
                this.handler(wrapper -> {
                    final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
                    final EntityType type = EntityRewriter.this.getEntityType(wrapper.user(), entityId);
                    final MetaStorage storage = new MetaStorage((List<Metadata>)wrapper.get(newMetaType, 0));
                    EntityRewriter.this.handleMeta(wrapper.user(), entityId, storage);
                    final EntityData entityData = EntityRewriter.this.getEntityData(type);
                    if (entityData != null && entityData.hasBaseMeta()) {
                        entityData.getDefaultMeta().createMeta(storage);
                    }
                    wrapper.set(newMetaType, 0, (Object)storage.getMetaDataList());
                });
            }
        });
    }
    
    protected void registerMetadataRewriter(final ClientboundPacketType packetType, final Type<List<Metadata>> metaType) {
        this.registerMetadataRewriter(packetType, null, metaType);
    }
}
