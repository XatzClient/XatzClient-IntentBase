// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.packets;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import java.util.ArrayList;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data.EntityTypeMapping;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_15Types;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data.ImmediateRespawn;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.Protocol1_14_4To1_15;
import nl.matsv.viabackwards.api.rewriters.EntityRewriter;

public class EntityPackets1_15 extends EntityRewriter<Protocol1_14_4To1_15>
{
    public EntityPackets1_15(final Protocol1_14_4To1_15 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.UPDATE_HEALTH, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final float health = (float)wrapper.passthrough((Type)Type.FLOAT);
                    if (health > 0.0f) {
                        return;
                    }
                    if (!((ImmediateRespawn)wrapper.user().get((Class)ImmediateRespawn.class)).isImmediateRespawn()) {
                        return;
                    }
                    final PacketWrapper statusPacket = wrapper.create(4);
                    statusPacket.write((Type)Type.VAR_INT, (Object)0);
                    statusPacket.sendToServer((Class)Protocol1_14_4To1_15.class);
                });
            }
        });
        ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.GAME_EVENT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.FLOAT);
                this.handler(wrapper -> {
                    if ((short)wrapper.get(Type.UNSIGNED_BYTE, 0) == 11) {
                        ((ImmediateRespawn)wrapper.user().get((Class)ImmediateRespawn.class)).setImmediateRespawn((float)wrapper.get((Type)Type.FLOAT, 0) == 1.0f);
                    }
                });
            }
        });
        this.registerSpawnTrackerWithData((ClientboundPacketType)ClientboundPackets1_15.SPAWN_ENTITY, (EntityType)Entity1_15Types.EntityType.FALLING_BLOCK);
        ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.SPAWN_MOB, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.create(wrapper -> wrapper.write(Types1_14.METADATA_LIST, (Object)new ArrayList()));
                this.handler(wrapper -> {
                    final int type = (int)wrapper.get((Type)Type.VAR_INT, 1);
                    final Entity1_15Types.EntityType entityType = Entity1_15Types.getTypeFromId(type);
                    EntityRewriterBase.this.addTrackedEntity(wrapper, (int)wrapper.get((Type)Type.VAR_INT, 0), (EntityType)entityType);
                    wrapper.set((Type)Type.VAR_INT, 1, (Object)EntityTypeMapping.getOldEntityId(type));
                });
            }
        });
        ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.RESPAWN, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.LONG, Type.NOTHING);
            }
        });
        ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.JOIN_GAME, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.map(Type.LONG, Type.NOTHING);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map((Type)Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.handler(EntityRewriterBase.this.getTrackerHandler((EntityType)Entity1_15Types.EntityType.PLAYER, Type.INT));
                this.handler(wrapper -> ((ImmediateRespawn)wrapper.user().get((Class)ImmediateRespawn.class)).setImmediateRespawn((boolean)wrapper.read(Type.BOOLEAN)));
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_15.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_15Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_15.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_15Types.EntityType.LIGHTNING_BOLT);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_15.SPAWN_PAINTING, (EntityType)Entity1_15Types.EntityType.PAINTING);
        ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.SPAWN_PLAYER, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.create(wrapper -> wrapper.write(Types1_14.METADATA_LIST, (Object)new ArrayList()));
                this.handler(EntityRewriterBase.this.getTrackerHandler((EntityType)Entity1_15Types.EntityType.PLAYER, (Type)Type.VAR_INT));
            }
        });
        this.registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_15.DESTROY_ENTITIES);
        this.registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_15.ENTITY_METADATA, (Type<List<Metadata>>)Types1_14.METADATA_LIST);
        ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.ENTITY_PROPERTIES, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
                    final EntityType entityType = EntityRewriterBase.this.getEntityType(wrapper.user(), entityId);
                    if (entityType != Entity1_15Types.EntityType.BEE) {
                        return;
                    }
                    int newSize;
                    final int size = newSize = (int)wrapper.get(Type.INT, 0);
                    for (int i = 0; i < size; ++i) {
                        final String key = (String)wrapper.read(Type.STRING);
                        if (key.equals("generic.flyingSpeed")) {
                            --newSize;
                            wrapper.read(Type.DOUBLE);
                            for (int modSize = (int)wrapper.read((Type)Type.VAR_INT), j = 0; j < modSize; ++j) {
                                wrapper.read(Type.UUID);
                                wrapper.read(Type.DOUBLE);
                                wrapper.read(Type.BYTE);
                            }
                        }
                        else {
                            wrapper.write(Type.STRING, (Object)key);
                            wrapper.passthrough(Type.DOUBLE);
                            for (int modSize = (int)wrapper.passthrough((Type)Type.VAR_INT), j = 0; j < modSize; ++j) {
                                wrapper.passthrough(Type.UUID);
                                wrapper.passthrough(Type.DOUBLE);
                                wrapper.passthrough(Type.BYTE);
                            }
                        }
                    }
                    if (newSize != size) {
                        wrapper.set(Type.INT, 0, (Object)newSize);
                    }
                });
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        final Metadata meta;
        final MetaType type;
        Item item;
        int blockstate;
        this.registerMetaHandler().handle(e -> {
            meta = e.getData();
            type = meta.getMetaType();
            if (type == MetaType1_14.Slot) {
                item = (Item)meta.getValue();
                meta.setValue((Object)((Protocol1_14_4To1_15)this.protocol).getBlockItemPackets().handleItemToClient(item));
            }
            else if (type == MetaType1_14.BlockID) {
                blockstate = (int)meta.getValue();
                meta.setValue((Object)((Protocol1_14_4To1_15)this.protocol).getMappingData().getNewBlockStateId(blockstate));
            }
            else if (type == MetaType1_14.PARTICLE) {
                this.rewriteParticle((Particle)meta.getValue());
            }
            return meta;
        });
        final int index;
        this.registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.LIVINGENTITY, true).handle(e -> {
            index = e.getIndex();
            if (index == 12) {
                throw RemovedValueException.EX;
            }
            else {
                if (index > 12) {
                    e.getData().setId(index - 1);
                }
                return e.getData();
            }
        });
        this.registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.BEE, 15).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.BEE, 16).removed();
        this.mapEntity((EntityType)Entity1_15Types.EntityType.BEE, (EntityType)Entity1_15Types.EntityType.PUFFERFISH).jsonName("Bee").spawnMetadata(storage -> {
            storage.add(new Metadata(14, (MetaType)MetaType1_14.Boolean, (Object)false));
            storage.add(new Metadata(15, (MetaType)MetaType1_14.VarInt, (Object)2));
            return;
        });
        this.registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.ENDERMAN, 16).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.TRIDENT, 10).removed();
        final int index2;
        this.registerMetaHandler().filter((EntityType)Entity1_15Types.EntityType.WOLF).handle(e -> {
            index2 = e.getIndex();
            if (index2 >= 17) {
                e.getData().setId(index2 + 1);
            }
            return e.getData();
        });
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_15Types.getTypeFromId(typeId);
    }
    
    @Override
    public int getOldEntityId(final int newId) {
        return EntityTypeMapping.getOldEntityId(newId);
    }
}
