// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.utils;

import us.myles.ViaVersion.api.data.UserConnection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import us.myles.ViaVersion.api.Via;

public class Ticker
{
    private static boolean init;
    
    public static void init() {
        if (Ticker.init) {
            return;
        }
        synchronized (Ticker.class) {
            if (Ticker.init) {
                return;
            }
            Ticker.init = true;
        }
        Via.getPlatform().runRepeatingSync(() -> Via.getManager().getPortedPlayers().values().forEach(user -> user.getStoredObjects().values().stream().filter(Tickable.class::isInstance).map(Tickable.class::cast).forEach(Tickable::tick)), Long.valueOf(1L));
    }
    
    static {
        Ticker.init = false;
    }
}
