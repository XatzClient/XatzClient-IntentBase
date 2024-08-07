// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion;

import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.ViaVersionConfig;
import java.util.Iterator;
import us.myles.ViaVersion.velocity.service.ProtocolDetectorService;
import us.myles.ViaVersion.util.GsonUtil;
import java.util.List;
import com.velocitypowered.api.plugin.PluginContainer;
import us.myles.ViaVersion.dump.PluginInfo;
import java.util.ArrayList;
import us.myles.viaversion.libs.gson.JsonObject;
import java.io.File;
import us.myles.ViaVersion.api.configuration.ConfigurationProvider;
import us.myles.viaversion.libs.bungeecordchat.chat.ComponentSerializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import net.kyori.text.serializer.gson.GsonComponentSerializer;
import java.util.UUID;
import java.util.function.Function;
import us.myles.ViaVersion.velocity.command.VelocityCommandSender;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.velocity.platform.VelocityTaskId;
import java.util.concurrent.TimeUnit;
import us.myles.ViaVersion.api.platform.TaskId;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.platform.ViaInjector;
import us.myles.ViaVersion.velocity.platform.VelocityViaInjector;
import us.myles.ViaVersion.api.platform.ViaPlatformLoader;
import us.myles.ViaVersion.velocity.platform.VelocityViaLoader;
import us.myles.ViaVersion.commands.ViaCommandHandler;
import us.myles.ViaVersion.velocity.util.LoggerWrapper;
import com.velocitypowered.api.command.Command;
import us.myles.ViaVersion.velocity.command.VelocityCommandHandler;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import us.myles.ViaVersion.api.platform.ViaConnectionManager;
import us.myles.ViaVersion.velocity.platform.VelocityViaConfig;
import us.myles.ViaVersion.velocity.platform.VelocityViaAPI;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import java.nio.file.Path;
import org.slf4j.Logger;
import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import us.myles.ViaVersion.api.platform.ViaPlatform;

@Plugin(id = "viaversion", name = "ViaVersion", version = "3.3.0-20w45a", authors = { "_MylesC", "creeper123123321", "Gerrygames", "KennyTV", "Matsv" }, description = "Allow newer Minecraft versions to connect to an older server version.", url = "https://viaversion.com")
public class VelocityPlugin implements ViaPlatform<Player>
{
    public static ProxyServer PROXY;
    @Inject
    private ProxyServer proxy;
    @Inject
    private Logger loggerslf4j;
    @Inject
    @DataDirectory
    private Path configDir;
    private VelocityViaAPI api;
    private java.util.logging.Logger logger;
    private VelocityViaConfig conf;
    private ViaConnectionManager connectionManager;
    
    @Subscribe
    public void onProxyInit(final ProxyInitializeEvent e) {
        VelocityPlugin.PROXY = this.proxy;
        final VelocityCommandHandler commandHandler = new VelocityCommandHandler();
        VelocityPlugin.PROXY.getCommandManager().register((Command)commandHandler, new String[] { "viaver", "vvvelocity", "viaversion" });
        this.api = new VelocityViaAPI();
        this.conf = new VelocityViaConfig(this.configDir.toFile());
        this.logger = new LoggerWrapper(this.loggerslf4j);
        this.connectionManager = new ViaConnectionManager();
        Via.init(ViaManager.builder().platform(this).commandHandler(commandHandler).loader(new VelocityViaLoader()).injector(new VelocityViaInjector()).build());
        if (this.proxy.getPluginManager().getPlugin("viabackwards").isPresent()) {
            MappingDataLoader.enableMappingsCache();
        }
    }
    
    @Subscribe(order = PostOrder.LAST)
    public void onProxyLateInit(final ProxyInitializeEvent e) {
        Via.getManager().init();
    }
    
    @Override
    public String getPlatformName() {
        final String proxyImpl = ProxyServer.class.getPackage().getImplementationTitle();
        return (proxyImpl != null) ? proxyImpl : "Velocity";
    }
    
    @Override
    public String getPlatformVersion() {
        final String version = ProxyServer.class.getPackage().getImplementationVersion();
        return (version != null) ? version : "Unknown";
    }
    
    @Override
    public boolean isProxy() {
        return true;
    }
    
    @Override
    public String getPluginVersion() {
        return "3.3.0-20w45a";
    }
    
    @Override
    public TaskId runAsync(final Runnable runnable) {
        return this.runSync(runnable);
    }
    
    @Override
    public TaskId runSync(final Runnable runnable) {
        return this.runSync(runnable, 0L);
    }
    
    @Override
    public TaskId runSync(final Runnable runnable, final Long ticks) {
        return new VelocityTaskId(VelocityPlugin.PROXY.getScheduler().buildTask((Object)this, runnable).delay(ticks * 50L, TimeUnit.MILLISECONDS).schedule());
    }
    
    @Override
    public TaskId runRepeatingSync(final Runnable runnable, final Long ticks) {
        return new VelocityTaskId(VelocityPlugin.PROXY.getScheduler().buildTask((Object)this, runnable).repeat(ticks * 50L, TimeUnit.MILLISECONDS).schedule());
    }
    
    @Override
    public void cancelTask(final TaskId taskId) {
        if (taskId instanceof VelocityTaskId) {
            ((VelocityTaskId)taskId).getObject().cancel();
        }
    }
    
    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return (ViaCommandSender[])VelocityPlugin.PROXY.getAllPlayers().stream().map(VelocityCommandSender::new).toArray(ViaCommandSender[]::new);
    }
    
    @Override
    public void sendMessage(final UUID uuid, final String message) {
        VelocityPlugin.PROXY.getPlayer(uuid).ifPresent(it -> it.sendMessage(GsonComponentSerializer.INSTANCE.deserialize(ComponentSerializer.toString(TextComponent.fromLegacyText(message)))));
    }
    
    @Override
    public boolean kickPlayer(final UUID uuid, final String message) {
        return VelocityPlugin.PROXY.getPlayer(uuid).map(it -> {
            it.disconnect(GsonComponentSerializer.INSTANCE.deserialize(ComponentSerializer.toString(TextComponent.fromLegacyText(message))));
            return true;
        }).orElse(false);
    }
    
    @Override
    public boolean isPluginEnabled() {
        return true;
    }
    
    @Override
    public ConfigurationProvider getConfigurationProvider() {
        return this.conf;
    }
    
    @Override
    public File getDataFolder() {
        return this.configDir.toFile();
    }
    
    @Override
    public VelocityViaAPI getApi() {
        return this.api;
    }
    
    @Override
    public VelocityViaConfig getConf() {
        return this.conf;
    }
    
    @Override
    public void onReload() {
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject extra = new JsonObject();
        final List<PluginInfo> plugins = new ArrayList<PluginInfo>();
        for (final PluginContainer p : VelocityPlugin.PROXY.getPluginManager().getPlugins()) {
            plugins.add(new PluginInfo(true, p.getDescription().getName().orElse(p.getDescription().getId()), p.getDescription().getVersion().orElse("Unknown Version"), p.getInstance().isPresent() ? p.getInstance().get().getClass().getCanonicalName() : "Unknown", p.getDescription().getAuthors()));
        }
        extra.add("plugins", GsonUtil.getGson().toJsonTree(plugins));
        extra.add("servers", GsonUtil.getGson().toJsonTree(ProtocolDetectorService.getDetectedIds()));
        return extra;
    }
    
    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }
    
    @Override
    public java.util.logging.Logger getLogger() {
        return this.logger;
    }
    
    @Override
    public ViaConnectionManager getConnectionManager() {
        return this.connectionManager;
    }
}
