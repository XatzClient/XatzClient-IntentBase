// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets;

import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.meta.MetaHandler;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.VillagerData;
import us.myles.ViaVersion.api.type.types.Particle;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import us.myles.ViaVersion.api.minecraft.item.Item;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.type.types.version.Types1_13_2;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import java.util.function.Supplier;
import nl.matsv.viabackwards.api.entities.storage.EntityPositionStorage;
import nl.matsv.viabackwards.api.rewriters.EntityRewriterBase;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage.EntityPositionStorage1_14;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.entities.Entity1_14Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_13_2;
import nl.matsv.viabackwards.api.entities.storage.EntityPositionHandler;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_14 extends LegacyEntityRewriter<Protocol1_13_2To1_14>
{
    private EntityPositionHandler positionHandler;
    
    public EntityPackets1_14(final Protocol1_13_2To1_14 protocol) {
        super(protocol, (MetaType)MetaType1_13_2.OptChat, (MetaType)MetaType1_13_2.Boolean);
    }
    
    @Override
    protected void addTrackedEntity(final PacketWrapper wrapper, final int entityId, final EntityType type) throws Exception {
        super.addTrackedEntity(wrapper, entityId, type);
        if (type == Entity1_14Types.EntityType.PAINTING) {
            final Position position = (Position)wrapper.get(Type.POSITION, 0);
            this.positionHandler.cacheEntityPosition(wrapper, position.getX(), position.getY(), position.getZ(), true, false);
        }
        else if (wrapper.getId() != 37) {
            this.positionHandler.cacheEntityPosition(wrapper, true, false);
        }
    }
    
    @Override
    protected void registerPackets() {
        this.positionHandler = new EntityPositionHandler(this, EntityPositionStorage1_14.class, EntityPositionStorage1_14::new);
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_STATUS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final int entityId = (int)wrapper.passthrough(Type.INT);
                    final byte status = (byte)wrapper.passthrough(Type.BYTE);
                    if (status != 3) {
                        return;
                    }
                    final EntityTracker.ProtocolEntityTracker tracker = EntityPackets1_14.this.getEntityTracker(wrapper.user());
                    final EntityType entityType = tracker.getEntityType(entityId);
                    if (entityType != Entity1_14Types.EntityType.PLAYER) {
                        return;
                    }
                    for (int i = 0; i <= 5; ++i) {
                        final PacketWrapper equipmentPacket = wrapper.create(66);
                        equipmentPacket.write((Type)Type.VAR_INT, (Object)entityId);
                        equipmentPacket.write((Type)Type.VAR_INT, (Object)i);
                        equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, (Object)null);
                        equipmentPacket.send((Class)Protocol1_13_2To1_14.class, true, true);
                    }
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_TELEPORT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler(wrapper -> EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, false, false));
            }
        });
        final PacketRemapper relativeMoveHandler = new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final double x = (short)wrapper.get((Type)Type.SHORT, 0) / 4096.0;
                        final double y = (short)wrapper.get((Type)Type.SHORT, 1) / 4096.0;
                        final double z = (short)wrapper.get((Type)Type.SHORT, 2) / 4096.0;
                        EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, x, y, z, false, true);
                    }
                });
            }
        };
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_POSITION, relativeMoveHandler);
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_POSITION_AND_ROTATION, relativeMoveHandler);
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_ENTITY, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT, Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.handler(LegacyEntityRewriter.this.getObjectTrackerHandler());
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = (byte)wrapper.get(Type.BYTE, 0);
                        final int mappedId = EntityPackets1_14.this.getOldEntityId(id);
                        final Entity1_13Types.EntityType entityType = Entity1_13Types.getTypeFromId(mappedId, false);
                        Entity1_13Types.ObjectType objectType;
                        if (entityType.isOrHasParent((EntityType)Entity1_13Types.EntityType.MINECART_ABSTRACT)) {
                            objectType = Entity1_13Types.ObjectType.MINECART;
                            int data = 0;
                            switch (entityType) {
                                case CHEST_MINECART: {
                                    data = 1;
                                    break;
                                }
                                case FURNACE_MINECART: {
                                    data = 2;
                                    break;
                                }
                                case TNT_MINECART: {
                                    data = 3;
                                    break;
                                }
                                case SPAWNER_MINECART: {
                                    data = 4;
                                    break;
                                }
                                case HOPPER_MINECART: {
                                    data = 5;
                                    break;
                                }
                                case COMMAND_BLOCK_MINECART: {
                                    data = 6;
                                    break;
                                }
                            }
                            if (data != 0) {
                                wrapper.set(Type.INT, 0, (Object)data);
                            }
                        }
                        else {
                            objectType = Entity1_13Types.ObjectType.fromEntityType(entityType).orElse(null);
                        }
                        if (objectType == null) {
                            return;
                        }
                        wrapper.set(Type.BYTE, 0, (Object)(byte)objectType.getId());
                        int data = (int)wrapper.get(Type.INT, 0);
                        if (objectType == Entity1_13Types.ObjectType.FALLING_BLOCK) {
                            final int blockState = (int)wrapper.get(Type.INT, 0);
                            final int combined = ((Protocol1_13_2To1_14)EntityPackets1_14.this.protocol).getMappingData().getNewBlockStateId(blockState);
                            wrapper.set(Type.INT, 0, (Object)combined);
                        }
                        else if (entityType.isOrHasParent((EntityType)Entity1_13Types.EntityType.ABSTRACT_ARROW)) {
                            wrapper.set(Type.INT, 0, (Object)(data + 1));
                        }
                    }
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_MOB, (PacketRemapper)new PacketRemapper() {
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
                this.map(Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = (int)wrapper.get((Type)Type.VAR_INT, 1);
                        final Entity1_14Types.EntityType entityType = Entity1_14Types.getTypeFromId(type);
                        EntityPackets1_14.this.addTrackedEntity(wrapper, (int)wrapper.get((Type)Type.VAR_INT, 0), (EntityType)entityType);
                        final int oldId = EntityPackets1_14.this.typeMapping.get(type);
                        if (oldId == -1) {
                            final EntityData entityData = EntityRewriterBase.this.getEntityData((EntityType)entityType);
                            if (entityData == null) {
                                ViaBackwards.getPlatform().getLogger().warning("Could not find 1.13.2 entity type for 1.14 entity type " + type + "/" + entityType);
                                wrapper.cancel();
                            }
                            else {
                                wrapper.set((Type)Type.VAR_INT, 1, (Object)entityData.getReplacementId());
                            }
                        }
                        else {
                            wrapper.set((Type)Type.VAR_INT, 1, (Object)oldId);
                        }
                    }
                });
                this.handler(LegacyEntityRewriter.this.getMobSpawnRewriter((Type<List<Metadata>>)Types1_13_2.METADATA_LIST));
            }
        });
        this.getProtocol().registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_EXPERIENCE_ORB, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, (int)wrapper.get((Type)Type.VAR_INT, 0), (EntityType)Entity1_14Types.EntityType.EXPERIENCE_ORB));
            }
        });
        this.getProtocol().registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_GLOBAL_ENTITY, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, (int)wrapper.get((Type)Type.VAR_INT, 0), (EntityType)Entity1_14Types.EntityType.LIGHTNING_BOLT));
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PAINTING, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT);
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.BYTE);
                this.handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, (int)wrapper.get((Type)Type.VAR_INT, 0), (EntityType)Entity1_14Types.EntityType.PAINTING));
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PLAYER, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler((Type<List<Metadata>>)Types1_13_2.METADATA_LIST, (EntityType)Entity1_14Types.EntityType.PLAYER));
                this.handler(wrapper -> EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, true, false));
            }
        });
        this.registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_14.DESTROY_ENTITIES);
        this.registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_14.ENTITY_METADATA, (Type<List<Metadata>>)Types1_14.METADATA_LIST, (Type<List<Metadata>>)Types1_13_2.METADATA_LIST);
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.JOIN_GAME, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.handler(EntityRewriterBase.this.getTrackerHandler((EntityType)Entity1_14Types.EntityType.PLAYER, Type.INT));
                this.handler(EntityRewriterBase.this.getDimensionHandler(1));
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.UNSIGNED_BYTE, (Object)0);
                        wrapper.passthrough(Type.UNSIGNED_BYTE);
                        wrapper.passthrough(Type.STRING);
                        wrapper.read((Type)Type.VAR_INT);
                    }
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.RESPAWN, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
                        final int dimensionId = (int)wrapper.get(Type.INT, 0);
                        clientWorld.setEnvironment(dimensionId);
                        wrapper.write(Type.UNSIGNED_BYTE, (Object)0);
                        ((ChunkLightStorage)wrapper.user().get((Class)ChunkLightStorage.class)).clear();
                    }
                });
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.mapTypes((EntityType[])Entity1_14Types.EntityType.values(), Entity1_13Types.EntityType.class);
        this.mapEntity((EntityType)Entity1_14Types.EntityType.CAT, (EntityType)Entity1_14Types.EntityType.OCELOT).jsonName("Cat");
        this.mapEntity((EntityType)Entity1_14Types.EntityType.TRADER_LLAMA, (EntityType)Entity1_14Types.EntityType.LLAMA).jsonName("Trader Llama");
        this.mapEntity((EntityType)Entity1_14Types.EntityType.FOX, (EntityType)Entity1_14Types.EntityType.WOLF).jsonName("Fox");
        this.mapEntity((EntityType)Entity1_14Types.EntityType.PANDA, (EntityType)Entity1_14Types.EntityType.POLAR_BEAR).jsonName("Panda");
        this.mapEntity((EntityType)Entity1_14Types.EntityType.PILLAGER, (EntityType)Entity1_14Types.EntityType.VILLAGER).jsonName("Pillager");
        this.mapEntity((EntityType)Entity1_14Types.EntityType.WANDERING_TRADER, (EntityType)Entity1_14Types.EntityType.VILLAGER).jsonName("Wandering Trader");
        this.mapEntity((EntityType)Entity1_14Types.EntityType.RAVAGER, (EntityType)Entity1_14Types.EntityType.COW).jsonName("Ravager");
        final Metadata meta;
        final int typeId;
        final MetaType type;
        Item item;
        int blockstate;
        this.registerMetaHandler().handle(e -> {
            meta = e.getData();
            typeId = meta.getMetaType().getTypeID();
            if (typeId <= 15) {
                meta.setMetaType((MetaType)MetaType1_13_2.byId(typeId));
            }
            type = meta.getMetaType();
            if (type == MetaType1_13_2.Slot) {
                item = (Item)meta.getValue();
                meta.setValue((Object)this.getProtocol().getBlockItemPackets().handleItemToClient(item));
            }
            else if (type == MetaType1_13_2.BlockID) {
                blockstate = (int)meta.getValue();
                meta.setValue((Object)((Protocol1_13_2To1_14)this.protocol).getMappingData().getNewBlockStateId(blockstate));
            }
            return meta;
        });
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PILLAGER, 15).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.FOX, 15).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.FOX, 16).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.FOX, 17).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.FOX, 18).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 15).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 16).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 17).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 18).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 19).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.PANDA, 20).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.CAT, 18).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.CAT, 19).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.CAT, 20).removed();
        final EntityType type2;
        final Metadata meta2;
        int index;
        this.registerMetaHandler().handle(e -> {
            type2 = e.getEntity().getType();
            meta2 = e.getData();
            if (type2.isOrHasParent((EntityType)Entity1_14Types.EntityType.ABSTRACT_ILLAGER_BASE) || type2 == Entity1_14Types.EntityType.RAVAGER || type2 == Entity1_14Types.EntityType.WITCH) {
                index = e.getIndex();
                if (index == 14) {
                    throw RemovedValueException.EX;
                }
                else if (index > 14) {
                    meta2.setId(index - 1);
                }
            }
            return meta2;
        });
        final Metadata meta3;
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.AREA_EFFECT_CLOUD, 10).handle(e -> {
            meta3 = e.getData();
            this.rewriteParticle((Particle)meta3.getValue());
            return meta3;
        });
        final Metadata meta4;
        final Integer value;
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.FIREWORK_ROCKET, 8).handle(e -> {
            meta4 = e.getData();
            meta4.setMetaType((MetaType)MetaType1_13_2.VarInt);
            value = (Integer)meta4.getValue();
            if (value == null) {
                meta4.setValue((Object)0);
            }
            return meta4;
        });
        final Metadata meta5;
        final int index2;
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.ABSTRACT_ARROW, true).handle(e -> {
            meta5 = e.getData();
            index2 = e.getIndex();
            if (index2 == 9) {
                throw RemovedValueException.EX;
            }
            else {
                if (index2 > 9) {
                    meta5.setId(index2 - 1);
                }
                return meta5;
            }
        });
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.VILLAGER, 15).removed();
        final Metadata meta6;
        final VillagerData villagerData;
        final MetaHandler villagerDataHandler = e -> {
            meta6 = e.getData();
            villagerData = (VillagerData)meta6.getValue();
            meta6.setValue((Object)this.villagerDataToProfession(villagerData));
            meta6.setMetaType((MetaType)MetaType1_13_2.VarInt);
            if (meta6.getId() == 16) {
                meta6.setId(15);
            }
            return meta6;
        };
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.ZOMBIE_VILLAGER, 18).handle(villagerDataHandler);
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.VILLAGER, 16).handle(villagerDataHandler);
        final byte value2;
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.ABSTRACT_SKELETON, true, 13).handle(e -> {
            value2 = (byte)e.getData().getValue();
            if ((value2 & 0x4) != 0x0) {
                e.createMeta(new Metadata(14, (MetaType)MetaType1_13_2.Boolean, (Object)true));
            }
            return e.getData();
        });
        final byte value3;
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.ZOMBIE, true, 13).handle(e -> {
            value3 = (byte)e.getData().getValue();
            if ((value3 & 0x4) != 0x0) {
                e.createMeta(new Metadata(16, (MetaType)MetaType1_13_2.Boolean, (Object)true));
            }
            return e.getData();
        });
        final Metadata meta7;
        final int index3;
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.ZOMBIE, true).handle(e -> {
            meta7 = e.getData();
            index3 = e.getIndex();
            if (index3 >= 16) {
                meta7.setId(index3 + 1);
            }
            return meta7;
        });
        final Metadata meta8;
        final int index4;
        Position position;
        PacketWrapper wrapper;
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.LIVINGENTITY, true).handle(e -> {
            meta8 = e.getData();
            index4 = e.getIndex();
            if (index4 == 12) {
                position = (Position)meta8.getValue();
                if (position != null) {
                    wrapper = new PacketWrapper(51, (ByteBuf)null, e.getUser());
                    wrapper.write((Type)Type.VAR_INT, (Object)e.getEntity().getEntityId());
                    wrapper.write(Type.POSITION, (Object)position);
                    try {
                        wrapper.send((Class)Protocol1_13_2To1_14.class);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                throw RemovedValueException.EX;
            }
            else {
                if (index4 > 12) {
                    meta8.setId(index4 - 1);
                }
                return meta8;
            }
        });
        final Metadata meta9;
        final int index5;
        this.registerMetaHandler().handle(e -> {
            meta9 = e.getData();
            index5 = e.getIndex();
            if (index5 == 6) {
                throw RemovedValueException.EX;
            }
            else {
                if (index5 > 6) {
                    meta9.setId(index5 - 1);
                }
                return meta9;
            }
        });
        final Metadata meta10;
        final int typeId2;
        this.registerMetaHandler().handle(e -> {
            meta10 = e.getData();
            typeId2 = meta10.getMetaType().getTypeID();
            if (typeId2 > 15) {
                ViaBackwards.getPlatform().getLogger().warning("New 1.14 metadata was not handled: " + meta10 + " entity: " + e.getEntity().getType());
                return null;
            }
            else {
                return meta10;
            }
        });
        final Metadata meta11;
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.OCELOT, 13).handle(e -> {
            meta11 = e.getData();
            meta11.setId(15);
            meta11.setMetaType((MetaType)MetaType1_13_2.VarInt);
            meta11.setValue((Object)0);
            return meta11;
        });
        final Metadata meta12;
        this.registerMetaHandler().filter((EntityType)Entity1_14Types.EntityType.CAT).handle(e -> {
            meta12 = e.getData();
            if (meta12.getId() == 15) {
                meta12.setValue((Object)1);
            }
            else if (meta12.getId() == 13) {
                meta12.setValue((Object)(byte)((byte)meta12.getValue() & 0x4));
            }
            return meta12;
        });
    }
    
    public int villagerDataToProfession(final VillagerData data) {
        switch (data.getProfession()) {
            case 1:
            case 10:
            case 13:
            case 14: {
                return 3;
            }
            case 2:
            case 8: {
                return 4;
            }
            case 3:
            case 9: {
                return 1;
            }
            case 4: {
                return 2;
            }
            case 5:
            case 6:
            case 7:
            case 12: {
                return 0;
            }
            default: {
                return 5;
            }
        }
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_14Types.getTypeFromId(typeId);
    }
}
