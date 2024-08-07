// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.platform;

import java.util.Collection;
import java.util.TreeSet;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import java.util.SortedSet;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.data.UserConnection;
import io.netty.buffer.ByteBuf;
import java.util.function.Supplier;
import java.util.NoSuchElementException;
import us.myles.ViaVersion.VelocityPlugin;
import java.util.UUID;
import us.myles.ViaVersion.api.Via;
import com.velocitypowered.api.proxy.Player;
import us.myles.ViaVersion.api.ViaAPI;

public class VelocityViaAPI implements ViaAPI<Player>
{
    @Override
    public int getPlayerVersion(final Player player) {
        if (!this.isInjected(player.getUniqueId())) {
            return player.getProtocolVersion().getProtocol();
        }
        return Via.getManager().getConnection(player.getUniqueId()).getProtocolInfo().getProtocolVersion();
    }
    
    @Override
    public int getPlayerVersion(final UUID uuid) {
        return this.getPlayerVersion((Player)VelocityPlugin.PROXY.getPlayer(uuid).orElseThrow(NoSuchElementException::new));
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
    public void sendRawPacket(final Player player, final ByteBuf packet) throws IllegalArgumentException {
        this.sendRawPacket(player.getUniqueId(), packet);
    }
    
    @Override
    public BossBar createBossBar(final String title, final BossColor color, final BossStyle style) {
        return new VelocityBossBar(title, 1.0f, color, style);
    }
    
    @Override
    public BossBar createBossBar(final String title, final float health, final BossColor color, final BossStyle style) {
        return new VelocityBossBar(title, health, color, style);
    }
    
    @Override
    public SortedSet<Integer> getSupportedVersions() {
        final SortedSet<Integer> outputSet = new TreeSet<Integer>(ProtocolRegistry.getSupportedVersions());
        outputSet.removeAll(Via.getPlatform().getConf().getBlockedProtocols());
        return outputSet;
    }
}
