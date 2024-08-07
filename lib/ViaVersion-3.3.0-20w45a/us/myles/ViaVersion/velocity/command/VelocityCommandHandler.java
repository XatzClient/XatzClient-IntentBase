// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.command;

import java.util.List;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import com.velocitypowered.api.command.CommandSource;
import us.myles.ViaVersion.api.command.ViaSubCommand;
import us.myles.ViaVersion.velocity.command.subs.ProbeSubCmd;
import com.velocitypowered.api.command.Command;
import us.myles.ViaVersion.commands.ViaCommandHandler;

public class VelocityCommandHandler extends ViaCommandHandler implements Command
{
    public VelocityCommandHandler() {
        try {
            this.registerSubCommand(new ProbeSubCmd());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void execute(final CommandSource source, final String[] args) {
        this.onCommand(new VelocityCommandSender(source), args);
    }
    
    public List<String> suggest(final CommandSource source, final String[] currentArgs) {
        return this.onTabComplete(new VelocityCommandSender(source), currentArgs);
    }
}
