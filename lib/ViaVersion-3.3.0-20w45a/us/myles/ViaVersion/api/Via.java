// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api;

import com.google.common.base.Preconditions;
import us.myles.ViaVersion.ViaManager;
import us.myles.ViaVersion.api.platform.ViaPlatform;

public class Via
{
    private static ViaPlatform platform;
    private static ViaManager manager;
    
    public static void init(final ViaManager viaManager) {
        Preconditions.checkArgument(Via.manager == null, (Object)"ViaManager is already set");
        Via.platform = viaManager.getPlatform();
        Via.manager = viaManager;
    }
    
    public static ViaAPI getAPI() {
        Preconditions.checkArgument(Via.platform != null, (Object)"ViaVersion has not loaded the Platform");
        return Via.platform.getApi();
    }
    
    public static ViaVersionConfig getConfig() {
        Preconditions.checkArgument(Via.platform != null, (Object)"ViaVersion has not loaded the Platform");
        return Via.platform.getConf();
    }
    
    public static ViaPlatform getPlatform() {
        return Via.platform;
    }
    
    public static ViaManager getManager() {
        return Via.manager;
    }
}
