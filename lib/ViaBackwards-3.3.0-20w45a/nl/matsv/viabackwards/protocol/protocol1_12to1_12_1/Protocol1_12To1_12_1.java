// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12to1_12_1;

import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_12To1_12_1 extends BackwardsProtocol<ClientboundPackets1_12_1, ClientboundPackets1_12, ServerboundPackets1_12_1, ServerboundPackets1_12>
{
    public Protocol1_12To1_12_1() {
        super(ClientboundPackets1_12_1.class, ClientboundPackets1_12.class, ServerboundPackets1_12_1.class, ServerboundPackets1_12.class);
    }
    
    protected void registerPackets() {
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_12_1.CRAFT_RECIPE_RESPONSE);
        this.cancelIncoming((ServerboundPacketType)ServerboundPackets1_12.PREPARE_CRAFTING_GRID);
    }
}
