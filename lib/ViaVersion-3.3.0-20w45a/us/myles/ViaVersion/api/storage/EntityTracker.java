// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.storage;

import org.jetbrains.annotations.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.EntityType;
import java.util.Map;
import us.myles.ViaVersion.api.data.ExternalJoinGameListener;
import us.myles.ViaVersion.api.data.StoredObject;

public abstract class EntityTracker extends StoredObject implements ExternalJoinGameListener
{
    private final Map<Integer, EntityType> clientEntityTypes;
    private final EntityType playerType;
    private int clientEntityId;
    
    protected EntityTracker(final UserConnection user, final EntityType playerType) {
        super(user);
        this.clientEntityTypes = new ConcurrentHashMap<Integer, EntityType>();
        this.playerType = playerType;
    }
    
    public void removeEntity(final int entityId) {
        this.clientEntityTypes.remove(entityId);
    }
    
    public void addEntity(final int entityId, final EntityType type) {
        this.clientEntityTypes.put(entityId, type);
    }
    
    public boolean hasEntity(final int entityId) {
        return this.clientEntityTypes.containsKey(entityId);
    }
    
    @Nullable
    public EntityType getEntity(final int entityId) {
        return this.clientEntityTypes.get(entityId);
    }
    
    @Override
    public void onExternalJoinGame(final int playerEntityId) {
        this.clientEntityId = playerEntityId;
        this.clientEntityTypes.put(playerEntityId, this.playerType);
    }
    
    public int getClientEntityId() {
        return this.clientEntityId;
    }
    
    public void setClientEntityId(final int clientEntityId) {
        this.clientEntityId = clientEntityId;
    }
}
