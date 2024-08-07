// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion;

import us.myles.ViaVersion.api.ViaVersionConfig;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.ViaVersion.dump.PluginInfo;
import us.myles.viaversion.libs.gson.JsonObject;
import org.bukkit.ChatColor;
import us.myles.ViaVersion.api.configuration.ConfigurationProvider;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import us.myles.ViaVersion.bukkit.commands.BukkitCommandSender;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.bukkit.platform.BukkitTaskId;
import us.myles.ViaVersion.api.platform.TaskId;
import java.util.Iterator;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import us.myles.ViaVersion.bukkit.classgenerator.ClassGenerator;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import us.myles.ViaVersion.bukkit.util.NMSUtil;
import org.bukkit.Bukkit;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.platform.ViaPlatformLoader;
import us.myles.ViaVersion.bukkit.platform.BukkitViaLoader;
import us.myles.ViaVersion.api.platform.ViaInjector;
import us.myles.ViaVersion.bukkit.platform.BukkitViaInjector;
import us.myles.ViaVersion.commands.ViaCommandHandler;
import java.util.ArrayList;
import us.myles.ViaVersion.bukkit.platform.BukkitViaAPI;
import java.util.List;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.bukkit.platform.BukkitViaConfig;
import us.myles.ViaVersion.bukkit.commands.BukkitCommandHandler;
import us.myles.ViaVersion.api.platform.ViaConnectionManager;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.platform.ViaPlatform;
import org.bukkit.plugin.java.JavaPlugin;

public class ViaVersionPlugin extends JavaPlugin implements ViaPlatform<Player>
{
    private static ViaVersionPlugin instance;
    private final ViaConnectionManager connectionManager;
    private final BukkitCommandHandler commandHandler;
    private final BukkitViaConfig conf;
    private final ViaAPI<Player> api;
    private final List<Runnable> queuedTasks;
    private final List<Runnable> asyncQueuedTasks;
    private final boolean protocolSupport;
    private boolean compatSpigotBuild;
    private boolean spigot;
    private boolean lateBind;
    
    public ViaVersionPlugin() {
        this.connectionManager = new ViaConnectionManager();
        this.api = new BukkitViaAPI(this);
        this.queuedTasks = new ArrayList<Runnable>();
        this.asyncQueuedTasks = new ArrayList<Runnable>();
        this.spigot = true;
        ViaVersionPlugin.instance = this;
        this.commandHandler = new BukkitCommandHandler();
        Via.init(ViaManager.builder().platform(this).commandHandler(this.commandHandler).injector(new BukkitViaInjector()).loader(new BukkitViaLoader(this)).build());
        this.conf = new BukkitViaConfig();
        this.protocolSupport = (Bukkit.getPluginManager().getPlugin("ProtocolSupport") != null);
        if (this.protocolSupport) {
            this.getLogger().info("Hooking into ProtocolSupport, to prevent issues!");
            try {
                BukkitViaInjector.patchLists();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void onLoad() {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
        }
        catch (ClassNotFoundException e) {
            this.spigot = false;
        }
        try {
            NMSUtil.nms("PacketEncoder").getDeclaredField("version");
            this.compatSpigotBuild = true;
        }
        catch (Exception e2) {
            this.compatSpigotBuild = false;
        }
        if (this.getServer().getPluginManager().getPlugin("ViaBackwards") != null) {
            MappingDataLoader.enableMappingsCache();
        }
        ClassGenerator.generate();
        this.lateBind = !BukkitViaInjector.isBinded();
        this.getLogger().info("ViaVersion " + this.getDescription().getVersion() + (this.compatSpigotBuild ? "compat" : "") + " is now loaded" + (this.lateBind ? ", waiting for boot. (late-bind)" : ", injecting!"));
        if (!this.lateBind) {
            Via.getManager().init();
        }
    }
    
    public void onEnable() {
        if (this.lateBind) {
            Via.getManager().init();
        }
        this.getCommand("viaversion").setExecutor((CommandExecutor)this.commandHandler);
        this.getCommand("viaversion").setTabCompleter((TabCompleter)this.commandHandler);
        if (this.conf.isAntiXRay() && !this.spigot) {
            this.getLogger().info("You have anti-xray on in your config, since you're not using spigot it won't fix xray!");
        }
        for (final Runnable r : this.queuedTasks) {
            Bukkit.getScheduler().runTask((Plugin)this, r);
        }
        this.queuedTasks.clear();
        for (final Runnable r : this.asyncQueuedTasks) {
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)this, r);
        }
        this.asyncQueuedTasks.clear();
    }
    
