// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.commands.defaultsubs;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.command.ViaSubCommand;

public class HelpSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "help";
    }
    
    @Override
    public String description() {
        return "You are looking at it right now!";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        Via.getManager().getCommandHandler().showHelp(sender);
        return true;
    }
}
