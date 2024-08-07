// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.platform;

import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonArray;
import us.myles.viaversion.libs.gson.JsonObject;
import java.lang.reflect.Method;
import org.spongepowered.api.MinecraftVersion;
import org.spongepowered.api.Sponge;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import us.myles.ViaVersion.sponge.handlers.SpongeChannelInitializer;
import us.myles.ViaVersion.util.ReflectionUtil;
import io.netty.channel.ChannelInitializer;
import java.util.Iterator;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.util.ListWrapper;
import java.util.ArrayList;
import java.lang.reflect.Field;
import us.myles.ViaVersion.api.Pair;
import io.netty.channel.ChannelFuture;
import java.util.List;
import us.myles.ViaVersion.api.platform.ViaInjector;

public class SpongeViaInjector implements ViaInjector
{
    private List<ChannelFuture> injectedFutures;
    private List<Pair<Field, Object>> injectedLists;
    
    public SpongeViaInjector() {
        this.injectedFutures = new ArrayList<ChannelFuture>();
        this.injectedLists = new ArrayList<Pair<Field, Object>>();
    }
    
    @Override
    public void inject() throws Exception {
        try {
            final Object connection = getServerConnection();
            if (connection == null) {
                throw new Exception("We failed to find the core component 'ServerConnection', please file an issue on our GitHub.");
            }
            for (final Field field : connection.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                final Object value = field.get(connection);
                if (value instanceof List) {
                    final List wrapper = new ListWrapper((List)value) {
                        @Override
                        public void handleAdd(final Object o) {
                            if (o instanceof ChannelFuture) {
                                try {
                                    SpongeViaInjector.this.injectChannelFuture((ChannelFuture)o);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    this.injectedLists.add(new Pair<Field, Object>(field, connection));
                    field.set(connection, wrapper);
                    synchronized (wrapper) {
                        for (final Object o : (List)value) {
                            if (!(o instanceof ChannelFuture)) {
                                break;
                            }
                            this.injectChannelFuture((ChannelFuture)o);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().severe("Unable to inject ViaVersion, please post these details on our GitHub and ensure you're using a compatible server version.");
            throw e;
        }
    }
    
    private void injectChannelFuture(final ChannelFuture future) throws Exception {
        try {
            final List<String> names = (List<String>)future.channel().pipeline().names();
            ChannelHandler bootstrapAcceptor = null;
            for (final String name : names) {
                final ChannelHandler handler = future.channel().pipeline().get(name);
                try {
                    ReflectionUtil.get(handler, "childHandler", ChannelInitializer.class);
                    bootstrapAcceptor = handler;
                }
                catch (Exception ex) {}
            }
            if (bootstrapAcceptor == null) {
                bootstrapAcceptor = future.channel().pipeline().first();
            }
            try {
                final ChannelInitializer<Channel> oldInit = (ChannelInitializer<Channel>)ReflectionUtil.get(bootstrapAcceptor, "childHandler", ChannelInitializer.class);
                final ChannelInitializer newInit = new SpongeChannelInitializer(oldInit);
                ReflectionUtil.set(bootstrapAcceptor, "childHandler", newInit);
                this.injectedFutures.add(future);
            }
            catch (NoSuchFieldException e2) {
                throw new Exception("Unable to find core component 'childHandler', please check your plugins. issue: " + bootstrapAcceptor.getClass().getName());
            }
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().severe("We failed to inject ViaVersion, have you got late-bind enabled with something else?");
            throw e;
        }
    }
    
    @Override
    public void uninject() {
        for (final ChannelFuture future : this.injectedFutures) {
            final List<String> names = (List<String>)future.channel().pipeline().names();
            ChannelHandler bootstrapAcceptor = null;
            for (final String name : names) {
                final ChannelHandler handler = future.channel().pipeline().get(name);
                try {
                    final ChannelInitializer<Channel> oldInit = (ChannelInitializer<Channel>)ReflectionUtil.get(handler, "childHandler", ChannelInitializer.class);
                    if (!(oldInit instanceof SpongeChannelInitializer)) {
                        continue;
                    }
                    bootstrapAcceptor = handler;
                }
                catch (Exception ex) {}
            }
            if (bootstrapAcceptor == null) {
                bootstrapAcceptor = future.channel().pipeline().first();
            }
            try {
                final ChannelInitializer<Channel> oldInit2 = (ChannelInitializer<Channel>)ReflectionUtil.get(bootstrapAcceptor, "childHandler", ChannelInitializer.class);
                if (!(oldInit2 instanceof SpongeChannelInitializer)) {
                    continue;
                }
                ReflectionUtil.set(bootstrapAcceptor, "childHandler", ((SpongeChannelInitializer)oldInit2).getOriginal());
            }
            catch (Exception e) {
                Via.getPlatform().getLogger().severe("Failed to remove injection handler, reload won't work with connections, please reboot!");
            }
        }
        this.injectedFutures.clear();
        for (final Pair<Field, Object> pair : this.injectedLists) {
            try {
                final Object o = pair.getKey().get(pair.getValue());
                if (!(o instanceof ListWrapper)) {
                    continue;
                }
                pair.getKey().set(pair.getValue(), ((ListWrapper)o).getOriginalList());
            }
            catch (IllegalAccessException e2) {
                Via.getPlatform().getLogger().severe("Failed to remove injection, reload won't work with connections, please reboot!");
            }
        }
        this.injectedLists.clear();
    }
    
    public static Object getServer() throws Exception {
        return Sponge.getServer();
    }
    
    @Override
    public int getServerProtocolVersion() throws Exception {
        final MinecraftVersion mcv = Sponge.getPlatform().getMinecraftVersion();
        try {
            return (int)mcv.getClass().getDeclaredMethod("getProtocol", (Class<?>[])new Class[0]).invoke(mcv, new Object[0]);
        }
        catch (Exception e) {
            throw new Exception("Failed to get server protocol", e);
        }
    }
    
    @Override
    public String getEncoderName() {
        return "encoder";
    }
    
    @Override
    public String getDecoderName() {
        return "decoder";
    }
    
    public static Object getServerConnection() throws Exception {
        final Class<?> serverClazz = Class.forName("net.minecraft.server.MinecraftServer");
        final Object server = getServer();
        Object connection = null;
        for (final Method m : serverClazz.getDeclaredMethods()) {
            if (m.getReturnType() != null && m.getReturnType().getSimpleName().equals("NetworkSystem") && m.getParameterTypes().length == 0) {
                connection = m.invoke(server, new Object[0]);
            }
        }
        return connection;
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject data = new JsonObject();
        final JsonArray injectedChannelInitializers = new JsonArray();
        for (final ChannelFuture cf : this.injectedFutures) {
            final JsonObject info = new JsonObject();
            info.addProperty("futureClass", cf.getClass().getName());
            info.addProperty("channelClass", cf.channel().getClass().getName());
            final JsonArray pipeline = new JsonArray();
            for (final String pipeName : cf.channel().pipeline().names()) {
                final JsonObject pipe = new JsonObject();
                pipe.addProperty("name", pipeName);
                if (cf.channel().pipeline().get(pipeName) != null) {
                    pipe.addProperty("class", cf.channel().pipeline().get(pipeName).getClass().getName());
                    try {
                        final Object child = ReflectionUtil.get(cf.channel().pipeline().get(pipeName), "childHandler", ChannelInitializer.class);
                        pipe.addProperty("childClass", child.getClass().getName());
                        if (child instanceof SpongeChannelInitializer) {
                            pipe.addProperty("oldInit", ((SpongeChannelInitializer)child).getOriginal().getClass().getName());
                        }
                    }
                    catch (Exception ex) {}
                }
                pipeline.add(pipe);
            }
            info.add("pipeline", pipeline);
            injectedChannelInitializers.add(info);
        }
        data.add("injectedChannelInitializers", injectedChannelInitializers);
        final JsonObject wrappedLists = new JsonObject();
        final JsonObject currentLists = new JsonObject();
        try {
            for (final Pair<Field, Object> pair : this.injectedLists) {
                final Object list = pair.getKey().get(pair.getValue());
                currentLists.addProperty(pair.getKey().getName(), list.getClass().getName());
                if (list instanceof ListWrapper) {
                    wrappedLists.addProperty(pair.getKey().getName(), ((ListWrapper)list).getOriginalList().getClass().getName());
                }
            }
            data.add("wrappedLists", wrappedLists);
            data.add("currentLists", currentLists);
        }
        catch (Exception ex2) {}
        return data;
    }
}
