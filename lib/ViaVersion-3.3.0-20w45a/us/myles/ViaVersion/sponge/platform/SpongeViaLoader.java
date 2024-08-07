// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.platform;

import java.util.function.Consumer;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.HandItemProvider;
import us.myles.ViaVersion.sponge.providers.SpongeViaMovementTransmitter;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import us.myles.ViaVersion.sponge.providers.SpongeViaBulkChunkTranslator;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BulkChunkTranslatorProvider;
import us.myles.ViaVersion.sponge.listeners.protocol1_9to1_8.HandItemCache;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.sponge.listeners.protocol1_9to1_8.BlockListener;
import us.myles.ViaVersion.sponge.listeners.protocol1_9to1_8.DeathListener;
import us.myles.ViaVersion.sponge.listeners.protocol1_9to1_8.sponge5.Sponge5ArmorListener;
import us.myles.ViaVersion.sponge.listeners.protocol1_9to1_8.sponge4.Sponge4ArmorListener;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.sponge.listeners.UpdateListener;
import org.spongepowered.api.Sponge;
import java.util.HashSet;
import us.myles.ViaVersion.api.platform.TaskId;
import java.util.Set;
import us.myles.ViaVersion.SpongePlugin;
import us.myles.ViaVersion.api.platform.ViaPlatformLoader;

public class SpongeViaLoader implements ViaPlatformLoader
{
    private final SpongePlugin plugin;
    private final Set<Object> listeners;
    private final Set<TaskId> tasks;
    
    public SpongeViaLoader(final SpongePlugin plugin) {
        this.listeners = new HashSet<Object>();
        this.tasks = new HashSet<TaskId>();
        this.plugin = plugin;
    }
    
    private void registerListener(final Object listener) {
        Sponge.getEventManager().registerListeners((Object)this.plugin, this.storeListener(listener));
    }
    
    private <T> T storeListener(final T listener) {
        this.listeners.add(listener);
        return listener;
    }
    
    @Override
    public void load() {
        this.registerListener(new UpdateListener());
        if (ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_9.getVersion()) {
            try {
                Class.forName("org.spongepowered.api.event.entity.DisplaceEntityEvent");
                this.storeListener(new Sponge4ArmorListener()).register();
            }
            catch (ClassNotFoundException e) {
                this.storeListener(new Sponge5ArmorListener(this.plugin)).register();
            }
            this.storeListener(new DeathListener(this.plugin)).register();
            this.storeListener(new BlockListener(this.plugin)).register();
            if (this.plugin.getConf().isItemCache()) {
                this.tasks.add(Via.getPlatform().runRepeatingSync(new HandItemCache(), 2L));
                HandItemCache.CACHE = true;
            }
        }
        if (ProtocolRegistry.SERVER_PROTOCOL < ProtocolVersion.v1_9.getVersion()) {
            Via.getManager().getProviders().use((Class<SpongeViaBulkChunkTranslator>)BulkChunkTranslatorProvider.class, new SpongeViaBulkChunkTranslator());
            Via.getManager().getProviders().use((Class<SpongeViaMovementTransmitter>)MovementTransmitterProvider.class, new SpongeViaMovementTransmitter());
            Via.getManager().getProviders().use((Class<SpongeViaLoader$1>)HandItemProvider.class, new HandItemProvider() {
                @Override
                public Item getHandItem(final UserConnection info) {
                    if (HandItemCache.CACHE) {
                        return HandItemCache.getHandItem(info.getProtocolInfo().getUuid());
                    }
                    return super.getHandItem(info);
                }
            });
        }
    }
    
    @Override
    public void unload() {
        this.listeners.forEach(Sponge.getEventManager()::unregisterListeners);
        this.listeners.clear();
        this.tasks.forEach(Via.getPlatform()::cancelTask);
        this.tasks.clear();
    }
}
