// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.boss;

import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.Objects;
import us.myles.ViaVersion.api.Via;
import java.util.HashSet;
import java.util.Map;
import java.util.Collections;
import java.util.WeakHashMap;
import com.google.common.base.Preconditions;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossFlag;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.Set;
import java.util.UUID;
import us.myles.ViaVersion.api.boss.BossBar;

public abstract class CommonBoss<T> extends BossBar<T>
{
    private final UUID uuid;
    private final Set<UserConnection> connections;
    private final Set<BossFlag> flags;
    private String title;
    private float health;
    private BossColor color;
    private BossStyle style;
    private boolean visible;
    
    public CommonBoss(final String title, final float health, final BossColor color, final BossStyle style) {
        Preconditions.checkNotNull((Object)title, (Object)"Title cannot be null");
        Preconditions.checkArgument(health >= 0.0f && health <= 1.0f, (Object)"Health must be between 0 and 1");
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.health = health;
        this.color = ((color == null) ? BossColor.PURPLE : color);
        this.style = ((style == null) ? BossStyle.SOLID : style);
        this.connections = Collections.newSetFromMap(new WeakHashMap<UserConnection, Boolean>());
        this.flags = new HashSet<BossFlag>();
        this.visible = true;
    }
    
    @Override
    public BossBar setTitle(final String title) {
        Preconditions.checkNotNull((Object)title);
        this.title = title;
        this.sendPacket(UpdateAction.UPDATE_TITLE);
        return this;
    }
    
    @Override
    public BossBar setHealth(final float health) {
        Preconditions.checkArgument(health >= 0.0f && health <= 1.0f, (Object)"Health must be between 0 and 1");
        this.health = health;
        this.sendPacket(UpdateAction.UPDATE_HEALTH);
        return this;
    }
    
    @Override
    public BossColor getColor() {
        return this.color;
    }
    
    @Override
    public BossBar setColor(final BossColor color) {
        Preconditions.checkNotNull((Object)color);
        this.color = color;
        this.sendPacket(UpdateAction.UPDATE_STYLE);
        return this;
    }
    
    @Override
    public BossBar setStyle(final BossStyle style) {
        Preconditions.checkNotNull((Object)style);
        this.style = style;
        this.sendPacket(UpdateAction.UPDATE_STYLE);
        return this;
    }
    
    @Override
    public BossBar addPlayer(final UUID player) {
        return this.addConnection(Via.getManager().getConnection(player));
    }
    
    @Override
    public BossBar addConnection(final UserConnection conn) {
        if (this.connections.add(conn) && this.visible) {
            this.sendPacketConnection(conn, this.getPacket(UpdateAction.ADD, conn));
        }
        return this;
    }
    
    @Override
    public BossBar removePlayer(final UUID uuid) {
        return this.removeConnection(Via.getManager().getConnection(uuid));
    }
    
    @Override
    public BossBar removeConnection(final UserConnection conn) {
        if (this.connections.remove(conn)) {
            this.sendPacketConnection(conn, this.getPacket(UpdateAction.REMOVE, conn));
        }
        return this;
    }
    
    @Override
    public BossBar addFlag(final BossFlag flag) {
        Preconditions.checkNotNull((Object)flag);
        if (!this.hasFlag(flag)) {
            this.flags.add(flag);
        }
        this.sendPacket(UpdateAction.UPDATE_FLAGS);
        return this;
    }
    
    @Override
    public BossBar removeFlag(final BossFlag flag) {
        Preconditions.checkNotNull((Object)flag);
        if (this.hasFlag(flag)) {
            this.flags.remove(flag);
        }
        this.sendPacket(UpdateAction.UPDATE_FLAGS);
        return this;
    }
    
    @Override
    public boolean hasFlag(final BossFlag flag) {
        Preconditions.checkNotNull((Object)flag);
        return this.flags.contains(flag);
    }
    
    @Override
    public Set<UUID> getPlayers() {
        return this.connections.stream().map(conn -> Via.getManager().getConnectedClientId(conn)).filter(Objects::nonNull).collect((Collector<? super Object, ?, Set<UUID>>)Collectors.toSet());
    }
    
    @Override
    public Set<UserConnection> getConnections() {
        return Collections.unmodifiableSet((Set<? extends UserConnection>)this.connections);
    }
    
    @Override
    public BossBar show() {
        this.setVisible(true);
        return this;
    }
    
    @Override
    public BossBar hide() {
        this.setVisible(false);
        return this;
    }
    
    @Override
    public boolean isVisible() {
        return this.visible;
    }
    
    private void setVisible(final boolean value) {
        if (this.visible != value) {
            this.visible = value;
            this.sendPacket(value ? UpdateAction.ADD : UpdateAction.REMOVE);
        }
    }
    
    @Override
    public UUID getId() {
        return this.uuid;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    @Override
    public String getTitle() {
        return this.title;
    }
    
    @Override
    public float getHealth() {
        return this.health;
    }
    
    @Override
    public BossStyle getStyle() {
        return this.style;
    }
    
    public Set<BossFlag> getFlags() {
        return this.flags;
    }
    
    private void sendPacket(final UpdateAction action) {
        for (final UserConnection conn : new ArrayList<UserConnection>(this.connections)) {
            final PacketWrapper wrapper = this.getPacket(action, conn);
            this.sendPacketConnection(conn, wrapper);
        }
    }
    
    private void sendPacketConnection(final UserConnection conn, final PacketWrapper wrapper) {
        if (conn.getProtocolInfo() == null || !conn.getProtocolInfo().getPipeline().contains(Protocol1_9To1_8.class)) {
            this.connections.remove(conn);
            return;
        }
        try {
            wrapper.send(Protocol1_9To1_8.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private PacketWrapper getPacket(final UpdateAction action, final UserConnection connection) {
        try {
            final PacketWrapper wrapper = new PacketWrapper(12, null, connection);
            wrapper.write(Type.UUID, this.uuid);
            wrapper.write(Type.VAR_INT, action.getId());
            switch (action) {
                case ADD: {
                    Protocol1_9To1_8.FIX_JSON.write(wrapper, this.title);
                    wrapper.write(Type.FLOAT, this.health);
                    wrapper.write(Type.VAR_INT, this.color.getId());
                    wrapper.write(Type.VAR_INT, this.style.getId());
                    wrapper.write(Type.BYTE, (byte)this.flagToBytes());
                }
                case UPDATE_HEALTH: {
                    wrapper.write(Type.FLOAT, this.health);
                    break;
                }
                case UPDATE_TITLE: {
                    Protocol1_9To1_8.FIX_JSON.write(wrapper, this.title);
                    break;
                }
                case UPDATE_STYLE: {
                    wrapper.write(Type.VAR_INT, this.color.getId());
                    wrapper.write(Type.VAR_INT, this.style.getId());
                    break;
                }
                case UPDATE_FLAGS: {
                    wrapper.write(Type.BYTE, (byte)this.flagToBytes());
                    break;
                }
            }
            return wrapper;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private int flagToBytes() {
        int bitmask = 0;
        for (final BossFlag flag : this.flags) {
            bitmask |= flag.getId();
        }
        return bitmask;
    }
    
    private enum UpdateAction
    {
        ADD(0), 
        REMOVE(1), 
        UPDATE_HEALTH(2), 
        UPDATE_TITLE(3), 
        UPDATE_STYLE(4), 
        UPDATE_FLAGS(5);
        
        private final int id;
        
        private UpdateAction(final int id) {
            this.id = id;
        }
        
        public int getId() {
            return this.id;
        }
    }
}
