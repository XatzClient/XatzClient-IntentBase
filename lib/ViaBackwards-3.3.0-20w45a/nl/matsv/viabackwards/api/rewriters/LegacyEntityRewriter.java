// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.rewriters;

import nl.matsv.viabackwards.ViaBackwards;
import java.util.function.Function;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import org.jetbrains.annotations.Nullable;
import nl.matsv.viabackwards.api.entities.storage.EntityObjectData;
import java.util.HashMap;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import us.myles.ViaVersion.api.entities.ObjectType;
import java.util.Map;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public abstract class LegacyEntityRewriter<T extends BackwardsProtocol> extends EntityRewriterBase<T>
{
    private final Map<ObjectType, EntityData> objectTypes;
    
    protected LegacyEntityRewriter(final T protocol) {
        this(protocol, (MetaType)MetaType1_9.String, (MetaType)MetaType1_9.Boolean);
    }
    
    protected LegacyEntityRewriter(final T protocol, final MetaType displayType, final MetaType displayVisibilityType) {
        super(protocol, displayType, 2, displayVisibilityType, 3);
        this.objectTypes = new HashMap<ObjectType, EntityData>();
    }
    
    protected EntityObjectData mapObjectType(final ObjectType oldObjectType, final ObjectType replacement, final int data) {
        final EntityObjectData entData = new EntityObjectData(oldObjectType.getId(), true, replacement.getId(), data);
        this.objectTypes.put(oldObjectType, entData);
        return entData;
    }
    
    @Nullable
    protected EntityData getObjectData(final ObjectType type) {
        return this.objectTypes.get(type);
    }
    
    protected void registerRespawn(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final ClientWorld clientWorld = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
                    clientWorld.setEnvironment((int)wrapper.get(Type.INT, 0));
                });
            }
        });
    }
    
    protected void registerJoinGame(final ClientboundPacketType packetType, final EntityType playerType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final ClientWorld clientChunks = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
                    clientChunks.setEnvironment((int)wrapper.get(Type.INT, 1));
                    LegacyEntityRewriter.this.getEntityTracker(wrapper.user()).trackEntityType((int)wrapper.get(Type.INT, 0), playerType);
                });
            }
        });
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
                    final List<Metadata> metadata = (List<Metadata>)wrapper.get(newMetaType, 0);
                    wrapper.set(newMetaType, 0, (Object)LegacyEntityRewriter.this.handleMeta(wrapper.user(), (int)wrapper.get((Type)Type.VAR_INT, 0), new MetaStorage(metadata)).getMetaDataList());
                });
            }
        });
    }
    
    protected void registerMetadataRewriter(final ClientboundPacketType packetType, final Type<List<Metadata>> metaType) {
        this.registerMetadataRewriter(packetType, null, metaType);
    }
    
    protected PacketHandler getMobSpawnRewriter(final Type<List<Metadata>> metaType) {
        return wrapper -> {
            final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
            final EntityType type = this.getEntityType(wrapper.user(), entityId);
            final MetaStorage storage = new MetaStorage((List<Metadata>)wrapper.get(metaType, 0));
            this.handleMeta(wrapper.user(), entityId, storage);
            final EntityData entityData = this.getEntityData(type);
            if (entityData != null) {
                wrapper.set((Type)Type.VAR_INT, 1, (Object)entityData.getReplacementId());
                if (entityData.hasBaseMeta()) {
                    entityData.getDefaultMeta().createMeta(storage);
                }
            }
            wrapper.set(metaType, 0, (Object)storage.getMetaDataList());
        };
    }
    
    protected PacketHandler getObjectTrackerHandler() {
        return wrapper -> this.addTrackedEntity(wrapper, (int)wrapper.get((Type)Type.VAR_INT, 0), this.getObjectTypeFromId((byte)wrapper.get(Type.BYTE, 0)));
    }
    
    protected PacketHandler getTrackerAndMetaHandler(final Type<List<Metadata>> metaType, final EntityType entityType) {
        return wrapper -> {
            this.addTrackedEntity(wrapper, (int)wrapper.get((Type)Type.VAR_INT, 0), entityType);
            final List<Metadata> metaDataList = this.handleMeta(wrapper.user(), (int)wrapper.get((Type)Type.VAR_INT, 0), new MetaStorage((List<Metadata>)wrapper.get(metaType, 0))).getMetaDataList();
            wrapper.set(metaType, 0, (Object)metaDataList);
        };
    }
    
    protected PacketHandler getObjectRewriter(final Function<Byte, ObjectType> objectGetter) {
        return wrapper -> {
            final ObjectType type = objectGetter.apply(wrapper.get(Type.BYTE, 0));
            if (type == null) {
                ViaBackwards.getPlatform().getLogger().warning("Could not find Entity Type" + wrapper.get(Type.BYTE, 0));
                return;
            }
            final EntityData data = this.getObjectData(type);
            if (data != null) {
                wrapper.set(Type.BYTE, 0, (Object)(byte)data.getReplacementId());
                if (data.getObjectData() != -1) {
                    wrapper.set(Type.INT, 0, (Object)data.getObjectData());
                }
            }
        };
    }
    
    protected EntityType getObjectTypeFromId(final int typeId) {
        return this.getTypeFromId(typeId);
    }
}
