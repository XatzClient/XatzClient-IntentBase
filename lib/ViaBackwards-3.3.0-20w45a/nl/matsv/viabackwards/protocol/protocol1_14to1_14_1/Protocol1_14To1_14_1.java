// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_14to1_14_1;

import us.myles.ViaVersion.api.data.StoredObject;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.protocol.protocol1_14to1_14_1.packets.EntityPackets1_14_1;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_14To1_14_1 extends BackwardsProtocol<ClientboundPackets1_14, ClientboundPackets1_14, ServerboundPackets1_14, ServerboundPackets1_14>
{
    public Protocol1_14To1_14_1() {
        super(ClientboundPackets1_14.class, ClientboundPackets1_14.class, ServerboundPackets1_14.class, ServerboundPackets1_14.class);
    }
    
    protected void registerPackets() {
        new EntityPackets1_14_1(this).register();
    }
    
    public void init(final UserConnection user) {
        if (!user.has((Class)EntityTracker.class)) {
            user.put((StoredObject)new EntityTracker(user));
        }
        ((EntityTracker)user.get((Class)EntityTracker.class)).initProtocol(this);
    }
}
