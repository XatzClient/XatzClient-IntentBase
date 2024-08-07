// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_10to1_9_3.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public class ResourcePackTracker extends StoredObject
{
    private String lastHash;
    
    public ResourcePackTracker(final UserConnection user) {
        super(user);
        this.lastHash = "";
    }
    
    public String getLastHash() {
        return this.lastHash;
    }
    
    public void setLastHash(final String lastHash) {
        this.lastHash = lastHash;
    }
    
    @Override
    public String toString() {
        return "ResourcePackTracker{lastHash='" + this.lastHash + '\'' + '}';
    }
}
