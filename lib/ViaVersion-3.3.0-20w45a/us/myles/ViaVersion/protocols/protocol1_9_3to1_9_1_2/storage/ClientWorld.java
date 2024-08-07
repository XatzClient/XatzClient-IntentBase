// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.Environment;
import us.myles.ViaVersion.api.data.StoredObject;

public class ClientWorld extends StoredObject
{
    private Environment environment;
    
    public ClientWorld(final UserConnection user) {
        super(user);
    }
    
    public Environment getEnvironment() {
        return this.environment;
    }
    
    public void setEnvironment(final int environmentId) {
        this.environment = Environment.getEnvironmentById(environmentId);
    }
}
