// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.handlers;

import io.netty.channel.ChannelHandler;
import us.myles.ViaVersion.api.protocol.ProtocolPipeline;
import us.myles.ViaVersion.api.data.UserConnection;
import java.lang.reflect.Method;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class BungeeChannelInitializer extends ChannelInitializer<Channel>
{
    private final ChannelInitializer<Channel> original;
    private Method method;
    
    public BungeeChannelInitializer(final ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
        try {
            (this.method = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    protected void initChannel(final Channel socketChannel) throws Exception {
        final UserConnection info = new UserConnection(socketChannel);
        new ProtocolPipeline(info);
        this.method.invoke(this.original, socketChannel);
        if (socketChannel.pipeline().get("packet-encoder") == null) {
            return;
        }
        if (socketChannel.pipeline().get("packet-decoder") == null) {
            return;
        }
        final BungeeEncodeHandler encoder = new BungeeEncodeHandler(info);
        final BungeeDecodeHandler decoder = new BungeeDecodeHandler(info);
        socketChannel.pipeline().addBefore("packet-encoder", "via-encoder", (ChannelHandler)encoder);
        socketChannel.pipeline().addBefore("packet-decoder", "via-decoder", (ChannelHandler)decoder);
    }
    
    public ChannelInitializer<Channel> getOriginal() {
        return this.original;
    }
}
