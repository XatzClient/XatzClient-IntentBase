// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2;

import java.util.Iterator;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage.TabCompleteTracker;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.Via;

public class TabCompleteThread implements Runnable
{
    @Override
    public void run() {
        for (final UserConnection info : Via.getManager().getConnections()) {
            if (info.getProtocolInfo() == null) {
                continue;
            }
            if (!info.getProtocolInfo().getPipeline().contains(Protocol1_13To1_12_2.class) || !info.getChannel().isOpen()) {
                continue;
            }
            info.get(TabCompleteTracker.class).sendPacketToServer();
        }
    }
}
