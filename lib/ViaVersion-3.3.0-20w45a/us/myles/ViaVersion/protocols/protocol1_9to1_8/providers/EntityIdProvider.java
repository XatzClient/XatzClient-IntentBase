// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.providers;

import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.platform.providers.Provider;

public class EntityIdProvider implements Provider
{
    public int getEntityId(final UserConnection user) throws Exception {
        return user.get(EntityTracker1_9.class).getClientEntityId();
    }
}
