// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.listeners;

import org.spongepowered.api.event.Listener;
import us.myles.ViaVersion.update.UpdateUtil;
import us.myles.ViaVersion.api.Via;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class UpdateListener
{
    @Listener
    public void onJoin(final ClientConnectionEvent.Join join) {
        if (join.getTargetEntity().hasPermission("viaversion.update") && Via.getConfig().isCheckForUpdates()) {
            UpdateUtil.sendUpdateMessage(join.getTargetEntity().getUniqueId());
        }
    }
}
