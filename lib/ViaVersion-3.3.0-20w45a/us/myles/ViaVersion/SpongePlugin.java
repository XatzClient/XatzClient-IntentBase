// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion;

import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.ViaVersionConfig;
import us.myles.ViaVersion.util.GsonUtil;
import java.util.List;
import us.myles.ViaVersion.dump.PluginInfo;
import java.util.ArrayList;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.api.configuration.ConfigurationProvider;
import us.myles.viaversion.libs.bungeecordchat.chat.ComponentSerializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import org.spongepowered.api.text.serializer.TextSerializers;
import java.util.UUID;
import java.util.Iterator;
import org.spongepowered.api.command.CommandSource;
import us.myles.ViaVersion.sponge.commands.SpongeCommandSender;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.sponge.platform.SpongeTaskId;
import org.spongepowered.api.scheduler.Task;
import us.myles.ViaVersion.api.platform.TaskId;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.Listener;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.platform.ViaPlatformLoader;
import us.myles.ViaVersion.sponge.platform.SpongeViaLoader;
import us.myles.ViaVersion.api.platform.ViaInjector;
import us.myles.ViaVersion.sponge.platform.SpongeViaInjector;
import us.myles.ViaVersion.commands.ViaCommandHandler;
import org.spongepowered.api.command.CommandCallable;
import us.myles.ViaVersion.sponge.commands.SpongeCommandHandler;
import us.myles.ViaVersion.sponge.util.LoggerWrapper;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import java.util.logging.Logger;
import us.myles.ViaVersion.sponge.platform.SpongeViaConfig;
import us.myles.ViaVersion.sponge.platform.SpongeViaAPI;
import us.myles.ViaVersion.api.platform.ViaConnectionManager;
import org.spongepowered.api.config.DefaultConfig;
import java.io.File;
import org.spongepowered.api.plugin.PluginContainer;
import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.entity.living.player.Player;
import us.myles.ViaVersion.api.platform.ViaPlatform;

@Plugin(id = "viaversion", name = "ViaVersion", version = "3.3.0-20w45a", authors = { "_MylesC", "creeper123123321", "Gerrygames", "KennyTV", "Matsv" }, description = "Allow newer Minecraft versions to connect to an older server version.")
public class SpongePlugin implements ViaPlatform<Player>
{
    @Inject
    private Game game;
    @Inject
    private PluginContainer container;
    @Inject
    @DefaultConfig(sharedRoot = false)
    private File spongeConfig;
    private final ViaConnectionManager connectionManager;
    private final SpongeViaAPI api;
    private SpongeViaConfig conf;
    private Logger logger;
    
    public SpongePlugin() {
        this.connectionManager = new ViaConnectionManager();
        this.api = new SpongeViaAPI();
    }
    
    @Listener
    public void onGameStart(final GameInitializationEvent event) {
        this.logger = new LoggerWrapper(this.container.getLogger());
        this.conf = new SpongeViaConfig(this.container, this.spongeConfig.getParentFile());
        final SpongeCommandHandler commandHandler = new SpongeCommandHandler();
        this.game.getCommandManager().register((Object)this, (CommandCallable)commandHandler, new String[] { "viaversion", "viaver", "vvsponge" });
        this.logger.info("ViaVersion " + this.getPluginVersion() + " is now loaded!");
        Via.init(ViaManager.builder().platform(this).commandHandler(commandHandler).injector(new SpongeViaInjector()).loader(new SpongeViaLoader(this)).build());
    }
    
    @Listener
    public void onServerStart(final GameAboutToStartServerEvent event) {
        if (this.game.getPluginManager().getPlugin("viabackwards").isPresent()) {
            MappingDataLoader.enableMappingsCache();
        }
        this.logger.info("ViaVersion is injecting!");
        Via.getManager().init();
    }
    
    @Listener
    public void onServerStop(final GameStoppingServerEvent event) {
        Via.getManager().destroy();
    }
    
    @Override
    public String getPlatformName() {
        return this.game.getPlatform().getImplementation().getName();
    }
    
    @Override
    public String getPlatformVersion() {
        return this.game.getPlatform().getImplementation().getVersion().orElse("Unknown Version");
    }
    
    @Override
    public String getPluginVersion() {
        return this.container.getVersion().orElse("Unknown Version");
    }
    
    @Override
    public TaskId runAsync(final Runnable runnable) {
        return new SpongeTaskId(Task.builder().execute(runnable).async().submit((Object)this));
    }
    
    @Override
    public TaskId runSync(final Runnable runnable) {
        return new SpongeTaskId(Task.builder().execute(runnable).submit((Object)this));
    }
    
    @Override
    public TaskId runSync(final Runnable runnable, final Long ticks) {
        return new SpongeTaskId(Task.builder().execute(runnable).delayTicks((long)ticks).submit((Object)this));
    }
    
    @Override
    public TaskId runRepeatingSync(final Runnable runnable, final Long ticks) {
        return new SpongeTaskId(Task.builder().execute(runnable).intervalTicks((long)ticks).submit((Object)this));
    }
    
    @Override
    public void cancelTask(final TaskId taskId) {
        if (taskId == null) {
            return;
        }
        if (taskId.getObject() == null) {
            return;
        }
        if (taskId instanceof SpongeTaskId) {
            ((SpongeTaskId)taskId).getObject().cancel();
        }
    }
    
    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        final ViaCommandSender[] array = new ViaCommandSender[this.game.getServer().getOnlinePlayers().size()];
        int i = 0;
        for (final Player player : this.game.getServer().getOnlinePlayers()) {
            array[i++] = new SpongeCommandSender((CommandSource)player);
        }
        return array;
    }
    
    @Override
    public void sendMessage(final UUID uuid, final String message) {
        this.game.getServer().getPlayer(uuid).ifPresent(player -> player.sendMessage(TextSerializers.JSON.deserialize(ComponentSerializer.toString(TextComponent.fromLegacyText(message)))));
    }
    
    @Override
    public boolean kickPlayer(final UUID uuid, final String message) {
        return this.game.getServer().getPlayer(uuid).map(player -> {
            player.kick(TextSerializers.formattingCode('§').deserialize(message));
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
        return this.spongeConfig.getParentFile();
    }
    
    @Override
    public void onReload() {
        this.getLogger().severe("ViaVersion is already loaded, this should work fine. If you get any console errors, try rebooting.");
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject platformSpecific = new JsonObject();
        final List<PluginInfo> plugins = new ArrayList<PluginInfo>();
        for (final PluginContainer p : this.game.getPluginManager().getPlugins()) {
            plugins.add(new PluginInfo(true, p.getName(), p.getVersion().orElse("Unknown Version"), p.getInstance().isPresent() ? p.getInstance().get().getClass().getCanonicalName() : "Unknown", p.getAuthors()));
        }
        platformSpecific.add("plugins", GsonUtil.getGson().toJsonTree(plugins));
        return platformSpecific;
    }
    
    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }
    
    @Override
    public ViaConnectionManager getConnectionManager() {
        return this.connectionManager;
    }
    
    @Override
    public SpongeViaAPI getApi() {
        return this.api;
    }
    
    @Override
    public SpongeViaConfig getConf() {
        return this.conf;
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
}
