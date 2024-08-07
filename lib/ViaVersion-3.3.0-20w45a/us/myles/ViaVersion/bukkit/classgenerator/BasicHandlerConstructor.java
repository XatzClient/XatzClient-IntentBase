// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.classgenerator;

import us.myles.ViaVersion.bukkit.handlers.BukkitDecodeHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import us.myles.ViaVersion.bukkit.handlers.BukkitEncodeHandler;
import io.netty.handler.codec.MessageToByteEncoder;
import us.myles.ViaVersion.api.data.UserConnection;

public class BasicHandlerConstructor implements HandlerConstructor
{
    @Override
    public BukkitEncodeHandler newEncodeHandler(final UserConnection info, final MessageToByteEncoder minecraftEncoder) {
        return new BukkitEncodeHandler(info, minecraftEncoder);
    }
    
    @Override
    public BukkitDecodeHandler newDecodeHandler(final UserConnection info, final ByteToMessageDecoder minecraftDecoder) {
        return new BukkitDecodeHandler(info, minecraftDecoder);
    }
}
