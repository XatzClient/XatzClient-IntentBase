// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.handlers;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.exception.InformativeException;
import us.myles.ViaVersion.bukkit.util.NMSUtil;
import us.myles.ViaVersion.exception.CancelCodecException;
import java.util.function.Function;
import us.myles.ViaVersion.exception.CancelEncoderException;
import java.lang.reflect.InvocationTargetException;
import us.myles.ViaVersion.util.PipelineUtil;
import us.myles.ViaVersion.handlers.ChannelHandlerContextWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import us.myles.ViaVersion.api.data.UserConnection;
import java.lang.reflect.Field;
import us.myles.ViaVersion.handlers.ViaHandler;
import io.netty.handler.codec.MessageToByteEncoder;

public class BukkitEncodeHandler extends MessageToByteEncoder implements ViaHandler
{
    private static Field versionField;
    private final UserConnection info;
    private final MessageToByteEncoder minecraftEncoder;
    
    public BukkitEncodeHandler(final UserConnection info, final MessageToByteEncoder minecraftEncoder) {
        this.info = info;
        this.minecraftEncoder = minecraftEncoder;
    }
    
    protected void encode(final ChannelHandlerContext ctx, final Object o, final ByteBuf bytebuf) throws Exception {
        if (BukkitEncodeHandler.versionField != null) {
            BukkitEncodeHandler.versionField.set(this.minecraftEncoder, BukkitEncodeHandler.versionField.get(this));
        }
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
        if (PipelineUtil.containsCause(cause, CancelCodecException.class)) {
            return;
        }
        super.exceptionCaught(ctx, cause);
        if (!NMSUtil.isDebugPropertySet() && PipelineUtil.containsCause(cause, InformativeException.class) && (this.info.getProtocolInfo().getState() != State.HANDSHAKE || Via.getManager().isDebug())) {
            cause.printStackTrace();
        }
    }
    
    static {
        try {
            (BukkitEncodeHandler.versionField = NMSUtil.nms("PacketEncoder").getDeclaredField("version")).setAccessible(true);
        }
        catch (Exception ex) {}
    }
}
