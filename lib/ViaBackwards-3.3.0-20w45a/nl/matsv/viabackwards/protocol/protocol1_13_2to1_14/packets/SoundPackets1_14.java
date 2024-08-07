// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets;

import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage.EntityPositionStorage1_14;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import nl.matsv.viabackwards.api.rewriters.Rewriter;

public class SoundPackets1_14 extends Rewriter<Protocol1_13_2To1_14>
{
    public SoundPackets1_14(final Protocol1_13_2To1_14 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        final SoundRewriter soundRewriter = new SoundRewriter(this.protocol);
        soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_14.SOUND);
        soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_14.NAMED_SOUND);
        soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_14.STOP_SOUND);
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_SOUND, (ClientboundPacketType)null, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.cancel();
                    final int soundId = (int)wrapper.read((Type)Type.VAR_INT);
                    final int newId = ((Protocol1_13_2To1_14)SoundPackets1_14.this.protocol).getMappingData().getSoundMappings().getNewId(soundId);
                    if (newId == -1) {
                        return;
                    }
                    final int category = (int)wrapper.read((Type)Type.VAR_INT);
                    final int entityId = (int)wrapper.read((Type)Type.VAR_INT);
                    final EntityTracker.StoredEntity storedEntity = ((EntityTracker)wrapper.user().get((Class)EntityTracker.class)).get(SoundPackets1_14.this.protocol).getEntity(entityId);
                    final EntityPositionStorage1_14 entityStorage;
                    if (storedEntity == null || (entityStorage = storedEntity.get(EntityPositionStorage1_14.class)) == null) {
                        ViaBackwards.getPlatform().getLogger().warning("Untracked entity with id " + entityId);
                        return;
                    }
                    final float volume = (float)wrapper.read((Type)Type.FLOAT);
                    final float pitch = (float)wrapper.read((Type)Type.FLOAT);
                    final int x = (int)(entityStorage.getX() * 8.0);
                    final int y = (int)(entityStorage.getY() * 8.0);
                    final int z = (int)(entityStorage.getZ() * 8.0);
                    final PacketWrapper soundPacket = wrapper.create(77);
                    soundPacket.write((Type)Type.VAR_INT, (Object)newId);
                    soundPacket.write((Type)Type.VAR_INT, (Object)category);
                    soundPacket.write(Type.INT, (Object)x);
                    soundPacket.write(Type.INT, (Object)y);
                    soundPacket.write(Type.INT, (Object)z);
                    soundPacket.write((Type)Type.FLOAT, (Object)volume);
                    soundPacket.write((Type)Type.FLOAT, (Object)pitch);
                    soundPacket.send((Class)Protocol1_13_2To1_14.class);
                });
            }
        });
    }
}
