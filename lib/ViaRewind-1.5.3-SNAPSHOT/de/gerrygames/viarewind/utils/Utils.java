// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.utils;

import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import java.util.UUID;
import us.myles.ViaVersion.api.data.UserConnection;

public class Utils
{
    public static UUID getUUID(final UserConnection user) {
        return ((ProtocolInfo)user.get((Class)ProtocolInfo.class)).getUuid();
    }
}
