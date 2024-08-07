// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.rewriters;

import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.data.ParticleMappings;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import java.util.logging.Logger;
import java.util.Iterator;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import java.util.Collection;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.exception.CancelException;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.Via;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import us.myles.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.ArrayList;
import java.util.HashMap;
import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerSettings;
import java.util.List;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import us.myles.ViaVersion.api.entities.EntityType;
import java.util.Map;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public abstract class EntityRewriterBase<T extends BackwardsProtocol> extends Rewriter<T>
{
    private final Map<EntityType, EntityData> entityTypes;
    private final List<MetaHandlerSettings> metaHandlers;
    private final MetaType displayNameMetaType;
    private final MetaType displayVisibilityMetaType;
    private final int displayNameIndex;
    private final int displayVisibilityIndex;
    protected Int2IntMap typeMapping;
    
    EntityRewriterBase(final T protocol, final MetaType displayNameMetaType, final int displayNameIndex, final MetaType displayVisibilityMetaType, final int displayVisibilityIndex) {
        super(protocol);
        this.entityTypes = new HashMap<EntityType, EntityData>();
        this.metaHandlers = new ArrayList<MetaHandlerSettings>();
        this.displayNameMetaType = displayNameMetaType;
        this.displayNameIndex = displayNameIndex;
        this.displayVisibilityMetaType = displayVisibilityMetaType;
        this.displayVisibilityIndex = displayVisibilityIndex;
    }
    
    protected EntityType getEntityType(final UserConnection connection, final int id) {
        return this.getEntityTracker(connection).getEntityType(id);
    }
    
    protected void addTrackedEntity(final PacketWrapper wrapper, final int entityId, final EntityType type) throws Exception {
        this.getEntityTracker(wrapper.user()).trackEntityType(entityId, type);
    }
    
    protected boolean hasData(final EntityType type) {
        return this.entityTypes.containsKey(type);
    }
    
    @Nullable
    protected EntityData getEntityData(final EntityType type) {
        return this.entityTypes.get(type);
    }
    
    protected EntityData mapEntity(final EntityType oldType, final EntityType replacement) {
        Preconditions.checkArgument(oldType.getClass() == replacement.getClass());
        final int mappedReplacementId = this.getOldEntityId(replacement.getId());
        final EntityData data = new EntityData(oldType.getId(), mappedReplacementId);
        this.mapEntityDirect(oldType.getId(), mappedReplacementId);
        this.entityTypes.put(oldType, data);
        return data;
    }
    
    public <T extends Enum> void mapTypes(final EntityType[] oldTypes, final Class<T> newTypeClass) {
        if (this.typeMapping == null) {
            (this.typeMapping = (Int2IntMap)new Int2IntOpenHashMap(oldTypes.length, 1.0f)).defaultReturnValue(-1);
        }
        for (final EntityType oldType : oldTypes) {
            try {
                final T newType = java.lang.Enum.valueOf(newTypeClass, oldType.name());
                this.typeMapping.put(oldType.getId(), ((EntityType)newType).getId());
            }
            catch (IllegalArgumentException ex) {}
        }
    }
    
    public void mapEntityDirect(final EntityType oldType, final EntityType newType) {
        Preconditions.checkArgument(oldType.getClass() != newType.getClass());
        this.mapEntityDirect(oldType.getId(), newType.getId());
    }
    
    private void mapEntityDirect(final int oldType, final int newType) {
        if (this.typeMapping == null) {
            (this.typeMapping = (Int2IntMap)new Int2IntOpenHashMap()).defaultReturnValue(-1);
        }
        this.typeMapping.put(oldType, newType);
    }
    
    public MetaHandlerSettings registerMetaHandler() {
        final MetaHandlerSettings settings = new MetaHandlerSettings();
        this.metaHandlers.add(settings);
        return settings;
    }
    
    protected MetaStorage handleMeta(final UserConnection user, final int entityId, final MetaStorage storage) throws Exception {
        final EntityTracker.StoredEntity storedEntity = this.getEntityTracker(user).getEntity(entityId);
        if (storedEntity == null) {
            if (!Via.getConfig().isSuppressMetadataErrors()) {
                ViaBackwards.getPlatform().getLogger().warning("Metadata for entity id: " + entityId + " not sent because the entity doesn't exist. " + storage);
            }
            throw CancelException.CACHED;
        }
        final EntityType type = storedEntity.getType();
        for (final MetaHandlerSettings settings : this.metaHandlers) {
            final List<Metadata> newData = new ArrayList<Metadata>();
            for (final Metadata meta : storage.getMetaDataList()) {
                MetaHandlerEvent event = null;
                try {
                    Metadata modifiedMeta = meta;
                    if (settings.isGucci(type, meta)) {
                        event = new MetaHandlerEvent(user, storedEntity, meta.getId(), meta, storage);
                        modifiedMeta = settings.getHandler().handle(event);
                        if (event.getExtraData() != null) {
                            newData.addAll(event.getExtraData());
                            event.clearExtraData();
                        }
                    }
                    if (modifiedMeta == null) {
                        throw RemovedValueException.EX;
                    }
                    newData.add(modifiedMeta);
                }
                catch (RemovedValueException e2) {
                    if (event == null || event.getExtraData() == null) {
                        continue;
                    }
                    newData.addAll(event.getExtraData());
                }
                catch (Exception e) {
                    final Logger log = ViaBackwards.getPlatform().getLogger();
                    log.warning("Unable to handle metadata " + meta + " for entity type " + type);
                    log.warning(storage.getMetaDataList().stream().sorted(Comparator.comparingInt(Metadata::getId)).map((Function<? super Object, ?>)Metadata::toString).collect((Collector<? super Object, ?, String>)Collectors.joining("\n", "Full metadata list: ", "")));
                    e.printStackTrace();
                }
            }
            storage.setMetaDataList(newData);
        }
        final Metadata data = storage.get(this.displayNameIndex);
        if (data != null) {
            final EntityData entityData = this.getEntityData(type);
            if (entityData != null && entityData.getMobName() != null && (data.getValue() == null || data.getValue().toString().isEmpty()) && data.getMetaType().getTypeID() == this.displayNameMetaType.getTypeID()) {
                data.setValue(entityData.getMobName());
                if (ViaBackwards.getConfig().alwaysShowOriginalMobName()) {
                    storage.delete(this.displayVisibilityIndex);
                    storage.add(new Metadata(this.displayVisibilityIndex, this.displayVisibilityMetaType, (Object)true));
                }
            }
        }
        return storage;
    }
    
    protected void registerExtraTracker(final ClientboundPacketType packetType, final EntityType entityType, final Type intType) {
        this.getProtocol().registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(intType);
                this.handler(wrapper -> EntityRewriterBase.this.addTrackedEntity(wrapper, (int)wrapper.get(intType, 0), entityType));
            }
        });
    }
    
    protected void registerExtraTracker(final ClientboundPacketType packetType, final EntityType entityType) {
        this.registerExtraTracker(packetType, entityType, (Type)Type.VAR_INT);
    }
    
    protected void registerEntityDestroy(final ClientboundPacketType packetType) {
        this.getProtocol().registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.VAR_INT_ARRAY_PRIMITIVE);
                this.handler(wrapper -> {
                    final EntityTracker.ProtocolEntityTracker tracker = EntityRewriterBase.this.getEntityTracker(wrapper.user());
                    for (final int entity : (int[])wrapper.get(Type.VAR_INT_ARRAY_PRIMITIVE, 0)) {
                        tracker.removeEntity(entity);
                    }
                });
            }
        });
    }
    
    protected PacketHandler getTrackerHandler(final Type intType, final int typeIndex) {
        return wrapper -> {
            final Number id = (Number)wrapper.get(intType, typeIndex);
            this.addTrackedEntity(wrapper, (int)wrapper.get((Type)Type.VAR_INT, 0), this.getTypeFromId(id.intValue()));
        };
    }
    
    protected PacketHandler getTrackerHandler() {
        return this.getTrackerHandler((Type)Type.VAR_INT, 1);
    }
    
    protected PacketHandler getTrackerHandler(final EntityType entityType, final Type intType) {
        return wrapper -> this.addTrackedEntity(wrapper, (int)wrapper.get(intType, 0), entityType);
    }
    
    protected PacketHandler getDimensionHandler(final int index) {
        return wrapper -> {
            final ClientWorld clientWorld = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
            final int dimensionId = (int)wrapper.get(Type.INT, index);
            clientWorld.setEnvironment(dimensionId);
        };
    }
    
    public EntityTracker.ProtocolEntityTracker getEntityTracker(final UserConnection user) {
        return ((EntityTracker)user.get((Class)EntityTracker.class)).get(this.getProtocol());
    }
    
    protected void rewriteParticle(final Particle particle) {
        final ParticleMappings mappings = this.protocol.getMappingData().getParticleMappings();
        final int id = particle.getId();
        if (id == mappings.getBlockId() || id == mappings.getFallingDustId()) {
            final Particle.ParticleData data = particle.getArguments().get(0);
            data.setValue((Object)this.protocol.getMappingData().getNewBlockStateId((int)data.get()));
        }
        else if (id == mappings.getItemId()) {
            final Particle.ParticleData data = particle.getArguments().get(0);
            data.setValue((Object)this.protocol.getMappingData().getNewItemId((int)data.get()));
        }
        particle.setId(this.protocol.getMappingData().getNewParticleId(id));
    }
    
    protected abstract EntityType getTypeFromId(final int p0);
    
    public int getOldEntityId(final int newId) {
        return (this.typeMapping != null) ? this.typeMapping.getOrDefault(newId, newId) : newId;
    }
}
