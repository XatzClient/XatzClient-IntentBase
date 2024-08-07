// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.boss;

import java.util.Set;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.UUID;
import us.myles.ViaVersion.api.Via;

public abstract class BossBar<T>
{
    public abstract String getTitle();
    
    public abstract BossBar setTitle(final String p0);
    
    public abstract float getHealth();
    
    public abstract BossBar setHealth(final float p0);
    
    public abstract BossColor getColor();
    
    public abstract BossBar setColor(final BossColor p0);
    
    public abstract BossStyle getStyle();
    
    public abstract BossBar setStyle(final BossStyle p0);
    
    @Deprecated
    public BossBar addPlayer(final T player) {
        throw new UnsupportedOperationException("This method is not implemented for the platform " + Via.getPlatform().getPlatformName());
    }
    
    public abstract BossBar addPlayer(final UUID p0);
    
    public abstract BossBar addConnection(final UserConnection p0);
    
    @Deprecated
    public BossBar addPlayers(final T... players) {
        throw new UnsupportedOperationException("This method is not implemented for the platform " + Via.getPlatform().getPlatformName());
    }
    
    @Deprecated
    public BossBar removePlayer(final T player) {
        throw new UnsupportedOperationException("This method is not implemented for the platform " + Via.getPlatform().getPlatformName());
    }
    
    public abstract BossBar removePlayer(final UUID p0);
    
    public abstract BossBar removeConnection(final UserConnection p0);
    
    public abstract BossBar addFlag(final BossFlag p0);
    
    public abstract BossBar removeFlag(final BossFlag p0);
    
    public abstract boolean hasFlag(final BossFlag p0);
    
    public abstract Set<UUID> getPlayers();
    
    public abstract Set<UserConnection> getConnections();
    
    public abstract BossBar show();
    
    public abstract BossBar hide();
    
    public abstract boolean isVisible();
    
    public abstract UUID getId();
}
