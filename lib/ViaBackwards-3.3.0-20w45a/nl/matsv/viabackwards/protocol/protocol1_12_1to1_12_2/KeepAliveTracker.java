// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_1to1_12_2;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public class KeepAliveTracker extends StoredObject
{
    private long keepAlive;
    
    public KeepAliveTracker(final UserConnection user) {
        super(user);
        this.keepAlive = 2147483647L;
    }
    
    public long getKeepAlive() {
        return this.keepAlive;
    }
    
    public void setKeepAlive(final long keepAlive) {
        this.keepAlive = keepAlive;
    }
    
    public String toString() {
        return "KeepAliveTracker{keepAlive=" + this.keepAlive + '}';
    }
}
