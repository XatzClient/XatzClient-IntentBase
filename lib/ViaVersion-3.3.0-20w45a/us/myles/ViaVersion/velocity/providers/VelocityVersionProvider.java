// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.providers;

import java.util.stream.IntStream;
import us.myles.ViaVersion.api.Via;
import java.util.Arrays;
import us.myles.ViaVersion.VelocityPlugin;
import us.myles.ViaVersion.velocity.platform.VelocityViaInjector;
import java.util.function.ToIntFunction;
import com.velocitypowered.api.network.ProtocolVersion;
import io.netty.channel.ChannelHandler;
import us.myles.ViaVersion.velocity.service.ProtocolDetectorService;
import com.velocitypowered.api.proxy.ServerConnection;
import us.myles.ViaVersion.api.data.UserConnection;
import java.lang.reflect.Method;
import us.myles.ViaVersion.protocols.base.VersionProvider;

public class VelocityVersionProvider extends VersionProvider
{
    private static Method getAssociation;
    
    @Override
    public int getServerProtocol(final UserConnection user) throws Exception {
        return user.isClientSide() ? this.getBackProtocol(user) : this.getFrontProtocol(user);
    }
    
    private int getBackProtocol(final UserConnection user) throws Exception {
        final ChannelHandler mcHandler = user.getChannel().pipeline().get("handler");
        return ProtocolDetectorService.getProtocolId(((ServerConnection)VelocityVersionProvider.getAssociation.invoke(mcHandler, new Object[0])).getServerInfo().getName());
    }
    
    private int getFrontProtocol(final UserConnection user) throws Exception {
        final int playerVersion = user.getProtocolInfo().getProtocolVersion();
        IntStream versions = ProtocolVersion.SUPPORTED_VERSIONS.stream().mapToInt(ProtocolVersion::getProtocol);
        if (VelocityViaInjector.getPlayerInfoForwardingMode != null && ((Enum)VelocityViaInjector.getPlayerInfoForwardingMode.invoke(VelocityPlugin.PROXY.getConfiguration(), new Object[0])).name().equals("MODERN")) {
            versions = versions.filter(ver -> ver >= us.myles.ViaVersion.api.protocol.ProtocolVersion.v1_13.getVersion());
        }
        final int[] compatibleProtocols = versions.toArray();
        if (Arrays.binarySearch(compatibleProtocols, playerVersion) >= 0) {
            return playerVersion;
        }
        if (playerVersion < compatibleProtocols[0]) {
            return compatibleProtocols[0];
        }
        for (int i = compatibleProtocols.length - 1; i >= 0; --i) {
            final int protocol = compatibleProtocols[i];
            if (playerVersion > protocol && us.myles.ViaVersion.api.protocol.ProtocolVersion.isRegistered(protocol)) {
                return protocol;
            }
        }
        Via.getPlatform().getLogger().severe("Panic, no protocol id found for " + playerVersion);
        return playerVersion;
    }
    
    static {
        try {
            VelocityVersionProvider.getAssociation = Class.forName("com.velocitypowered.proxy.connection.MinecraftConnection").getMethod("getAssociation", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException | ClassNotFoundException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            e.printStackTrace();
        }
    }
}
