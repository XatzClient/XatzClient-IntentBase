// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.utils;

import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.PacketWrapper;

public interface ServerSender
{
    void sendToServer(final PacketWrapper p0, final Class<? extends Protocol> p1, final boolean p2, final boolean p3) throws Exception;
}
