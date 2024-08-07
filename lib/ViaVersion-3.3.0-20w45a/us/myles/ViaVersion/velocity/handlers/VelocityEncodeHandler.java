// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.handlers;

import us.myles.ViaVersion.exception.CancelCodecException;
import io.netty.handler.codec.MessageToByteEncoder;
import java.lang.reflect.InvocationTargetException;
import us.myles.ViaVersion.util.PipelineUtil;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.function.Function;
import us.myles.ViaVersion.exception.CancelEncoderException;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import us.myles.ViaVersion.api.data.UserConnection;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class VelocityEncodeHandler extends MessageToMessageEncoder<ByteBuf>
{
    private final UserConnection info;
    private boolean handledCompression;
    
    public VelocityEncodeHandler(final UserConnection info) {
        this.info = info;
    }
    
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf bytebuf, final List<Object> out) throws Exception {
        if (!this.info.checkOutgoingPacket()) {
            throw CancelEncoderException.generate(null);
        }
        if (!this.info.shouldTransformPacket()) {
            out.add(bytebuf.retain());
            return;
        }
        final ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
        try {
            final boolean needsCompress = this.handleCompressionOrder(ctx, transformedBuf);
            this.info.transformOutgoing(transformedBuf, (Function<Throwable, Exception>)CancelEncoderException::generate);
            if (needsCompress) {
                this.recompress(ctx, transformedBuf);
            }
            out.add(transformedBuf.retain());
        }
        finally {
            transformedBuf.release();
        }
    }
    
    private boolean handleCompressionOrder(final ChannelHandlerContext ctx, final ByteBuf buf) throws InvocationTargetException {
        if (this.handledCompression) {
            return false;
        }
        final int encoderIndex = ctx.pipeline().names().indexOf("compression-encoder");
        if (encoderIndex == -1) {
            return false;
        }
        this.handledCompression = true;
        if (encoderIndex > ctx.pipeline().names().indexOf("via-encoder")) {
            final ByteBuf decompressed = PipelineUtil.callDecode((MessageToMessageDecoder)ctx.pipeline().get("compression-decoder"), ctx, buf).get(0);
            try {
                buf.clear().writeBytes(decompressed);
            }
            finally {
                decompressed.release();
            }
            final ChannelHandler encoder = ctx.pipeline().get("via-encoder");
            final ChannelHandler decoder = ctx.pipeline().get("via-decoder");
            ctx.pipeline().remove(encoder);
            ctx.pipeline().remove(decoder);
            ctx.pipeline().addAfter("compression-encoder", "via-encoder", encoder);
            ctx.pipeline().addAfter("compression-decoder", "via-decoder", decoder);
            return true;
        }
        return false;
    }
    
    private void recompress(final ChannelHandlerContext ctx, final ByteBuf buf) throws InvocationTargetException {
        final ByteBuf compressed = ctx.alloc().buffer();
        try {
            PipelineUtil.callEncode((MessageToByteEncoder)ctx.pipeline().get("compression-encoder"), ctx, buf, compressed);
            buf.clear().writeBytes(compressed);
        }
        finally {
            compressed.release();
        }
    }
    
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (cause instanceof CancelCodecException) {
            return;
        }
        super.exceptionCaught(ctx, cause);
    }
}
