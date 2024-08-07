// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.listeners;

import org.bukkit.event.EventHandler;
import us.myles.ViaVersion.update.UpdateUtil;
import us.myles.ViaVersion.api.Via;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class UpdateListener implements Listener
{
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("viaversion.update") && Via.getConfig().isCheckForUpdates()) {
            UpdateUtil.sendUpdateMessage(e.getPlayer().getUniqueId());
        }
    }
}
