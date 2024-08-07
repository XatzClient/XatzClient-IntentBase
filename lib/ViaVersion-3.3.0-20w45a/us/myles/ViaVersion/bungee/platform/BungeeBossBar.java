// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.platform;

import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.myles.ViaVersion.boss.CommonBoss;

public class BungeeBossBar extends CommonBoss<ProxiedPlayer>
{
    public BungeeBossBar(final String title, final float health, final BossColor color, final BossStyle style) {
        super(title, health, color, style);
    }
    
    @Override
    public BossBar addPlayer(final ProxiedPlayer player) {
        this.addPlayer(player.getUniqueId());
        return this;
    }
    
    @Override
    public BossBar addPlayers(final ProxiedPlayer... players) {
        for (final ProxiedPlayer p : players) {
            this.addPlayer(p);
        }
        return this;
    }
    
    @Override
    public BossBar removePlayer(final ProxiedPlayer player) {
        this.removePlayer(player.getUniqueId());
        return this;
    }
}
