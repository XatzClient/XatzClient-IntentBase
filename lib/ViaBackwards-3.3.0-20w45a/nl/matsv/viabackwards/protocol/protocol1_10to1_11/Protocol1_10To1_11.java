// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_10to1_11;

import nl.matsv.viabackwards.protocol.protocol1_10to1_11.storage.WindowTracker;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets.SoundPackets1_11;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets.PlayerPackets1_11;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets.BlockItemPackets1_11;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets.EntityPackets1_11;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_10To1_11 extends BackwardsProtocol<ClientboundPackets1_9_3, ClientboundPackets1_9_3, ServerboundPackets1_9_3, ServerboundPackets1_9_3>
{
    private EntityPackets1_11 entityPackets;
    private BlockItemPackets1_11 blockItemPackets;
    
    public Protocol1_10To1_11() {
        super(ClientboundPackets1_9_3.class, ClientboundPackets1_9_3.class, ServerboundPackets1_9_3.class, ServerboundPackets1_9_3.class);
    }
    
    protected void registerPackets() {
        (this.entityPackets = new EntityPackets1_11(this)).register();
        new PlayerPackets1_11().register(this);
        (this.blockItemPackets = new BlockItemPackets1_11(this)).register();
        new SoundPackets1_11(this).register();
    }
    
    public void init(final UserConnection user) {
        if (!user.has((Class)ClientWorld.class)) {
            user.put((StoredObject)new ClientWorld(user));
        }
        if (!user.has((Class)EntityTracker.class)) {
            user.put((StoredObject)new EntityTracker(user));
        }
        if (!user.has((Class)WindowTracker.class)) {
            user.put((StoredObject)new WindowTracker(user));
        }
        ((EntityTracker)user.get((Class)EntityTracker.class)).initProtocol(this);
    }
    
    public EntityPackets1_11 getEntityPackets() {
        return this.entityPackets;
    }
    
    public BlockItemPackets1_11 getBlockItemPackets() {
        return this.blockItemPackets;
    }
}
