// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.listeners;

import net.md_5.bungee.event.EventHandler;
import us.myles.ViaVersion.update.UpdateUtil;
import us.myles.ViaVersion.api.Via;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;

public class UpdateListener implements Listener
{
    @EventHandler
    public void onJoin(final PostLoginEvent e) {
        if (e.getPlayer().hasPermission("viaversion.update") && Via.getConfig().isCheckForUpdates()) {
            UpdateUtil.sendUpdateMessage(e.getPlayer().getUniqueId());
        }
    }
}
