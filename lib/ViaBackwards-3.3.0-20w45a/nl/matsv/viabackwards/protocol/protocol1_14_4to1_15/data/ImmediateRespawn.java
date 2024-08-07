// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public class ImmediateRespawn extends StoredObject
{
    private boolean immediateRespawn;
    
    public ImmediateRespawn(final UserConnection user) {
        super(user);
    }
    
    public boolean isImmediateRespawn() {
        return this.immediateRespawn;
    }
    
    public void setImmediateRespawn(final boolean immediateRespawn) {
        this.immediateRespawn = immediateRespawn;
    }
}
