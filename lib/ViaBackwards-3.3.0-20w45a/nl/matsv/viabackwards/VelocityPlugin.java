// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards;

import java.io.File;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.sponge.util.LoggerWrapper;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import java.nio.file.Path;
import com.google.inject.Inject;
import java.util.logging.Logger;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;

@Plugin(id = "viabackwards", name = "ViaBackwards", version = "3.3.0-20w45a", authors = { "Matsv", "KennyTV", "Gerrygames", "creeper123123321", "ForceUpdate1" }, description = "Allow older Minecraft versions to connect to a newer server version.", dependencies = { @Dependency(id = "viaversion") })
public class VelocityPlugin implements ViaBackwardsPlatform
{
    private Logger logger;
    @Inject
    private org.slf4j.Logger loggerSlf4j;
    @Inject
    @DataDirectory
    private Path configPath;
    
    @Subscribe(order = PostOrder.LATE)
    public void onProxyStart(final ProxyInitializeEvent e) {
        this.logger = (Logger)new LoggerWrapper(this.loggerSlf4j);
        Via.getManager().addEnableListener(() -> this.init(this.configPath.resolve("config.yml").toFile()));
    }
    
    @Override
    public void disable() {
    }
    
    @Override
    public File getDataFolder() {
        return this.configPath.toFile();
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
}
