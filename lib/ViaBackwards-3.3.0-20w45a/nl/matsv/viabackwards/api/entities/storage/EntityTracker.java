// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.entities.storage;

import us.myles.ViaVersion.api.entities.EntityType;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import java.util.Map;
import us.myles.ViaVersion.api.data.StoredObject;

public class EntityTracker extends StoredObject
{
    private final Map<BackwardsProtocol, ProtocolEntityTracker> trackers;
    
    public EntityTracker(final UserConnection user) {
        super(user);
        this.trackers = new ConcurrentHashMap<BackwardsProtocol, ProtocolEntityTracker>();
    }
    
    public void initProtocol(final BackwardsProtocol protocol) {
        this.trackers.put(protocol, new ProtocolEntityTracker());
    }
    
    @Nullable
    public ProtocolEntityTracker get(final BackwardsProtocol protocol) {
        return this.trackers.get(protocol);
    }
    
    public static class ProtocolEntityTracker
    {
        private final Map<Integer, StoredEntity> entityMap;
        
        public ProtocolEntityTracker() {
            this.entityMap = new ConcurrentHashMap<Integer, StoredEntity>();
        }
        
        public void trackEntityType(final int id, final EntityType type) {
            this.entityMap.putIfAbsent(id, new StoredEntity(id, type));
        }
        
        public void removeEntity(final int id) {
            this.entityMap.remove(id);
        }
        
        @Nullable
        public EntityType getEntityType(final int id) {
            final StoredEntity storedEntity = this.entityMap.get(id);
            return (storedEntity != null) ? storedEntity.getType() : null;
        }
        
        @Nullable
        public StoredEntity getEntity(final int id) {
            return this.entityMap.get(id);
        }
    }
    
    public static final class StoredEntity
    {
        private final int entityId;
        private final EntityType type;
        private final Map<Class<? extends EntityStorage>, EntityStorage> storedObjects;
        
        private StoredEntity(final int entityId, final EntityType type) {
            this.storedObjects = new ConcurrentHashMap<Class<? extends EntityStorage>, EntityStorage>();
            this.entityId = entityId;
            this.type = type;
        }
        
        @Nullable
        public <T extends EntityStorage> T get(final Class<T> objectClass) {
            return (T)this.storedObjects.get(objectClass);
        }
        
        public boolean has(final Class<? extends EntityStorage> objectClass) {
            return this.storedObjects.containsKey(objectClass);
        }
        
        public void put(final EntityStorage object) {
            this.storedObjects.put(object.getClass(), object);
        }
        
        public int getEntityId() {
            return this.entityId;
        }
        
        public EntityType getType() {
            return this.type;
        }
        
        @Override
        public String toString() {
            return "StoredEntity{entityId=" + this.entityId + ", type=" + this.type + ", storedObjects=" + this.storedObjects + '}';
        }
    }
}
