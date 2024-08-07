// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public class InventoryTracker extends StoredObject
{
    private String inventory;
    
    public InventoryTracker(final UserConnection user) {
        super(user);
    }
    
    public String getInventory() {
        return this.inventory;
    }
    
    public void setInventory(final String inventory) {
        this.inventory = inventory;
    }
}
