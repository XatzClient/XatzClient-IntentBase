// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.providers;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.bungee.storage.BungeeStorage;
import java.util.UUID;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BossBarProvider;

public class BungeeBossBarProvider extends BossBarProvider
{
    @Override
    public void handleAdd(final UserConnection user, final UUID barUUID) {
        if (user.has(BungeeStorage.class)) {
            final BungeeStorage storage = user.get(BungeeStorage.class);
            if (storage.getBossbar() != null) {
                storage.getBossbar().add(barUUID);
            }
        }
    }
    
    @Override
    public void handleRemove(final UserConnection user, final UUID barUUID) {
        if (user.has(BungeeStorage.class)) {
            final BungeeStorage storage = user.get(BungeeStorage.class);
            if (storage.getBossbar() != null) {
                storage.getBossbar().remove(barUUID);
            }
        }
    }
}
