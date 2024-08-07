// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.providers;

import net.md_5.bungee.api.ProxyServer;
import java.util.Iterator;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import us.myles.ViaVersion.util.ReflectionUtil;
import java.util.List;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.base.VersionProvider;

public class BungeeVersionProvider extends VersionProvider
{
    private static Class<?> ref;
    
    @Override
    public int getServerProtocol(final UserConnection user) throws Exception {
        if (BungeeVersionProvider.ref == null) {
            return super.getServerProtocol(user);
        }
        final List<Integer> list = ReflectionUtil.getStatic(BungeeVersionProvider.ref, "SUPPORTED_VERSION_IDS", (Class<List<Integer>>)List.class);
        final List<Integer> sorted = new ArrayList<Integer>(list);
        Collections.sort(sorted);
        final ProtocolInfo info = user.getProtocolInfo();
        if (sorted.contains(info.getProtocolVersion())) {
            return info.getProtocolVersion();
        }
        if (info.getProtocolVersion() < sorted.get(0)) {
            return getLowestSupportedVersion();
        }
        for (final Integer protocol : Lists.reverse((List)sorted)) {
            if (info.getProtocolVersion() > protocol && ProtocolVersion.isRegistered(protocol)) {
                return protocol;
            }
        }
        Via.getPlatform().getLogger().severe("Panic, no protocol id found for " + info.getProtocolVersion());
        return info.getProtocolVersion();
    }
    
    public static int getLowestSupportedVersion() {
        try {
            final List<Integer> list = ReflectionUtil.getStatic(BungeeVersionProvider.ref, "SUPPORTED_VERSION_IDS", (Class<List<Integer>>)List.class);
            return list.get(0);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        return ProxyServer.getInstance().getProtocolVersion();
    }
    
    static {
        try {
            BungeeVersionProvider.ref = Class.forName("net.md_5.bungee.protocol.ProtocolConstants");
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().severe("Could not detect the ProtocolConstants class");
            e.printStackTrace();
        }
    }
}
