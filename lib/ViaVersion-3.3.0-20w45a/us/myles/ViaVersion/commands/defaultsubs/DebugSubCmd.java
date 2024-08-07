// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.commands.defaultsubs;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.command.ViaSubCommand;

public class DebugSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "debug";
    }
    
    @Override
    public String description() {
        return "Toggle debug mode";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        Via.getManager().setDebug(!Via.getManager().isDebug());
        this.sendMessage(sender, "&6Debug mode is now %s", Via.getManager().isDebug() ? "&aenabled" : "&cdisabled");
        return true;
    }
}
