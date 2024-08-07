// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.rewriters;

import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.data.ParticleMappings;
import us.myles.ViaVersion.api.type.types.Particle;
import java.util.logging.Logger;
import java.util.Iterator;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.Via;
import java.util.Collection;
import java.util.ArrayList;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.storage.EntityTracker;

public abstract class MetadataRewriter
{
    private final Class<? extends EntityTracker> entityTrackerClass;
    protected final Protocol protocol;
    private Int2IntMap typeMapping;
    
    protected MetadataRewriter(final Protocol protocol, final Class<? extends EntityTracker> entityTrackerClass) {
        this.protocol = protocol;
        this.entityTrackerClass = entityTrackerClass;
        protocol.put(this);
    }
    
    public final void handleMetadata(final int entityId, final List<Metadata> metadatas, final UserConnection connection) {
        final EntityType type = connection.get(this.entityTrackerClass).getEntity(entityId);
        for (final Metadata metadata : new ArrayList<Metadata>(metadatas)) {
            try {
                this.handleMetadata(entityId, type, metadata, metadatas, connection);
            }
            catch (Exception e) {
                metadatas.remove(metadata);
                if (Via.getConfig().isSuppressMetadataErrors() && !Via.getManager().isDebug()) {
                    continue;
                }
                final Logger logger = Via.getPlatform().getLogger();
                logger.warning("An error occurred with entity metadata handler");
                logger.warning("This is most likely down to one of your plugins sending bad datawatchers. Please test if this occurs without any plugins except ViaVersion before reporting it on GitHub");
                logger.warning("Also make sure that all your plugins are compatible with your server version.");
                logger.warning("Entity type: " + type);
                logger.warning("Metadata: " + metadata);
                e.printStackTrace();
            }
        }
    }
    
    protected void rewriteParticle(final Particle particle) {
        final ParticleMappings mappings = this.protocol.getMappingData().getParticleMappings();
        final int id = particle.getId();
        if (id == mappings.getBlockId() || id == mappings.getFallingDustId()) {
            final Particle.ParticleData data = particle.getArguments().get(0);
            data.setValue(this.protocol.getMappingData().getNewBlockStateId(data.get()));
        }
        else if (id == mappings.getItemId()) {
            final Particle.ParticleData data = particle.getArguments().get(0);
            data.setValue(this.protocol.getMappingData().getNewItemId(data.get()));
        }
        particle.setId(this.protocol.getMappingData().getNewParticleId(id));
    }
    
