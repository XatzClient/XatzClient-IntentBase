// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.netty;

import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class ForwardMessageToMessageDecoder extends MessageToMessageDecoder
{
    protected void decode(final ChannelHandlerContext ctx, final Object msg, final List out) throws Exception {
        out.add(msg);
    }
}
