// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_16_3to1_16_4.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public class PlayerHandStorage extends StoredObject
{
    private int currentHand;
    
    public PlayerHandStorage(final UserConnection user) {
        super(user);
    }
    
    public int getCurrentHand() {
        return this.currentHand;
    }
    
    public void setCurrentHand(final int currentHand) {
        this.currentHand = currentHand;
    }
}
