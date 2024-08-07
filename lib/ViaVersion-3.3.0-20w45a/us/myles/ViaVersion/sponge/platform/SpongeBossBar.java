// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.platform;

import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import org.spongepowered.api.entity.living.player.Player;
import us.myles.ViaVersion.boss.CommonBoss;

public class SpongeBossBar extends CommonBoss<Player>
{
    public SpongeBossBar(final String title, final float health, final BossColor color, final BossStyle style) {
        super(title, health, color, style);
    }
    
    @Override
    public BossBar addPlayer(final Player player) {
        this.addPlayer(player.getUniqueId());
        return this;
    }
    
    @Override
    public BossBar addPlayers(final Player... players) {
        for (final Player p : players) {
            this.addPlayer(p);
        }
        return this;
    }
    
    @Override
    public BossBar removePlayer(final Player player) {
        this.removePlayer(player.getUniqueId());
        return this;
    }
}
