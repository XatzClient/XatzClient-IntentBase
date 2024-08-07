// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.base;

import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.platform.providers.Provider;

public class VersionProvider implements Provider
{
    public int getServerProtocol(final UserConnection connection) throws Exception {
        return ProtocolRegistry.SERVER_PROTOCOL;
    }
}
