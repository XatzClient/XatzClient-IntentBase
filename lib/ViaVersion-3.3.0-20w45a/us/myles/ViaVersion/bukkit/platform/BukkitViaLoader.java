// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.platform;

import java.util.Iterator;
import org.bukkit.event.HandlerList;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
import us.myles.ViaVersion.bukkit.providers.BukkitBlockConnectionProvider;
import us.myles.ViaVersion.bukkit.providers.BukkitInventoryQuickMoveProvider;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.providers.InventoryQuickMoveProvider;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.HandItemProvider;
import us.myles.ViaVersion.bukkit.providers.BukkitViaMovementTransmitter;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import us.myles.ViaVersion.bukkit.providers.BukkitViaBulkChunkTranslator;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BulkChunkTranslatorProvider;
import us.myles.ViaVersion.bukkit.listeners.protocol1_9to1_8.PaperPatch;
import java.util.Locale;
import us.myles.ViaVersion.bukkit.listeners.protocol1_15to1_14_4.EntityToggleGlideListener;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.bukkit.listeners.multiversion.PlayerSneakListener;
import us.myles.ViaVersion.bukkit.listeners.protocol1_9to1_8.BlockListener;
import us.myles.ViaVersion.bukkit.listeners.protocol1_9to1_8.DeathListener;
import us.myles.ViaVersion.bukkit.listeners.protocol1_9to1_8.ArmorListener;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.bukkit.classgenerator.ClassGenerator;
import us.myles.ViaVersion.bukkit.listeners.UpdateListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import java.util.HashSet;
import us.myles.ViaVersion.bukkit.listeners.protocol1_9to1_8.HandItemCache;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.event.Listener;
import java.util.Set;
import us.myles.ViaVersion.ViaVersionPlugin;
import us.myles.ViaVersion.api.platform.ViaPlatformLoader;

public class BukkitViaLoader implements ViaPlatformLoader
{
    private final ViaVersionPlugin plugin;
    private final Set<Listener> listeners;
    private final Set<BukkitTask> tasks;
    private HandItemCache handItemCache;
    
    public BukkitViaLoader(final ViaVersionPlugin plugin) {
        this.listeners = new HashSet<Listener>();
        this.tasks = new HashSet<BukkitTask>();
        this.plugin = plugin;
    }
    
    public void registerListener(final Listener listener) {
        Bukkit.getPluginManager().registerEvents(this.storeListener(listener), (Plugin)this.plugin);
    }
    
    public <T extends Listener> T storeListener(final T listener) {
        this.listeners.add(listener);
        return listener;
    }
    
    @Override
    public void load() {
        this.registerListener((Listener)new UpdateListener());
        final ViaVersionPlugin plugin = (ViaVersionPlugin)Bukkit.getPluginManager().getPlugin("ViaVersion");
        ClassGenerator.registerPSConnectListener(plugin);
        if (ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_9.getVersion()) {
            this.storeListener(new ArmorListener((Plugin)plugin)).register();
            this.storeListener(new DeathListener((Plugin)plugin)).register();
            this.storeListener(new BlockListener((Plugin)plugin)).register();
            if (plugin.getConf().isItemCache()) {
                this.handItemCache = new HandItemCache();
                this.tasks.add(this.handItemCache.runTaskTimerAsynchronously((Plugin)plugin, 2L, 2L));
            }
        }
        Label_0241: {
            if (ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_14.getVersion()) {
                final boolean use1_9Fix = plugin.getConf().is1_9HitboxFix() && ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_9.getVersion();
                if (!use1_9Fix) {
                    if (!plugin.getConf().is1_14HitboxFix()) {
                        break Label_0241;
                    }
                }
                try {
                    this.storeListener(new PlayerSneakListener(plugin, use1_9Fix, plugin.getConf().is1_14HitboxFix())).register();
                }
                catch (ReflectiveOperationException e) {
                    Via.getPlatform().getLogger().warning("Could not load hitbox fix - please report this on our GitHub");
                    e.printStackTrace();
                }
            }
        }
        if (ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_15.getVersion()) {
            try {
                Class.forName("org.bukkit.event.entity.EntityToggleGlideEvent");
                this.storeListener(new EntityToggleGlideListener(plugin)).register();
            }
            catch (ClassNotFoundException ex) {}
        }
        if ((Bukkit.getVersion().toLowerCase(Locale.ROOT).contains("paper") || Bukkit.getVersion().toLowerCase(Locale.ROOT).contains("taco") || Bukkit.getVersion().toLowerCase(Locale.ROOT).contains("torch")) && ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_12.getVersion()) {
            plugin.getLogger().info("Enabling Paper/TacoSpigot/Torch patch: Fixes block placement.");
            this.storeListener(new PaperPatch((Plugin)plugin)).register();
        }
        if (ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_9.getVersion()) {
            Via.getManager().getProviders().use((Class<BukkitViaBulkChunkTranslator>)BulkChunkTranslatorProvider.class, new BukkitViaBulkChunkTranslator());
            Via.getManager().getProviders().use((Class<BukkitViaMovementTransmitter>)MovementTransmitterProvider.class, new BukkitViaMovementTransmitter());
            Via.getManager().getProviders().use((Class<BukkitViaLoader$1>)HandItemProvider.class, new HandItemProvider() {
                @Override
                public Item getHandItem(final UserConnection info) {
                    if (BukkitViaLoader.this.handItemCache != null) {
                        return BukkitViaLoader.this.handItemCache.getHandItem(info.getProtocolInfo().getUuid());
                    }
                    try {
                        final UUID playerUUID;
                        final Player player;
                        return Bukkit.getScheduler().callSyncMethod(Bukkit.getPluginManager().getPlugin("ViaVersion"), () -> {
                            playerUUID = info.getProtocolInfo().getUuid();
                            player = Bukkit.getPlayer(playerUUID);
                            if (player != null) {
                                return HandItemCache.convert(player.getItemInHand());
                            }
                            else {
                                return null;
                            }
                        }).get(10L, TimeUnit.SECONDS);
                    }
                    catch (Exception e) {
                        Via.getPlatform().getLogger().severe("Error fetching hand item: " + e.getClass().getName());
                        if (Via.getManager().isDebug()) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
            });
        }
        if (ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_12.getVersion() && plugin.getConf().is1_12QuickMoveActionFix()) {
            Via.getManager().getProviders().use((Class<BukkitInventoryQuickMoveProvider>)InventoryQuickMoveProvider.class, new BukkitInventoryQuickMoveProvider());
        }
        if (ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_13.getVersion() && Via.getConfig().getBlockConnectionMethod().equalsIgnoreCase("world")) {
            final BukkitBlockConnectionProvider blockConnectionProvider = new BukkitBlockConnectionProvider();
            Via.getManager().getProviders().use(BlockConnectionProvider.class, blockConnectionProvider);
            ConnectionData.blockConnectionProvider = blockConnectionProvider;
        }
    }
    
    @Override
    public void unload() {
        for (final Listener listener : this.listeners) {
            HandlerList.unregisterAll(listener);
        }
        this.listeners.clear();
        for (final BukkitTask task : this.tasks) {
            task.cancel();
        }
        this.tasks.clear();
    }
}
