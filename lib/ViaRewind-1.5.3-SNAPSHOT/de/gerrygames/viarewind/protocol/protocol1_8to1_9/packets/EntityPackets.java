// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Cooldown;
import java.util.UUID;
import us.myles.ViaVersion.api.Pair;
import java.util.Iterator;
import java.util.ArrayList;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ItemRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata.MetadataRewriter;
import java.util.List;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Levitation;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.PlayerPosition;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.Vector;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import io.netty.buffer.ByteBuf;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.util.RelativeMoveUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class EntityPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 27, 26, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final byte status = (byte)packetWrapper.read(Type.BYTE);
                        if (status > 23) {
                            packetWrapper.cancel();
                            return;
                        }
                        packetWrapper.write(Type.BYTE, (Object)status);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 37, 21, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final int relX = (short)packetWrapper.read((Type)Type.SHORT);
                        final int relY = (short)packetWrapper.read((Type)Type.SHORT);
                        final int relZ = (short)packetWrapper.read((Type)Type.SHORT);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (replacement != null) {
                            packetWrapper.cancel();
                            replacement.relMove(relX / 4096.0, relY / 4096.0, relZ / 4096.0);
                            return;
                        }
                        final Vector[] moves = RelativeMoveUtil.calculateRelativeMoves(packetWrapper.user(), entityId, relX, relY, relZ);
                        packetWrapper.write(Type.BYTE, (Object)(byte)moves[0].getBlockX());
                        packetWrapper.write(Type.BYTE, (Object)(byte)moves[0].getBlockY());
                        packetWrapper.write(Type.BYTE, (Object)(byte)moves[0].getBlockZ());
                        final boolean onGround = (boolean)packetWrapper.passthrough(Type.BOOLEAN);
                        if (moves.length > 1) {
                            final PacketWrapper secondPacket = new PacketWrapper(21, (ByteBuf)null, packetWrapper.user());
                            secondPacket.write((Type)Type.VAR_INT, packetWrapper.get((Type)Type.VAR_INT, 0));
                            secondPacket.write(Type.BYTE, (Object)(byte)moves[1].getBlockX());
                            secondPacket.write(Type.BYTE, (Object)(byte)moves[1].getBlockY());
                            secondPacket.write(Type.BYTE, (Object)(byte)moves[1].getBlockZ());
                            secondPacket.write(Type.BOOLEAN, (Object)onGround);
                            PacketUtil.sendPacket(secondPacket, Protocol1_8TO1_9.class);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 38, 23, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final int relX = (short)packetWrapper.read((Type)Type.SHORT);
                        final int relY = (short)packetWrapper.read((Type)Type.SHORT);
                        final int relZ = (short)packetWrapper.read((Type)Type.SHORT);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (replacement != null) {
                            packetWrapper.cancel();
                            replacement.relMove(relX / 4096.0, relY / 4096.0, relZ / 4096.0);
                            replacement.setYawPitch((byte)packetWrapper.read(Type.BYTE) * 360.0f / 256.0f, (byte)packetWrapper.read(Type.BYTE) * 360.0f / 256.0f);
                            return;
                        }
                        final Vector[] moves = RelativeMoveUtil.calculateRelativeMoves(packetWrapper.user(), entityId, relX, relY, relZ);
                        packetWrapper.write(Type.BYTE, (Object)(byte)moves[0].getBlockX());
                        packetWrapper.write(Type.BYTE, (Object)(byte)moves[0].getBlockY());
                        packetWrapper.write(Type.BYTE, (Object)(byte)moves[0].getBlockZ());
                        byte yaw = (byte)packetWrapper.passthrough(Type.BYTE);
                        final byte pitch = (byte)packetWrapper.passthrough(Type.BYTE);
                        final boolean onGround = (boolean)packetWrapper.passthrough(Type.BOOLEAN);
                        final Entity1_10Types.EntityType type = ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).getClientEntityTypes().get(entityId);
                        if (type == Entity1_10Types.EntityType.BOAT) {
                            yaw -= 64;
                            packetWrapper.set(Type.BYTE, 3, (Object)yaw);
                        }
                        if (moves.length > 1) {
                            final PacketWrapper secondPacket = new PacketWrapper(23, (ByteBuf)null, packetWrapper.user());
                            secondPacket.write((Type)Type.VAR_INT, packetWrapper.get((Type)Type.VAR_INT, 0));
                            secondPacket.write(Type.BYTE, (Object)(byte)moves[1].getBlockX());
                            secondPacket.write(Type.BYTE, (Object)(byte)moves[1].getBlockY());
                            secondPacket.write(Type.BYTE, (Object)(byte)moves[1].getBlockZ());
                            secondPacket.write(Type.BYTE, (Object)yaw);
                            secondPacket.write(Type.BYTE, (Object)pitch);
                            secondPacket.write(Type.BOOLEAN, (Object)onGround);
                            PacketUtil.sendPacket(secondPacket, Protocol1_8TO1_9.class);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 39, 22, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
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
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final Entity1_10Types.EntityType type = ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).getClientEntityTypes().get(entityId);
                        if (type == Entity1_10Types.EntityType.BOAT) {
                            byte yaw = (byte)packetWrapper.get(Type.BYTE, 0);
                            yaw -= 64;
                            packetWrapper.set(Type.BYTE, 0, (Object)yaw);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 40, 20);
        protocol.registerOutgoing(State.PLAY, 41, 24, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.create((ValueCreator)new ValueCreator() {
                    public void write(final PacketWrapper packetWrapper) throws Exception {
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final int vehicle = tracker.getVehicle(tracker.getPlayerId());
                        if (vehicle == -1) {
                            packetWrapper.cancel();
                        }
                        packetWrapper.write((Type)Type.VAR_INT, (Object)vehicle);
                    }
                });
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map((Type)Type.FLOAT, (ValueTransformer)Protocol1_8TO1_9.DEGREES_TO_ANGLE);
                this.map((Type)Type.FLOAT, (ValueTransformer)Protocol1_8TO1_9.DEGREES_TO_ANGLE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        if (packetWrapper.isCancelled()) {
                            return;
                        }
                        final PlayerPosition position = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        final double x = (int)packetWrapper.get(Type.INT, 0) / 32.0;
                        final double y = (int)packetWrapper.get(Type.INT, 1) / 32.0;
                        final double z = (int)packetWrapper.get(Type.INT, 2) / 32.0;
                        position.setPos(x, y, z);
                    }
                });
                this.create((ValueCreator)new ValueCreator() {
                    public void write(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.write(Type.BOOLEAN, (Object)true);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final Entity1_10Types.EntityType type = ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).getClientEntityTypes().get(entityId);
                        if (type == Entity1_10Types.EntityType.BOAT) {
                            byte yaw = (byte)packetWrapper.get(Type.BYTE, 1);
                            yaw -= 64;
                            packetWrapper.set(Type.BYTE, 0, (Object)yaw);
                            int y = (int)packetWrapper.get(Type.INT, 1);
                            y += 10;
                            packetWrapper.set(Type.INT, 1, (Object)y);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 47, 10);
        protocol.registerOutgoing(State.PLAY, 48, 19, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.VAR_INT_ARRAY_PRIMITIVE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        for (final int entityId : (int[])packetWrapper.get(Type.VAR_INT_ARRAY_PRIMITIVE, 0)) {
                            tracker.removeEntity(entityId);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 49, 30, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int id = (byte)packetWrapper.get(Type.BYTE, 0);
                        if (id > 23) {
                            packetWrapper.cancel();
                        }
                        if (id == 25) {
                            if ((int)packetWrapper.get((Type)Type.VAR_INT, 0) != ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).getPlayerId()) {
                                return;
                            }
                            final Levitation levitation = (Levitation)packetWrapper.user().get((Class)Levitation.class);
                            levitation.setActive(false);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 52, 25, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
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
        protocol.registerOutgoing(State.PLAY, 57, 28, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Types1_9.METADATA_LIST, Types1_8.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final List<Metadata> metadataList = (List<Metadata>)wrapper.get(Types1_8.METADATA_LIST, 0);
                        final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        final EntityTracker tracker = (EntityTracker)wrapper.user().get((Class)EntityTracker.class);
                        if (tracker.getClientEntityTypes().containsKey(entityId)) {
                            MetadataRewriter.transform(tracker.getClientEntityTypes().get(entityId), metadataList);
                            if (metadataList.isEmpty()) {
                                wrapper.cancel();
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
        protocol.registerOutgoing(State.PLAY, 58, 27, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.create((ValueCreator)new ValueCreator() {
                    public void write(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.write(Type.BOOLEAN, (Object)true);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 59, 18);
        protocol.registerOutgoing(State.PLAY, 60, 4, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        int slot = (int)packetWrapper.read((Type)Type.VAR_INT);
                        if (slot == 1) {
                            packetWrapper.cancel();
                        }
                        else if (slot > 1) {
                            --slot;
                        }
                        packetWrapper.write((Type)Type.SHORT, (Object)(short)slot);
                    }
                });
                this.map(Type.ITEM);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.set(Type.ITEM, 0, (Object)ItemRewriter.toClient((Item)packetWrapper.get(Type.ITEM, 0)));
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 64, 27, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                        final EntityTracker entityTracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final int vehicle = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final int count = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final ArrayList<Integer> passengers = new ArrayList<Integer>();
                        for (int i = 0; i < count; ++i) {
                            passengers.add((Integer)packetWrapper.read((Type)Type.VAR_INT));
                        }
                        final List<Integer> oldPassengers = entityTracker.getPassengers(vehicle);
                        entityTracker.setPassengers(vehicle, passengers);
                        if (!oldPassengers.isEmpty()) {
                            for (final Integer passenger : oldPassengers) {
                                final PacketWrapper detach = new PacketWrapper(27, (ByteBuf)null, packetWrapper.user());
                                detach.write(Type.INT, (Object)passenger);
                                detach.write(Type.INT, (Object)(-1));
                                detach.write(Type.BOOLEAN, (Object)false);
                                PacketUtil.sendPacket(detach, Protocol1_8TO1_9.class);
                            }
                        }
                        for (int j = 0; j < count; ++j) {
                            final int v = (j == 0) ? vehicle : passengers.get(j - 1);
                            final int p = passengers.get(j);
                            final PacketWrapper attach = new PacketWrapper(27, (ByteBuf)null, packetWrapper.user());
                            attach.write(Type.INT, (Object)p);
                            attach.write(Type.INT, (Object)v);
                            attach.write(Type.BOOLEAN, (Object)false);
                            PacketUtil.sendPacket(attach, Protocol1_8TO1_9.class);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 73, 13);
        protocol.registerOutgoing(State.PLAY, 74, 24, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, (ValueTransformer)Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final Entity1_10Types.EntityType type = ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).getClientEntityTypes().get(entityId);
                        if (type == Entity1_10Types.EntityType.BOAT) {
                            byte yaw = (byte)packetWrapper.get(Type.BYTE, 1);
                            yaw -= 64;
                            packetWrapper.set(Type.BYTE, 0, (Object)yaw);
                            int y = (int)packetWrapper.get(Type.INT, 1);
                            y += 10;
                            packetWrapper.set(Type.INT, 1, (Object)y);
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).resetEntityOffset(entityId);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (replacement != null) {
                            packetWrapper.cancel();
                            final int x = (int)packetWrapper.get(Type.INT, 0);
                            final int y = (int)packetWrapper.get(Type.INT, 1);
                            final int z = (int)packetWrapper.get(Type.INT, 2);
                            final int yaw = (byte)packetWrapper.get(Type.BYTE, 0);
                            final int pitch = (byte)packetWrapper.get(Type.BYTE, 1);
                            replacement.setLocation(x / 32.0, y / 32.0, z / 32.0);
                            replacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 75, 32, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final boolean player = (int)packetWrapper.get((Type)Type.VAR_INT, 0) == ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).getPlayerId();
                        final int size = (int)packetWrapper.get(Type.INT, 0);
                        int removed = 0;
                        for (int i = 0; i < size; ++i) {
                            final String key = (String)packetWrapper.read(Type.STRING);
                            final boolean skip = !Protocol1_8TO1_9.VALID_ATTRIBUTES.contains((Object)key);
                            final double value = (double)packetWrapper.read(Type.DOUBLE);
                            final int modifiersize = (int)packetWrapper.read((Type)Type.VAR_INT);
                            if (!skip) {
                                packetWrapper.write(Type.STRING, (Object)key);
                                packetWrapper.write(Type.DOUBLE, (Object)value);
                                packetWrapper.write((Type)Type.VAR_INT, (Object)modifiersize);
                            }
                            else {
                                ++removed;
                            }
                            final ArrayList<Pair<Byte, Double>> modifiers = new ArrayList<Pair<Byte, Double>>();
                            for (int j = 0; j < modifiersize; ++j) {
                                final UUID uuid = (UUID)packetWrapper.read(Type.UUID);
                                final double amount = (double)packetWrapper.read(Type.DOUBLE);
                                final byte operation = (byte)packetWrapper.read(Type.BYTE);
                                modifiers.add((Pair<Byte, Double>)new Pair((Object)operation, (Object)amount));
                                if (!skip) {
                                    packetWrapper.write(Type.UUID, (Object)uuid);
                                    packetWrapper.write(Type.DOUBLE, (Object)amount);
                                    packetWrapper.write(Type.BYTE, (Object)operation);
                                }
                            }
                            if (player && key.equals("generic.attackSpeed")) {
                                ((Cooldown)packetWrapper.user().get((Class)Cooldown.class)).setAttackSpeed(value, modifiers);
                            }
                        }
                        packetWrapper.set(Type.INT, 0, (Object)(size - removed));
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 76, 29, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int id = (byte)packetWrapper.get(Type.BYTE, 0);
                        if (id > 23) {
                            packetWrapper.cancel();
                        }
                        if (id == 25) {
                            if ((int)packetWrapper.get((Type)Type.VAR_INT, 0) != ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).getPlayerId()) {
                                return;
                            }
                            final Levitation levitation = (Levitation)packetWrapper.user().get((Class)Levitation.class);
                            levitation.setActive(true);
                            levitation.setAmplifier((byte)packetWrapper.get(Type.BYTE, 1));
                        }
                    }
                });
            }
        });
    }
}
