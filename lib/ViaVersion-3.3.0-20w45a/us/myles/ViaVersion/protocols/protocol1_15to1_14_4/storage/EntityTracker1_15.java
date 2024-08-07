// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_15to1_14_4.storage;

import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_15Types;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.storage.EntityTracker;

public class EntityTracker1_15 extends EntityTracker
{
    public EntityTracker1_15(final UserConnection user) {
        super(user, Entity1_15Types.EntityType.PLAYER);
    }
}
