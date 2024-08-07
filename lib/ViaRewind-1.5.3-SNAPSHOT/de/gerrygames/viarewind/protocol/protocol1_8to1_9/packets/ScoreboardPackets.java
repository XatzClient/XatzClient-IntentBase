// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class ScoreboardPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 56, 61);
        protocol.registerOutgoing(State.PLAY, 63, 59);
        protocol.registerOutgoing(State.PLAY, 65, 62, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final byte mode = (byte)packetWrapper.get(Type.BYTE, 0);
                        if (mode == 0 || mode == 2) {
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough(Type.BYTE);
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.read(Type.STRING);
                            packetWrapper.passthrough(Type.BYTE);
                        }
                        if (mode == 0 || mode == 3 || mode == 4) {
                            packetWrapper.passthrough(Type.STRING_ARRAY);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 66, 60);
    }
}
