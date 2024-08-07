// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.commands.defaultsubs;

import us.myles.ViaVersion.api.configuration.ConfigurationProvider;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.command.ViaSubCommand;

public class DontBugMeSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "dontbugme";
    }
    
    @Override
    public String description() {
        return "Toggle checking for updates";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        final ConfigurationProvider provider = Via.getPlatform().getConfigurationProvider();
        final boolean newValue = !Via.getConfig().isCheckForUpdates();
        Via.getConfig().setCheckForUpdates(newValue);
        provider.saveConfig();
        this.sendMessage(sender, "&6We will %snotify you about updates.", newValue ? "&a" : "&cnot ");
        return true;
    }
}
