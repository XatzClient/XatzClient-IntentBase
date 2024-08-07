// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards;

import us.myles.ViaVersion.api.Via;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin implements ViaBackwardsPlatform
{
    public void onLoad() {
        Via.getManager().addEnableListener(() -> this.init(this.getDataFolder()));
    }
    
    public void disable() {
    }
}
