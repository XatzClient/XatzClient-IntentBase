// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16_4to1_16_3;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_16_4To1_16_3 extends Protocol<ClientboundPackets1_16_2, ClientboundPackets1_16_2, ServerboundPackets1_16_2, ServerboundPackets1_16_2>
{
    public Protocol1_16_4To1_16_3() {
        super(ClientboundPackets1_16_2.class, ClientboundPackets1_16_2.class, ServerboundPackets1_16_2.class, ServerboundPackets1_16_2.class);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol<C1, C2, S1, ServerboundPackets1_16_2>)this).registerIncoming(ServerboundPackets1_16_2.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLAT_VAR_INT_ITEM);
                this.map(Type.BOOLEAN);
                final int slot;
                this.handler(wrapper -> {
                    slot = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.VAR_INT, (slot == 40) ? 1 : 0);
                });
            }
        });
    }
}
