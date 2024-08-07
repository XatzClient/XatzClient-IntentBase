// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_14_3to1_14_2;

import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_14_3To1_14_2 extends Protocol<ClientboundPackets1_14, ClientboundPackets1_14, ServerboundPackets1_14, ServerboundPackets1_14>
{
    public Protocol1_14_3To1_14_2() {
        super(ClientboundPackets1_14.class, ClientboundPackets1_14.class, null, null);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol<ClientboundPackets1_14, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_14.TRADE_LIST, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        for (int size = wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
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
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                        }
                        wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        final boolean regularVillager = wrapper.passthrough(Type.BOOLEAN);
                        wrapper.write(Type.BOOLEAN, regularVillager);
                    }
                });
            }
        });
    }
}
