// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.packets;

import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.types.version.Types1_13;
import us.myles.ViaVersion.api.type.types.version.Types1_12;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage.EntityTracker1_13;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.metadata.MetadataRewriter1_13To1_12_2;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;

public class EntityPackets
{
    public static void register(final Protocol1_13To1_12_2 protocol) {
        final MetadataRewriter1_13To1_12_2 metadataRewriter = protocol.get(MetadataRewriter1_13To1_12_2.class);
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_12_1.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final byte type = wrapper.get(Type.BYTE, 0);
                        final Entity1_13Types.EntityType entType = Entity1_13Types.getTypeFromId(type, true);
                        if (entType != null) {
                            if (entType.is(Entity1_13Types.EntityType.FALLING_BLOCK)) {
                                final int oldId = wrapper.get(Type.INT, 0);
                                final int combined = (oldId & 0xFFF) << 4 | (oldId >> 12 & 0xF);
                                wrapper.set(Type.INT, 0, WorldPackets.toNewId(combined));
                            }
                            if (entType.is(Entity1_13Types.EntityType.ITEM_FRAME)) {
                                int data = wrapper.get(Type.INT, 0);
                                switch (data) {
                                    case 0: {
                                        data = 3;
                                        break;
                                    }
                                    case 1: {
                                        data = 4;
                                        break;
                                    }
                                    case 3: {
                                        data = 5;
                                        break;
                                    }
                                }
                                wrapper.set(Type.INT, 0, data);
                                wrapper.user().get(EntityTracker1_13.class).addEntity(entityId, entType);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_12_1.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Types1_12.METADATA_LIST, Types1_13.METADATA_LIST);
                this.handler(metadataRewriter.getTrackerAndRewriter(Types1_13.METADATA_LIST));
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_12_1.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_12.METADATA_LIST, Types1_13.METADATA_LIST);
                this.handler(metadataRewriter.getTrackerAndRewriter(Types1_13.METADATA_LIST, Entity1_13Types.EntityType.PLAYER));
            }
        });
        metadataRewriter.registerEntityDestroy(ClientboundPackets1_12_1.DESTROY_ENTITIES);
        metadataRewriter.registerMetadataRewriter(ClientboundPackets1_12_1.ENTITY_METADATA, Types1_12.METADATA_LIST, Types1_13.METADATA_LIST);
    }
}
