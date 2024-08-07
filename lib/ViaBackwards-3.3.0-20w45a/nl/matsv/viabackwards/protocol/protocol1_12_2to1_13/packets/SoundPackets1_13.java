// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.Via;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.NamedSoundMapping;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import nl.matsv.viabackwards.api.rewriters.Rewriter;

public class SoundPackets1_13 extends Rewriter<Protocol1_12_2To1_13>
{
    private static final String[] SOUND_SOURCES;
    
    public SoundPackets1_13(final Protocol1_12_2To1_13 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.NAMED_SOUND, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    String newSound = (String)wrapper.get(Type.STRING, 0);
                    if (newSound.startsWith("minecraft:")) {
                        newSound = newSound.substring(10);
                    }
                    String oldSound = NamedSoundMapping.getOldId(newSound);
                    if (oldSound != null || (oldSound = ((Protocol1_12_2To1_13)SoundPackets1_13.this.protocol).getMappingData().getMappedNamedSound(newSound)) != null) {
                        wrapper.set(Type.STRING, 0, (Object)oldSound);
                    }
                    else if (!Via.getConfig().isSuppressConversionWarnings()) {
                        ViaBackwards.getPlatform().getLogger().warning("Unknown named sound in 1.13->1.12 protocol: " + newSound);
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.STOP_SOUND, (ClientboundPacketType)ClientboundPackets1_12_1.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.write(Type.STRING, (Object)"MC|StopSound");
                    final byte flags = (byte)wrapper.read(Type.BYTE);
                    String source;
                    if ((flags & 0x1) != 0x0) {
                        source = SoundPackets1_13.SOUND_SOURCES[(int)wrapper.read((Type)Type.VAR_INT)];
                    }
                    else {
                        source = "";
                    }
                    String sound;
                    if ((flags & 0x2) != 0x0) {
                        String newSound = (String)wrapper.read(Type.STRING);
                        if (newSound.startsWith("minecraft:")) {
                            newSound = newSound.substring(10);
                        }
                        sound = ((Protocol1_12_2To1_13)SoundPackets1_13.this.protocol).getMappingData().getMappedNamedSound(newSound);
                        if (sound == null) {
                            sound = "";
                        }
                    }
                    else {
                        sound = "";
                    }
                    wrapper.write(Type.STRING, (Object)source);
                    wrapper.write(Type.STRING, (Object)sound);
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SOUND, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int newSound = (int)wrapper.get((Type)Type.VAR_INT, 0);
                    final int oldSound = ((Protocol1_12_2To1_13)SoundPackets1_13.this.protocol).getMappingData().getSoundMappings().getNewId(newSound);
                    if (oldSound == -1) {
                        wrapper.cancel();
                    }
                    else {
                        wrapper.set((Type)Type.VAR_INT, 0, (Object)oldSound);
                    }
                });
            }
        });
    }
    
    static {
        SOUND_SOURCES = new String[] { "master", "music", "record", "weather", "block", "hostile", "neutral", "player", "ambient", "voice" };
    }
}
