// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_14_3to1_14_4;

import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_14_3To1_14_4 extends BackwardsProtocol<ClientboundPackets1_14, ClientboundPackets1_14, ServerboundPackets1_14, ServerboundPackets1_14>
{
    public Protocol1_14_3To1_14_4() {
        super(ClientboundPackets1_14.class, ClientboundPackets1_14.class, ServerboundPackets1_14.class, ServerboundPackets1_14.class);
    }
    
    protected void registerPackets() {
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ACKNOWLEDGE_PLAYER_DIGGING, (ClientboundPacketType)ClientboundPackets1_14.BLOCK_CHANGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION1_14);
                this.map((Type)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int status = (int)wrapper.read((Type)Type.VAR_INT);
                    final boolean allGood = (boolean)wrapper.read(Type.BOOLEAN);
                    if (allGood && status == 0) {
                        wrapper.cancel();
                    }
                });
            }
        });
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.TRADE_LIST, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough((Type)Type.VAR_INT);
                        for (int size = (short)wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                            wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                            wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                            if (wrapper.passthrough(Type.BOOLEAN)) {
                                wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                            }
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough((Type)Type.FLOAT);
                            wrapper.read(Type.INT);
                        }
                    }
                });
            }
        });
    }
}
