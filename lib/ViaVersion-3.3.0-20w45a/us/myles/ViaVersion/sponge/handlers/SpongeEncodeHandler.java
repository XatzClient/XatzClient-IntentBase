// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.handlers;

import us.myles.ViaVersion.exception.CancelCodecException;
import java.util.function.Function;
import us.myles.ViaVersion.exception.CancelEncoderException;
import java.lang.reflect.InvocationTargetException;
import us.myles.ViaVersion.util.PipelineUtil;
import us.myles.ViaVersion.handlers.ChannelHandlerContextWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.handlers.ViaHandler;
import io.netty.handler.codec.MessageToByteEncoder;

public class SpongeEncodeHandler extends MessageToByteEncoder<Object> implements ViaHandler
{
    private final UserConnection info;
    private final MessageToByteEncoder<?> minecraftEncoder;
    
    public SpongeEncodeHandler(final UserConnection info, final MessageToByteEncoder<?> minecraftEncoder) {
        this.info = info;
        this.minecraftEncoder = minecraftEncoder;
    }
    
    protected void encode(final ChannelHandlerContext ctx, final Object o, final ByteBuf bytebuf) throws Exception {
        if (!(o instanceof ByteBuf)) {
            try {
                PipelineUtil.callEncode(this.minecraftEncoder, (ChannelHandlerContext)new ChannelHandlerContextWrapper(ctx, this), o, bytebuf);
            }
            catch (InvocationTargetException e) {
                if (e.getCause() instanceof Exception) {
                    throw (Exception)e.getCause();
                }
                if (e.getCause() instanceof Error) {
                    throw (Error)e.getCause();
                }
            }
        }
        this.transform(bytebuf);
    }
    
    public void transform(final ByteBuf bytebuf) throws Exception {
        if (!this.info.checkOutgoingPacket()) {
            throw CancelEncoderException.generate(null);
        }
        if (!this.info.shouldTransformPacket()) {
            return;
        }
        this.info.transformOutgoing(bytebuf, (Function<Throwable, Exception>)CancelEncoderException::generate);
    }
    
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (cause instanceof CancelCodecException) {
            return;
        }
        super.exceptionCaught(ctx, cause);
    }
}
