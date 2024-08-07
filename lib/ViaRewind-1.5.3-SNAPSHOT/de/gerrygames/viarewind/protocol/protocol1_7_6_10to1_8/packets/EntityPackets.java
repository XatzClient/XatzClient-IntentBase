// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import java.util.List;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.Position;
import java.util.UUID;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class EntityPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 4, 4, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map((Type)Type.SHORT);
                this.map(Type.ITEM, (Type)Types1_7_6_10.COMPRESSED_NBT_ITEM);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Item item = (Item)packetWrapper.get((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                        ItemRewriter.toClient(item);
                        packetWrapper.set((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, 0, (Object)item);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        if ((short)packetWrapper.get((Type)Type.SHORT, 0) > 4) {
                            packetWrapper.cancel();
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        if (packetWrapper.isCancelled()) {
                            return;
                        }
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final UUID uuid = tracker.getPlayerUUID((int)packetWrapper.get(Type.INT, 0));
                        if (uuid == null) {
                            return;
                        }
                        Item[] equipment = tracker.getPlayerEquipment(uuid);
                        if (equipment == null) {
                            tracker.setPlayerEquipment(uuid, equipment = new Item[5]);
                        }
                        equipment[packetWrapper.get((Type)Type.SHORT, 0)] = (Item)packetWrapper.get((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                        final GameProfileStorage storage = (GameProfileStorage)packetWrapper.user().get((Class)GameProfileStorage.class);
                        final GameProfileStorage.GameProfile profile = storage.get(uuid);
                        if (profile != null && profile.gamemode == 3) {
                            packetWrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 10, 10, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Position position = (Position)packetWrapper.read(Type.POSITION);
                        packetWrapper.write(Type.INT, (Object)position.getX());
                        packetWrapper.write(Type.UNSIGNED_BYTE, (Object)position.getY());
                        packetWrapper.write(Type.INT, (Object)position.getZ());
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 13, 13, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map((Type)Type.VAR_INT, Type.INT);
            }
        });
        protocol.registerOutgoing(State.PLAY, 18, 18, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
            }
        });
        protocol.registerOutgoing(State.PLAY, 19, 19, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        int[] entityIds = (int[])packetWrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        for (final int entityId : entityIds) {
                            tracker.removeEntity(entityId);
                        }
                        while (entityIds.length > 127) {
                            final int[] entityIds2 = new int[127];
                            System.arraycopy(entityIds, 0, entityIds2, 0, 127);
                            final int[] temp = new int[entityIds.length - 127];
                            System.arraycopy(entityIds, 127, temp, 0, temp.length);
                            entityIds = temp;
                            final PacketWrapper destroy = new PacketWrapper(19, (ByteBuf)null, packetWrapper.user());
                            destroy.write((Type)Types1_7_6_10.INT_ARRAY, (Object)entityIds2);
                            PacketUtil.sendPacket(destroy, Protocol1_7_6_10TO1_8.class);
                        }
                        packetWrapper.write((Type)Types1_7_6_10.INT_ARRAY, (Object)entityIds);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 20, 20, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
            }
        });
        protocol.registerOutgoing(State.PLAY, 21, 21, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.BOOLEAN);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get(Type.INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (replacement != null) {
                            packetWrapper.cancel();
                            final int x = (byte)packetWrapper.get(Type.BYTE, 0);
                            final int y = (byte)packetWrapper.get(Type.BYTE, 1);
                            final int z = (byte)packetWrapper.get(Type.BYTE, 2);
                            replacement.relMove(x / 32.0, y / 32.0, z / 32.0);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 22, 22, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.BOOLEAN);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get(Type.INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (replacement != null) {
                            packetWrapper.cancel();
                            final int yaw = (byte)packetWrapper.get(Type.BYTE, 0);
                            final int pitch = (byte)packetWrapper.get(Type.BYTE, 1);
                            replacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 23, 23, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.BOOLEAN);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get(Type.INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (replacement != null) {
                            packetWrapper.cancel();
                            final int x = (byte)packetWrapper.get(Type.BYTE, 0);
                            final int y = (byte)packetWrapper.get(Type.BYTE, 1);
                            final int z = (byte)packetWrapper.get(Type.BYTE, 2);
                            final int yaw = (byte)packetWrapper.get(Type.BYTE, 3);
                            final int pitch = (byte)packetWrapper.get(Type.BYTE, 4);
                            replacement.relMove(x / 32.0, y / 32.0, z / 32.0);
                            replacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 24, 24, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.BOOLEAN);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get(Type.INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final Entity1_10Types.EntityType type = tracker.getClientEntityTypes().get(entityId);
                        if (type == Entity1_10Types.EntityType.MINECART_ABSTRACT) {
                            int y = (int)packetWrapper.get(Type.INT, 2);
                            y += 12;
                            packetWrapper.set(Type.INT, 2, (Object)y);
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get(Type.INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (replacement != null) {
                            packetWrapper.cancel();
                            final int x = (int)packetWrapper.get(Type.INT, 1);
                            final int y = (int)packetWrapper.get(Type.INT, 2);
                            final int z = (int)packetWrapper.get(Type.INT, 3);
                            final int yaw = (byte)packetWrapper.get(Type.BYTE, 0);
                            final int pitch = (byte)packetWrapper.get(Type.BYTE, 1);
                            replacement.setLocation(x / 32.0, y / 32.0, z / 32.0);
                            replacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 25, 25, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get(Type.INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (replacement != null) {
                            packetWrapper.cancel();
                            final int yaw = (byte)packetWrapper.get(Type.BYTE, 0);
                            replacement.setHeadYaw(yaw * 360.0f / 256.0f);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 27, 27, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final boolean leash = (boolean)packetWrapper.get(Type.BOOLEAN, 0);
                        if (leash) {
                            return;
                        }
                        final int passenger = (int)packetWrapper.get(Type.INT, 0);
                        final int vehicle = (int)packetWrapper.get(Type.INT, 1);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        tracker.setPassenger(vehicle, passenger);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 28, 28, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map(Types1_8.METADATA_LIST, (Type)Types1_7_6_10.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final List<Metadata> metadataList = (List<Metadata>)wrapper.get((Type)Types1_7_6_10.METADATA_LIST, 0);
                        final int entityId = (int)wrapper.get(Type.INT, 0);
                        final EntityTracker tracker = (EntityTracker)wrapper.user().get((Class)EntityTracker.class);
                        if (tracker.getClientEntityTypes().containsKey(entityId)) {
                            final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                            if (replacement != null) {
                                wrapper.cancel();
                                replacement.updateMetadata(metadataList);
                            }
                            else {
                                MetadataRewriter.transform(tracker.getClientEntityTypes().get(entityId), metadataList);
                                if (metadataList.isEmpty()) {
                                    wrapper.cancel();
                                }
                            }
                        }
                        else {
                            tracker.addMetadataToBuffer(entityId, metadataList);
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 29, 29, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.VAR_INT, (Type)Type.SHORT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.BYTE);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 30, 30, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
            }
        });
        protocol.registerOutgoing(State.PLAY, 32, 32, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get(Type.INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        if (tracker.getEntityReplacement(entityId) != null) {
                            packetWrapper.cancel();
                            return;
                        }
                        for (int amount = (int)packetWrapper.passthrough(Type.INT), i = 0; i < amount; ++i) {
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough(Type.DOUBLE);
                            final int modifierlength = (int)packetWrapper.read((Type)Type.VAR_INT);
                            packetWrapper.write((Type)Type.SHORT, (Object)(short)modifierlength);
                            for (int j = 0; j < modifierlength; ++j) {
                                packetWrapper.passthrough(Type.UUID);
                                packetWrapper.passthrough(Type.DOUBLE);
                                packetWrapper.passthrough(Type.BYTE);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 73, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                    }
                });
            }
        });
    }
}
