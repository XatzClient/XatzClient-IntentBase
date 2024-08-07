// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.handlers;

import us.myles.ViaVersion.api.protocol.ProtocolPipeline;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.score.Team;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.function.Function;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
import net.md_5.bungee.protocol.packet.PluginMessage;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.UUID;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import java.util.Iterator;
import us.myles.ViaVersion.api.data.ExternalJoinGameListener;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.event.EventHandler;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.Pair;
import java.util.List;
import us.myles.ViaVersion.api.data.UserConnection;
import java.lang.reflect.InvocationTargetException;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.bungee.service.ProtocolDetectorService;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.bungee.storage.BungeeStorage;
import us.myles.ViaVersion.api.Via;
import net.md_5.bungee.api.event.ServerConnectEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.md_5.bungee.api.plugin.Listener;

public class BungeeServerHandler implements Listener
{
    private static Method getHandshake;
    private static Method getRelayMessages;
    private static Method setProtocol;
    private static Method getEntityMap;
    private static Method setVersion;
    private static Field entityRewrite;
    private static Field channelWrapper;
    
    @EventHandler(priority = 120)
    public void onServerConnect(final ServerConnectEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final UserConnection user = Via.getManager().getConnection(e.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }
        if (!user.has(BungeeStorage.class)) {
            user.put(new BungeeStorage(user, e.getPlayer()));
        }
        final int protocolId = ProtocolDetectorService.getProtocolId(e.getTarget().getName());
        final List<Pair<Integer, Protocol>> protocols = ProtocolRegistry.getProtocolPath(user.getProtocolInfo().getProtocolVersion(), protocolId);
        try {
            final Object handshake = BungeeServerHandler.getHandshake.invoke(e.getPlayer().getPendingConnection(), new Object[0]);
            BungeeServerHandler.setProtocol.invoke(handshake, (protocols == null) ? user.getProtocolInfo().getProtocolVersion() : protocolId);
        }
        catch (InvocationTargetException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e2 = ex;
            e2.printStackTrace();
        }
    }
    
