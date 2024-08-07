// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12;

import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data.ShoulderTracker;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets.ChatPackets1_12;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets.SoundPackets1_12;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets.BlockItemPackets1_12;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets.EntityPackets1_12;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_11_1To1_12 extends BackwardsProtocol<ClientboundPackets1_12, ClientboundPackets1_9_3, ServerboundPackets1_12, ServerboundPackets1_9_3>
{
    private EntityPackets1_12 entityPackets;
    private BlockItemPackets1_12 blockItemPackets;
    
    public Protocol1_11_1To1_12() {
        super(ClientboundPackets1_12.class, ClientboundPackets1_9_3.class, ServerboundPackets1_12.class, ServerboundPackets1_9_3.class);
    }
    
    protected void registerPackets() {
        (this.entityPackets = new EntityPackets1_12(this)).register();
        (this.blockItemPackets = new BlockItemPackets1_12(this)).register();
        new SoundPackets1_12(this).register();
        new ChatPackets1_12(this).register();
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.TITLE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final int action = (int)wrapper.passthrough((Type)Type.VAR_INT);
                    if (action >= 0 && action <= 2) {
                        final JsonElement component = (JsonElement)wrapper.read(Type.COMPONENT);
                        wrapper.write(Type.COMPONENT, (Object)Protocol1_9To1_8.fixJson(component.toString()));
                    }
                });
            }
        });
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_12.ADVANCEMENTS);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_12.UNLOCK_RECIPES);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_12.SELECT_ADVANCEMENTS_TAB);
    }
    
    public void init(final UserConnection user) {
        if (!user.has((Class)ClientWorld.class)) {
            user.put((StoredObject)new ClientWorld(user));
        }
        if (!user.has((Class)EntityTracker.class)) {
            user.put((StoredObject)new EntityTracker(user));
        }
        user.put((StoredObject)new ShoulderTracker(user));
        ((EntityTracker)user.get((Class)EntityTracker.class)).initProtocol(this);
    }
    
    public EntityPackets1_12 getEntityPackets() {
        return this.entityPackets;
    }
    
    public BlockItemPackets1_12 getBlockItemPackets() {
        return this.blockItemPackets;
    }
}
