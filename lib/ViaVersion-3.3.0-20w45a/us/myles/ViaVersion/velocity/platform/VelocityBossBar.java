// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.platform;

import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import com.velocitypowered.api.proxy.Player;
import us.myles.ViaVersion.boss.CommonBoss;

public class VelocityBossBar extends CommonBoss<Player>
{
    public VelocityBossBar(final String title, final float health, final BossColor color, final BossStyle style) {
        super(title, health, color, style);
    }
}
