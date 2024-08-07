// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.commands.defaultsubs;

import us.myles.ViaVersion.api.configuration.ConfigurationProvider;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.command.ViaSubCommand;

public class AutoTeamSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "autoteam";
    }
    
    @Override
    public String description() {
        return "Toggle automatically teaming to prevent colliding.";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        final ConfigurationProvider provider = Via.getPlatform().getConfigurationProvider();
        final boolean newValue = !Via.getConfig().isAutoTeam();
        provider.set("auto-team", newValue);
        provider.saveConfig();
        this.sendMessage(sender, "&6We will %s", newValue ? "&aautomatically team players" : "&cno longer auto team players");
        this.sendMessage(sender, "&6All players will need to re-login for the change to take place.", new Object[0]);
        return true;
    }
}
