// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.commands.defaultsubs;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.command.ViaSubCommand;

public class ReloadSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "reload";
    }
    
    @Override
    public String description() {
        return "Reload the config from the disk";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        Via.getPlatform().getConfigurationProvider().reloadConfig();
        this.sendMessage(sender, "&6Configuration successfully reloaded! Some features may need a restart.", new Object[0]);
        return true;
    }
}
