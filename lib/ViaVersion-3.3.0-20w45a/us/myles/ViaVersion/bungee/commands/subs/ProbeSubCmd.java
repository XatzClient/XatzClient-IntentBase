// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.commands.subs;

import us.myles.ViaVersion.bungee.service.ProtocolDetectorService;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.bungee.platform.BungeeViaConfig;
import us.myles.ViaVersion.api.command.ViaSubCommand;

public class ProbeSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "probe";
    }
    
    @Override
    public String description() {
        return "Forces ViaVersion to scan server protocol versions " + ((((BungeeViaConfig)Via.getConfig()).getBungeePingInterval() == -1) ? "" : "(Also happens at an interval)");
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        ProtocolDetectorService.getInstance().run();
        this.sendMessage(sender, "&6Started searching for protocol versions", new Object[0]);
        return true;
    }
}
