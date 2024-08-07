// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import us.myles.ViaVersion.api.minecraft.Position;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.EndermiteReplacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.GuardianReplacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import de.gerrygames.viarewind.storage.BlockState;
import us.myles.ViaVersion.api.entities.EntityType;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.ArmorStandReplacement;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import java.util.List;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import java.util.Iterator;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import io.netty.buffer.ByteBuf;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import java.util.UUID;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class SpawnPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 12, 12, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final UUID uuid = (UUID)packetWrapper.read(Type.UUID);
                        packetWrapper.write(Type.STRING, (Object)uuid.toString());
                        final GameProfileStorage gameProfileStorage = (GameProfileStorage)packetWrapper.user().get((Class)GameProfileStorage.class);
                        final GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                        if (gameProfile == null) {
                            packetWrapper.write(Type.STRING, (Object)"");
                            packetWrapper.write((Type)Type.VAR_INT, (Object)0);
                        }
                        else {
                            packetWrapper.write(Type.STRING, (Object)((gameProfile.name.length() > 16) ? gameProfile.name.substring(0, 16) : gameProfile.name));
                            packetWrapper.write((Type)Type.VAR_INT, (Object)gameProfile.properties.size());
                            for (final GameProfileStorage.Property property : gameProfile.properties) {
                                packetWrapper.write(Type.STRING, (Object)property.name);
                                packetWrapper.write(Type.STRING, (Object)property.value);
                                packetWrapper.write(Type.STRING, (Object)((property.signature == null) ? "" : property.signature));
                            }
                        }
                        if (gameProfile != null && gameProfile.gamemode == 3) {
                            final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                            PacketWrapper equipmentPacket = new PacketWrapper(4, (ByteBuf)null, packetWrapper.user());
                            equipmentPacket.write(Type.INT, (Object)entityId);
                            equipmentPacket.write((Type)Type.SHORT, (Object)4);
                            equipmentPacket.write((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, (Object)gameProfile.getSkull());
                            PacketUtil.sendPacket(equipmentPacket, Protocol1_7_6_10TO1_8.class);
                            for (short i = 0; i < 4; ++i) {
                                equipmentPacket = new PacketWrapper(4, (ByteBuf)null, packetWrapper.user());
                                equipmentPacket.write(Type.INT, (Object)entityId);
                                equipmentPacket.write((Type)Type.SHORT, (Object)i);
                                equipmentPacket.write((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, (Object)null);
                                PacketUtil.sendPacket(equipmentPacket, Protocol1_7_6_10TO1_8.class);
                            }
                        }
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        tracker.addPlayer((Integer)packetWrapper.get((Type)Type.VAR_INT, 0), uuid);
                    }
                });
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map(Types1_8.METADATA_LIST, (Type)Types1_7_6_10.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final List<Metadata> metadata = (List<Metadata>)packetWrapper.get((Type)Types1_7_6_10.METADATA_LIST, 0);
                        MetadataRewriter.transform(Entity1_10Types.EntityType.PLAYER, metadata);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        tracker.getClientEntityTypes().put(entityId, Entity1_10Types.EntityType.PLAYER);
                        tracker.sendMetadataBuffer(entityId);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 14, 14, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final byte typeId = (byte)packetWrapper.get(Type.BYTE, 0);
                        int x = (int)packetWrapper.get(Type.INT, 0);
                        int y = (int)packetWrapper.get(Type.INT, 1);
                        int z = (int)packetWrapper.get(Type.INT, 2);
                        final byte pitch = (byte)packetWrapper.get(Type.BYTE, 1);
                        byte yaw = (byte)packetWrapper.get(Type.BYTE, 2);
                        if (typeId == 71) {
                            switch (yaw) {
                                case Byte.MIN_VALUE: {
                                    z += 32;
                                    yaw = 0;
                                    break;
                                }
                                case -64: {
                                    x -= 32;
                                    yaw = -64;
                                    break;
                                }
                                case 0: {
                                    z -= 32;
                                    yaw = -128;
                                    break;
                                }
                                case 64: {
                                    x += 32;
                                    yaw = 64;
                                    break;
                                }
                            }
                        }
                        else if (typeId == 78) {
                            packetWrapper.cancel();
                            final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                            final ArmorStandReplacement armorStand = new ArmorStandReplacement(entityId, packetWrapper.user());
                            armorStand.setLocation(x / 32.0, y / 32.0, z / 32.0);
                            armorStand.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                            armorStand.setHeadYaw(yaw * 360.0f / 256.0f);
                            tracker.addEntityReplacement(armorStand);
                        }
                        else if (typeId == 10) {
                            y += 12;
                        }
                        packetWrapper.set(Type.BYTE, 0, (Object)typeId);
                        packetWrapper.set(Type.INT, 0, (Object)x);
                        packetWrapper.set(Type.INT, 1, (Object)y);
                        packetWrapper.set(Type.INT, 2, (Object)z);
                        packetWrapper.set(Type.BYTE, 1, (Object)pitch);
                        packetWrapper.set(Type.BYTE, 2, (Object)yaw);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final Entity1_10Types.EntityType type = Entity1_10Types.getTypeFromId((int)typeId, true);
                        tracker.getClientEntityTypes().put(entityId, type);
                        tracker.sendMetadataBuffer(entityId);
                        int data = (int)packetWrapper.get(Type.INT, 3);
                        if (type != null && type.isOrHasParent((EntityType)Entity1_10Types.EntityType.FALLING_BLOCK)) {
                            BlockState state = new BlockState(data & 0xFFF, data >> 12 & 0xF);
                            state = ReplacementRegistry1_7_6_10to1_8.replace(state);
                            packetWrapper.set(Type.INT, 3, (Object)(data = (state.getId() | state.getData() << 16)));
                        }
                        if (data > 0) {
                            packetWrapper.passthrough((Type)Type.SHORT);
                            packetWrapper.passthrough((Type)Type.SHORT);
                            packetWrapper.passthrough((Type)Type.SHORT);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 15, 15, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map(Types1_8.METADATA_LIST, (Type)Types1_7_6_10.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final int typeId = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        final int x = (int)packetWrapper.get(Type.INT, 0);
                        final int y = (int)packetWrapper.get(Type.INT, 1);
                        final int z = (int)packetWrapper.get(Type.INT, 2);
                        final byte pitch = (byte)packetWrapper.get(Type.BYTE, 1);
                        final byte yaw = (byte)packetWrapper.get(Type.BYTE, 0);
                        final byte headYaw = (byte)packetWrapper.get(Type.BYTE, 2);
                        if (typeId == 30) {
                            packetWrapper.cancel();
                            final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                            final ArmorStandReplacement armorStand = new ArmorStandReplacement(entityId, packetWrapper.user());
                            armorStand.setLocation(x / 32.0, y / 32.0, z / 32.0);
                            armorStand.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                            armorStand.setHeadYaw(headYaw * 360.0f / 256.0f);
                            tracker.addEntityReplacement(armorStand);
                        }
                        else if (typeId == 68) {
                            packetWrapper.cancel();
                            final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                            final GuardianReplacement guardian = new GuardianReplacement(entityId, packetWrapper.user());
                            guardian.setLocation(x / 32.0, y / 32.0, z / 32.0);
                            guardian.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                            guardian.setHeadYaw(headYaw * 360.0f / 256.0f);
                            tracker.addEntityReplacement(guardian);
                        }
                        else if (typeId == 67) {
                            packetWrapper.cancel();
                            final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                            final EndermiteReplacement endermite = new EndermiteReplacement(entityId, packetWrapper.user());
                            endermite.setLocation(x / 32.0, y / 32.0, z / 32.0);
                            endermite.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                            endermite.setHeadYaw(headYaw * 360.0f / 256.0f);
                            tracker.addEntityReplacement(endermite);
                        }
                        else if (typeId == 101 || typeId == 255 || typeId == -1) {
                            packetWrapper.cancel();
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final int typeId = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        tracker.getClientEntityTypes().put(entityId, Entity1_10Types.getTypeFromId(typeId, false));
                        tracker.sendMetadataBuffer(entityId);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final List<Metadata> metadataList = (List<Metadata>)wrapper.get((Type)Types1_7_6_10.METADATA_LIST, 0);
                        final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        final EntityTracker tracker = (EntityTracker)wrapper.user().get((Class)EntityTracker.class);
                        if (tracker.getEntityReplacement(entityId) != null) {
                            tracker.getEntityReplacement(entityId).updateMetadata(metadataList);
                        }
                        else if (tracker.getClientEntityTypes().containsKey(entityId)) {
                            MetadataRewriter.transform(tracker.getClientEntityTypes().get(entityId), metadataList);
                        }
                        else {
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 16, 16, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Position position = (Position)packetWrapper.read(Type.POSITION);
                        packetWrapper.write(Type.INT, (Object)position.getX());
                        packetWrapper.write(Type.INT, (Object)(int)position.getY());
                        packetWrapper.write(Type.INT, (Object)position.getZ());
                    }
                });
                this.map(Type.UNSIGNED_BYTE, Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        tracker.getClientEntityTypes().put(entityId, Entity1_10Types.EntityType.PAINTING);
                        tracker.sendMetadataBuffer(entityId);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 17, 17, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map((Type)Type.SHORT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        tracker.getClientEntityTypes().put(entityId, Entity1_10Types.EntityType.EXPERIENCE_ORB);
                        tracker.sendMetadataBuffer(entityId);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 44, 44, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        tracker.getClientEntityTypes().put(entityId, Entity1_10Types.EntityType.LIGHTNING);
                        tracker.sendMetadataBuffer(entityId);
                    }
                });
            }
        });
    }
}
