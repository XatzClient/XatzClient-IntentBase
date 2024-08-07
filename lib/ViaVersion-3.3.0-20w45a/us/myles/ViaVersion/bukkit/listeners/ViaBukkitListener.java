// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.listeners;

import us.myles.ViaVersion.api.data.UserConnection;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.protocol.Protocol;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import us.myles.ViaVersion.api.ViaListener;

public class ViaBukkitListener extends ViaListener implements Listener
{
    private final Plugin plugin;
    
    public ViaBukkitListener(final Plugin plugin, final Class<? extends Protocol> requiredPipeline) {
        super(requiredPipeline);
        this.plugin = plugin;
    }
    
    protected UserConnection getUserConnection(final Player player) {
        return this.getUserConnection(player.getUniqueId());
    }
    
    protected boolean isOnPipe(final Player player) {
        return this.isOnPipe(player.getUniqueId());
    }
    
    @Override
    public void register() {
        if (this.isRegistered()) {
            return;
        }
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, this.plugin);
        this.setRegistered(true);
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
}
