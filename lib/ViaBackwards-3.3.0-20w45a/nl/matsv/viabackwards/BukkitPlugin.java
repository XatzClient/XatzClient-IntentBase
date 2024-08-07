// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards;

import org.bukkit.plugin.Plugin;
import nl.matsv.viabackwards.listener.LecternInteractListener;
import org.bukkit.event.Listener;
import nl.matsv.viabackwards.listener.FireExtinguishListener;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.bukkit.platform.BukkitViaLoader;
import us.myles.ViaVersion.api.Via;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin implements ViaBackwardsPlatform
{
    public void onEnable() {
        this.init(this.getDataFolder());
        Via.getPlatform().runSync(this::onServerLoaded);
    }
    
    private void onServerLoaded() {
        final BukkitViaLoader loader = (BukkitViaLoader)Via.getManager().getLoader();
        if (ProtocolRegistry.SERVER_PROTOCOL >= ProtocolVersion.v1_16.getVersion()) {
            ((FireExtinguishListener)loader.storeListener((Listener)new FireExtinguishListener(this))).register();
        }
        if (ProtocolRegistry.SERVER_PROTOCOL >= ProtocolVersion.v1_14.getVersion()) {
            ((LecternInteractListener)loader.storeListener((Listener)new LecternInteractListener(this))).register();
        }
    }
    
    public void disable() {
        this.getPluginLoader().disablePlugin((Plugin)this);
    }
}
