// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13_1to1_13_2.packets;

import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_13_1to1_13_2.Protocol1_13_1To1_13_2;

public class WorldPackets1_13_2
{
    public static void register(final Protocol1_13_1To1_13_2 protocol) {
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PARTICLE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = (int)wrapper.get(Type.INT, 0);
                        if (id == 27) {
                            wrapper.write(Type.FLAT_ITEM, wrapper.read(Type.FLAT_VAR_INT_ITEM));
                        }
                    }
                });
            }
        });
    }
}
