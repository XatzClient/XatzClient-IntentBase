// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import us.myles.ViaVersion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import de.gerrygames.viarewind.utils.Tickable;
import us.myles.ViaVersion.api.data.StoredObject;

public class Levitation extends StoredObject implements Tickable
{
    private int amplifier;
    private volatile boolean active;
    
    public Levitation(final UserConnection user) {
        super(user);
        this.active = false;
    }
    
    public void tick() {
        if (!this.active) {
            return;
        }
        final int vY = (this.amplifier + 1) * 360;
        final PacketWrapper packet = new PacketWrapper(18, (ByteBuf)null, this.getUser());
        packet.write((Type)Type.VAR_INT, (Object)((EntityTracker)this.getUser().get((Class)EntityTracker.class)).getPlayerId());
        packet.write((Type)Type.SHORT, (Object)0);
        packet.write((Type)Type.SHORT, (Object)(short)vY);
        packet.write((Type)Type.SHORT, (Object)0);
        PacketUtil.sendPacket(packet, Protocol1_8TO1_9.class);
    }
    
    public void setActive(final boolean active) {
        this.active = active;
    }
    
    public void setAmplifier(final int amplifier) {
        this.amplifier = amplifier;
    }
}
