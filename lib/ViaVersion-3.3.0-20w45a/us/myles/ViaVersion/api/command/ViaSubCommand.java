// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.command;

import us.myles.ViaVersion.commands.ViaCommandHandler;
import java.util.Collections;
import java.util.List;

public abstract class ViaSubCommand
{
    public abstract String name();
    
    public abstract String description();
    
    public String usage() {
        return this.name();
    }
    
    public String permission() {
        return "viaversion.admin";
    }
    
    public abstract boolean execute(final ViaCommandSender p0, final String[] p1);
    
    public List<String> onTabComplete(final ViaCommandSender sender, final String[] args) {
        return Collections.emptyList();
    }
    
    public String color(final String s) {
        return ViaCommandHandler.color(s);
    }
    
    public void sendMessage(final ViaCommandSender sender, final String message, final Object... args) {
        ViaCommandHandler.sendMessage(sender, message, args);
    }
}
