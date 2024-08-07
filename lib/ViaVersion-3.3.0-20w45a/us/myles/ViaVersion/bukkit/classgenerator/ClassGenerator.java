// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.classgenerator;

import org.bukkit.event.EventException;
import java.lang.reflect.Method;
import org.bukkit.event.Event;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.myles.viaversion.libs.javassist.CtNewMethod;
import us.myles.viaversion.libs.javassist.CtNewConstructor;
import us.myles.viaversion.libs.javassist.CtField;
import us.myles.viaversion.libs.javassist.expr.ConstructorCall;
import us.myles.viaversion.libs.javassist.expr.ExprEditor;
import org.bukkit.plugin.Plugin;
import us.myles.viaversion.libs.javassist.NotFoundException;
import us.myles.viaversion.libs.javassist.CannotCompileException;
import us.myles.viaversion.libs.javassist.CtMethod;
import us.myles.viaversion.libs.javassist.CtClass;
import us.myles.ViaVersion.bukkit.handlers.BukkitEncodeHandler;
import us.myles.ViaVersion.bukkit.handlers.BukkitDecodeHandler;
import us.myles.ViaVersion.bukkit.util.NMSUtil;
import us.myles.viaversion.libs.javassist.ClassPath;
import us.myles.viaversion.libs.javassist.LoaderClassPath;
import org.bukkit.Bukkit;
import us.myles.viaversion.libs.javassist.ClassPool;
import us.myles.ViaVersion.ViaVersionPlugin;

public class ClassGenerator
{
    private static HandlerConstructor constructor;
    private static String psPackage;
    private static Class psConnectListener;
    
    public static HandlerConstructor getConstructor() {
        return ClassGenerator.constructor;
    }
    
    public static void generate() {
        if (!ViaVersionPlugin.getInstance().isCompatSpigotBuild()) {
            if (!ViaVersionPlugin.getInstance().isProtocolSupport()) {
                return;
            }
        }
        try {
            final ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new LoaderClassPath(Bukkit.class.getClassLoader()));
            for (final Plugin p : Bukkit.getPluginManager().getPlugins()) {
                pool.insertClassPath(new LoaderClassPath(p.getClass().getClassLoader()));
            }
            if (ViaVersionPlugin.getInstance().isCompatSpigotBuild()) {
                final Class decodeSuper = NMSUtil.nms("PacketDecoder");
                final Class encodeSuper = NMSUtil.nms("PacketEncoder");
                addSpigotCompatibility(pool, BukkitDecodeHandler.class, decodeSuper);
                addSpigotCompatibility(pool, BukkitEncodeHandler.class, encodeSuper);
            }
            else {
                if (isMultiplatformPS()) {
                    ClassGenerator.psConnectListener = makePSConnectListener(pool, shouldUseNewHandshakeVersionMethod());
                    return;
                }
                final String psPackage = getOldPSPackage();
                final Class decodeSuper2 = Class.forName(psPackage.equals("unknown") ? "protocolsupport.protocol.pipeline.common.PacketDecoder" : (psPackage + ".wrapped.WrappedDecoder"));
                final Class encodeSuper2 = Class.forName(psPackage.equals("unknown") ? "protocolsupport.protocol.pipeline.common.PacketEncoder" : (psPackage + ".wrapped.WrappedEncoder"));
                addPSCompatibility(pool, BukkitDecodeHandler.class, decodeSuper2);
                addPSCompatibility(pool, BukkitEncodeHandler.class, encodeSuper2);
            }
            final CtClass generated = pool.makeClass("us.myles.ViaVersion.classgenerator.generated.GeneratedConstructor");
            final CtClass handlerInterface = pool.get(HandlerConstructor.class.getName());
            generated.setInterfaces(new CtClass[] { handlerInterface });
            pool.importPackage("us.myles.ViaVersion.classgenerator.generated");
            pool.importPackage("us.myles.ViaVersion.classgenerator");
            pool.importPackage("us.myles.ViaVersion.api.data");
            pool.importPackage("io.netty.handler.codec");
            generated.addMethod(CtMethod.make("public MessageToByteEncoder newEncodeHandler(UserConnection info, MessageToByteEncoder minecraftEncoder) {\n        return new BukkitEncodeHandler(info, minecraftEncoder);\n    }", generated));
            generated.addMethod(CtMethod.make("public ByteToMessageDecoder newDecodeHandler(UserConnection info, ByteToMessageDecoder minecraftDecoder) {\n        return new BukkitDecodeHandler(info, minecraftDecoder);\n    }", generated));
            ClassGenerator.constructor = (HandlerConstructor)generated.toClass(HandlerConstructor.class.getClassLoader()).newInstance();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (CannotCompileException e2) {
            e2.printStackTrace();
        }
        catch (NotFoundException e3) {
            e3.printStackTrace();
        }
        catch (InstantiationException e4) {
            e4.printStackTrace();
        }
        catch (IllegalAccessException e5) {
            e5.printStackTrace();
        }
    }
    
