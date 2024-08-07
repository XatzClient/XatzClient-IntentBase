// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.providers;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.velocity.storage.VelocityStorage;
import java.util.UUID;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BossBarProvider;

public class VelocityBossBarProvider extends BossBarProvider
{
    @Override
    public void handleAdd(final UserConnection user, final UUID barUUID) {
        if (user.has(VelocityStorage.class)) {
            final VelocityStorage storage = user.get(VelocityStorage.class);
            if (storage.getBossbar() != null) {
                storage.getBossbar().add(barUUID);
            }
        }
    }
    
    @Override
    public void handleRemove(final UserConnection user, final UUID barUUID) {
        if (user.has(VelocityStorage.class)) {
            final VelocityStorage storage = user.get(VelocityStorage.class);
            if (storage.getBossbar() != null) {
                storage.getBossbar().remove(barUUID);
            }
        }
    }
}
