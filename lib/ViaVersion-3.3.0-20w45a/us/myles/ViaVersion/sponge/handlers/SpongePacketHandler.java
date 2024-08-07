// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.handlers;

import io.netty.buffer.ByteBuf;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import us.myles.ViaVersion.api.data.UserConnection;
import io.netty.handler.codec.MessageToMessageEncoder;

public class SpongePacketHandler extends MessageToMessageEncoder
{
    private final UserConnection info;
    
    public SpongePacketHandler(final UserConnection info) {
        this.info = info;
    }
    
    protected void encode(final ChannelHandlerContext ctx, final Object o, final List list) throws Exception {
        if (!(o instanceof ByteBuf)) {
            this.info.setLastPacket(o);
            if (this.info.isActive() && this.info.getProtocolInfo().getPipeline().filter(o, list)) {
                return;
            }
        }
        list.add(o);
    }
}
