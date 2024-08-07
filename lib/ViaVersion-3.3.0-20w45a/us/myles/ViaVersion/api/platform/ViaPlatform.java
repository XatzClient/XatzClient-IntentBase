// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.platform;

import us.myles.viaversion.libs.gson.JsonObject;
import java.io.File;
import us.myles.ViaVersion.api.configuration.ConfigurationProvider;
import us.myles.ViaVersion.api.ViaVersionConfig;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.UUID;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import java.util.logging.Logger;

public interface ViaPlatform<T>
{
    Logger getLogger();
    
    String getPlatformName();
    
    String getPlatformVersion();
    
    default boolean isProxy() {
        return false;
    }
    
    String getPluginVersion();
    
    TaskId runAsync(final Runnable p0);
    
    TaskId runSync(final Runnable p0);
    
    TaskId runSync(final Runnable p0, final Long p1);
    
    TaskId runRepeatingSync(final Runnable p0, final Long p1);
    
    void cancelTask(final TaskId p0);
    
    ViaCommandSender[] getOnlinePlayers();
    
    void sendMessage(final UUID p0, final String p1);
    
    boolean kickPlayer(final UUID p0, final String p1);
    
    default boolean disconnect(final UserConnection connection, final String message) {
        if (connection.isClientSide()) {
            return false;
        }
        final UUID uuid = connection.get(ProtocolInfo.class).getUuid();
        return uuid != null && this.kickPlayer(uuid, message);
    }
    
    boolean isPluginEnabled();
    
    ViaAPI<T> getApi();
    
    ViaVersionConfig getConf();
    
    ConfigurationProvider getConfigurationProvider();
    
    File getDataFolder();
    
    void onReload();
    
    JsonObject getDump();
    
    boolean isOldClientsAllowed();
    
    ViaConnectionManager getConnectionManager();
}
