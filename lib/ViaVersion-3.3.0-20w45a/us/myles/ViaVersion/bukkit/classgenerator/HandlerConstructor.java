// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.classgenerator;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import us.myles.ViaVersion.api.data.UserConnection;

public interface HandlerConstructor
{
    MessageToByteEncoder newEncodeHandler(final UserConnection p0, final MessageToByteEncoder p1);
    
    ByteToMessageDecoder newDecodeHandler(final UserConnection p0, final ByteToMessageDecoder p1);
}