    @EventHandler(priority = -120)
    public void onServerConnected(final ServerConnectedEvent e) {
        try {
            this.checkServerChange(e, Via.getManager().getConnection(e.getPlayer().getUniqueId()));
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    
    @EventHandler(priority = -120)
    public void onServerSwitch(final ServerSwitchEvent e) {
        final UserConnection userConnection = Via.getManager().getConnection(e.getPlayer().getUniqueId());
        if (userConnection == null) {
            return;
        }
        int playerId;
        try {
            playerId = Via.getManager().getProviders().get(EntityIdProvider.class).getEntityId(userConnection);
        }
        catch (Exception ex) {
            return;
        }
        for (final StoredObject storedObject : userConnection.getStoredObjects().values()) {
            if (storedObject instanceof ExternalJoinGameListener) {
                ((ExternalJoinGameListener)storedObject).onExternalJoinGame(playerId);
            }
        }
    }
    
    public void checkServerChange(final ServerConnectedEvent e, final UserConnection user) throws Exception {
        if (user == null) {
            return;
        }
        if (user.has(BungeeStorage.class)) {
            final BungeeStorage storage = user.get(BungeeStorage.class);
            final ProxiedPlayer player = storage.getPlayer();
            if (e.getServer() != null && !e.getServer().getInfo().getName().equals(storage.getCurrentServer())) {
                final EntityTracker1_9 oldEntityTracker = user.get(EntityTracker1_9.class);
                if (oldEntityTracker != null && oldEntityTracker.isAutoTeam() && oldEntityTracker.isTeamExists()) {
                    oldEntityTracker.sendTeamPacket(false, true);
                }
                final String serverName = e.getServer().getInfo().getName();
                storage.setCurrentServer(serverName);
                int protocolId = ProtocolDetectorService.getProtocolId(serverName);
                if (protocolId <= ProtocolVersion.v1_8.getVersion() && storage.getBossbar() != null) {
                    if (user.getProtocolInfo().getPipeline().contains(Protocol1_9To1_8.class)) {
                        for (final UUID uuid : storage.getBossbar()) {
                            final PacketWrapper wrapper = new PacketWrapper(12, null, user);
                            wrapper.write(Type.UUID, uuid);
                            wrapper.write(Type.VAR_INT, 1);
                            wrapper.send(Protocol1_9To1_8.class, true, true);
                        }
                    }
                    storage.getBossbar().clear();
                }
                final ProtocolInfo info = user.getProtocolInfo();
                final int previousServerProtocol = info.getServerProtocolVersion();
                final List<Pair<Integer, Protocol>> protocols = ProtocolRegistry.getProtocolPath(info.getProtocolVersion(), protocolId);
                final ProtocolPipeline pipeline = user.getProtocolInfo().getPipeline();
                user.clearStoredObjects();
                pipeline.cleanPipes();
                if (protocols == null) {
                    protocolId = info.getProtocolVersion();
                }
                else {
                    for (final Pair<Integer, Protocol> prot : protocols) {
                        pipeline.add(prot.getValue());
                    }
                }
                info.setServerProtocolVersion(protocolId);
                pipeline.add(ProtocolRegistry.getBaseProtocol(protocolId));
                final Object relayMessages = BungeeServerHandler.getRelayMessages.invoke(e.getPlayer().getPendingConnection(), new Object[0]);
                for (final Object message : (List)relayMessages) {
                    final PluginMessage plMsg = (PluginMessage)message;
                    String channel = plMsg.getTag();
                    final int id1_13 = ProtocolVersion.v1_13.getVersion();
                    if (previousServerProtocol != -1) {
                        final String oldChannel = channel;
                        if (previousServerProtocol < id1_13 && protocolId >= id1_13) {
                            channel = InventoryPackets.getNewPluginChannelId(channel);
                            if (channel == null) {
                                throw new RuntimeException(oldChannel + " found in relayMessages");
                            }
                            if (channel.equals("minecraft:register")) {
                                plMsg.setData(Arrays.stream(new String(plMsg.getData(), StandardCharsets.UTF_8).split("\u0000")).map((Function<? super String, ?>)InventoryPackets::getNewPluginChannelId).filter(Objects::nonNull).collect((Collector<? super Object, ?, String>)Collectors.joining("\u0000")).getBytes(StandardCharsets.UTF_8));
                            }
                        }
                        else if (previousServerProtocol >= id1_13 && protocolId < id1_13) {
                            channel = InventoryPackets.getOldPluginChannelId(channel);
                            if (channel == null) {
                                throw new RuntimeException(oldChannel + " found in relayMessages");
                            }
                            if (channel.equals("REGISTER")) {
                                plMsg.setData(Arrays.stream(new String(plMsg.getData(), StandardCharsets.UTF_8).split("\u0000")).map((Function<? super String, ?>)InventoryPackets::getOldPluginChannelId).filter(Objects::nonNull).collect((Collector<? super Object, ?, String>)Collectors.joining("\u0000")).getBytes(StandardCharsets.UTF_8));
                            }
                        }
                    }
                    plMsg.setTag(channel);
                }
                user.put(info);
                user.put(storage);
                user.setActive(protocols != null);
                for (final Protocol protocol : pipeline.pipes()) {
                    protocol.init(user);
                }
                final EntityTracker1_9 newTracker = user.get(EntityTracker1_9.class);
                if (newTracker != null && Via.getConfig().isAutoTeam()) {
                    String currentTeam = null;
                    for (final Team team : player.getScoreboard().getTeams()) {
                        if (team.getPlayers().contains(info.getUsername())) {
                            currentTeam = team.getName();
                        }
                    }
                    newTracker.setAutoTeam(true);
                    if (currentTeam == null) {
                        newTracker.sendTeamPacket(true, true);
                        newTracker.setCurrentTeam("viaversion");
                    }
                    else {
                        newTracker.setAutoTeam(Via.getConfig().isAutoTeam());
                        newTracker.setCurrentTeam(currentTeam);
                    }
                }
                final Object wrapper2 = BungeeServerHandler.channelWrapper.get(player);
                BungeeServerHandler.setVersion.invoke(wrapper2, protocolId);
                final Object entityMap = BungeeServerHandler.getEntityMap.invoke(null, protocolId);
                BungeeServerHandler.entityRewrite.set(player, entityMap);
            }
        }
    }
    
    static {
        BungeeServerHandler.getEntityMap = null;
        BungeeServerHandler.setVersion = null;
        BungeeServerHandler.entityRewrite = null;
        BungeeServerHandler.channelWrapper = null;
        try {
            BungeeServerHandler.getHandshake = Class.forName("net.md_5.bungee.connection.InitialHandler").getDeclaredMethod("getHandshake", (Class<?>[])new Class[0]);
            BungeeServerHandler.getRelayMessages = Class.forName("net.md_5.bungee.connection.InitialHandler").getDeclaredMethod("getRelayMessages", (Class<?>[])new Class[0]);
            BungeeServerHandler.setProtocol = Class.forName("net.md_5.bungee.protocol.packet.Handshake").getDeclaredMethod("setProtocolVersion", Integer.TYPE);
            BungeeServerHandler.getEntityMap = Class.forName("net.md_5.bungee.entitymap.EntityMap").getDeclaredMethod("getEntityMap", Integer.TYPE);
            BungeeServerHandler.setVersion = Class.forName("net.md_5.bungee.netty.ChannelWrapper").getDeclaredMethod("setVersion", Integer.TYPE);
            (BungeeServerHandler.channelWrapper = Class.forName("net.md_5.bungee.UserConnection").getDeclaredField("ch")).setAccessible(true);
            (BungeeServerHandler.entityRewrite = Class.forName("net.md_5.bungee.UserConnection").getDeclaredField("entityRewrite")).setAccessible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
