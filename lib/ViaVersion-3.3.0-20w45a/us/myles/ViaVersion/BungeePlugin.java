// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion;

import us.myles.ViaVersion.api.ViaVersionConfig;
import java.util.List;
import us.myles.ViaVersion.bungee.service.ProtocolDetectorService;
import us.myles.ViaVersion.util.GsonUtil;
import java.util.Collections;
import us.myles.ViaVersion.dump.PluginInfo;
import java.util.ArrayList;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.api.configuration.ConfigurationProvider;
import us.myles.ViaVersion.api.ViaAPI;
import java.util.UUID;
import java.util.Iterator;
import net.md_5.bungee.api.CommandSender;
import us.myles.ViaVersion.bungee.commands.BungeeCommandSender;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import java.util.concurrent.TimeUnit;
import us.myles.ViaVersion.bungee.platform.BungeeTaskId;
import us.myles.ViaVersion.api.platform.TaskId;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.commands.ViaCommandHandler;
import us.myles.ViaVersion.api.platform.ViaPlatformLoader;
import us.myles.ViaVersion.bungee.platform.BungeeViaLoader;
import us.myles.ViaVersion.api.platform.ViaInjector;
import us.myles.ViaVersion.bungee.platform.BungeeViaInjector;
import net.md_5.bungee.api.plugin.Command;
import us.myles.ViaVersion.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.ProxyServer;
import us.myles.ViaVersion.bungee.commands.BungeeCommandHandler;
import net.md_5.bungee.protocol.ProtocolConstants;
import us.myles.ViaVersion.bungee.platform.BungeeViaConfig;
import us.myles.ViaVersion.bungee.platform.BungeeViaAPI;
import us.myles.ViaVersion.api.platform.ViaConnectionManager;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.myles.ViaVersion.api.platform.ViaPlatform;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin implements ViaPlatform<ProxiedPlayer>, Listener
{
    private final ViaConnectionManager connectionManager;
    private BungeeViaAPI api;
    private BungeeViaConfig config;
    
    public BungeePlugin() {
        this.connectionManager = new ViaConnectionManager();
    }
    
    public void onLoad() {
        try {
            ProtocolConstants.class.getField("MINECRAFT_1_16_3");
        }
        catch (NoSuchFieldException e) {
            this.getLogger().warning("      / \\");
            this.getLogger().warning("     /   \\");
            this.getLogger().warning("    /  |  \\");
            this.getLogger().warning("   /   |   \\         BUNGEECORD IS OUTDATED");
            this.getLogger().warning("  /         \\   VIAVERSION MAY NOT WORK AS INTENDED");
            this.getLogger().warning(" /     o     \\");
            this.getLogger().warning("/_____________\\");
        }
        this.api = new BungeeViaAPI();
        this.config = new BungeeViaConfig(this.getDataFolder());
        final BungeeCommandHandler commandHandler = new BungeeCommandHandler();
        ProxyServer.getInstance().getPluginManager().registerCommand((Plugin)this, (Command)new BungeeCommand(commandHandler));
        Via.init(ViaManager.builder().platform(this).injector(new BungeeViaInjector()).loader(new BungeeViaLoader(this)).commandHandler(commandHandler).build());
    }
    
    public void onEnable() {
        if (ProxyServer.getInstance().getPluginManager().getPlugin("ViaBackwards") != null) {
            MappingDataLoader.enableMappingsCache();
        }
        Via.getManager().init();
    }
    
    public String getPlatformName() {
        return this.getProxy().getName();
    }
    
    public String getPlatformVersion() {
        return this.getProxy().getVersion();
    }
    
    public boolean isProxy() {
        return true;
    }
    
    public String getPluginVersion() {
        return this.getDescription().getVersion();
    }
    
    public TaskId runAsync(final Runnable runnable) {
        return new BungeeTaskId(this.getProxy().getScheduler().runAsync((Plugin)this, runnable).getId());
    }
    
    public TaskId runSync(final Runnable runnable) {
        return this.runAsync(runnable);
    }
    
    public TaskId runSync(final Runnable runnable, final Long ticks) {
        return new BungeeTaskId(this.getProxy().getScheduler().schedule((Plugin)this, runnable, ticks * 50L, TimeUnit.MILLISECONDS).getId());
    }
    
    public TaskId runRepeatingSync(final Runnable runnable, final Long ticks) {
        return new BungeeTaskId(this.getProxy().getScheduler().schedule((Plugin)this, runnable, 0L, ticks * 50L, TimeUnit.MILLISECONDS).getId());
    }
    
    public void cancelTask(final TaskId taskId) {
        if (taskId == null) {
            return;
        }
        if (taskId.getObject() == null) {
            return;
        }
        if (taskId instanceof BungeeTaskId) {
            this.getProxy().getScheduler().cancel((int)taskId.getObject());
        }
    }
    
    public ViaCommandSender[] getOnlinePlayers() {
        final ViaCommandSender[] array = new ViaCommandSender[this.getProxy().getPlayers().size()];
        int i = 0;
        for (final ProxiedPlayer player : this.getProxy().getPlayers()) {
            array[i++] = new BungeeCommandSender((CommandSender)player);
        }
        return array;
    }
    
    public void sendMessage(final UUID uuid, final String message) {
        this.getProxy().getPlayer(uuid).sendMessage(message);
    }
    
    public boolean kickPlayer(final UUID uuid, final String message) {
        final ProxiedPlayer player = this.getProxy().getPlayer(uuid);
        if (player != null) {
            player.disconnect(message);
            return true;
        }
        return false;
    }
    
    public boolean isPluginEnabled() {
        return true;
    }
    
    public ViaAPI<ProxiedPlayer> getApi() {
        return this.api;
    }
    
    public BungeeViaConfig getConf() {
        return this.config;
    }
    
    public ConfigurationProvider getConfigurationProvider() {
        return this.config;
    }
    
    public void onReload() {
    }
    
    public JsonObject getDump() {
        final JsonObject platformSpecific = new JsonObject();
        final List<PluginInfo> plugins = new ArrayList<PluginInfo>();
        for (final Plugin p : ProxyServer.getInstance().getPluginManager().getPlugins()) {
            plugins.add(new PluginInfo(true, p.getDescription().getName(), p.getDescription().getVersion(), p.getDescription().getMain(), Collections.singletonList(p.getDescription().getAuthor())));
        }
        platformSpecific.add("plugins", GsonUtil.getGson().toJsonTree(plugins));
        platformSpecific.add("servers", GsonUtil.getGson().toJsonTree(ProtocolDetectorService.getDetectedIds()));
        return platformSpecific;
    }
    
    public boolean isOldClientsAllowed() {
        return true;
    }
    
    public ViaConnectionManager getConnectionManager() {
        return this.connectionManager;
    }
}