    public void registerTracker(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.handler(MetadataRewriter.this.getTracker());
            }
        });
    }
    
    public void registerSpawnTrackerWithData(final ClientboundPacketType packetType, final EntityType fallingBlockType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
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
                this.map(Type.INT);
                this.handler(MetadataRewriter.this.getTracker());
                final EntityType val$fallingBlockType;
                final int entityId;
                final EntityType entityType;
                this.handler(wrapper -> {
                    val$fallingBlockType = fallingBlockType;
                    entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    entityType = wrapper.user().get(MetadataRewriter.this.entityTrackerClass).getEntity(entityId);
                    if (entityType == val$fallingBlockType) {
                        wrapper.set(Type.INT, 0, MetadataRewriter.this.protocol.getMappingData().getNewBlockStateId(wrapper.get(Type.INT, 0)));
                    }
                });
            }
        });
    }
    
    public void registerTracker(final ClientboundPacketType packetType, final EntityType entityType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final EntityType val$entityType;
                final int entityId;
                this.handler(wrapper -> {
                    val$entityType = entityType;
                    entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    wrapper.user().get(MetadataRewriter.this.entityTrackerClass).addEntity(entityId, val$entityType);
                });
            }
        });
    }
    
    public void registerEntityDestroy(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT_ARRAY_PRIMITIVE);
                final EntityTracker entityTracker;
                final int[] array;
                int length;
                int i = 0;
                int entity;
                this.handler(wrapper -> {
                    entityTracker = wrapper.user().get(MetadataRewriter.this.entityTrackerClass);
                    array = wrapper.get(Type.VAR_INT_ARRAY_PRIMITIVE, 0);
                    for (length = array.length; i < length; ++i) {
                        entity = array[i];
                        entityTracker.removeEntity(entity);
                    }
                });
            }
        });
    }
    
    public void registerMetadataRewriter(final ClientboundPacketType packetType, @Nullable final Type<List<Metadata>> oldMetaType, final Type<List<Metadata>> newMetaType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                if (oldMetaType != null) {
                    this.map(oldMetaType, newMetaType);
                }
                else {
                    this.map(newMetaType);
                }
                final Type<List<Metadata>> val$newMetaType;
                final int entityId;
                final List<Metadata> metadata;
                this.handler(wrapper -> {
                    val$newMetaType = newMetaType;
                    entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    metadata = wrapper.get(val$newMetaType, 0);
                    MetadataRewriter.this.handleMetadata(entityId, metadata, wrapper.user());
                });
            }
        });
    }
    
    public void registerMetadataRewriter(final ClientboundPacketType packetType, final Type<List<Metadata>> metaType) {
        this.registerMetadataRewriter(packetType, null, metaType);
    }
    
    public <T extends Enum> void mapTypes(final EntityType[] oldTypes, final Class<T> newTypeClass) {
        if (this.typeMapping == null) {
            (this.typeMapping = new Int2IntOpenHashMap(oldTypes.length, 1.0f)).defaultReturnValue(-1);
        }
        for (final EntityType oldType : oldTypes) {
            try {
                final T newType = java.lang.Enum.valueOf(newTypeClass, oldType.name());
                this.typeMapping.put(oldType.getId(), ((EntityType)newType).getId());
            }
            catch (IllegalArgumentException notFound) {
                if (!this.typeMapping.containsKey(oldType.getId())) {
                    Via.getPlatform().getLogger().warning("Could not find new entity type for " + oldType + "! Old type: " + oldType.getClass().getEnclosingClass().getSimpleName() + ", new type: " + newTypeClass.getEnclosingClass().getSimpleName());
                }
            }
        }
    }
    
    public void mapType(final EntityType oldType, final EntityType newType) {
        if (this.typeMapping == null) {
            (this.typeMapping = new Int2IntOpenHashMap()).defaultReturnValue(-1);
        }
        this.typeMapping.put(oldType.getId(), newType.getId());
    }
    
    public PacketHandler getTracker() {
        return this.getTrackerAndRewriter(null);
    }
    
    public PacketHandler getTrackerAndRewriter(@Nullable final Type<List<Metadata>> metaType) {
        final int entityId;
        final int type;
        final int newType;
        final EntityType entType;
        return wrapper -> {
            entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            type = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
            newType = this.getNewEntityId(type);
            if (newType != type) {
                wrapper.set(Type.VAR_INT, 1, newType);
            }
            entType = this.getTypeFromId(newType);
            wrapper.user().get(this.entityTrackerClass).addEntity(entityId, entType);
            if (metaType != null) {
                this.handleMetadata(entityId, wrapper.get(metaType, 0), wrapper.user());
            }
        };
    }
    
    public PacketHandler getTrackerAndRewriter(@Nullable final Type<List<Metadata>> metaType, final EntityType entityType) {
        final int entityId;
        return wrapper -> {
            entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            wrapper.user().get(this.entityTrackerClass).addEntity(entityId, entityType);
            if (metaType != null) {
                this.handleMetadata(entityId, wrapper.get(metaType, 0), wrapper.user());
            }
        };
    }
    
    public PacketHandler getObjectTracker() {
        final int entityId;
        final byte type;
        final EntityType entType;
        return wrapper -> {
            entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            type = wrapper.get(Type.BYTE, 0);
            entType = this.getObjectTypeFromId(type);
            wrapper.user().get(this.entityTrackerClass).addEntity(entityId, entType);
        };
    }
    
    protected abstract EntityType getTypeFromId(final int p0);
    
    protected EntityType getObjectTypeFromId(final int type) {
        return this.getTypeFromId(type);
    }
    
    public int getNewEntityId(final int oldId) {
        return (this.typeMapping != null) ? this.typeMapping.getOrDefault(oldId, oldId) : oldId;
    }
    
    protected abstract void handleMetadata(final int p0, @Nullable final EntityType p1, final Metadata p2, final List<Metadata> p3, final UserConnection p4) throws Exception;
    
    @Nullable
    protected Metadata getMetaByIndex(final int index, final List<Metadata> metadataList) {
        for (final Metadata metadata : metadataList) {
            if (metadata.getId() == index) {
                return metadata;
            }
        }
        return null;
    }
}
