// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.platform;

import io.netty.channel.ChannelFuture;
import org.jetbrains.annotations.Nullable;
import io.netty.util.concurrent.GenericFutureListener;
import us.myles.ViaVersion.api.Via;
import java.util.Objects;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.UUID;
import java.util.Map;

public class ViaConnectionManager
{
    protected final Map<UUID, UserConnection> clients;
    protected final Set<UserConnection> connections;
    
    public ViaConnectionManager() {
        this.clients = new ConcurrentHashMap<UUID, UserConnection>();
        this.connections = Collections.newSetFromMap(new ConcurrentHashMap<UserConnection, Boolean>());
    }
    
    public void onLoginSuccess(final UserConnection connection) {
        Objects.requireNonNull(connection, "connection is null!");
        this.connections.add(connection);
        if (this.isFrontEnd(connection)) {
            final UUID id = connection.getProtocolInfo().getUuid();
            if (this.clients.put(id, connection) != null) {
                Via.getPlatform().getLogger().warning("Duplicate UUID on frontend connection! (" + id + ")");
            }
        }
        if (connection.getChannel() != null) {
            connection.getChannel().closeFuture().addListener((GenericFutureListener)(future -> this.onDisconnect(connection)));
        }
    }
    
    public void onDisconnect(final UserConnection connection) {
        Objects.requireNonNull(connection, "connection is null!");
        this.connections.remove(connection);
        if (this.isFrontEnd(connection)) {
            final UUID id = connection.getProtocolInfo().getUuid();
            this.clients.remove(id);
        }
    }
    
    public boolean isFrontEnd(final UserConnection conn) {
        return !conn.isClientSide();
    }
    
    public Map<UUID, UserConnection> getConnectedClients() {
        return Collections.unmodifiableMap((Map<? extends UUID, ? extends UserConnection>)this.clients);
    }
    
    @Nullable
    public UserConnection getConnectedClient(final UUID clientIdentifier) {
        return this.clients.get(clientIdentifier);
    }
    
    @Nullable
    public UUID getConnectedClientId(final UserConnection conn) {
        if (conn.getProtocolInfo() == null) {
            return null;
        }
        final UUID uuid = conn.getProtocolInfo().getUuid();
        final UserConnection client = this.clients.get(uuid);
        if (client != null && client.equals(conn)) {
            return uuid;
        }
        return null;
    }
    
    public Set<UserConnection> getConnections() {
        return Collections.unmodifiableSet((Set<? extends UserConnection>)this.connections);
    }
    
    public boolean isClientConnected(final UUID playerId) {
        return this.clients.containsKey(playerId);
    }
}
