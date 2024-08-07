// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public class PlayerSneakStorage extends StoredObject
{
    private boolean sneaking;
    
    public PlayerSneakStorage(final UserConnection user) {
        super(user);
    }
    
    public boolean isSneaking() {
        return this.sneaking;
    }
    
    public void setSneaking(final boolean sneaking) {
        this.sneaking = sneaking;
    }
}
