// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import us.myles.ViaVersion.api.protocol.ProtocolPipeline;
import us.myles.ViaVersion.api.data.UserConnection;
import io.netty.channel.socket.SocketChannel;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import java.lang.reflect.Method;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class SpongeChannelInitializer extends ChannelInitializer<Channel>
{
    private final ChannelInitializer<Channel> original;
    private Method method;
    
    public SpongeChannelInitializer(final ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
        try {
            (this.method = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    protected void initChannel(final Channel channel) throws Exception {
        if (ProtocolRegistry.SERVER_PROTOCOL != -1 && channel instanceof SocketChannel) {
            final UserConnection info = new UserConnection(channel);
            new ProtocolPipeline(info);
            this.method.invoke(this.original, channel);
            final MessageToByteEncoder encoder = new SpongeEncodeHandler(info, (MessageToByteEncoder<?>)channel.pipeline().get("encoder"));
            final ByteToMessageDecoder decoder = new SpongeDecodeHandler(info, (ByteToMessageDecoder)channel.pipeline().get("decoder"));
            final SpongePacketHandler chunkHandler = new SpongePacketHandler(info);
            channel.pipeline().replace("encoder", "encoder", (ChannelHandler)encoder);
            channel.pipeline().replace("decoder", "decoder", (ChannelHandler)decoder);
            channel.pipeline().addAfter("packet_handler", "viaversion_packet_handler", (ChannelHandler)chunkHandler);
        }
        else {
            this.method.invoke(this.original, channel);
        }
    }
    
    public ChannelInitializer<Channel> getOriginal() {
        return this.original;
    }
}
