// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.providers;

import io.netty.channel.ChannelHandlerContext;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.MovementTracker;
import us.myles.ViaVersion.util.PipelineUtil;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.platform.providers.Provider;

public abstract class MovementTransmitterProvider implements Provider
{
    public abstract Object getFlyingPacket();
    
    public abstract Object getGroundPacket();
    
    public void sendPlayer(final UserConnection userConnection) {
        final ChannelHandlerContext context = PipelineUtil.getContextBefore("decoder", userConnection.getChannel().pipeline());
        if (context != null) {
            if (userConnection.get(MovementTracker.class).isGround()) {
                context.fireChannelRead(this.getGroundPacket());
            }
            else {
                context.fireChannelRead(this.getFlyingPacket());
            }
            userConnection.get(MovementTracker.class).incrementIdlePacket();
        }
    }
}
