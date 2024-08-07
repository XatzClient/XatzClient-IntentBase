// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.platform;

import us.myles.ViaVersion.bungee.service.ProtocolDetectorService;
import net.md_5.bungee.api.config.ServerInfo;
import java.util.Collection;
import java.util.TreeSet;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import java.util.SortedSet;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.ProxyServer;
import java.util.UUID;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.Via;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.myles.ViaVersion.api.ViaAPI;

public class BungeeViaAPI implements ViaAPI<ProxiedPlayer>
{
    @Override
    public int getPlayerVersion(final ProxiedPlayer player) {
        final UserConnection conn = Via.getManager().getConnection(player.getUniqueId());
        if (conn == null) {
            return player.getPendingConnection().getVersion();
        }
        return conn.getProtocolInfo().getProtocolVersion();
    }
    
    @Override
    public int getPlayerVersion(final UUID uuid) {
        return this.getPlayerVersion(ProxyServer.getInstance().getPlayer(uuid));
    }
    
    @Override
    public boolean isInjected(final UUID playerUUID) {
        return Via.getManager().isClientConnected(playerUUID);
    }
    
    @Override
    public String getVersion() {
        return Via.getPlatform().getPluginVersion();
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
    public void sendRawPacket(final ProxiedPlayer player, final ByteBuf packet) throws IllegalArgumentException {
        this.sendRawPacket(player.getUniqueId(), packet);
    }
    
    @Override
    public BossBar createBossBar(final String title, final BossColor color, final BossStyle style) {
        return new BungeeBossBar(title, 1.0f, color, style);
    }
    
    @Override
    public BossBar createBossBar(final String title, final float health, final BossColor color, final BossStyle style) {
        return new BungeeBossBar(title, health, color, style);
    }
    
    @Override
    public SortedSet<Integer> getSupportedVersions() {
        final SortedSet<Integer> outputSet = new TreeSet<Integer>(ProtocolRegistry.getSupportedVersions());
        outputSet.removeAll(Via.getPlatform().getConf().getBlockedProtocols());
        return outputSet;
    }
    
    public void probeServer(final ServerInfo serverInfo) {
        ProtocolDetectorService.probeServer(serverInfo);
    }
}
