// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.providers;

import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.MovementTracker;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;

public class BungeeMovementTransmitter extends MovementTransmitterProvider
{
    @Override
    public Object getFlyingPacket() {
        return null;
    }
    
    @Override
    public Object getGroundPacket() {
        return null;
    }
    
    @Override
    public void sendPlayer(final UserConnection userConnection) {
        if (userConnection.getProtocolInfo().getState() == State.PLAY) {
            final PacketWrapper wrapper = new PacketWrapper(3, null, userConnection);
            wrapper.write(Type.BOOLEAN, userConnection.get(MovementTracker.class).isGround());
            try {
                wrapper.sendToServer(Protocol1_9To1_8.class);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
