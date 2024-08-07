// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.platform;

import us.myles.viaversion.libs.gson.JsonObject;
import java.lang.reflect.InvocationTargetException;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.velocity.handlers.VelocityChannelInitializer;
import us.myles.ViaVersion.util.ReflectionUtil;
import us.myles.ViaVersion.VelocityPlugin;
import io.netty.channel.ChannelInitializer;
import java.lang.reflect.Method;
import us.myles.ViaVersion.api.platform.ViaInjector;

public class VelocityViaInjector implements ViaInjector
{
    public static Method getPlayerInfoForwardingMode;
    
    private ChannelInitializer getInitializer() throws Exception {
        final Object connectionManager = ReflectionUtil.get(VelocityPlugin.PROXY, "cm", Object.class);
        final Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        return (ChannelInitializer)ReflectionUtil.invoke(channelInitializerHolder, "get");
    }
    
    private ChannelInitializer getBackendInitializer() throws Exception {
        final Object connectionManager = ReflectionUtil.get(VelocityPlugin.PROXY, "cm", Object.class);
        final Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        return (ChannelInitializer)ReflectionUtil.invoke(channelInitializerHolder, "get");
    }
    
    @Override
    public void inject() throws Exception {
        final Object connectionManager = ReflectionUtil.get(VelocityPlugin.PROXY, "cm", Object.class);
        final Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        final ChannelInitializer originalInitializer = this.getInitializer();
        channelInitializerHolder.getClass().getMethod("set", ChannelInitializer.class).invoke(channelInitializerHolder, new VelocityChannelInitializer((ChannelInitializer<?>)originalInitializer, false));
        final Object backendInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        final ChannelInitializer backendInitializer = this.getBackendInitializer();
        backendInitializerHolder.getClass().getMethod("set", ChannelInitializer.class).invoke(backendInitializerHolder, new VelocityChannelInitializer((ChannelInitializer<?>)backendInitializer, true));
    }
    
    @Override
    public void uninject() {
        Via.getPlatform().getLogger().severe("ViaVersion cannot remove itself from Velocity without a reboot!");
    }
    
    @Override
    public int getServerProtocolVersion() throws Exception {
        return getLowestSupportedProtocolVersion();
    }
    
    public static int getLowestSupportedProtocolVersion() {
        try {
            if (VelocityViaInjector.getPlayerInfoForwardingMode != null && ((Enum)VelocityViaInjector.getPlayerInfoForwardingMode.invoke(VelocityPlugin.PROXY.getConfiguration(), new Object[0])).name().equals("MODERN")) {
                return ProtocolVersion.v1_13.getVersion();
            }
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex2) {}
        return com.velocitypowered.api.network.ProtocolVersion.MINIMUM_VERSION.getProtocol();
    }
    
    @Override
    public String getEncoderName() {
        return "via-encoder";
    }
    
    @Override
    public String getDecoderName() {
        return "via-decoder";
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject data = new JsonObject();
        try {
            data.addProperty("currentInitializer", this.getInitializer().getClass().getName());
        }
        catch (Exception ex) {}
        return data;
    }
    
    static {
        try {
            VelocityViaInjector.getPlayerInfoForwardingMode = Class.forName("com.velocitypowered.proxy.config.VelocityConfiguration").getMethod("getPlayerInfoForwardingMode", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException | ClassNotFoundException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            e.printStackTrace();
        }
    }
}
