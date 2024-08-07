// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16to1_15_2.storage;

import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_16Types;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.storage.EntityTracker;

public class EntityTracker1_16 extends EntityTracker
{
    public EntityTracker1_16(final UserConnection user) {
        super(user, Entity1_16Types.EntityType.PLAYER);
    }
}
