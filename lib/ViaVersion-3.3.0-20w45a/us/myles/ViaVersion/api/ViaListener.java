// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api;

import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.UUID;
import us.myles.ViaVersion.api.protocol.Protocol;

public abstract class ViaListener
{
    private final Class<? extends Protocol> requiredPipeline;
    private boolean registered;
    
    public ViaListener(final Class<? extends Protocol> requiredPipeline) {
        this.requiredPipeline = requiredPipeline;
    }
    
    @Nullable
    protected UserConnection getUserConnection(final UUID uuid) {
        return Via.getManager().getConnection(uuid);
    }
    
    protected boolean isOnPipe(final UUID uuid) {
        final UserConnection userConnection = this.getUserConnection(uuid);
        return userConnection != null && (this.requiredPipeline == null || userConnection.getProtocolInfo().getPipeline().contains(this.requiredPipeline));
    }
    
    public abstract void register();
    
    protected Class<? extends Protocol> getRequiredPipeline() {
        return this.requiredPipeline;
    }
    
    protected boolean isRegistered() {
        return this.registered;
    }
    
    protected void setRegistered(final boolean registered) {
        this.registered = registered;
    }
}
