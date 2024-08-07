// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards;

import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import nl.matsv.viabackwards.fabric.util.LoggerWrapper;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.util.logging.Logger;
import nl.matsv.viabackwards.api.ViaBackwardsPlatform;

public class ViaFabricAddon implements ViaBackwardsPlatform, Runnable
{
    private final Logger logger;
    private File configDir;
    
    public ViaFabricAddon() {
        this.logger = new LoggerWrapper(LogManager.getLogger("ViaBackwards"));
    }
    
    @Override
    public void run() {
        final Path configDirPath = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("ViaBackwards");
        this.configDir = configDirPath.toFile();
        this.init(configDirPath.resolve("config.yml").toFile());
    }
    
    @Override
    public void disable() {
    }
    
    @Override
    public File getDataFolder() {
        return this.configDir;
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
}
