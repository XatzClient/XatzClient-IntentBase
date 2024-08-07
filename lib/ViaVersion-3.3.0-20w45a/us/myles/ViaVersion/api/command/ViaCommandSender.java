// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.command;

import java.util.UUID;

public interface ViaCommandSender
{
    boolean hasPermission(final String p0);
    
    void sendMessage(final String p0);
    
    UUID getUUID();
    
    String getName();
}
