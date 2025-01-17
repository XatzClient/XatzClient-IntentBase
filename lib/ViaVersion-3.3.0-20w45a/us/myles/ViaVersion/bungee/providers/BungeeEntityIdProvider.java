// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.providers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.myles.ViaVersion.bungee.storage.BungeeStorage;
import us.myles.ViaVersion.api.data.UserConnection;
import java.lang.reflect.Method;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.EntityIdProvider;

public class BungeeEntityIdProvider extends EntityIdProvider
{
    private static Method getClientEntityId;
    
    @Override
    public int getEntityId(final UserConnection user) throws Exception {
        final BungeeStorage storage = user.get(BungeeStorage.class);
        final ProxiedPlayer player = storage.getPlayer();
        return (int)BungeeEntityIdProvider.getClientEntityId.invoke(player, new Object[0]);
    }
    
    static {
        try {
            BungeeEntityIdProvider.getClientEntityId = Class.forName("net.md_5.bungee.UserConnection").getDeclaredMethod("getClientEntityId", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException | ClassNotFoundException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            e.printStackTrace();
        }
    }
}
