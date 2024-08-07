// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import us.myles.ViaVersion.update.UpdateUtil;
import us.myles.ViaVersion.api.Via;
import com.velocitypowered.api.event.connection.PostLoginEvent;

public class UpdateListener
{
    @Subscribe
    public void onJoin(final PostLoginEvent e) {
        if (e.getPlayer().hasPermission("viaversion.update") && Via.getConfig().isCheckForUpdates()) {
            UpdateUtil.sendUpdateMessage(e.getPlayer().getUniqueId());
        }
    }
}
