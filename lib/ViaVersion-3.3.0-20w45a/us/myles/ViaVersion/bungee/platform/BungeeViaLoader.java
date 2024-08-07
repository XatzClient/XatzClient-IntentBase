// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.platform;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import us.myles.ViaVersion.bungee.service.ProtocolDetectorService;
import us.myles.ViaVersion.bungee.providers.BungeeMainHandProvider;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.MainHandProvider;
import us.myles.ViaVersion.bungee.providers.BungeeBossBarProvider;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BossBarProvider;
import us.myles.ViaVersion.bungee.providers.BungeeMovementTransmitter;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import us.myles.ViaVersion.bungee.providers.BungeeEntityIdProvider;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
import us.myles.ViaVersion.bungee.providers.BungeeVersionProvider;
import us.myles.ViaVersion.protocols.base.VersionProvider;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.bungee.listeners.ElytraPatch;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.bungee.handlers.BungeeServerHandler;
import us.myles.ViaVersion.bungee.listeners.UpdateListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.ProxyServer;
import java.util.HashSet;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.plugin.Listener;
import java.util.Set;
import us.myles.ViaVersion.BungeePlugin;
import us.myles.ViaVersion.api.platform.ViaPlatformLoader;

public class BungeeViaLoader implements ViaPlatformLoader
{
    private final BungeePlugin plugin;
    private final Set<Listener> listeners;
    private final Set<ScheduledTask> tasks;
    
    public BungeeViaLoader(final BungeePlugin plugin) {
        this.listeners = new HashSet<Listener>();
        this.tasks = new HashSet<ScheduledTask>();
        this.plugin = plugin;
    }
    
    private void registerListener(final Listener listener) {
        this.listeners.add(listener);
        ProxyServer.getInstance().getPluginManager().registerListener((Plugin)this.plugin, listener);
    }
    
    @Override
    public void load() {
        this.registerListener((Listener)this.plugin);
        this.registerListener((Listener)new UpdateListener());
        this.registerListener((Listener)new BungeeServerHandler());
        if (ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_9.getVersion()) {
            this.registerListener((Listener)new ElytraPatch());
        }
        Via.getManager().getProviders().use((Class<BungeeVersionProvider>)VersionProvider.class, new BungeeVersionProvider());
        Via.getManager().getProviders().use((Class<BungeeEntityIdProvider>)EntityIdProvider.class, new BungeeEntityIdProvider());
        if (ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_9.getVersion()) {
            Via.getManager().getProviders().use((Class<BungeeMovementTransmitter>)MovementTransmitterProvider.class, new BungeeMovementTransmitter());
            Via.getManager().getProviders().use((Class<BungeeBossBarProvider>)BossBarProvider.class, new BungeeBossBarProvider());
            Via.getManager().getProviders().use((Class<BungeeMainHandProvider>)MainHandProvider.class, new BungeeMainHandProvider());
        }
        if (this.plugin.getConf().getBungeePingInterval() > 0) {
            this.tasks.add(this.plugin.getProxy().getScheduler().schedule((Plugin)this.plugin, (Runnable)new ProtocolDetectorService(this.plugin), 0L, (long)this.plugin.getConf().getBungeePingInterval(), TimeUnit.SECONDS));
        }
    }
    
    @Override
    public void unload() {
        for (final Listener listener : this.listeners) {
            ProxyServer.getInstance().getPluginManager().unregisterListener(listener);
        }
        this.listeners.clear();
        for (final ScheduledTask task : this.tasks) {
            task.cancel();
        }
        this.tasks.clear();
    }
}
