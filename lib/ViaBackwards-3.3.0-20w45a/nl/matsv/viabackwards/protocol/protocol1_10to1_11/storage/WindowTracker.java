// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_10to1_11.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public class WindowTracker extends StoredObject
{
    private String inventory;
    private int entityId;
    
    public WindowTracker(final UserConnection user) {
        super(user);
        this.entityId = -1;
    }
    
    public String getInventory() {
        return this.inventory;
    }
    
    public void setInventory(final String inventory) {
        this.inventory = inventory;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(final int entityId) {
        this.entityId = entityId;
    }
    
    public String toString() {
        return "WindowTracker{inventory='" + this.inventory + '\'' + ", entityId=" + this.entityId + '}';
    }
}
