// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_11to1_10.storage;

import com.google.common.collect.Sets;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_11Types;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.Set;
import us.myles.ViaVersion.api.storage.EntityTracker;

public class EntityTracker1_11 extends EntityTracker
{
    private final Set<Integer> holograms;
    
    public EntityTracker1_11(final UserConnection user) {
        super(user, Entity1_11Types.EntityType.PLAYER);
        this.holograms = (Set<Integer>)Sets.newConcurrentHashSet();
    }
    
    @Override
    public void removeEntity(final int entityId) {
        super.removeEntity(entityId);
        if (this.isHologram(entityId)) {
            this.removeHologram(entityId);
        }
    }
    
    public void addHologram(final int entId) {
        this.holograms.add(entId);
    }
    
    public boolean isHologram(final int entId) {
        return this.holograms.contains(entId);
    }
    
    public void removeHologram(final int entId) {
        this.holograms.remove(entId);
    }
}
