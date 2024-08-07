// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api;

import us.myles.ViaVersion.api.data.MappingData;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;

public abstract class BackwardsProtocol<C1 extends ClientboundPacketType, C2 extends ClientboundPacketType, S1 extends ServerboundPacketType, S2 extends ServerboundPacketType> extends Protocol<C1, C2, S1, S2>
{
    protected BackwardsProtocol() {
    }
    
    protected BackwardsProtocol(@Nullable final Class<C1> oldClientboundPacketEnum, @Nullable final Class<C2> clientboundPacketEnum, @Nullable final Class<S1> oldServerboundPacketEnum, @Nullable final Class<S2> serverboundPacketEnum) {
        super((Class)oldClientboundPacketEnum, (Class)clientboundPacketEnum, (Class)oldServerboundPacketEnum, (Class)serverboundPacketEnum);
    }
    
    protected void executeAsyncAfterLoaded(final Class<? extends Protocol> protocolClass, final Runnable runnable) {
        ProtocolRegistry.addMappingLoaderFuture((Class)this.getClass(), (Class)protocolClass, runnable);
    }
    
    public boolean hasMappingDataToLoad() {
        return false;
    }
    
    public BackwardsMappings getMappingData() {
        return null;
    }
}
