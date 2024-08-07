// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.blockconnections;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.data.UserConnection;

public abstract class ConnectionHandler
{
    public abstract int connect(final UserConnection p0, final Position p1, final int p2);
    
    public int getBlockData(final UserConnection user, final Position position) {
        return Via.getManager().getProviders().get(BlockConnectionProvider.class).getBlockData(user, position.getX(), position.getY(), position.getZ());
    }
}
