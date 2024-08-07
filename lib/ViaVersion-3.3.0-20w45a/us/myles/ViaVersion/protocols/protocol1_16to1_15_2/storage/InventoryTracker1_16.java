// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16to1_15_2.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public class InventoryTracker1_16 extends StoredObject
{
    private short inventory;
    
    public InventoryTracker1_16(final UserConnection user) {
        super(user);
        this.inventory = -1;
    }
    
    public short getInventory() {
        return this.inventory;
    }
    
    public void setInventory(final short inventory) {
        this.inventory = inventory;
    }
}
