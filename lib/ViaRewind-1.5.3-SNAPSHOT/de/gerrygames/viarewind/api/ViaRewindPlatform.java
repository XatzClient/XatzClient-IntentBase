// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.api;

import java.util.logging.Logger;
import de.gerrygames.viarewind.protocol.protocol1_7_0_5to1_7_6_10.Protocol1_7_0_5to1_7_6_10;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import java.util.List;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import java.util.Collections;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import us.myles.ViaVersion.api.Via;
import de.gerrygames.viarewind.ViaRewind;

public interface ViaRewindPlatform
{
    default void init(final ViaRewindConfig config) {
        ViaRewind.init(this, config);
        final String version = ViaRewind.class.getPackage().getImplementationVersion();
        Via.getManager().getSubPlatforms().add((version != null) ? version : "UNKNOWN");
        ProtocolRegistry.registerProtocol((Protocol)new Protocol1_8TO1_9(), (List)Collections.singletonList(ProtocolVersion.v1_8.getId()), ProtocolVersion.v1_9.getId());
        ProtocolRegistry.registerProtocol((Protocol)new Protocol1_7_6_10TO1_8(), (List)Collections.singletonList(ProtocolVersion.v1_7_6.getId()), ProtocolVersion.v1_8.getId());
        ProtocolRegistry.registerProtocol((Protocol)new Protocol1_7_0_5to1_7_6_10(), (List)Collections.singletonList(ProtocolVersion.v1_7_1.getId()), ProtocolVersion.v1_7_6.getId());
    }
    
    Logger getLogger();
}