    private static Class addSpigotCompatibility(final ClassPool pool, final Class input, final Class superclass) {
        final String newName = "us.myles.ViaVersion.classgenerator.generated." + input.getSimpleName();
        try {
            final CtClass generated = pool.getAndRename(input.getName(), newName);
            if (superclass != null) {
                final CtClass toExtend = pool.get(superclass.getName());
                generated.setSuperclass(toExtend);
                if (superclass.getName().startsWith("net.minecraft") && generated.getConstructors().length != 0) {
                    generated.getConstructors()[0].instrument(new ExprEditor() {
                        @Override
                        public void edit(final ConstructorCall c) throws CannotCompileException {
                            if (c.isSuper()) {
                                c.replace("super(null);");
                            }
                            super.edit(c);
                        }
                    });
                }
            }
            return generated.toClass(HandlerConstructor.class.getClassLoader());
        }
        catch (NotFoundException e) {
            e.printStackTrace();
        }
        catch (CannotCompileException e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    private static Class addPSCompatibility(final ClassPool pool, final Class input, final Class superclass) {
        final boolean newPS = getOldPSPackage().equals("unknown");
        final String newName = "us.myles.ViaVersion.classgenerator.generated." + input.getSimpleName();
        try {
            final CtClass generated = pool.getAndRename(input.getName(), newName);
            if (superclass != null) {
                final CtClass toExtend = pool.get(superclass.getName());
                generated.setSuperclass(toExtend);
                if (!newPS) {
                    pool.importPackage(getOldPSPackage());
                    pool.importPackage(getOldPSPackage() + ".wrapped");
                    if (superclass.getName().endsWith("Decoder")) {
                        generated.addMethod(CtMethod.make("public void setRealDecoder(IPacketDecoder dec) {\n        ((WrappedDecoder) this.minecraftDecoder).setRealDecoder(dec);\n    }", generated));
                    }
                    else {
                        pool.importPackage("protocolsupport.api");
                        pool.importPackage("java.lang.reflect");
                        generated.addMethod(CtMethod.make("public void setRealEncoder(IPacketEncoder enc) {\n         try {\n             Field field = enc.getClass().getDeclaredField(\"version\");\n             field.setAccessible(true);\n             ProtocolVersion version = (ProtocolVersion) field.get(enc);\n             if (version == ProtocolVersion.MINECRAFT_FUTURE) enc = enc.getClass().getConstructor(\n                 new Class[]{ProtocolVersion.class}).newInstance(new Object[] {ProtocolVersion.getLatest()});\n         } catch (Exception e) {\n         }\n        ((WrappedEncoder) this.minecraftEncoder).setRealEncoder(enc);\n    }", generated));
                    }
                }
            }
            return generated.toClass(HandlerConstructor.class.getClassLoader());
        }
        catch (NotFoundException e) {
            e.printStackTrace();
        }
        catch (CannotCompileException e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    private static Class makePSConnectListener(final ClassPool pool, final boolean newVersionMethod) {
        try {
            final CtClass toExtend = pool.get("protocolsupport.api.Connection$PacketListener");
            final CtClass connectListenerClazz = pool.makeClass("us.myles.ViaVersion.classgenerator.generated.ProtocolSupportConnectListener");
            connectListenerClazz.setSuperclass(toExtend);
            pool.importPackage("java.util.Arrays");
            pool.importPackage("us.myles.ViaVersion.api.protocol.ProtocolRegistry");
            pool.importPackage("protocolsupport.api.ProtocolVersion");
            pool.importPackage("protocolsupport.api.ProtocolType");
            pool.importPackage("protocolsupport.api.Connection");
            pool.importPackage("protocolsupport.api.Connection.PacketListener");
            pool.importPackage("protocolsupport.api.Connection.PacketListener.PacketEvent");
            pool.importPackage("protocolsupport.protocol.ConnectionImpl");
            pool.importPackage(NMSUtil.nms("PacketHandshakingInSetProtocol").getName());
            connectListenerClazz.addField(CtField.make("private ConnectionImpl connection;", connectListenerClazz));
            connectListenerClazz.addConstructor(CtNewConstructor.make("public ProtocolSupportConnectListener (ConnectionImpl connection) {\n    this.connection = connection;\n}", connectListenerClazz));
            connectListenerClazz.addMethod(CtNewMethod.make("public void onPacketReceiving(protocolsupport.api.Connection.PacketListener.PacketEvent event) {\n    if (event.getPacket() instanceof PacketHandshakingInSetProtocol) {\n        PacketHandshakingInSetProtocol packet = (PacketHandshakingInSetProtocol) event.getPacket();\n" + (newVersionMethod ? "        int protoVersion = packet.getProtocolVersion();\n" : "        int protoVersion = packet.b();\n") + "        if (connection.getVersion() == ProtocolVersion.MINECRAFT_FUTURE && protoVersion == us.myles.ViaVersion.api.protocol.ProtocolRegistry.SERVER_PROTOCOL) {\n            connection.setVersion(ProtocolVersion.getLatest(ProtocolType.PC));\n        }\n    }\n    connection.removePacketListener(this);\n}", connectListenerClazz));
            return connectListenerClazz.toClass(HandlerConstructor.class.getClassLoader());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void registerPSConnectListener(final ViaVersionPlugin plugin) {
        if (getPSConnectListener() != null) {
            try {
                final Class<? extends Event> connectionOpenEvent = (Class<? extends Event>)Class.forName("protocolsupport.api.events.ConnectionOpenEvent");
                Bukkit.getPluginManager().registerEvent((Class)connectionOpenEvent, (Listener)new Listener() {}, EventPriority.HIGH, (EventExecutor)new EventExecutor() {
                    public void execute(final Listener listener, final Event event) throws EventException {
                        try {
                            final Object connection = event.getClass().getMethod("getConnection", (Class<?>[])new Class[0]).invoke(event, new Object[0]);
                            final Object connectListener = ClassGenerator.getPSConnectListener().getConstructor(connection.getClass()).newInstance(connection);
                            final Method addConnectListener = connection.getClass().getMethod("addPacketListener", Class.forName("protocolsupport.api.Connection$PacketListener"));
                            addConnectListener.invoke(connection, connectListener);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, (Plugin)plugin);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static Class getPSConnectListener() {
        return ClassGenerator.psConnectListener;
    }
    
    public static String getOldPSPackage() {
        if (ClassGenerator.psPackage == null) {
            try {
                Class.forName("protocolsupport.protocol.core.IPacketDecoder");
                ClassGenerator.psPackage = "protocolsupport.protocol.core";
            }
            catch (ClassNotFoundException e) {
                try {
                    Class.forName("protocolsupport.protocol.pipeline.IPacketDecoder");
                    ClassGenerator.psPackage = "protocolsupport.protocol.pipeline";
                }
                catch (ClassNotFoundException e2) {
                    ClassGenerator.psPackage = "unknown";
                }
            }
        }
        return ClassGenerator.psPackage;
    }
    
    public static boolean isMultiplatformPS() {
        try {
            Class.forName("protocolsupport.zplatform.impl.spigot.network.pipeline.SpigotPacketEncoder");
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    public static boolean shouldUseNewHandshakeVersionMethod() {
        try {
            NMSUtil.nms("PacketHandshakingInSetProtocol").getMethod("getProtocolVersion", (Class<?>[])new Class[0]);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    static {
        ClassGenerator.constructor = new BasicHandlerConstructor();
    }
}
