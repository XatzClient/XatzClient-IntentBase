// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.data;

public class StoredObject
{
    private final UserConnection user;
    
    public StoredObject(final UserConnection user) {
        this.user = user;
    }
    
    public UserConnection getUser() {
        return this.user;
    }
}
