// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_16_3to1_16_4;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import nl.matsv.viabackwards.protocol.protocol1_16_3to1_16_4.storage.PlayerHandStorage;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_16_3To1_16_4 extends BackwardsProtocol<ClientboundPackets1_16_2, ClientboundPackets1_16_2, ServerboundPackets1_16_2, ServerboundPackets1_16_2>
{
    public Protocol1_16_3To1_16_4() {
        super(ClientboundPackets1_16_2.class, ClientboundPackets1_16_2.class, ServerboundPackets1_16_2.class, ServerboundPackets1_16_2.class);
    }
    
    protected void registerPackets() {
        this.registerIncoming((ServerboundPacketType)ServerboundPackets1_16_2.EDIT_BOOK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.FLAT_VAR_INT_ITEM);
                this.map(Type.BOOLEAN);
                this.handler(wrapper -> {
                    final int slot = (int)wrapper.read((Type)Type.VAR_INT);
                    if (slot == 1) {
                        wrapper.write((Type)Type.VAR_INT, (Object)40);
                    }
                    else {
                        wrapper.write((Type)Type.VAR_INT, (Object)((PlayerHandStorage)wrapper.user().get((Class)PlayerHandStorage.class)).getCurrentHand());
                    }
                });
            }
        });
        this.registerIncoming((ServerboundPacketType)ServerboundPackets1_16_2.HELD_ITEM_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final short slot = (short)wrapper.passthrough((Type)Type.SHORT);
                    ((PlayerHandStorage)wrapper.user().get((Class)PlayerHandStorage.class)).setCurrentHand(slot);
                });
            }
        });
    }
    
    public void init(final UserConnection user) {
        user.put((StoredObject)new PlayerHandStorage(user));
    }
}
