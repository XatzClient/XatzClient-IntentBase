// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.platform;

import java.util.Collection;
import java.util.TreeSet;
import java.util.SortedSet;
import us.myles.ViaVersion.boss.ViaBossBar;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.data.UserConnection;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.bukkit.util.ProtocolSupportUtil;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.api.Via;
import org.bukkit.Bukkit;
import java.util.UUID;
import us.myles.ViaVersion.ViaVersionPlugin;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.ViaAPI;

public class BukkitViaAPI implements ViaAPI<Player>
{
    private final ViaVersionPlugin plugin;
    
    public BukkitViaAPI(final ViaVersionPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public int getPlayerVersion(final Player player) {
        return this.getPlayerVersion(player.getUniqueId());
    }
    
    @Override
    public int getPlayerVersion(final UUID uuid) {
        if (!this.isInjected(uuid)) {
            return this.getExternalVersion(Bukkit.getPlayer(uuid));
        }
        return Via.getManager().getConnection(uuid).getProtocolInfo().getProtocolVersion();
    }
    
    private int getExternalVersion(final Player player) {
        if (!this.isProtocolSupport()) {
            return ProtocolRegistry.SERVER_PROTOCOL;
        }
        return ProtocolSupportUtil.getProtocolVersion(player);
    }
    
    @Override
    public boolean isInjected(final UUID playerUUID) {
        return Via.getManager().isClientConnected(playerUUID);
    }
    
    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }
    
    @Override
    public void sendRawPacket(final UUID uuid, final ByteBuf packet) throws IllegalArgumentException {
        if (!this.isInjected(uuid)) {
            throw new IllegalArgumentException("This player is not controlled by ViaVersion!");
        }
        final UserConnection ci = Via.getManager().getConnection(uuid);
        ci.sendRawPacket(packet);
    }
    
    @Override
    public void sendRawPacket(final Player player, final ByteBuf packet) throws IllegalArgumentException {
        this.sendRawPacket(player.getUniqueId(), packet);
    }
    
    @Override
    public BossBar<Player> createBossBar(final String title, final BossColor color, final BossStyle style) {
        return new ViaBossBar(title, 1.0f, color, style);
    }
    
    @Override
    public BossBar<Player> createBossBar(final String title, final float health, final BossColor color, final BossStyle style) {
        return new ViaBossBar(title, health, color, style);
    }
    
    @Override
    public SortedSet<Integer> getSupportedVersions() {
        final SortedSet<Integer> outputSet = new TreeSet<Integer>(ProtocolRegistry.getSupportedVersions());
        outputSet.removeAll(Via.getPlatform().getConf().getBlockedProtocols());
        return outputSet;
    }
    
    public boolean isCompatSpigotBuild() {
        return this.plugin.isCompatSpigotBuild();
    }
    
    public boolean isProtocolSupport() {
        return this.plugin.isProtocolSupport();
    }
}
