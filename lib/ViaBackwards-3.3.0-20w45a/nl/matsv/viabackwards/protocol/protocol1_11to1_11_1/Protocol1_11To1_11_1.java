// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_11to1_11_1;

import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.protocol.protocol1_11to1_11_1.packets.ItemPackets1_11_1;
import nl.matsv.viabackwards.protocol.protocol1_11to1_11_1.packets.EntityPackets1_11_1;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_11To1_11_1 extends BackwardsProtocol<ClientboundPackets1_9_3, ClientboundPackets1_9_3, ServerboundPackets1_9_3, ServerboundPackets1_9_3>
{
    private EntityPackets1_11_1 entityPackets;
    
    public Protocol1_11To1_11_1() {
        super(ClientboundPackets1_9_3.class, ClientboundPackets1_9_3.class, ServerboundPackets1_9_3.class, ServerboundPackets1_9_3.class);
    }
    
    protected void registerPackets() {
        (this.entityPackets = new EntityPackets1_11_1(this)).register();
        new ItemPackets1_11_1(this).register();
    }
    
    public void init(final UserConnection user) {
        if (!user.has((Class)ClientWorld.class)) {
            user.put((StoredObject)new ClientWorld(user));
        }
        if (!user.has((Class)EntityTracker.class)) {
            user.put((StoredObject)new EntityTracker(user));
        }
        ((EntityTracker)user.get((Class)EntityTracker.class)).initProtocol(this);
    }
    
    public EntityPackets1_11_1 getEntityPackets() {
        return this.entityPackets;
    }
}
