// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata.MetadataRewriter;
import java.util.List;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement.ShulkerReplacement;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import de.gerrygames.viarewind.utils.PacketUtil;
import io.netty.buffer.ByteBuf;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ReplacementRegistry1_8to1_9;
import de.gerrygames.viarewind.storage.BlockState;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement.ShulkerBulletReplacement;
import us.myles.ViaVersion.api.entities.EntityType;
import de.gerrygames.viarewind.ViaRewind;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class SpawnPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 0, 14, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.UUID);
                    }
                });
                this.map(Type.BYTE);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final int typeId = (byte)packetWrapper.get(Type.BYTE, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final Entity1_10Types.EntityType type = Entity1_10Types.getTypeFromId(typeId, true);
                        if (typeId == 3 || typeId == 91 || typeId == 92 || typeId == 93) {
                            packetWrapper.cancel();
                            return;
                        }
                        if (type == null) {
                            ViaRewind.getPlatform().getLogger().warning("[ViaRewind] Unhandled Spawn Object Type: " + typeId);
                            packetWrapper.cancel();
                            return;
                        }
                        final int x = (int)packetWrapper.get(Type.INT, 0);
                        int y = (int)packetWrapper.get(Type.INT, 1);
                        final int z = (int)packetWrapper.get(Type.INT, 2);
                        if (type.is((EntityType)Entity1_10Types.EntityType.BOAT)) {
                            byte yaw = (byte)packetWrapper.get(Type.BYTE, 1);
                            yaw -= 64;
                            packetWrapper.set(Type.BYTE, 1, (Object)yaw);
                            y += 10;
                            packetWrapper.set(Type.INT, 1, (Object)y);
                        }
                        else if (type.is((EntityType)Entity1_10Types.EntityType.SHULKER_BULLET)) {
                            packetWrapper.cancel();
                            final ShulkerBulletReplacement shulkerBulletReplacement = new ShulkerBulletReplacement(entityId, packetWrapper.user());
                            shulkerBulletReplacement.setLocation(x / 32.0, y / 32.0, z / 32.0);
                            tracker.addEntityReplacement(shulkerBulletReplacement);
                            return;
                        }
                        int data = (int)packetWrapper.get(Type.INT, 3);
                        if (type.isOrHasParent((EntityType)Entity1_10Types.EntityType.ARROW) && data != 0) {
                            packetWrapper.set(Type.INT, 3, (Object)(--data));
                        }
                        if (type.is((EntityType)Entity1_10Types.EntityType.FALLING_BLOCK)) {
                            BlockState state = new BlockState(data & 0xFFF, data >> 12 & 0xF);
                            state = ReplacementRegistry1_8to1_9.replace(state);
                            packetWrapper.set(Type.INT, 3, (Object)(state.getId() | state.getData() << 12));
                        }
                        if (data > 0) {
                            packetWrapper.passthrough((Type)Type.SHORT);
                            packetWrapper.passthrough((Type)Type.SHORT);
                            packetWrapper.passthrough((Type)Type.SHORT);
                        }
                        else {
                            final short vX = (short)packetWrapper.read((Type)Type.SHORT);
                            final short vY = (short)packetWrapper.read((Type)Type.SHORT);
                            final short vZ = (short)packetWrapper.read((Type)Type.SHORT);
                            final PacketWrapper velocityPacket = new PacketWrapper(18, (ByteBuf)null, packetWrapper.user());
                            velocityPacket.write((Type)Type.VAR_INT, (Object)entityId);
                            velocityPacket.write((Type)Type.SHORT, (Object)vX);
                            velocityPacket.write((Type)Type.SHORT, (Object)vY);
                            velocityPacket.write((Type)Type.SHORT, (Object)vZ);
                            PacketUtil.sendPacket(velocityPacket, Protocol1_8TO1_9.class);
                        }
                        tracker.getClientEntityTypes().put(entityId, type);
                        tracker.sendMetadataBuffer(entityId);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 1, 17, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
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
        protocol.registerOutgoing(State.PLAY, 2, 44, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
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
        protocol.registerOutgoing(State.PLAY, 3, 15, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.UUID);
                    }
                });
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map(Types1_9.METADATA_LIST, Types1_8.METADATA_LIST);
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
                        if (typeId == 69) {
                            packetWrapper.cancel();
                            final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                            final ShulkerReplacement shulkerReplacement = new ShulkerReplacement(entityId, packetWrapper.user());
                            shulkerReplacement.setLocation(x / 32.0, y / 32.0, z / 32.0);
                            shulkerReplacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                            shulkerReplacement.setHeadYaw(headYaw * 360.0f / 256.0f);
                            tracker.addEntityReplacement(shulkerReplacement);
                        }
                        else if (typeId == -1 || typeId == 255) {
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
                        final List<Metadata> metadataList = (List<Metadata>)wrapper.get(Types1_8.METADATA_LIST, 0);
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
        protocol.registerOutgoing(State.PLAY, 4, 16, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.UUID);
                    }
                });
                this.map(Type.STRING);
                this.map(Type.POSITION);
                this.map(Type.BYTE, Type.UNSIGNED_BYTE);
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
        protocol.registerOutgoing(State.PLAY, 5, 12, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.create((ValueCreator)new ValueCreator() {
                    public void write(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.write((Type)Type.SHORT, (Object)0);
                    }
                });
                this.map(Types1_9.METADATA_LIST, Types1_8.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final List<Metadata> metadataList = (List<Metadata>)wrapper.get(Types1_8.METADATA_LIST, 0);
                        MetadataRewriter.transform(Entity1_10Types.EntityType.PLAYER, metadataList);
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
    }
}
