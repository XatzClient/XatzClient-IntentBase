// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.command;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public interface ViaVersionCommand
{
    void registerSubCommand(final ViaSubCommand p0) throws Exception;
    
    boolean hasSubCommand(final String p0);
    
    @Nullable
    ViaSubCommand getSubCommand(final String p0);
    
    boolean onCommand(final ViaCommandSender p0, final String[] p1);
    
    List<String> onTabComplete(final ViaCommandSender p0, final String[] p1);
}
