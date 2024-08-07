// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_1to1_12_2;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_12_1To1_12_2 extends BackwardsProtocol<ClientboundPackets1_12_1, ClientboundPackets1_12_1, ServerboundPackets1_12_1, ServerboundPackets1_12_1>
{
    public Protocol1_12_1To1_12_2() {
        super(ClientboundPackets1_12_1.class, ClientboundPackets1_12_1.class, ServerboundPackets1_12_1.class, ServerboundPackets1_12_1.class);
    }
    
    protected void registerPackets() {
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_12_1.KEEP_ALIVE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Long keepAlive = (Long)packetWrapper.read(Type.LONG);
                        ((KeepAliveTracker)packetWrapper.user().get((Class)KeepAliveTracker.class)).setKeepAlive(keepAlive);
                        packetWrapper.write((Type)Type.VAR_INT, (Object)keepAlive.hashCode());
                    }
                });
            }
        });
        this.registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.KEEP_ALIVE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int keepAlive = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final long realKeepAlive = ((KeepAliveTracker)packetWrapper.user().get((Class)KeepAliveTracker.class)).getKeepAlive();
                        if (keepAlive != Long.hashCode(realKeepAlive)) {
                            packetWrapper.cancel();
                            return;
                        }
                        packetWrapper.write(Type.LONG, (Object)realKeepAlive);
                        ((KeepAliveTracker)packetWrapper.user().get((Class)KeepAliveTracker.class)).setKeepAlive(2147483647L);
                    }
                });
            }
        });
    }
    
    public void init(final UserConnection userConnection) {
        userConnection.put((StoredObject)new KeepAliveTracker(userConnection));
    }
}
