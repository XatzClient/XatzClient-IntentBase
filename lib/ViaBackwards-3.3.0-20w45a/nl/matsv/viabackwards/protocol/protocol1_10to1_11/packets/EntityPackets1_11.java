// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets;

import us.myles.ViaVersion.api.data.UserConnection;
import java.util.function.Function;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.storage.EntityStorage;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.storage.ChestedHorseStorage;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import nl.matsv.viabackwards.api.entities.storage.EntityData;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import java.util.List;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.ObjectType;
import nl.matsv.viabackwards.utils.Block;
import java.util.Optional;
import us.myles.ViaVersion.api.entities.Entity1_12Types;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.entities.Entity1_11Types;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.PotionSplashHandler;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_11 extends LegacyEntityRewriter<Protocol1_10To1_11>
{
    public EntityPackets1_11(final Protocol1_10To1_11 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.EFFECT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION);
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final int type = (int)wrapper.get(Type.INT, 0);
                    if (type == 2002 || type == 2007) {
                        if (type == 2007) {
                            wrapper.set(Type.INT, 0, (Object)2002);
                        }
                        final int mappedData = PotionSplashHandler.getOldData((int)wrapper.get(Type.INT, 1));
                        if (mappedData != -1) {
                            wrapper.set(Type.INT, 1, (Object)mappedData);
                        }
                    }
                });
            }
        });
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_ENTITY, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler(LegacyEntityRewriter.this.getObjectTrackerHandler());
                this.handler(LegacyEntityRewriter.this.getObjectRewriter(id -> Entity1_11Types.ObjectType.findById((int)id).orElse(null)));
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Optional<Entity1_12Types.ObjectType> type = (Optional<Entity1_12Types.ObjectType>)Entity1_12Types.ObjectType.findById((int)(byte)wrapper.get(Type.BYTE, 0));
                        if (type.isPresent() && type.get() == Entity1_12Types.ObjectType.FALLING_BLOCK) {
                            final int objectData = (int)wrapper.get(Type.INT, 0);
                            final int objType = objectData & 0xFFF;
                            final int data = objectData >> 12 & 0xF;
                            final Block block = EntityPackets1_11.this.getProtocol().getBlockItemPackets().handleBlock(objType, data);
                            if (block == null) {
                                return;
                            }
                            wrapper.set(Type.INT, 0, (Object)(block.getId() | block.getData() << 12));
                        }
                    }
                });
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_11Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_11Types.EntityType.WEATHER);
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_MOB, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT, Type.UNSIGNED_BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map(Types1_9.METADATA_LIST);
                this.handler(EntityRewriterBase.this.getTrackerHandler(Type.UNSIGNED_BYTE, 0));
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        final EntityType type = EntityRewriterBase.this.getEntityType(wrapper.user(), entityId);
                        final MetaStorage storage = new MetaStorage((List<Metadata>)wrapper.get(Types1_9.METADATA_LIST, 0));
                        EntityRewriterBase.this.handleMeta(wrapper.user(), (int)wrapper.get((Type)Type.VAR_INT, 0), storage);
                        final EntityData entityData = EntityRewriterBase.this.getEntityData(type);
                        if (entityData != null) {
                            wrapper.set(Type.UNSIGNED_BYTE, 0, (Object)(short)entityData.getReplacementId());
                            if (entityData.hasBaseMeta()) {
                                entityData.getDefaultMeta().createMeta(storage);
                            }
                        }
                        wrapper.set(Types1_9.METADATA_LIST, 0, (Object)storage.getMetaDataList());
                    }
                });
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PAINTING, (EntityType)Entity1_11Types.EntityType.PAINTING);
        this.registerJoinGame((ClientboundPacketType)ClientboundPackets1_9_3.JOIN_GAME, (EntityType)Entity1_11Types.EntityType.PLAYER);
        this.registerRespawn((ClientboundPacketType)ClientboundPackets1_9_3.RESPAWN);
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SPAWN_PLAYER, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_9.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler((Type<List<Metadata>>)Types1_9.METADATA_LIST, (EntityType)Entity1_11Types.EntityType.PLAYER));
            }
        });
        this.registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_9_3.DESTROY_ENTITIES);
        this.registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_METADATA, (Type<List<Metadata>>)Types1_9.METADATA_LIST);
        ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_STATUS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final byte b = (byte)wrapper.get(Type.BYTE, 0);
                        if (b == 35) {
                            wrapper.clearPacket();
                            wrapper.setId(30);
                            wrapper.write(Type.UNSIGNED_BYTE, (Object)10);
                            wrapper.write((Type)Type.FLOAT, (Object)0.0f);
                        }
                    }
                });
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.mapEntity((EntityType)Entity1_11Types.EntityType.ELDER_GUARDIAN, (EntityType)Entity1_11Types.EntityType.GUARDIAN);
        this.mapEntity((EntityType)Entity1_11Types.EntityType.WITHER_SKELETON, (EntityType)Entity1_11Types.EntityType.SKELETON).mobName("Wither Skeleton").spawnMetadata(storage -> storage.add(this.getSkeletonTypeMeta(1)));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.STRAY, (EntityType)Entity1_11Types.EntityType.SKELETON).mobName("Stray").spawnMetadata(storage -> storage.add(this.getSkeletonTypeMeta(2)));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.HUSK, (EntityType)Entity1_11Types.EntityType.ZOMBIE).mobName("Husk").spawnMetadata(storage -> this.handleZombieType(storage, 6));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.ZOMBIE_VILLAGER, (EntityType)Entity1_11Types.EntityType.ZOMBIE).spawnMetadata(storage -> this.handleZombieType(storage, 1));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.HORSE, (EntityType)Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(this.getHorseMetaType(0)));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.DONKEY, (EntityType)Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(this.getHorseMetaType(1)));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.MULE, (EntityType)Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(this.getHorseMetaType(2)));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.SKELETON_HORSE, (EntityType)Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(this.getHorseMetaType(4)));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.ZOMBIE_HORSE, (EntityType)Entity1_11Types.EntityType.HORSE).spawnMetadata(storage -> storage.add(this.getHorseMetaType(3)));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.EVOCATION_FANGS, (EntityType)Entity1_11Types.EntityType.SHULKER);
        this.mapEntity((EntityType)Entity1_11Types.EntityType.EVOCATION_ILLAGER, (EntityType)Entity1_11Types.EntityType.VILLAGER).mobName("Evoker");
        this.mapEntity((EntityType)Entity1_11Types.EntityType.VEX, (EntityType)Entity1_11Types.EntityType.BAT).mobName("Vex");
        this.mapEntity((EntityType)Entity1_11Types.EntityType.VINDICATION_ILLAGER, (EntityType)Entity1_11Types.EntityType.VILLAGER).mobName("Vindicator").spawnMetadata(storage -> storage.add(new Metadata(13, (MetaType)MetaType1_9.VarInt, (Object)4)));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.LIAMA, (EntityType)Entity1_11Types.EntityType.HORSE).mobName("Llama").spawnMetadata(storage -> storage.add(this.getHorseMetaType(1)));
        this.mapEntity((EntityType)Entity1_11Types.EntityType.LIAMA_SPIT, (EntityType)Entity1_11Types.EntityType.SNOWBALL);
        this.mapObjectType((ObjectType)Entity1_11Types.ObjectType.LIAMA_SPIT, (ObjectType)Entity1_11Types.ObjectType.SNOWBALL, -1);
        this.mapObjectType((ObjectType)Entity1_11Types.ObjectType.EVOCATION_FANGS, (ObjectType)Entity1_11Types.ObjectType.FALLING_BLOCK, 4294);
        final Metadata data;
        final boolean b;
        int bitmask;
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.GUARDIAN, true, 12).handle(e -> {
            data = e.getData();
            b = (boolean)data.getValue();
            bitmask = (b ? 2 : 0);
            if (e.getEntity().getType().is((EntityType)Entity1_11Types.EntityType.ELDER_GUARDIAN)) {
                bitmask |= 0x4;
            }
            data.setMetaType((MetaType)MetaType1_9.Byte);
            data.setValue((Object)(byte)bitmask);
            return data;
        });
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.ABSTRACT_SKELETON, true, 12).handleIndexChange(13);
        final Metadata data2;
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.ZOMBIE, true).handle(e -> {
            data2 = e.getData();
            switch (data2.getId()) {
                case 13: {
                    throw RemovedValueException.EX;
                }
                case 14: {
                    data2.setId(15);
                    break;
                }
                case 15: {
                    data2.setId(14);
                    break;
                }
                case 16: {
                    data2.setId(13);
                    data2.setValue((Object)(1 + (int)data2.getValue()));
                    break;
                }
            }
            return data2;
        });
        final Metadata data3;
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.EVOCATION_ILLAGER, 12).handle(e -> {
            data3 = e.getData();
            data3.setId(13);
            data3.setMetaType((MetaType)MetaType1_9.VarInt);
            data3.setValue((Object)(int)data3.getValue());
            return data3;
        });
        final Metadata data4;
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.VEX, 12).handle(e -> {
            data4 = e.getData();
            data4.setValue((Object)0);
            return data4;
        });
        final Metadata data5;
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.VINDICATION_ILLAGER, 12).handle(e -> {
            data5 = e.getData();
            data5.setId(13);
            data5.setMetaType((MetaType)MetaType1_9.VarInt);
            data5.setValue((Object)((((Number)data5.getValue()).intValue() == 1) ? 2 : 4));
            return data5;
        });
        final Metadata data6;
        final byte b2;
        byte b3;
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.ABSTRACT_HORSE, true, 13).handle(e -> {
            data6 = e.getData();
            b2 = (byte)data6.getValue();
            if (e.getEntity().has(ChestedHorseStorage.class) && e.getEntity().get(ChestedHorseStorage.class).isChested()) {
                b3 = (byte)(b2 | 0x8);
                data6.setValue((Object)b3);
            }
            return data6;
        });
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.CHESTED_HORSE, true).handle(e -> {
            if (!e.getEntity().has(ChestedHorseStorage.class)) {
                e.getEntity().put(new ChestedHorseStorage());
            }
            return e.getData();
        });
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.HORSE, 16).handleIndexChange(17);
        final ChestedHorseStorage storage2;
        final boolean b4;
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.CHESTED_HORSE, true, 15).handle(e -> {
            storage2 = e.getEntity().get(ChestedHorseStorage.class);
            b4 = (boolean)e.getData().getValue();
            storage2.setChested(b4);
            throw RemovedValueException.EX;
        });
        final Metadata data7;
        final ChestedHorseStorage storage3;
        final int index;
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.LIAMA).handle(e -> {
            data7 = e.getData();
            storage3 = e.getEntity().get(ChestedHorseStorage.class);
            index = e.getIndex();
            switch (index) {
                case 16: {
                    storage3.setLiamaStrength((int)data7.getValue());
                    throw RemovedValueException.EX;
                }
                case 17: {
                    storage3.setLiamaCarpetColor((int)data7.getValue());
                    throw RemovedValueException.EX;
                }
                case 18: {
                    storage3.setLiamaVariant((int)data7.getValue());
                    throw RemovedValueException.EX;
                }
                default: {
                    return e.getData();
                }
            }
        });
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.ABSTRACT_HORSE, true, 14).handleIndexChange(16);
        final Metadata data8;
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.VILLAGER, 13).handle(e -> {
            data8 = e.getData();
            if ((int)data8.getValue() == 5) {
                data8.setValue((Object)0);
            }
            return data8;
        });
        this.registerMetaHandler().filter((EntityType)Entity1_11Types.EntityType.SHULKER, 15).removed();
    }
    
    private Metadata getSkeletonTypeMeta(final int type) {
        return new Metadata(12, (MetaType)MetaType1_9.VarInt, (Object)type);
    }
    
    private Metadata getZombieTypeMeta(final int type) {
        return new Metadata(13, (MetaType)MetaType1_9.VarInt, (Object)type);
    }
    
    private void handleZombieType(final MetaStorage storage, final int type) {
        final Metadata meta = storage.get(13);
        if (meta == null) {
            storage.add(this.getZombieTypeMeta(type));
        }
    }
    
    private Metadata getHorseMetaType(final int type) {
        return new Metadata(14, (MetaType)MetaType1_9.VarInt, (Object)type);
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_11Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return (EntityType)Entity1_11Types.getTypeFromId(typeId, true);
    }
}
