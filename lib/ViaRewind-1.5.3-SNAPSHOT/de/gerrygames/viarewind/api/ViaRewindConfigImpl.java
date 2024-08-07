// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.api;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.net.URL;
import java.io.File;
import us.myles.ViaVersion.util.Config;

public class ViaRewindConfigImpl extends Config implements ViaRewindConfig
{
    public ViaRewindConfigImpl(final File configFile) {
        super(configFile);
        this.reloadConfig();
    }
    
    public CooldownIndicator getCooldownIndicator() {
        return CooldownIndicator.valueOf(this.getString("cooldown-indicator", "TITLE").toUpperCase());
    }
    
    public boolean isReplaceAdventureMode() {
        return this.getBoolean("replace-adventure", false);
    }
    
    public boolean isReplaceParticles() {
        return this.getBoolean("replace-particles", false);
    }
    
    public URL getDefaultConfigURL() {
        return this.getClass().getClassLoader().getResource("assets/viarewind/config.yml");
    }
    
    protected void handleConfig(final Map<String, Object> map) {
    }
    
    public List<String> getUnsupportedOptions() {
        return Collections.emptyList();
    }
}
