// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets;

import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.ParticleMapping;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_12;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import nl.matsv.viabackwards.api.entities.storage.EntityPositionHandler;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.PaintingMapping;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.EntityTypeMapping;
import us.myles.ViaVersion.api.type.types.version.Types1_12;
import us.myles.ViaVersion.api.type.types.version.Types1_13;
import us.myles.ViaVersion.api.entities.EntityType;
import java.util.Optional;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.PlayerPositionStorage1_13;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_13 extends LegacyEntityRewriter<Protocol1_12_2To1_13>
{
    public EntityPackets1_13(final Protocol1_12_2To1_13 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.PLAYER_POSITION, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (!ViaBackwards.getConfig().isFix1_13FacePlayer()) {
                            return;
                        }
                        final PlayerPositionStorage1_13 playerStorage = (PlayerPositionStorage1_13)wrapper.user().get((Class)PlayerPositionStorage1_13.class);
                        final byte bitField = (byte)wrapper.get(Type.BYTE, 0);
                        playerStorage.setX(this.toSet(bitField, 0, playerStorage.getX(), (double)wrapper.get(Type.DOUBLE, 0)));
                        playerStorage.setY(this.toSet(bitField, 1, playerStorage.getY(), (double)wrapper.get(Type.DOUBLE, 1)));
                        playerStorage.setZ(this.toSet(bitField, 2, playerStorage.getZ(), (double)wrapper.get(Type.DOUBLE, 2)));
                    }
                    
                    private double toSet(final int field, final int bitIndex, final double origin, final double packetValue) {
                        return ((field & 1 << bitIndex) != 0x0) ? (origin + packetValue) : packetValue;
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_ENTITY, (PacketRemapper)new PacketRemapper() {
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
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Optional<Entity1_13Types.ObjectType> optionalType = (Optional<Entity1_13Types.ObjectType>)Entity1_13Types.ObjectType.findById((int)(byte)wrapper.get(Type.BYTE, 0));
                        if (!optionalType.isPresent()) {
                            return;
                        }
                        final Entity1_13Types.ObjectType type = optionalType.get();
                        if (type == Entity1_13Types.ObjectType.FALLING_BLOCK) {
                            final int blockState = (int)wrapper.get(Type.INT, 0);
                            int combined = Protocol1_12_2To1_13.MAPPINGS.getNewBlockStateId(blockState);
                            combined = ((combined >> 4 & 0xFFF) | (combined & 0xF) << 12);
                            wrapper.set(Type.INT, 0, (Object)combined);
                        }
                        else if (type == Entity1_13Types.ObjectType.ITEM_FRAME) {
                            int data = (int)wrapper.get(Type.INT, 0);
                            switch (data) {
                                case 3: {
                                    data = 0;
                                    break;
                                }
                                case 4: {
                                    data = 1;
                                    break;
                                }
                                case 5: {
                                    data = 3;
                                    break;
                                }
                            }
                            wrapper.set(Type.INT, 0, (Object)data);
                        }
                        else if (type == Entity1_13Types.ObjectType.TRIDENT) {
                            wrapper.set(Type.BYTE, 0, (Object)(byte)Entity1_13Types.ObjectType.TIPPED_ARROW.getId());
                        }
                    }
                });
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_13.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_13Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_13.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_13Types.EntityType.LIGHTNING_BOLT);
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_MOB, (PacketRemapper)new PacketRemapper() {
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
                this.map(Types1_13.METADATA_LIST, Types1_12.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = (int)wrapper.get((Type)Type.VAR_INT, 1);
                        final EntityType entityType = (EntityType)Entity1_13Types.getTypeFromId(type, false);
                        EntityRewriterBase.this.addTrackedEntity(wrapper, (int)wrapper.get((Type)Type.VAR_INT, 0), entityType);
                        final int oldId = EntityTypeMapping.getOldId(type);
                        if (oldId == -1) {
                            if (!EntityRewriterBase.this.hasData(entityType)) {
                                ViaBackwards.getPlatform().getLogger().warning("Could not find 1.12 entity type for 1.13 entity type " + type + "/" + entityType);
                            }
                        }
                        else {
                            wrapper.set((Type)Type.VAR_INT, 1, (Object)oldId);
                        }
                    }
                });
                this.handler(LegacyEntityRewriter.this.getMobSpawnRewriter((Type<List<Metadata>>)Types1_12.METADATA_LIST));
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PLAYER, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_13.METADATA_LIST, Types1_12.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler((Type<List<Metadata>>)Types1_12.METADATA_LIST, (EntityType)Entity1_13Types.EntityType.PLAYER));
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PAINTING, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.handler(EntityRewriterBase.this.getTrackerHandler((EntityType)Entity1_13Types.EntityType.PAINTING, (Type)Type.VAR_INT));
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int motive = (int)wrapper.read((Type)Type.VAR_INT);
                        final String title = PaintingMapping.getStringId(motive);
                        wrapper.write(Type.STRING, (Object)title);
                    }
                });
            }
        });
        this.registerJoinGame((ClientboundPacketType)ClientboundPackets1_13.JOIN_GAME, (EntityType)Entity1_13Types.EntityType.PLAYER);
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.RESPAWN, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.handler(EntityRewriterBase.this.getDimensionHandler(0));
                this.handler(wrapper -> ((BackwardsBlockStorage)wrapper.user().get((Class)BackwardsBlockStorage.class)).clear());
            }
        });
        this.registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_13.DESTROY_ENTITIES);
        this.registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_13.ENTITY_METADATA, (Type<List<Metadata>>)Types1_13.METADATA_LIST, (Type<List<Metadata>>)Types1_12.METADATA_LIST);
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.FACE_PLAYER, (ClientboundPacketType)null, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.cancel();
                        if (!ViaBackwards.getConfig().isFix1_13FacePlayer()) {
                            return;
                        }
                        final int anchor = (int)wrapper.read((Type)Type.VAR_INT);
                        final double x = (double)wrapper.read(Type.DOUBLE);
                        final double y = (double)wrapper.read(Type.DOUBLE);
                        final double z = (double)wrapper.read(Type.DOUBLE);
                        final PlayerPositionStorage1_13 positionStorage = (PlayerPositionStorage1_13)wrapper.user().get((Class)PlayerPositionStorage1_13.class);
                        final PacketWrapper positionAndLook = wrapper.create(47);
                        positionAndLook.write(Type.DOUBLE, (Object)0.0);
                        positionAndLook.write(Type.DOUBLE, (Object)0.0);
                        positionAndLook.write(Type.DOUBLE, (Object)0.0);
                        EntityPositionHandler.writeFacingDegrees(positionAndLook, positionStorage.getX(), (anchor == 1) ? (positionStorage.getY() + 1.62) : positionStorage.getY(), positionStorage.getZ(), x, y, z);
                        positionAndLook.write(Type.BYTE, (Object)7);
                        positionAndLook.write((Type)Type.VAR_INT, (Object)(-1));
                        positionAndLook.send((Class)Protocol1_12_2To1_13.class, true, true);
                    }
                });
            }
        });
        if (ViaBackwards.getConfig().isFix1_13FacePlayer()) {
            final PacketRemapper movementRemapper = new PacketRemapper() {
                public void registerMap() {
                    this.map(Type.DOUBLE);
                    this.map(Type.DOUBLE);
                    this.map(Type.DOUBLE);
                    this.handler(wrapper -> ((PlayerPositionStorage1_13)wrapper.user().get((Class)PlayerPositionStorage1_13.class)).setCoordinates(wrapper, false));
                }
            };
            ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.PLAYER_POSITION, movementRemapper);
            ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.PLAYER_POSITION_AND_ROTATION, movementRemapper);
            ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.VEHICLE_MOVE, movementRemapper);
        }
    }
    
    @Override
    protected void registerRewrites() {
        this.mapEntity((EntityType)Entity1_13Types.EntityType.DROWNED, (EntityType)Entity1_13Types.EntityType.ZOMBIE_VILLAGER).mobName("Drowned");
        this.mapEntity((EntityType)Entity1_13Types.EntityType.COD, (EntityType)Entity1_13Types.EntityType.SQUID).mobName("Cod");
        this.mapEntity((EntityType)Entity1_13Types.EntityType.SALMON, (EntityType)Entity1_13Types.EntityType.SQUID).mobName("Salmon");
        this.mapEntity((EntityType)Entity1_13Types.EntityType.PUFFERFISH, (EntityType)Entity1_13Types.EntityType.SQUID).mobName("Puffer Fish");
        this.mapEntity((EntityType)Entity1_13Types.EntityType.TROPICAL_FISH, (EntityType)Entity1_13Types.EntityType.SQUID).mobName("Tropical Fish");
        this.mapEntity((EntityType)Entity1_13Types.EntityType.PHANTOM, (EntityType)Entity1_13Types.EntityType.PARROT).mobName("Phantom").spawnMetadata(storage -> storage.add(new Metadata(15, (MetaType)MetaType1_12.VarInt, (Object)3)));
        this.mapEntity((EntityType)Entity1_13Types.EntityType.DOLPHIN, (EntityType)Entity1_13Types.EntityType.SQUID).mobName("Dolphin");
        this.mapEntity((EntityType)Entity1_13Types.EntityType.TURTLE, (EntityType)Entity1_13Types.EntityType.OCELOT).mobName("Turtle");
        final Metadata meta;
        final int typeId;
        Item item;
        this.registerMetaHandler().handle(e -> {
            meta = e.getData();
            typeId = meta.getMetaType().getTypeID();
            if (typeId == 5) {
                meta.setMetaType((MetaType)MetaType1_12.String);
                if (meta.getValue() == null) {
                    meta.setValue((Object)"");
                }
            }
            else if (typeId == 6) {
                meta.setMetaType((MetaType)MetaType1_12.Slot);
                item = (Item)meta.getValue();
                meta.setValue((Object)((Protocol1_12_2To1_13)this.protocol).getBlockItemPackets().handleItemToClient(item));
            }
            else if (typeId == 15) {
                meta.setMetaType((MetaType)MetaType1_12.Discontinued);
            }
            else if (typeId > 5) {
                meta.setMetaType((MetaType)MetaType1_12.byId(typeId - 1));
            }
            return meta;
        });
        final Metadata meta2;
        final String value;
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ENTITY, true, 2).handle(e -> {
            meta2 = e.getData();
            value = meta2.getValue().toString();
            if (value.isEmpty()) {
                return meta2;
            }
            else {
                meta2.setValue((Object)ChatRewriter.jsonTextToLegacy(value));
                return meta2;
            }
        });
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ZOMBIE, true, 15).removed();
        final Metadata meta3;
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ZOMBIE, true).handle(e -> {
            meta3 = e.getData();
            if (meta3.getId() > 15) {
                meta3.setId(meta3.getId() - 1);
            }
            return meta3;
        });
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 13).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 14).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 15).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 16).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 17).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TURTLE, 18).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ABSTRACT_FISHES, true, 12).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ABSTRACT_FISHES, true, 13).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.PHANTOM, 12).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.BOAT, 12).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TRIDENT, 7).removed();
        final Metadata meta4;
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.WOLF, 17).handle(e -> {
            meta4 = e.getData();
            meta4.setValue((Object)(15 - (int)meta4.getValue()));
            return meta4;
        });
        final Metadata meta5;
        final Particle particle;
        final ParticleMapping.ParticleData data;
        int firstArg;
        int secondArg;
        final int[] particleArgs;
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.AREA_EFFECT_CLOUD, 9).handle(e -> {
            meta5 = e.getData();
            particle = (Particle)meta5.getValue();
            data = ParticleMapping.getMapping(particle.getId());
            firstArg = 0;
            secondArg = 0;
            particleArgs = data.rewriteMeta((Protocol1_12_2To1_13)this.protocol, particle.getArguments());
            if (particleArgs != null && particleArgs.length != 0) {
                if (data.getHandler().isBlockHandler() && particleArgs[0] == 0) {
                    particleArgs[0] = 102;
                }
                firstArg = particleArgs[0];
                secondArg = ((particleArgs.length == 2) ? particleArgs[1] : 0);
            }
            e.createMeta(new Metadata(9, (MetaType)MetaType1_12.VarInt, (Object)data.getHistoryId()));
            e.createMeta(new Metadata(10, (MetaType)MetaType1_12.VarInt, (Object)firstArg));
            e.createMeta(new Metadata(11, (MetaType)MetaType1_12.VarInt, (Object)secondArg));
            throw RemovedValueException.EX;
        });
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_13Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return (EntityType)Entity1_13Types.getTypeFromId(typeId, true);
    }
    
    @Override
    public int getOldEntityId(final int newId) {
        return EntityTypeMapping.getOldId(newId);
    }
}
