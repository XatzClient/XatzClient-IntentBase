// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.handlers;

import io.netty.channel.ChannelHandler;
import us.myles.ViaVersion.api.protocol.ProtocolPipeline;
import us.myles.ViaVersion.api.data.UserConnection;
import java.lang.reflect.Method;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class VelocityChannelInitializer extends ChannelInitializer<Channel>
{
    private final ChannelInitializer<?> original;
    private final boolean clientSide;
    private static Method initChannel;
    
    public VelocityChannelInitializer(final ChannelInitializer<?> original, final boolean clientSide) {
        this.original = original;
        this.clientSide = clientSide;
    }
    
    protected void initChannel(final Channel channel) throws Exception {
        VelocityChannelInitializer.initChannel.invoke(this.original, channel);
        final UserConnection user = new UserConnection(channel, this.clientSide);
        new ProtocolPipeline(user);
        channel.pipeline().addBefore("minecraft-encoder", "via-encoder", (ChannelHandler)new VelocityEncodeHandler(user));
        channel.pipeline().addBefore("minecraft-decoder", "via-decoder", (ChannelHandler)new VelocityDecodeHandler(user));
    }
    
    static {
        try {
            (VelocityChannelInitializer.initChannel = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