    public void onDisable() {
        Via.getManager().destroy();
    }
    
    public String getPlatformName() {
        return Bukkit.getServer().getName();
    }
    
    public String getPlatformVersion() {
        return Bukkit.getServer().getVersion();
    }
    
    public String getPluginVersion() {
        return this.getDescription().getVersion();
    }
    
    public TaskId runAsync(final Runnable runnable) {
        if (this.isPluginEnabled()) {
            return new BukkitTaskId(this.getServer().getScheduler().runTaskAsynchronously((Plugin)this, runnable).getTaskId());
        }
        this.asyncQueuedTasks.add(runnable);
        return new BukkitTaskId(null);
    }
    
    public TaskId runSync(final Runnable runnable) {
        if (this.isPluginEnabled()) {
            return new BukkitTaskId(this.getServer().getScheduler().runTask((Plugin)this, runnable).getTaskId());
        }
        this.queuedTasks.add(runnable);
        return new BukkitTaskId(null);
    }
    
    public TaskId runSync(final Runnable runnable, final Long ticks) {
        return new BukkitTaskId(this.getServer().getScheduler().runTaskLater((Plugin)this, runnable, (long)ticks).getTaskId());
    }
    
    public TaskId runRepeatingSync(final Runnable runnable, final Long ticks) {
        return new BukkitTaskId(this.getServer().getScheduler().runTaskTimer((Plugin)this, runnable, 0L, (long)ticks).getTaskId());
    }
    
    public void cancelTask(final TaskId taskId) {
        if (taskId == null) {
            return;
        }
        if (taskId.getObject() == null) {
            return;
        }
        if (taskId instanceof BukkitTaskId) {
            this.getServer().getScheduler().cancelTask((int)taskId.getObject());
        }
    }
    
    public ViaCommandSender[] getOnlinePlayers() {
        final ViaCommandSender[] array = new ViaCommandSender[Bukkit.getOnlinePlayers().size()];
        int i = 0;
        for (final Player player : Bukkit.getOnlinePlayers()) {
            array[i++] = new BukkitCommandSender((CommandSender)player);
        }
        return array;
    }
    
    public void sendMessage(final UUID uuid, final String message) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.sendMessage(message);
        }
    }
    
    public boolean kickPlayer(final UUID uuid, final String message) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.kickPlayer(message);
            return true;
        }
        return false;
    }
    
    public boolean isPluginEnabled() {
        return Bukkit.getPluginManager().getPlugin("ViaVersion").isEnabled();
    }
    
    public ConfigurationProvider getConfigurationProvider() {
        return this.conf;
    }
    
    public void onReload() {
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            this.getLogger().severe("ViaVersion is already loaded, we're going to kick all the players... because otherwise we'll crash because of ProtocolLib.");
            for (final Player player : Bukkit.getOnlinePlayers()) {
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&', this.conf.getReloadDisconnectMsg()));
            }
        }
        else {
            this.getLogger().severe("ViaVersion is already loaded, this should work fine. If you get any console errors, try rebooting.");
        }
    }
    
    public JsonObject getDump() {
        final JsonObject platformSpecific = new JsonObject();
        final List<PluginInfo> plugins = new ArrayList<PluginInfo>();
        for (final Plugin p : Bukkit.getPluginManager().getPlugins()) {
            plugins.add(new PluginInfo(p.isEnabled(), p.getDescription().getName(), p.getDescription().getVersion(), p.getDescription().getMain(), p.getDescription().getAuthors()));
        }
        platformSpecific.add("plugins", GsonUtil.getGson().toJsonTree(plugins));
        return platformSpecific;
    }
    
    public boolean isOldClientsAllowed() {
        return !this.protocolSupport;
    }
    
    public BukkitViaConfig getConf() {
        return this.conf;
    }
    
    public ViaAPI<Player> getApi() {
        return this.api;
    }
    
    public boolean isCompatSpigotBuild() {
        return this.compatSpigotBuild;
    }
    
    public boolean isSpigot() {
        return this.spigot;
    }
    
    public boolean isProtocolSupport() {
        return this.protocolSupport;
    }
    
    public static ViaVersionPlugin getInstance() {
        return ViaVersionPlugin.instance;
    }
    
    public ViaConnectionManager getConnectionManager() {
        return this.connectionManager;
    }
}
