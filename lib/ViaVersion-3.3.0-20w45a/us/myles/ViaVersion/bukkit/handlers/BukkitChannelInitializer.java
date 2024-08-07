// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.handlers;

import us.myles.ViaVersion.bukkit.classgenerator.HandlerConstructor;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import us.myles.ViaVersion.bukkit.classgenerator.ClassGenerator;
import us.myles.ViaVersion.api.protocol.ProtocolPipeline;
import us.myles.ViaVersion.api.data.UserConnection;
import io.netty.channel.Channel;
import java.lang.reflect.Method;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.ChannelInitializer;

public class BukkitChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private final ChannelInitializer<SocketChannel> original;
    private Method method;
    
    public BukkitChannelInitializer(final ChannelInitializer<SocketChannel> oldInit) {
        this.original = oldInit;
        try {
            (this.method = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public ChannelInitializer<SocketChannel> getOriginal() {
        return this.original;
    }
    
    protected void initChannel(final SocketChannel socketChannel) throws Exception {
        final UserConnection info = new UserConnection((Channel)socketChannel);
        new ProtocolPipeline(info);
        this.method.invoke(this.original, socketChannel);
        final HandlerConstructor constructor = ClassGenerator.getConstructor();
        final MessageToByteEncoder encoder = constructor.newEncodeHandler(info, (MessageToByteEncoder)socketChannel.pipeline().get("encoder"));
        final ByteToMessageDecoder decoder = constructor.newDecodeHandler(info, (ByteToMessageDecoder)socketChannel.pipeline().get("decoder"));
        final BukkitPacketHandler chunkHandler = new BukkitPacketHandler(info);
        socketChannel.pipeline().replace("encoder", "encoder", (ChannelHandler)encoder);
        socketChannel.pipeline().replace("decoder", "decoder", (ChannelHandler)decoder);
        socketChannel.pipeline().addAfter("packet_handler", "viaversion_packet_handler", (ChannelHandler)chunkHandler);
    }
}
