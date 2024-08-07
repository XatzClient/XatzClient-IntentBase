// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.platform;

import org.spongepowered.api.scheduler.Task;
import us.myles.ViaVersion.api.platform.TaskId;

public class SpongeTaskId implements TaskId
{
    private final Task object;
    
    public SpongeTaskId(final Task object) {
        this.object = object;
    }
    
    @Override
    public Task getObject() {
        return this.object;
    }
}
