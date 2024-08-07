// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.commands.defaultsubs;

import io.netty.util.ResourceLeakDetector;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.command.ViaSubCommand;

public class DisplayLeaksSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "displayleaks";
    }
    
    @Override
    public String description() {
        return "Try to hunt memory leaks!";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        if (ResourceLeakDetector.getLevel() != ResourceLeakDetector.Level.ADVANCED) {
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        }
        else {
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
        }
        this.sendMessage(sender, "&6Leak detector is now %s", (ResourceLeakDetector.getLevel() == ResourceLeakDetector.Level.ADVANCED) ? "&aenabled" : "&cdisabled");
        return true;
    }
}
