// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Windows;
import us.myles.ViaVersion.api.type.types.CustomByteType;
import java.util.Iterator;
import java.util.List;
import us.myles.ViaVersion.util.GsonUtil;
import de.gerrygames.viarewind.utils.ChatUtil;
import de.gerrygames.viarewind.utils.math.AABB;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.math.RayTracing;
import de.gerrygames.viarewind.utils.math.Ray3d;
import de.gerrygames.viarewind.utils.math.Vector3d;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.ArmorStandReplacement;
import de.gerrygames.viarewind.utils.Utils;
import us.myles.ViaVersion.api.Via;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.provider.TitleRenderProvider;
import io.netty.buffer.Unpooled;
import com.google.common.base.Charsets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerAbilities;
import java.util.UUID;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.item.Item;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerPosition;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.ViaRewind;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class PlayerPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 1, 1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        if (!ViaRewind.getConfig().isReplaceAdventureMode()) {
                            return;
                        }
                        if ((short)packetWrapper.get(Type.UNSIGNED_BYTE, 0) == 2) {
                            packetWrapper.set(Type.UNSIGNED_BYTE, 0, (Object)0);
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.BOOLEAN);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        tracker.setGamemode((short)packetWrapper.get(Type.UNSIGNED_BYTE, 0));
                        tracker.setPlayerId((int)packetWrapper.get(Type.INT, 0));
                        tracker.getClientEntityTypes().put(tracker.getPlayerId(), Entity1_10Types.EntityType.ENTITY_HUMAN);
                        tracker.setDimension((byte)packetWrapper.get(Type.BYTE, 0));
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
        protocol.registerOutgoing(State.PLAY, 2, 2, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.COMPONENT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int position = (byte)packetWrapper.read(Type.BYTE);
                        if (position == 2) {
                            packetWrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 5, 5, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Position position = (Position)packetWrapper.read(Type.POSITION);
                        packetWrapper.write(Type.INT, (Object)position.getX());
                        packetWrapper.write(Type.INT, (Object)(int)position.getY());
                        packetWrapper.write(Type.INT, (Object)position.getZ());
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 6, 6, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.VAR_INT, (Type)Type.SHORT);
                this.map((Type)Type.FLOAT);
            }
        });
        protocol.registerOutgoing(State.PLAY, 7, 7, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        if (!ViaRewind.getConfig().isReplaceAdventureMode()) {
                            return;
                        }
                        if ((short)packetWrapper.get(Type.UNSIGNED_BYTE, 1) == 2) {
                            packetWrapper.set(Type.UNSIGNED_BYTE, 1, (Object)0);
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        tracker.setGamemode((short)packetWrapper.get(Type.UNSIGNED_BYTE, 1));
                        if (tracker.getDimension() != (int)packetWrapper.get(Type.INT, 0)) {
                            tracker.setDimension((int)packetWrapper.get(Type.INT, 0));
                            tracker.clearEntities();
                            tracker.getClientEntityTypes().put(tracker.getPlayerId(), Entity1_10Types.EntityType.ENTITY_HUMAN);
                        }
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
        protocol.registerOutgoing(State.PLAY, 8, 8, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        playerPosition.setPositionPacketReceived(true);
                        final int flags = (byte)packetWrapper.read(Type.BYTE);
                        if ((flags & 0x1) == 0x1) {
                            double x = (double)packetWrapper.get(Type.DOUBLE, 0);
                            x += playerPosition.getPosX();
                            packetWrapper.set(Type.DOUBLE, 0, (Object)x);
                        }
                        double y = (double)packetWrapper.get(Type.DOUBLE, 1);
                        if ((flags & 0x2) == 0x2) {
                            y += playerPosition.getPosY();
                        }
                        playerPosition.setReceivedPosY(y);
                        y += 1.6200000047683716;
                        packetWrapper.set(Type.DOUBLE, 1, (Object)y);
                        if ((flags & 0x4) == 0x4) {
                            double z = (double)packetWrapper.get(Type.DOUBLE, 2);
                            z += playerPosition.getPosZ();
                            packetWrapper.set(Type.DOUBLE, 2, (Object)z);
                        }
                        if ((flags & 0x8) == 0x8) {
                            float yaw = (float)packetWrapper.get((Type)Type.FLOAT, 0);
                            yaw += playerPosition.getYaw();
                            packetWrapper.set((Type)Type.FLOAT, 0, (Object)yaw);
                        }
                        if ((flags & 0x10) == 0x10) {
                            float pitch = (float)packetWrapper.get((Type)Type.FLOAT, 1);
                            pitch += playerPosition.getPitch();
                            packetWrapper.set((Type)Type.FLOAT, 1, (Object)pitch);
                        }
                    }
                });
                this.create((ValueCreator)new ValueCreator() {
                    public void write(final PacketWrapper packetWrapper) throws Exception {
                        final PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        packetWrapper.write(Type.BOOLEAN, (Object)playerPosition.isOnGround());
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        if (tracker.getSpectating() != tracker.getPlayerId()) {
                            packetWrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 31, 31, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.VAR_INT, (Type)Type.SHORT);
                this.map((Type)Type.VAR_INT, (Type)Type.SHORT);
            }
        });
        protocol.registerOutgoing(State.PLAY, 43, 43, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.FLOAT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int mode = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (mode != 3) {
                            return;
                        }
                        final int gamemode = ((Float)packetWrapper.get((Type)Type.FLOAT, 0)).intValue();
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        if (gamemode == 3 || tracker.getGamemode() == 3) {
                            final UUID uuid = ((ProtocolInfo)packetWrapper.user().get((Class)ProtocolInfo.class)).getUuid();
                            Item[] equipment;
                            if (gamemode == 3) {
                                final GameProfileStorage.GameProfile profile = ((GameProfileStorage)packetWrapper.user().get((Class)GameProfileStorage.class)).get(uuid);
                                equipment = new Item[] { null, null, null, null, profile.getSkull() };
                            }
                            else {
                                equipment = tracker.getPlayerEquipment(uuid);
                                if (equipment == null) {
                                    equipment = new Item[5];
                                }
                            }
                            for (int i = 1; i < 5; ++i) {
                                final PacketWrapper setSlot = new PacketWrapper(47, (ByteBuf)null, packetWrapper.user());
                                setSlot.write(Type.BYTE, (Object)0);
                                setSlot.write((Type)Type.SHORT, (Object)(short)(9 - i));
                                setSlot.write((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, (Object)equipment[i]);
                                PacketUtil.sendPacket(setSlot, Protocol1_7_6_10TO1_8.class);
                            }
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int mode = (short)packetWrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (mode == 3) {
                            int gamemode = ((Float)packetWrapper.get((Type)Type.FLOAT, 0)).intValue();
                            if (gamemode == 2 && ViaRewind.getConfig().isReplaceAdventureMode()) {
                                gamemode = 0;
                                packetWrapper.set((Type)Type.FLOAT, 0, (Object)0.0f);
                            }
                            ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).setGamemode(gamemode);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 54, 54, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Position position = (Position)packetWrapper.read(Type.POSITION);
                        packetWrapper.write(Type.INT, (Object)position.getX());
                        packetWrapper.write(Type.INT, (Object)(int)position.getY());
                        packetWrapper.write(Type.INT, (Object)position.getZ());
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 56, 56, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                        final int action = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final int count = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final GameProfileStorage gameProfileStorage = (GameProfileStorage)packetWrapper.user().get((Class)GameProfileStorage.class);
                        for (int i = 0; i < count; ++i) {
                            final UUID uuid = (UUID)packetWrapper.read(Type.UUID);
                            if (action == 0) {
                                final String name = (String)packetWrapper.read(Type.STRING);
                                GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                                if (gameProfile == null) {
                                    gameProfile = gameProfileStorage.put(uuid, name);
                                }
                                int propertyCount = (int)packetWrapper.read((Type)Type.VAR_INT);
                                while (propertyCount-- > 0) {
                                    gameProfile.properties.add(new GameProfileStorage.Property((String)packetWrapper.read(Type.STRING), (String)packetWrapper.read(Type.STRING), ((boolean)packetWrapper.read(Type.BOOLEAN)) ? ((String)packetWrapper.read(Type.STRING)) : null));
                                }
                                final int gamemode = (int)packetWrapper.read((Type)Type.VAR_INT);
                                final int ping = (int)packetWrapper.read((Type)Type.VAR_INT);
                                gameProfile.ping = ping;
                                gameProfile.gamemode = gamemode;
                                if (packetWrapper.read(Type.BOOLEAN)) {
                                    gameProfile.setDisplayName((String)packetWrapper.read(Type.STRING));
                                }
                                final PacketWrapper packet = new PacketWrapper(56, (ByteBuf)null, packetWrapper.user());
                                packet.write(Type.STRING, (Object)gameProfile.name);
                                packet.write(Type.BOOLEAN, (Object)true);
                                packet.write((Type)Type.SHORT, (Object)(short)ping);
                                PacketUtil.sendPacket(packet, Protocol1_7_6_10TO1_8.class);
                            }
                            else if (action == 1) {
                                final int gamemode2 = (int)packetWrapper.read((Type)Type.VAR_INT);
                                final GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                                if (gameProfile != null) {
                                    if (gameProfile.gamemode != gamemode2) {
                                        if (gamemode2 == 3 || gameProfile.gamemode == 3) {
                                            final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                                            final int entityId = tracker.getPlayerEntityId(uuid);
                                            if (entityId != -1) {
                                                Item[] equipment;
                                                if (gamemode2 == 3) {
                                                    equipment = new Item[] { null, null, null, null, gameProfile.getSkull() };
                                                }
                                                else {
                                                    equipment = tracker.getPlayerEquipment(uuid);
                                                    if (equipment == null) {
                                                        equipment = new Item[5];
                                                    }
                                                }
                                                for (short slot = 0; slot < 5; ++slot) {
                                                    final PacketWrapper equipmentPacket = new PacketWrapper(4, (ByteBuf)null, packetWrapper.user());
                                                    equipmentPacket.write(Type.INT, (Object)entityId);
                                                    equipmentPacket.write((Type)Type.SHORT, (Object)slot);
                                                    equipmentPacket.write((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, (Object)equipment[slot]);
                                                    PacketUtil.sendPacket(equipmentPacket, Protocol1_7_6_10TO1_8.class);
                                                }
                                            }
                                        }
                                        gameProfile.gamemode = gamemode2;
                                    }
                                }
                            }
                            else if (action == 2) {
                                final int ping2 = (int)packetWrapper.read((Type)Type.VAR_INT);
                                final GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                                if (gameProfile != null) {
                                    gameProfile.ping = ping2;
                                    final PacketWrapper packet2 = new PacketWrapper(56, (ByteBuf)null, packetWrapper.user());
                                    packet2.write(Type.STRING, (Object)gameProfile.name);
                                    packet2.write(Type.BOOLEAN, (Object)true);
                                    packet2.write((Type)Type.SHORT, (Object)(short)ping2);
                                    PacketUtil.sendPacket(packet2, Protocol1_7_6_10TO1_8.class);
                                }
                            }
                            else if (action == 3) {
                                final String displayName = packetWrapper.read(Type.BOOLEAN) ? ((String)packetWrapper.read(Type.STRING)) : null;
                                final GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                                if (gameProfile != null) {
                                    if (gameProfile.displayName != null || displayName != null) {
                                        if ((gameProfile.displayName == null && displayName != null) || (gameProfile.displayName != null && displayName == null) || !gameProfile.displayName.equals(displayName)) {
                                            gameProfile.setDisplayName(displayName);
                                        }
                                    }
                                }
                            }
                            else if (action == 4) {
                                final GameProfileStorage.GameProfile gameProfile2 = gameProfileStorage.remove(uuid);
                                if (gameProfile2 != null) {
                                    final PacketWrapper packet3 = new PacketWrapper(56, (ByteBuf)null, packetWrapper.user());
                                    packet3.write(Type.STRING, (Object)gameProfile2.name);
                                    packet3.write(Type.BOOLEAN, (Object)false);
                                    packet3.write((Type)Type.SHORT, (Object)(short)gameProfile2.ping);
                                    PacketUtil.sendPacket(packet3, Protocol1_7_6_10TO1_8.class);
                                }
                            }
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 57, 57, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BYTE);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final byte flags = (byte)packetWrapper.get(Type.BYTE, 0);
                        final float flySpeed = (float)packetWrapper.get((Type)Type.FLOAT, 0);
                        final float walkSpeed = (float)packetWrapper.get((Type)Type.FLOAT, 1);
                        final PlayerAbilities abilities = (PlayerAbilities)packetWrapper.user().get((Class)PlayerAbilities.class);
                        abilities.setInvincible((flags & 0x8) == 0x8);
                        abilities.setAllowFly((flags & 0x4) == 0x4);
                        abilities.setFlying((flags & 0x2) == 0x2);
                        abilities.setCreative((flags & 0x1) == 0x1);
                        abilities.setFlySpeed(flySpeed);
                        abilities.setWalkSpeed(walkSpeed);
                        if (abilities.isSprinting() && abilities.isFlying()) {
                            packetWrapper.set((Type)Type.FLOAT, 0, (Object)(abilities.getFlySpeed() * 2.0f));
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 63, 63, (PacketRemapper)new PacketRemapper() {
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
                                Item item = ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM));
                                packetWrapper.write((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, (Object)item);
                                item = ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM));
                                packetWrapper.write((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, (Object)item);
                                final boolean has3Items = (boolean)packetWrapper.passthrough(Type.BOOLEAN);
                                if (has3Items) {
                                    item = ItemRewriter.toClient((Item)packetWrapper.read(Type.ITEM));
                                    packetWrapper.write((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM, (Object)item);
                                }
                                packetWrapper.passthrough(Type.BOOLEAN);
                                packetWrapper.read(Type.INT);
                                packetWrapper.read(Type.INT);
                            }
                        }
                        else if (channel.equalsIgnoreCase("MC|Brand")) {
                            packetWrapper.write(Type.REMAINING_BYTES, (Object)((String)packetWrapper.read(Type.STRING)).getBytes(Charsets.UTF_8));
                        }
                        packetWrapper.cancel();
                        packetWrapper.setId(-1);
                        final ByteBuf newPacketBuf = Unpooled.buffer();
                        packetWrapper.writeToBuffer(newPacketBuf);
                        final PacketWrapper newWrapper = new PacketWrapper(63, newPacketBuf, packetWrapper.user());
                        newWrapper.passthrough(Type.STRING);
                        if (newPacketBuf.readableBytes() <= 32767) {
                            newWrapper.write((Type)Type.SHORT, (Object)(short)newPacketBuf.readableBytes());
                            newWrapper.send((Class)Protocol1_7_6_10TO1_8.class, true, true);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 67, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final int entityId = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final int spectating = tracker.getSpectating();
                        if (spectating != entityId) {
                            tracker.setSpectating(entityId);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 69, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                        final TitleRenderProvider titleRenderProvider = (TitleRenderProvider)Via.getManager().getProviders().get((Class)TitleRenderProvider.class);
                        if (titleRenderProvider == null) {
                            return;
                        }
                        final int action = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final UUID uuid = Utils.getUUID(packetWrapper.user());
                        switch (action) {
                            case 0: {
                                titleRenderProvider.setTitle(uuid, (String)packetWrapper.read(Type.STRING));
                                break;
                            }
                            case 1: {
                                titleRenderProvider.setSubTitle(uuid, (String)packetWrapper.read(Type.STRING));
                                break;
                            }
                            case 2: {
                                titleRenderProvider.setTimings(uuid, (int)packetWrapper.read(Type.INT), (int)packetWrapper.read(Type.INT), (int)packetWrapper.read(Type.INT));
                                break;
                            }
                            case 3: {
                                titleRenderProvider.clear(uuid);
                                break;
                            }
                            case 4: {
                                titleRenderProvider.reset(uuid);
                                break;
                            }
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 71, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 72, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 1, 1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final String msg = (String)packetWrapper.get(Type.STRING, 0);
                        final int gamemode = ((EntityTracker)packetWrapper.user().get((Class)EntityTracker.class)).getGamemode();
                        if (gamemode == 3 && msg.toLowerCase().startsWith("/stp ")) {
                            final String username = msg.split(" ")[1];
                            final GameProfileStorage storage = (GameProfileStorage)packetWrapper.user().get((Class)GameProfileStorage.class);
                            final GameProfileStorage.GameProfile profile = storage.get(username, true);
                            if (profile != null && profile.uuid != null) {
                                packetWrapper.cancel();
                                final PacketWrapper teleportPacket = new PacketWrapper(24, (ByteBuf)null, packetWrapper.user());
                                teleportPacket.write(Type.UUID, (Object)profile.uuid);
                                PacketUtil.sendToServer(teleportPacket, Protocol1_7_6_10TO1_8.class, true, true);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 2, 2, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT, (Type)Type.VAR_INT);
                this.map(Type.BYTE, (Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        int mode = (int)packetWrapper.get((Type)Type.VAR_INT, 1);
                        if (mode != 0) {
                            return;
                        }
                        final int entityId = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (!(replacement instanceof ArmorStandReplacement)) {
                            return;
                        }
                        final ArmorStandReplacement armorStand = (ArmorStandReplacement)replacement;
                        final AABB boundingBox = armorStand.getBoundingBox();
                        final PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        final Vector3d pos = new Vector3d(playerPosition.getPosX(), playerPosition.getPosY() + 1.8, playerPosition.getPosZ());
                        final double yaw = Math.toRadians(playerPosition.getYaw());
                        final double pitch = Math.toRadians(playerPosition.getPitch());
                        final Vector3d dir = new Vector3d(-Math.cos(pitch) * Math.sin(yaw), -Math.sin(pitch), Math.cos(pitch) * Math.cos(yaw));
                        final Ray3d ray = new Ray3d(pos, dir);
                        final Vector3d intersection = RayTracing.trace(ray, boundingBox, 5.0);
                        if (intersection == null) {
                            return;
                        }
                        intersection.substract(boundingBox.getMin());
                        mode = 2;
                        packetWrapper.set((Type)Type.VAR_INT, 1, (Object)mode);
                        packetWrapper.write((Type)Type.FLOAT, (Object)(float)intersection.getX());
                        packetWrapper.write((Type)Type.FLOAT, (Object)(float)intersection.getY());
                        packetWrapper.write((Type)Type.FLOAT, (Object)(float)intersection.getZ());
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 3, 3, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        playerPosition.setOnGround((boolean)packetWrapper.get(Type.BOOLEAN, 0));
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 4, 4, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.DOUBLE);
                    }
                });
                this.map(Type.DOUBLE);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final double x = (double)packetWrapper.get(Type.DOUBLE, 0);
                        double feetY = (double)packetWrapper.get(Type.DOUBLE, 1);
                        final double z = (double)packetWrapper.get(Type.DOUBLE, 2);
                        final PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        if (playerPosition.isPositionPacketReceived()) {
                            playerPosition.setPositionPacketReceived(false);
                            feetY -= 0.01;
                            packetWrapper.set(Type.DOUBLE, 1, (Object)feetY);
                        }
                        playerPosition.setOnGround((boolean)packetWrapper.get(Type.BOOLEAN, 0));
                        playerPosition.setPos(x, feetY, z);
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 5, 5, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        playerPosition.setYaw((float)packetWrapper.get((Type)Type.FLOAT, 0));
                        playerPosition.setPitch((float)packetWrapper.get((Type)Type.FLOAT, 1));
                        playerPosition.setOnGround((boolean)packetWrapper.get(Type.BOOLEAN, 0));
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 6, 6, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.DOUBLE);
                    }
                });
                this.map(Type.DOUBLE);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final double x = (double)packetWrapper.get(Type.DOUBLE, 0);
                        double feetY = (double)packetWrapper.get(Type.DOUBLE, 1);
                        final double z = (double)packetWrapper.get(Type.DOUBLE, 2);
                        final float yaw = (float)packetWrapper.get((Type)Type.FLOAT, 0);
                        final float pitch = (float)packetWrapper.get((Type)Type.FLOAT, 1);
                        final PlayerPosition playerPosition = (PlayerPosition)packetWrapper.user().get((Class)PlayerPosition.class);
                        if (playerPosition.isPositionPacketReceived()) {
                            playerPosition.setPositionPacketReceived(false);
                            feetY = playerPosition.getReceivedPosY();
                            packetWrapper.set(Type.DOUBLE, 1, (Object)feetY);
                        }
                        playerPosition.setOnGround((boolean)packetWrapper.get(Type.BOOLEAN, 0));
                        playerPosition.setPos(x, feetY, z);
                        playerPosition.setYaw(yaw);
                        playerPosition.setPitch(pitch);
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 7, 7, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int x = (int)packetWrapper.read(Type.INT);
                        final short y = (short)packetWrapper.read(Type.UNSIGNED_BYTE);
                        final int z = (int)packetWrapper.read(Type.INT);
                        packetWrapper.write(Type.POSITION, (Object)new Position(x, y, z));
                    }
                });
                this.map(Type.BYTE);
            }
        });
        protocol.registerIncoming(State.PLAY, 8, 8, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int x = (int)packetWrapper.read(Type.INT);
                        final short y = (short)packetWrapper.read(Type.UNSIGNED_BYTE);
                        final int z = (int)packetWrapper.read(Type.INT);
                        packetWrapper.write(Type.POSITION, (Object)new Position(x, y, z));
                        packetWrapper.passthrough(Type.BYTE);
                        Item item = (Item)packetWrapper.read((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM);
                        item = ItemRewriter.toServer(item);
                        packetWrapper.write(Type.ITEM, (Object)item);
                        for (int i = 0; i < 3; ++i) {
                            packetWrapper.passthrough(Type.BYTE);
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 10, 10, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int entityId = (int)packetWrapper.read(Type.INT);
                        int animation = (byte)packetWrapper.read(Type.BYTE);
                        if (animation == 1) {
                            return;
                        }
                        packetWrapper.cancel();
                        switch (animation) {
                            case 104: {
                                animation = 0;
                                break;
                            }
                            case 105: {
                                animation = 1;
                                break;
                            }
                            case 3: {
                                animation = 2;
                                break;
                            }
                            default: {
                                return;
                            }
                        }
                        final PacketWrapper entityAction = new PacketWrapper(11, (ByteBuf)null, packetWrapper.user());
                        entityAction.write((Type)Type.VAR_INT, (Object)entityId);
                        entityAction.write((Type)Type.VAR_INT, (Object)animation);
                        entityAction.write((Type)Type.VAR_INT, (Object)0);
                        PacketUtil.sendPacket(entityAction, Protocol1_7_6_10TO1_8.class, true, true);
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 11, 11, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT, (Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.write((Type)Type.VAR_INT, (Object)((byte)packetWrapper.read(Type.BYTE) - 1));
                    }
                });
                this.map(Type.INT, (Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int action = (int)packetWrapper.get((Type)Type.VAR_INT, 1);
                        if (action == 3 || action == 4) {
                            final PlayerAbilities abilities = (PlayerAbilities)packetWrapper.user().get((Class)PlayerAbilities.class);
                            abilities.setSprinting(action == 3);
                            final PacketWrapper abilitiesPacket = new PacketWrapper(57, (ByteBuf)null, packetWrapper.user());
                            abilitiesPacket.write(Type.BYTE, (Object)abilities.getFlags());
                            abilitiesPacket.write((Type)Type.FLOAT, (Object)(abilities.isSprinting() ? (abilities.getFlySpeed() * 2.0f) : abilities.getFlySpeed()));
                            abilitiesPacket.write((Type)Type.FLOAT, (Object)abilities.getWalkSpeed());
                            PacketUtil.sendPacket(abilitiesPacket, Protocol1_7_6_10TO1_8.class);
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 12, 12, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final boolean jump = (boolean)packetWrapper.read(Type.BOOLEAN);
                        final boolean unmount = (boolean)packetWrapper.read(Type.BOOLEAN);
                        short flags = 0;
                        if (jump) {
                            ++flags;
                        }
                        if (unmount) {
                            flags += 2;
                        }
                        packetWrapper.write(Type.UNSIGNED_BYTE, (Object)flags);
                        if (unmount) {
                            final EntityTracker tracker = (EntityTracker)packetWrapper.user().get((Class)EntityTracker.class);
                            if (tracker.getSpectating() != tracker.getPlayerId()) {
                                final PacketWrapper sneakPacket = new PacketWrapper(11, (ByteBuf)null, packetWrapper.user());
                                sneakPacket.write((Type)Type.VAR_INT, (Object)tracker.getPlayerId());
                                sneakPacket.write((Type)Type.VAR_INT, (Object)0);
                                sneakPacket.write((Type)Type.VAR_INT, (Object)0);
                                final PacketWrapper unsneakPacket = new PacketWrapper(11, (ByteBuf)null, packetWrapper.user());
                                unsneakPacket.write((Type)Type.VAR_INT, (Object)tracker.getPlayerId());
                                unsneakPacket.write((Type)Type.VAR_INT, (Object)1);
                                unsneakPacket.write((Type)Type.VAR_INT, (Object)0);
                                PacketUtil.sendToServer(sneakPacket, Protocol1_7_6_10TO1_8.class);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 18, 18, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int x = (int)packetWrapper.read(Type.INT);
                        final short y = (short)packetWrapper.read((Type)Type.SHORT);
                        final int z = (int)packetWrapper.read(Type.INT);
                        packetWrapper.write(Type.POSITION, (Object)new Position(x, y, z));
                        for (int i = 0; i < 4; ++i) {
                            String line = (String)packetWrapper.read(Type.STRING);
                            line = ChatUtil.legacyToJson(line);
                            packetWrapper.write(Type.COMPONENT, (Object)GsonUtil.getJsonParser().parse(line));
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 19, 19, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BYTE);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final byte flags = (byte)packetWrapper.get(Type.BYTE, 0);
                        final PlayerAbilities abilities = (PlayerAbilities)packetWrapper.user().get((Class)PlayerAbilities.class);
                        abilities.setAllowFly((flags & 0x4) == 0x4);
                        abilities.setFlying((flags & 0x2) == 0x2);
                        packetWrapper.set((Type)Type.FLOAT, 0, (Object)abilities.getFlySpeed());
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 20, 20, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.create((ValueCreator)new ValueCreator() {
                    public void write(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.write(Type.OPTIONAL_POSITION, (Object)null);
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final String msg = (String)packetWrapper.get(Type.STRING, 0);
                        if (msg.toLowerCase().startsWith("/stp ")) {
                            packetWrapper.cancel();
                            final String[] args = msg.split(" ");
                            if (args.length <= 2) {
                                final String prefix = (args.length == 1) ? "" : args[1];
                                final GameProfileStorage storage = (GameProfileStorage)packetWrapper.user().get((Class)GameProfileStorage.class);
                                final List<GameProfileStorage.GameProfile> profiles = storage.getAllWithPrefix(prefix, true);
                                final PacketWrapper tabComplete = new PacketWrapper(58, (ByteBuf)null, packetWrapper.user());
                                tabComplete.write((Type)Type.VAR_INT, (Object)profiles.size());
                                for (final GameProfileStorage.GameProfile profile : profiles) {
                                    tabComplete.write(Type.STRING, (Object)profile.name);
                                }
                                PacketUtil.sendPacket(tabComplete, Protocol1_7_6_10TO1_8.class);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 21, 21, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.BYTE);
                        final boolean cape = (boolean)packetWrapper.read(Type.BOOLEAN);
                        packetWrapper.write(Type.UNSIGNED_BYTE, (Object)(short)(cape ? 127 : 126));
                    }
                });
            }
        });
        protocol.registerIncoming(State.PLAY, 23, 23, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final String channel = (String)packetWrapper.get(Type.STRING, 0);
                        final int length = (short)packetWrapper.read((Type)Type.SHORT);
                        if (channel.equalsIgnoreCase("MC|ItemName")) {
                            final CustomByteType customByteType = new CustomByteType(Integer.valueOf(length));
                            byte[] data = (byte[])packetWrapper.read((Type)customByteType);
                            final String name = new String(data, Charsets.UTF_8);
                            final ByteBuf buf = packetWrapper.user().getChannel().alloc().buffer();
                            Type.STRING.write(buf, (Object)name);
                            data = new byte[buf.readableBytes()];
                            buf.readBytes(data);
                            buf.release();
                            packetWrapper.write(Type.REMAINING_BYTES, (Object)data);
                            final Windows windows = (Windows)packetWrapper.user().get((Class)Windows.class);
                            final PacketWrapper updateCost = new PacketWrapper(49, (ByteBuf)null, packetWrapper.user());
                            updateCost.write(Type.UNSIGNED_BYTE, (Object)windows.anvilId);
                            updateCost.write((Type)Type.SHORT, (Object)0);
                            updateCost.write((Type)Type.SHORT, (Object)windows.levelCost);
                            PacketUtil.sendPacket(updateCost, Protocol1_7_6_10TO1_8.class, true, true);
                        }
                        else if (channel.equalsIgnoreCase("MC|BEdit") || channel.equalsIgnoreCase("MC|BSign")) {
                            final Item book = (Item)packetWrapper.read((Type)Types1_7_6_10.COMPRESSED_NBT_ITEM);
                            final CompoundTag tag = book.getTag();
                            if (tag != null && tag.contains("pages")) {
                                final ListTag pages = (ListTag)tag.get("pages");
                                for (int i = 0; i < pages.size(); ++i) {
                                    final StringTag page = (StringTag)pages.get(i);
                                    String value = page.getValue();
                                    value = ChatUtil.legacyToJson(value);
                                    page.setValue(value);
                                }
                            }
                            packetWrapper.write(Type.ITEM, (Object)book);
                        }
                        else if (channel.equalsIgnoreCase("MC|Brand")) {
                            packetWrapper.write((Type)Type.VAR_INT, (Object)length);
                        }
                    }
                });
            }
        });
    }
}
