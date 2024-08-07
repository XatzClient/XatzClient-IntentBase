// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_8;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.ArrayList;
import java.util.TimerTask;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Cooldown;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BlockPlaceDestroyTracker;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import us.myles.ViaVersion.api.minecraft.Position;
import io.netty.buffer.ByteBuf;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.PlayerPosition;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ItemRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import de.gerrygames.viarewind.utils.ChatUtil;
import us.myles.viaversion.libs.gson.JsonElement;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BossBarStorage;
import us.myles.ViaVersion.api.type.Type;
import java.util.UUID;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class PlayerPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 6, 11);
        protocol.registerOutgoing(State.PLAY, 7, 55);
        protocol.registerOutgoing(State.PLAY, 12, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                        final UUID uuid = (UUID)packetWrapper.read(Type.UUID);
                        final int action = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final BossBarStorage bossBarStorage = (BossBarStorage)packetWrapper.user().get((Class)BossBarStorage.class);
                        if (action == 0) {
                            bossBarStorage.add(uuid, ChatUtil.jsonToLegacy((JsonElement)packetWrapper.read(Type.COMPONENT)), (float)packetWrapper.read((Type)Type.FLOAT));
                            packetWrapper.read((Type)Type.VAR_INT);
                            packetWrapper.read((Type)Type.VAR_INT);
                            packetWrapper.read(Type.UNSIGNED_BYTE);
                        }
                        else if (action == 1) {
                            bossBarStorage.remove(uuid);
                        }
                        else if (action == 2) {
                            bossBarStorage.updateHealth(uuid, (float)packetWrapper.read((Type)Type.FLOAT));
                        }
                        else if (action == 3) {
                            final String title = ChatUtil.jsonToLegacy((JsonElement)packetWrapper.read(Type.COMPONENT));
                            bossBarStorage.updateTitle(uuid, title);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 14, 58);
        protocol.registerOutgoing(State.PLAY, 15, 2);
        protocol.registerOutgoing(State.PLAY, 23, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 24, 63, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final String channel = (String)packetWrapper.get(Type.STRING, 0);
                        if (channel.equalsIgnoreCase("MC|TrList")) {
                            packetWrapper.passthrough(Type.INT);
                            int size;
                            if (packetWrapper.isReadable(Type.BYTE, 0)) {
                                size = (byte)packetWrapper.passthrough(Type.BYTE);
                            }
                            else {
                                size = (short)packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                            }
                            for (int i = 0; i < size; ++i) {
                                packetWrapper.write(Type.ITEM, (Object)ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM)));
                                packetWrapper.write(Type.ITEM, (Object)ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM)));
                                final boolean has3Items = (boolean)packetWrapper.passthrough(Type.BOOLEAN);
                                if (has3Items) {
                                    packetWrapper.write(Type.ITEM, (Object)ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM)));
                                }
                                packetWrapper.passthrough(Type.BOOLEAN);
                                packetWrapper.passthrough(Type.INT);
                                packetWrapper.passthrough(Type.INT);
                            }
                        }
                        else if (channel.equalsIgnoreCase("MC|BOpen")) {
                            packetWrapper.read((Type)Type.VAR_INT);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 26, 64);
        protocol.registerOutgoing(State.PLAY, 30, 43, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.FLOAT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int reason = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (reason == 3) {
                            ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).setPlayerGamemode(((Float)packetWrapper.get((Type)Type.FLOAT, 0)).intValue());
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 35, 1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        tracker.setPlayerId((int)packetWrapper.get(Type.INT, 0));
                        tracker.setPlayerGamemode((short)packetWrapper.get(Type.UNSIGNED_BYTE, 0));
                        tracker.getClientEntityTypes().put(tracker.getPlayerId(), Entity1_10Types.EntityType.ENTITY_HUMAN);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final ClientWorld world = (ClientWorld)packetWrapper.user().get((Class)ClientWorld.class);
                        world.setEnvironment((int)(byte)packetWrapper.get(Type.BYTE, 0));
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 42, 54);
        protocol.registerOutgoing(State.PLAY, 43, 57);
        protocol.registerOutgoing(State.PLAY, 45, 56);
        protocol.registerOutgoing(State.PLAY, 46, 8, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final PlayerPosition pos = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        final int teleportId = (int)packetWrapper.read((Type)Type.VAR_INT);
                        pos.setConfirmId(teleportId);
                        final byte flags = (byte)packetWrapper.get(Type.BYTE, 0);
                        double x = (double)packetWrapper.get(Type.DOUBLE, 0);
                        double y = (double)packetWrapper.get(Type.DOUBLE, 1);
                        double z = (double)packetWrapper.get(Type.DOUBLE, 2);
                        float yaw = (float)packetWrapper.get((Type)Type.FLOAT, 0);
                        float pitch = (float)packetWrapper.get((Type)Type.FLOAT, 1);
                        packetWrapper.set(Type.BYTE, 0, (Object)0);
                        if (flags != 0) {
                            if ((flags & 0x1) != 0x0) {
                                x += pos.getPosX();
                                packetWrapper.set(Type.DOUBLE, 0, (Object)x);
                            }
                            if ((flags & 0x2) != 0x0) {
                                y += pos.getPosY();
                                packetWrapper.set(Type.DOUBLE, 1, (Object)y);
                            }
                            if ((flags & 0x4) != 0x0) {
                                z += pos.getPosZ();
                                packetWrapper.set(Type.DOUBLE, 2, (Object)z);
                            }
                            if ((flags & 0x8) != 0x0) {
                                yaw += pos.getYaw();
                                packetWrapper.set((Type)Type.FLOAT, 0, (Object)yaw);
                            }
                            if ((flags & 0x10) != 0x0) {
                                pitch += pos.getPitch();
                                packetWrapper.set((Type)Type.FLOAT, 1, (Object)pitch);
                            }
                        }
                        pos.setPos(x, y, z);
                        pos.setYaw(yaw);
                        pos.setPitch(pitch);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 50, 72);
        protocol.registerOutgoing(State.PLAY, 51, 7, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).setPlayerGamemode((short)packetWrapper.get(Type.UNSIGNED_BYTE, 1));
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        ((BossBarStorage)packetWrapper.user().get((Class)BossBarStorage.class)).updateLocation();
                        ((BossBarStorage)packetWrapper.user().get((Class)BossBarStorage.class)).changeWorld();
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final ClientWorld world = (ClientWorld)packetWrapper.user().get((Class)ClientWorld.class);
                        world.setEnvironment((int)packetWrapper.get(Type.INT, 0));
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 54, 67);
        protocol.registerOutgoing(State.PLAY, 55, 9);
        protocol.registerOutgoing(State.PLAY, 61, 31);
        protocol.registerOutgoing(State.PLAY, 62, 6);
        protocol.registerOutgoing(State.PLAY, 67, 5);
        protocol.registerOutgoing(State.PLAY, 69, 69);
        protocol.registerOutgoing(State.PLAY, 72, 71);
        protocol.registerIncoming(State.PLAY, 2, 1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final String msg = (String)packetWrapper.get(Type.STRING, 0);
                        if (msg.toLowerCase().startsWith("/offhand")) {
                            packetWrapper.cancel();
                            final PacketWrapper swapItems = new PacketWrapper(19, (ByteBuf)null, packetWrapper.user());
                            swapItems.write((Type)Type.VAR_INT, (Object)6);
                            swapItems.write(Type.POSITION, (Object)new Position(0, (short)0, 0));
                            swapItems.write(Type.BYTE, (Object)(-1));
                            PacketUtil.sendToServer(swapItems, Protocol1_8TO1_9.class, true, true);
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 5, 15);
        protocol.registerIncoming(State.PLAY, 10, 2, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int type = (int)packetWrapper.get((Type)Type.VAR_INT, 1);
                        if (type == 2) {
                            packetWrapper.passthrough((Type)Type.FLOAT);
                            packetWrapper.passthrough((Type)Type.FLOAT);
                            packetWrapper.passthrough((Type)Type.FLOAT);
                        }
                        if (type == 2 || type == 0) {
                            packetWrapper.write((Type)Type.VAR_INT, (Object)0);
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 15, 3, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final int playerId = tracker.getPlayerId();
                        if (tracker.isInsideVehicle(playerId)) {
                            packetWrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 12, 4, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final PlayerPosition pos = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        if (pos.getConfirmId() != -1) {
                            return;
                        }
                        pos.setPos((double)packetWrapper.get(Type.DOUBLE, 0), (double)packetWrapper.get(Type.DOUBLE, 1), (double)packetWrapper.get(Type.DOUBLE, 2));
                        pos.setOnGround((boolean)packetWrapper.get(Type.BOOLEAN, 0));
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        ((BossBarStorage)packetWrapper.user().get((Class)BossBarStorage.class)).updateLocation();
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 14, 5, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final PlayerPosition pos = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        if (pos.getConfirmId() != -1) {
                            return;
                        }
                        pos.setYaw((float)packetWrapper.get((Type)Type.FLOAT, 0));
                        pos.setPitch((float)packetWrapper.get((Type)Type.FLOAT, 1));
                        pos.setOnGround((boolean)packetWrapper.get(Type.BOOLEAN, 0));
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        ((BossBarStorage)packetWrapper.user().get((Class)BossBarStorage.class)).updateLocation();
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 13, 6, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final double x = (double)packetWrapper.get(Type.DOUBLE, 0);
                        final double y = (double)packetWrapper.get(Type.DOUBLE, 1);
                        final double z = (double)packetWrapper.get(Type.DOUBLE, 2);
                        final float yaw = (float)packetWrapper.get((Type)Type.FLOAT, 0);
                        final float pitch = (float)packetWrapper.get((Type)Type.FLOAT, 1);
                        final boolean onGround = (boolean)packetWrapper.get(Type.BOOLEAN, 0);
                        final PlayerPosition pos = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        if (pos.getConfirmId() != -1) {
                            if (pos.getPosX() == x && pos.getPosY() == y && pos.getPosZ() == z && pos.getYaw() == yaw && pos.getPitch() == pitch) {
                                final PacketWrapper confirmTeleport = packetWrapper.create(0);
                                confirmTeleport.write((Type)Type.VAR_INT, (Object)pos.getConfirmId());
                                PacketUtil.sendToServer(confirmTeleport, Protocol1_8TO1_9.class, true, true);
                                pos.setConfirmId(-1);
                            }
                        }
                        else {
                            pos.setPos(x, y, z);
                            pos.setYaw(yaw);
                            pos.setPitch(pitch);
                            pos.setOnGround(onGround);
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        ((BossBarStorage)packetWrapper.user().get((Class)BossBarStorage.class)).updateLocation();
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 19, 7, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BYTE, (Type)Type.VAR_INT);
                this.map(Type.POSITION);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int state = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        if (state == 0) {
                            ((BlockPlaceDestroyTracker)packetWrapper.user().get((Class)BlockPlaceDestroyTracker.class)).setMining(true);
                        }
                        else if (state == 2) {
                            final BlockPlaceDestroyTracker tracker = (BlockPlaceDestroyTracker)packetWrapper.user().get((Class)BlockPlaceDestroyTracker.class);
                            tracker.setMining(false);
                            tracker.setLastMining(System.currentTimeMillis() + 100L);
                            ((Cooldown)packetWrapper.user().get((Class)Cooldown.class)).setLastHit(0L);
                        }
                        else if (state == 1) {
                            final BlockPlaceDestroyTracker tracker = (BlockPlaceDestroyTracker)packetWrapper.user().get((Class)BlockPlaceDestroyTracker.class);
                            tracker.setMining(false);
                            tracker.setLastMining(0L);
                            ((Cooldown)packetWrapper.user().get((Class)Cooldown.class)).hit();
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 28, 8, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.BYTE, (Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.ITEM);
                    }
                });
                this.create((ValueCreator)new ValueCreator() {
                    public void write(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.write((Type)Type.VAR_INT, (Object)0);
                    }
                });
                this.map(Type.BYTE, Type.UNSIGNED_BYTE);
                this.map(Type.BYTE, Type.UNSIGNED_BYTE);
                this.map(Type.BYTE, Type.UNSIGNED_BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        if ((int)packetWrapper.get((Type)Type.VAR_INT, 0) == -1) {
                            packetWrapper.cancel();
                            final PacketWrapper useItem = new PacketWrapper(29, (ByteBuf)null, packetWrapper.user());
                            useItem.write((Type)Type.VAR_INT, (Object)0);
                            PacketUtil.sendToServer(useItem, Protocol1_8TO1_9.class, true, true);
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        if ((int)packetWrapper.get((Type)Type.VAR_INT, 0) != -1) {
                            ((BlockPlaceDestroyTracker)packetWrapper.user().get((Class)BlockPlaceDestroyTracker.class)).place();
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 23, 9, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        ((Cooldown)packetWrapper.user().get((Class)Cooldown.class)).hit();
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 26, 10, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.create((ValueCreator)new ValueCreator() {
                    public void write(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                        final PacketWrapper delayedPacket = new PacketWrapper(26, (ByteBuf)null, packetWrapper.user());
                        delayedPacket.write((Type)Type.VAR_INT, (Object)0);
                        Protocol1_8TO1_9.TIMER.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                PacketUtil.sendToServer(delayedPacket, Protocol1_8TO1_9.class);
                            }
                        }, 5L);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        ((BlockPlaceDestroyTracker)packetWrapper.user().get((Class)BlockPlaceDestroyTracker.class)).updateMining();
                        ((Cooldown)packetWrapper.user().get((Class)Cooldown.class)).hit();
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 20, 11, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int action = (int)packetWrapper.get((Type)Type.VAR_INT, 1);
                        if (action == 6) {
                            packetWrapper.set((Type)Type.VAR_INT, 1, (Object)7);
                        }
                        else if (action == 0) {
                            final PlayerPosition pos = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                            if (!pos.isOnGround()) {
                                final PacketWrapper elytra = new PacketWrapper(20, (ByteBuf)null, packetWrapper.user());
                                elytra.write((Type)Type.VAR_INT, packetWrapper.get((Type)Type.VAR_INT, 0));
                                elytra.write((Type)Type.VAR_INT, (Object)8);
                                elytra.write((Type)Type.VAR_INT, (Object)0);
                                PacketUtil.sendToServer(elytra, Protocol1_8TO1_9.class, true, false);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 21, 12, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.UNSIGNED_BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final int playerId = tracker.getPlayerId();
                        final int vehicle = tracker.getVehicle(playerId);
                        if (vehicle != -1 && tracker.getClientEntityTypes().get(vehicle) == Entity1_10Types.EntityType.BOAT) {
                            final PacketWrapper steerBoat = new PacketWrapper(17, (ByteBuf)null, packetWrapper.user());
                            final float left = (float)packetWrapper.get((Type)Type.FLOAT, 0);
                            final float forward = (float)packetWrapper.get((Type)Type.FLOAT, 1);
                            steerBoat.write(Type.BOOLEAN, (Object)(forward != 0.0f || left < 0.0f));
                            steerBoat.write(Type.BOOLEAN, (Object)(forward != 0.0f || left > 0.0f));
                            PacketUtil.sendToServer(steerBoat, Protocol1_8TO1_9.class);
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 25, 18, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        for (int i = 0; i < 4; ++i) {
                            packetWrapper.write(Type.STRING, (Object)ChatUtil.jsonToLegacy((JsonElement)packetWrapper.read(Type.COMPONENT)));
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 18, 19);
        protocol.registerIncoming(State.PLAY, 1, 20, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.create((ValueCreator)new ValueCreator() {
                    public void write(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.write(Type.BOOLEAN, (Object)false);
                    }
                });
                this.map(Type.OPTIONAL_POSITION);
            }
        });
        protocol.registerIncoming(State.PLAY, 4, 21, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.map(Type.BYTE, (Type)Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.create((ValueCreator)new ValueCreator() {
                    public void write(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.write((Type)Type.VAR_INT, (Object)1);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final short flags = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        final PacketWrapper updateSkin = new PacketWrapper(28, (ByteBuf)null, packetWrapper.user());
                        updateSkin.write((Type)Type.VAR_INT, (Object)((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).getPlayerId());
                        final ArrayList<Metadata> metadata = new ArrayList<Metadata>();
                        metadata.add(new Metadata(10, (MetaType)MetaType1_8.Byte, (Object)(byte)flags));
                        updateSkin.write(Types1_8.METADATA_LIST, (Object)metadata);
                        PacketUtil.sendPacket(updateSkin, Protocol1_8TO1_9.class);
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 3, 22);
        protocol.registerIncoming(State.PLAY, 9, 23, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        String channel = (String)packetWrapper.get(Type.STRING, 0);
                        if (channel.equalsIgnoreCase("MC|BEdit") || channel.equalsIgnoreCase("MC|BSign")) {
                            final Item book = (Item)packetWrapper.passthrough(Type.ITEM);
                            book.setIdentifier(386);
                            final CompoundTag tag = book.getTag();
                            if (tag.contains("pages")) {
                                final ListTag pages = (ListTag)tag.get("pages");
                                for (int i = 0; i < pages.size(); ++i) {
                                    final StringTag page = (StringTag)pages.get(i);
                                    String value = page.getValue();
                                    value = ChatUtil.jsonToLegacy(value);
                                    page.setValue(value);
                                }
                            }
                        }
                        else if (channel.equalsIgnoreCase("MC|AdvCdm")) {
                            packetWrapper.set(Type.STRING, 0, (Object)(channel = "MC|AdvCmd"));
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 27, 24);
        protocol.registerIncoming(State.PLAY, 22, 25);
    }
}
