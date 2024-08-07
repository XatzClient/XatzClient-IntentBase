// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.platform;

import com.velocitypowered.api.scheduler.ScheduledTask;
import us.myles.ViaVersion.api.platform.TaskId;

public class VelocityTaskId implements TaskId
{
    private final ScheduledTask object;
    
    public VelocityTaskId(final ScheduledTask object) {
        this.object = object;
    }
    
    @Override
    public ScheduledTask getObject() {
        return this.object;
    }
}
