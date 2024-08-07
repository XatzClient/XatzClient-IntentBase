// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.packets;

import us.myles.ViaVersion.protocols.protocol1_9to1_8.PlayerMovementMapper;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.MainHandProvider;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.ClientChunks;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ItemRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.CommandBlockProvider;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.chat.GameMode;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.chat.ChatRewriter;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_8.ClientboundPackets1_8;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;

public class PlayerPackets
{
    public static void register(final Protocol1_9To1_8 protocol) {
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        try {
                            final JsonObject obj = wrapper.get((Type<JsonObject>)Type.COMPONENT, 0);
                            ChatRewriter.toClient(obj, wrapper.user());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.TAB_LIST, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.DISCONNECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.TITLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (action == 0 || action == 1) {
                            Protocol1_9To1_8.FIX_JSON.write(wrapper, wrapper.read(Type.STRING));
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.PLAYER_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BYTE);
                this.create(new ValueCreator() {
                    @Override
                    public void write(final PacketWrapper wrapper) {
                        wrapper.write(Type.VAR_INT, 0);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.TEAMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final byte mode = wrapper.get(Type.BYTE, 0);
                        if (mode == 0 || mode == 2) {
                            wrapper.passthrough(Type.STRING);
                            wrapper.passthrough(Type.STRING);
                            wrapper.passthrough(Type.STRING);
                            wrapper.passthrough(Type.BYTE);
                            wrapper.passthrough(Type.STRING);
                            wrapper.write(Type.STRING, Via.getConfig().isPreventCollision() ? "never" : "");
                            wrapper.passthrough(Type.BYTE);
                        }
                        if (mode == 0 || mode == 3 || mode == 4) {
                            final String[] players = wrapper.passthrough(Type.STRING_ARRAY);
                            final EntityTracker1_9 entityTracker = wrapper.user().get(EntityTracker1_9.class);
                            final String myName = wrapper.user().getProtocolInfo().getUsername();
                            final String teamName = wrapper.get(Type.STRING, 0);
                            for (final String player : players) {
                                if (entityTracker.isAutoTeam() && player.equalsIgnoreCase(myName)) {
                                    if (mode == 4) {
                                        wrapper.send(Protocol1_9To1_8.class, true, true);
                                        wrapper.cancel();
                                        entityTracker.sendTeamPacket(true, true);
                                        entityTracker.setCurrentTeam("viaversion");
                                    }
                                    else {
                                        entityTracker.sendTeamPacket(false, true);
                                        entityTracker.setCurrentTeam(teamName);
                                    }
                                }
                            }
                        }
                        if (mode == 1) {
                            final EntityTracker1_9 entityTracker2 = wrapper.user().get(EntityTracker1_9.class);
                            final String teamName2 = wrapper.get(Type.STRING, 0);
                            if (entityTracker2.isAutoTeam() && teamName2.equals(entityTracker2.getCurrentTeam())) {
                                wrapper.send(Protocol1_9To1_8.class, true, true);
                                wrapper.cancel();
                                entityTracker2.sendTeamPacket(true, true);
                                entityTracker2.setCurrentTeam("viaversion");
                            }
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = wrapper.get(Type.INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().get(EntityTracker1_9.class);
                        tracker.addEntity(entityId, Entity1_10Types.EntityType.PLAYER);
                        tracker.setClientEntityId(entityId);
                    }
                });
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.BOOLEAN);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final EntityTracker1_9 tracker = wrapper.user().get(EntityTracker1_9.class);
                        tracker.setGameMode(GameMode.getById(wrapper.get(Type.UNSIGNED_BYTE, 0)));
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                        provider.sendPermission(wrapper.user());
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final EntityTracker1_9 entityTracker = wrapper.user().get(EntityTracker1_9.class);
                        if (Via.getConfig().isAutoTeam()) {
                            entityTracker.setAutoTeam(true);
                            wrapper.send(Protocol1_9To1_8.class, true, true);
                            wrapper.cancel();
                            entityTracker.sendTeamPacket(true, true);
                            entityTracker.setCurrentTeam("viaversion");
                        }
                        else {
                            entityTracker.setAutoTeam(false);
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.PLAYER_INFO, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        for (int count = wrapper.get((Type<Integer>)Type.VAR_INT, 1), i = 0; i < count; ++i) {
                            wrapper.passthrough(Type.UUID);
                            if (action == 0) {
                                wrapper.passthrough(Type.STRING);
                                for (int properties = wrapper.passthrough((Type<Integer>)Type.VAR_INT), j = 0; j < properties; ++j) {
                                    wrapper.passthrough(Type.STRING);
                                    wrapper.passthrough(Type.STRING);
                                    final boolean isSigned = wrapper.passthrough(Type.BOOLEAN);
                                    if (isSigned) {
                                        wrapper.passthrough(Type.STRING);
                                    }
                                }
                                wrapper.passthrough((Type<Object>)Type.VAR_INT);
                                wrapper.passthrough((Type<Object>)Type.VAR_INT);
                                final boolean hasDisplayName = wrapper.passthrough(Type.BOOLEAN);
                                if (hasDisplayName) {
                                    Protocol1_9To1_8.FIX_JSON.write(wrapper, wrapper.read(Type.STRING));
                                }
                            }
                            else if (action == 1 || action == 2) {
                                wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            }
                            else if (action == 3) {
                                final boolean hasDisplayName2 = wrapper.passthrough(Type.BOOLEAN);
                                if (hasDisplayName2) {
                                    Protocol1_9To1_8.FIX_JSON.write(wrapper, wrapper.read(Type.STRING));
                                }
                            }
                            else if (action == 4) {}
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String name = wrapper.get(Type.STRING, 0);
                        if (name.equalsIgnoreCase("MC|BOpen")) {
                            wrapper.read(Type.REMAINING_BYTES);
                            wrapper.write(Type.VAR_INT, 0);
                        }
                        if (name.equalsIgnoreCase("MC|TrList")) {
                            wrapper.passthrough(Type.INT);
                            final Short size = wrapper.passthrough(Type.UNSIGNED_BYTE);
                            for (int i = 0; i < size; ++i) {
                                final Item item1 = wrapper.passthrough(Type.ITEM);
                                ItemRewriter.toClient(item1);
                                final Item item2 = wrapper.passthrough(Type.ITEM);
                                ItemRewriter.toClient(item2);
                                final boolean present = wrapper.passthrough(Type.BOOLEAN);
                                if (present) {
                                    final Item item3 = wrapper.passthrough(Type.ITEM);
                                    ItemRewriter.toClient(item3);
                                }
                                wrapper.passthrough(Type.BOOLEAN);
                                wrapper.passthrough(Type.INT);
                                wrapper.passthrough(Type.INT);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.UPDATE_HEALTH, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final float health = wrapper.get((Type<Float>)Type.FLOAT, 0);
                        if (health <= 0.0f) {
                            final ClientChunks cc = wrapper.user().get(ClientChunks.class);
                            cc.getBulkChunks().clear();
                            cc.getLoadedChunks().clear();
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientChunks cc = wrapper.user().get(ClientChunks.class);
                        cc.getBulkChunks().clear();
                        cc.getLoadedChunks().clear();
                        final int gamemode = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        wrapper.user().get(EntityTracker1_9.class).setGameMode(GameMode.getById(gamemode));
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                        provider.sendPermission(wrapper.user());
                        provider.unloadChunks(wrapper.user());
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_8.GAME_EVENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.FLOAT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (wrapper.get(Type.UNSIGNED_BYTE, 0) == 3) {
                            final int gamemode = wrapper.get((Type<Float>)Type.FLOAT, 0).intValue();
                            wrapper.user().get(EntityTracker1_9.class).setGameMode(GameMode.getById(gamemode));
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, C2, S1, S2>)protocol).cancelOutgoing(ClientboundPackets1_8.SET_COMPRESSION);
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerIncoming(ServerboundPackets1_9.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BOOLEAN, Type.NOTHING);
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerIncoming(ServerboundPackets1_9.CLIENT_SETTINGS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT, Type.BYTE);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                        if (Via.getConfig().isLeftHandedHandling() && hand == 0) {
                            wrapper.set(Type.UNSIGNED_BYTE, 0, (short)(wrapper.get(Type.UNSIGNED_BYTE, 0) | 0x80));
                        }
                        wrapper.sendToServer(Protocol1_9To1_8.class, true, true);
                        wrapper.cancel();
                        Via.getManager().getProviders().get(MainHandProvider.class).setMainHand(wrapper.user(), hand);
                    }
                });
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerIncoming(ServerboundPackets1_9.ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.NOTHING);
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).cancelIncoming(ServerboundPackets1_9.TELEPORT_CONFIRM);
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).cancelIncoming(ServerboundPackets1_9.VEHICLE_MOVE);
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).cancelIncoming(ServerboundPackets1_9.STEER_BOAT);
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerIncoming(ServerboundPackets1_9.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String name = wrapper.get(Type.STRING, 0);
                        if (name.equalsIgnoreCase("MC|BSign")) {
                            final Item item = wrapper.passthrough(Type.ITEM);
                            if (item != null) {
                                item.setIdentifier(387);
                                ItemRewriter.rewriteBookToServer(item);
                            }
                        }
                        if (name.equalsIgnoreCase("MC|AutoCmd")) {
                            wrapper.set(Type.STRING, 0, "MC|AdvCdm");
                            wrapper.write(Type.BYTE, (Byte)0);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough(Type.STRING);
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.clearInputBuffer();
                        }
                        if (name.equalsIgnoreCase("MC|AdvCmd")) {
                            wrapper.set(Type.STRING, 0, "MC|AdvCdm");
                        }
                    }
                });
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerIncoming(ServerboundPackets1_9.CLIENT_STATUS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (action == 2) {
                            final EntityTracker1_9 tracker = wrapper.user().get(EntityTracker1_9.class);
                            if (tracker.isBlocking()) {
                                tracker.setSecondHand(null);
                                tracker.setBlocking(false);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerIncoming(ServerboundPackets1_9.PLAYER_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BOOLEAN);
                this.handler(new PlayerMovementMapper());
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerIncoming(ServerboundPackets1_9.PLAYER_POSITION_AND_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BOOLEAN);
                this.handler(new PlayerMovementMapper());
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerIncoming(ServerboundPackets1_9.PLAYER_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BOOLEAN);
                this.handler(new PlayerMovementMapper());
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerIncoming(ServerboundPackets1_9.PLAYER_MOVEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BOOLEAN);
                this.handler(new PlayerMovementMapper());
            }
        });
    }
}
