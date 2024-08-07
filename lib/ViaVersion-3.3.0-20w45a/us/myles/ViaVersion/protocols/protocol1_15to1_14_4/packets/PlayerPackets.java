// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_15to1_14_4.packets;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_15Types;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.storage.EntityTracker1_15;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.api.protocol.Protocol;

public class PlayerPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(ClientboundPackets1_14.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.create(wrapper -> wrapper.write(Type.LONG, 0L));
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_14.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                final EntityTracker1_15 tracker;
                final int entityId;
                this.handler(wrapper -> {
                    tracker = wrapper.user().get(EntityTracker1_15.class);
                    entityId = wrapper.get(Type.INT, 0);
                    tracker.addEntity(entityId, Entity1_15Types.EntityType.PLAYER);
                    return;
                });
                this.create(wrapper -> wrapper.write(Type.LONG, 0L));
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.create(wrapper -> wrapper.write(Type.BOOLEAN, !Via.getConfig().is1_15InstantRespawn()));
            }
        });
    }
}
