// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.rewriters;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class SoundRewriter extends us.myles.ViaVersion.api.rewriters.SoundRewriter
{
    private final BackwardsProtocol protocol;
    
    public SoundRewriter(final BackwardsProtocol protocol) {
        super((Protocol)protocol);
        this.protocol = protocol;
    }
    
    public void registerNamedSound(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    String soundId = (String)wrapper.get(Type.STRING, 0);
                    if (soundId.startsWith("minecraft:")) {
                        soundId = soundId.substring(10);
                    }
                    final String mappedId = SoundRewriter.this.protocol.getMappingData().getMappedNamedSound(soundId);
                    if (mappedId == null) {
                        return;
                    }
                    if (!mappedId.isEmpty()) {
                        wrapper.set(Type.STRING, 0, (Object)mappedId);
                    }
                    else {
                        wrapper.cancel();
                    }
                });
            }
        });
    }
    
    public void registerStopSound(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final byte flags = (byte)wrapper.passthrough(Type.BYTE);
                    if ((flags & 0x2) == 0x0) {
                        return;
                    }
                    if ((flags & 0x1) != 0x0) {
                        wrapper.passthrough((Type)Type.VAR_INT);
                    }
                    String soundId = (String)wrapper.read(Type.STRING);
                    if (soundId.startsWith("minecraft:")) {
                        soundId = soundId.substring(10);
                    }
                    final String mappedId = SoundRewriter.this.protocol.getMappingData().getMappedNamedSound(soundId);
                    if (mappedId == null) {
                        wrapper.write(Type.STRING, (Object)soundId);
                        return;
                    }
                    if (!mappedId.isEmpty()) {
                        wrapper.write(Type.STRING, (Object)mappedId);
                    }
                    else {
                        wrapper.cancel();
                    }
                });
            }
        });
    }
}
