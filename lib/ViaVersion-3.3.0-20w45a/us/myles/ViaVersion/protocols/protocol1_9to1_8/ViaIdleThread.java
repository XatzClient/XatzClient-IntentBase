// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8;

import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import java.util.Iterator;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.MovementTracker;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.Via;

public class ViaIdleThread implements Runnable
{
    @Override
    public void run() {
        for (final UserConnection info : Via.getManager().getConnections()) {
            final ProtocolInfo protocolInfo = info.getProtocolInfo();
            if (protocolInfo != null) {
                if (!protocolInfo.getPipeline().contains(Protocol1_9To1_8.class)) {
                    continue;
                }
                final MovementTracker movementTracker = info.get(MovementTracker.class);
                if (movementTracker == null) {
                    continue;
                }
                final long nextIdleUpdate = movementTracker.getNextIdlePacket();
                if (nextIdleUpdate > System.currentTimeMillis() || !info.getChannel().isOpen()) {
                    continue;
                }
                Via.getManager().getProviders().get(MovementTransmitterProvider.class).sendPlayer(info);
            }
        }
    }
}
