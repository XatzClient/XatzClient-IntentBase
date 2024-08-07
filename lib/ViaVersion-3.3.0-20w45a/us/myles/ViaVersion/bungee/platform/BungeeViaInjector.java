// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.platform;

import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.util.ReflectionUtil;
import java.util.List;
import java.lang.reflect.Method;
import us.myles.ViaVersion.api.Via;
import io.netty.channel.Channel;
import us.myles.ViaVersion.bungee.handlers.BungeeChannelInitializer;
import io.netty.channel.ChannelInitializer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import us.myles.ViaVersion.api.platform.ViaInjector;

public class BungeeViaInjector implements ViaInjector
{
    @Override
    public void inject() throws Exception {
        try {
            final Class<?> pipelineUtils = Class.forName("net.md_5.bungee.netty.PipelineUtils");
            final Field field = pipelineUtils.getDeclaredField("SERVER_CHILD");
            field.setAccessible(true);
            final int modifiers = field.getModifiers();
            Label_0178: {
                if (Modifier.isFinal(modifiers)) {
                    try {
                        final Field modifiersField = Field.class.getDeclaredField("modifiers");
                        modifiersField.setAccessible(true);
                        modifiersField.setInt(field, modifiers & 0xFFFFFFEF);
                    }
                    catch (NoSuchFieldException e2) {
                        final Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", Boolean.TYPE);
                        getDeclaredFields0.setAccessible(true);
                        final Field[] array;
                        final Field[] fields = array = (Field[])getDeclaredFields0.invoke(Field.class, false);
                        Block_6: {
                            for (final Field classField : array) {
                                if ("modifiers".equals(classField.getName())) {
                                    break Block_6;
                                }
                            }
                            break Label_0178;
                        }
                        final Field classField;
                        classField.setAccessible(true);
                        classField.set(field, modifiers & 0xFFFFFFEF);
                    }
                }
            }
            final BungeeChannelInitializer newInit = new BungeeChannelInitializer((ChannelInitializer<Channel>)field.get(null));
            field.set(null, newInit);
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().severe("Unable to inject ViaVersion, please post these details on our GitHub and ensure you're using a compatible server version.");
            throw e;
        }
    }
    
    @Override
    public void uninject() {
        Via.getPlatform().getLogger().severe("ViaVersion cannot remove itself from Bungee without a reboot!");
    }
    
    @Override
    public int getServerProtocolVersion() throws Exception {
        return ReflectionUtil.getStatic(Class.forName("net.md_5.bungee.protocol.ProtocolConstants"), "SUPPORTED_VERSION_IDS", (Class<List<Integer>>)List.class).get(0);
    }
    
    @Override
    public String getEncoderName() {
        return "via-encoder";
    }
    
    @Override
    public String getDecoderName() {
        return "via-decoder";
    }
    
    private ChannelInitializer<Channel> getChannelInitializer() throws Exception {
        final Class<?> pipelineUtils = Class.forName("net.md_5.bungee.netty.PipelineUtils");
        final Field field = pipelineUtils.getDeclaredField("SERVER_CHILD");
        field.setAccessible(true);
        return (ChannelInitializer<Channel>)field.get(null);
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject data = new JsonObject();
        try {
            final ChannelInitializer<Channel> initializer = this.getChannelInitializer();
            data.addProperty("currentInitializer", initializer.getClass().getName());
            if (initializer instanceof BungeeChannelInitializer) {
                data.addProperty("originalInitializer", ((BungeeChannelInitializer)initializer).getOriginal().getClass().getName());
            }
        }
        catch (Exception ex) {}
        return data;
    }
}
