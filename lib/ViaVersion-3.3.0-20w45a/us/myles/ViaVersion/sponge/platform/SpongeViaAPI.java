// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.platform;

import java.util.Collection;
import java.util.TreeSet;
import java.util.SortedSet;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.data.UserConnection;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import java.util.UUID;
import org.spongepowered.api.entity.living.player.Player;
import us.myles.ViaVersion.api.ViaAPI;

public class SpongeViaAPI implements ViaAPI<Player>
{
    @Override
    public int getPlayerVersion(final Player player) {
        return this.getPlayerVersion(player.getUniqueId());
    }
    
    @Override
    public int getPlayerVersion(final UUID uuid) {
        if (!this.isInjected(uuid)) {
            return ProtocolRegistry.SERVER_PROTOCOL;
        }
        return Via.getManager().getConnection(uuid).getProtocolInfo().getProtocolVersion();
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
        return new SpongeBossBar(title, 1.0f, color, style);
    }
    
    @Override
    public BossBar createBossBar(final String title, final float health, final BossColor color, final BossStyle style) {
        return new SpongeBossBar(title, health, color, style);
    }
    
    @Override
    public SortedSet<Integer> getSupportedVersions() {
        final SortedSet<Integer> outputSet = new TreeSet<Integer>(ProtocolRegistry.getSupportedVersions());
        outputSet.removeAll(Via.getPlatform().getConf().getBlockedProtocols());
        return outputSet;
    }
}
