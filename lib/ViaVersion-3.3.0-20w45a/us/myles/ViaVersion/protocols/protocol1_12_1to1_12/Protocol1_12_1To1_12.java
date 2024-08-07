// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_12_1to1_12;

import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_12_1To1_12 extends Protocol<ClientboundPackets1_12, ClientboundPackets1_12_1, ServerboundPackets1_12, ServerboundPackets1_12_1>
{
    public Protocol1_12_1To1_12() {
        super(ClientboundPackets1_12.class, ClientboundPackets1_12_1.class, ServerboundPackets1_12.class, ServerboundPackets1_12_1.class);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol<C1, C2, S1, ServerboundPackets1_12_1>)this).cancelIncoming(ServerboundPackets1_12_1.CRAFT_RECIPE_REQUEST);
    }
}
