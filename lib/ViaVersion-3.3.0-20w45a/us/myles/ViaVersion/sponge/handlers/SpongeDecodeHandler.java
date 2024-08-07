// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.handlers;

import us.myles.ViaVersion.exception.CancelCodecException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import us.myles.ViaVersion.util.PipelineUtil;
import java.util.function.Function;
import us.myles.ViaVersion.exception.CancelDecoderException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import us.myles.ViaVersion.api.data.UserConnection;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SpongeDecodeHandler extends ByteToMessageDecoder
{
    private final ByteToMessageDecoder minecraftDecoder;
    private final UserConnection info;
    
    public SpongeDecodeHandler(final UserConnection info, final ByteToMessageDecoder minecraftDecoder) {
        this.info = info;
        this.minecraftDecoder = minecraftDecoder;
    }
    
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf bytebuf, final List<Object> list) throws Exception {
        if (!this.info.checkIncomingPacket()) {
            bytebuf.clear();
            throw CancelDecoderException.generate(null);
        }
        ByteBuf transformedBuf = null;
        try {
            if (this.info.shouldTransformPacket()) {
                transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
                this.info.transformIncoming(transformedBuf, (Function<Throwable, Exception>)CancelDecoderException::generate);
            }
            try {
                list.addAll(PipelineUtil.callDecode(this.minecraftDecoder, ctx, (transformedBuf == null) ? bytebuf : transformedBuf));
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
        finally {
            if (transformedBuf != null) {
                transformedBuf.release();
            }
        }
    }
    
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (cause instanceof CancelCodecException) {
            return;
        }
        super.exceptionCaught(ctx, cause);
    }
}
