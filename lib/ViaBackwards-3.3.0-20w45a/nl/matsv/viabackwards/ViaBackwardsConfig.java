// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.net.URL;
import java.io.File;
import us.myles.ViaVersion.util.Config;

public class ViaBackwardsConfig extends Config implements nl.matsv.viabackwards.api.ViaBackwardsConfig
{
    private boolean addCustomEnchantsToLore;
    private boolean addTeamColorToPrefix;
    private boolean fix1_13FacePlayer;
    private boolean alwaysShowOriginalMobName;
    
    public ViaBackwardsConfig(final File configFile) {
        super(configFile);
    }
    
    public void reloadConfig() {
        super.reloadConfig();
        this.loadFields();
    }
    
    private void loadFields() {
        this.addCustomEnchantsToLore = this.getBoolean("add-custom-enchants-into-lore", true);
        this.addTeamColorToPrefix = this.getBoolean("add-teamcolor-to-prefix", true);
        this.fix1_13FacePlayer = this.getBoolean("fix-1_13-face-player", false);
        this.alwaysShowOriginalMobName = this.getBoolean("always-show-original-mob-name", true);
    }
    
    public boolean addCustomEnchantsToLore() {
        return this.addCustomEnchantsToLore;
    }
    
    public boolean addTeamColorTo1_13Prefix() {
        return this.addTeamColorToPrefix;
    }
    
    public boolean isFix1_13FacePlayer() {
        return this.fix1_13FacePlayer;
    }
    
    public boolean alwaysShowOriginalMobName() {
        return this.alwaysShowOriginalMobName;
    }
    
    public URL getDefaultConfigURL() {
        return this.getClass().getClassLoader().getResource("assets/viabackwards/config.yml");
    }
    
    protected void handleConfig(final Map<String, Object> map) {
    }
    
    public List<String> getUnsupportedOptions() {
        return Collections.emptyList();
    }
}
