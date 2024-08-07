// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9_1to1_9;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_9_1To1_9 extends Protocol<ClientboundPackets1_9, ClientboundPackets1_9, ServerboundPackets1_9, ServerboundPackets1_9>
{
    public Protocol1_9_1To1_9() {
        super(ClientboundPackets1_9.class, ClientboundPackets1_9.class, ServerboundPackets1_9.class, ServerboundPackets1_9.class);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol<ClientboundPackets1_9, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE, Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.BOOLEAN);
            }
        });
        ((Protocol<ClientboundPackets1_9, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_9.SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int sound = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (sound >= 415) {
                            wrapper.set(Type.VAR_INT, 0, sound + 1);
                        }
                    }
                });
            }
        });
    }
}
