// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.bossbar;

import java.util.List;
import us.myles.ViaVersion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_8;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.ArrayList;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.Collections;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import java.util.Set;
import us.myles.ViaVersion.api.boss.BossFlag;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.UUID;
import us.myles.ViaVersion.api.boss.BossBar;

public class WitherBossBar extends BossBar
{
    private static int highestId;
    private final UUID uuid;
    private String title;
    private float health;
    private boolean visible;
    private UserConnection connection;
    private final int entityId;
    private double locX;
    private double locY;
    private double locZ;
    
    public WitherBossBar(final UserConnection connection, final UUID uuid, final String title, final float health) {
        this.visible = false;
        this.entityId = WitherBossBar.highestId++;
        this.connection = connection;
        this.uuid = uuid;
        this.title = title;
        this.health = health;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public BossBar setTitle(final String title) {
        this.title = title;
        if (this.visible) {
            this.updateMetadata();
        }
        return this;
    }
    
    public float getHealth() {
        return this.health;
    }
    
    public BossBar setHealth(final float health) {
        this.health = health;
        if (this.health <= 0.0f) {
            this.health = 1.0E-4f;
        }
        if (this.visible) {
            this.updateMetadata();
        }
        return this;
    }
    
    public BossColor getColor() {
        return null;
    }
    
    public BossBar setColor(final BossColor bossColor) {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support color");
    }
    
    public BossStyle getStyle() {
        return null;
    }
    
    public BossBar setStyle(final BossStyle bossStyle) {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support styles");
    }
    
    public BossBar addPlayer(final UUID uuid) {
        throw new UnsupportedOperationException(this.getClass().getName() + " is only for one UserConnection!");
    }
    
    public BossBar addConnection(final UserConnection userConnection) {
        throw new UnsupportedOperationException(this.getClass().getName() + " is only for one UserConnection!");
    }
    
    public BossBar removePlayer(final UUID uuid) {
        throw new UnsupportedOperationException(this.getClass().getName() + " is only for one UserConnection!");
    }
    
    public BossBar removeConnection(final UserConnection userConnection) {
        throw new UnsupportedOperationException(this.getClass().getName() + " is only for one UserConnection!");
    }
    
    public BossBar addFlag(final BossFlag bossFlag) {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support flags");
    }
    
    public BossBar removeFlag(final BossFlag bossFlag) {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support flags");
    }
    
    public boolean hasFlag(final BossFlag bossFlag) {
        return false;
    }
    
    public Set<UUID> getPlayers() {
        return Collections.singleton(((ProtocolInfo)this.connection.get((Class)ProtocolInfo.class)).getUuid());
    }
    
    public Set<UserConnection> getConnections() {
        throw new UnsupportedOperationException(this.getClass().getName() + " is only for one UserConnection!");
    }
    
    public BossBar show() {
        if (!this.visible) {
            this.visible = true;
            this.spawnWither();
        }
        return this;
    }
    
    public BossBar hide() {
        if (this.visible) {
            this.visible = false;
            this.despawnWither();
        }
        return this;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public UUID getId() {
        return this.uuid;
    }
    
    public void setLocation(final double x, final double y, final double z) {
        this.locX = x;
        this.locY = y;
        this.locZ = z;
        this.updateLocation();
    }
    
    private void spawnWither() {
        final PacketWrapper packetWrapper = new PacketWrapper(15, (ByteBuf)null, this.connection);
        packetWrapper.write((Type)Type.VAR_INT, (Object)this.entityId);
        packetWrapper.write(Type.UNSIGNED_BYTE, (Object)64);
        packetWrapper.write(Type.INT, (Object)(int)(this.locX * 32.0));
        packetWrapper.write(Type.INT, (Object)(int)(this.locY * 32.0));
        packetWrapper.write(Type.INT, (Object)(int)(this.locZ * 32.0));
        packetWrapper.write(Type.BYTE, (Object)0);
        packetWrapper.write(Type.BYTE, (Object)0);
        packetWrapper.write(Type.BYTE, (Object)0);
        packetWrapper.write((Type)Type.SHORT, (Object)0);
        packetWrapper.write((Type)Type.SHORT, (Object)0);
        packetWrapper.write((Type)Type.SHORT, (Object)0);
        final List<Metadata> metadata = new ArrayList<Metadata>();
        metadata.add(new Metadata(0, (MetaType)MetaType1_8.Byte, (Object)32));
        metadata.add(new Metadata(2, (MetaType)MetaType1_8.String, (Object)this.title));
        metadata.add(new Metadata(3, (MetaType)MetaType1_8.Byte, (Object)1));
        metadata.add(new Metadata(6, (MetaType)MetaType1_8.Float, (Object)(this.health * 300.0f)));
        packetWrapper.write(Types1_8.METADATA_LIST, (Object)metadata);
        PacketUtil.sendPacket(packetWrapper, Protocol1_8TO1_9.class, true, true);
    }
    
    private void updateLocation() {
        final PacketWrapper packetWrapper = new PacketWrapper(24, (ByteBuf)null, this.connection);
        packetWrapper.write((Type)Type.VAR_INT, (Object)this.entityId);
        packetWrapper.write(Type.INT, (Object)(int)(this.locX * 32.0));
        packetWrapper.write(Type.INT, (Object)(int)(this.locY * 32.0));
        packetWrapper.write(Type.INT, (Object)(int)(this.locZ * 32.0));
        packetWrapper.write(Type.BYTE, (Object)0);
        packetWrapper.write(Type.BYTE, (Object)0);
        packetWrapper.write(Type.BOOLEAN, (Object)false);
        PacketUtil.sendPacket(packetWrapper, Protocol1_8TO1_9.class, true, true);
    }
    
    private void updateMetadata() {
        final PacketWrapper packetWrapper = new PacketWrapper(28, (ByteBuf)null, this.connection);
        packetWrapper.write((Type)Type.VAR_INT, (Object)this.entityId);
        final List<Metadata> metadata = new ArrayList<Metadata>();
        metadata.add(new Metadata(2, (MetaType)MetaType1_8.String, (Object)this.title));
        metadata.add(new Metadata(6, (MetaType)MetaType1_8.Float, (Object)(this.health * 300.0f)));
        packetWrapper.write(Types1_8.METADATA_LIST, (Object)metadata);
        PacketUtil.sendPacket(packetWrapper, Protocol1_8TO1_9.class, true, true);
    }
    
    private void despawnWither() {
        final PacketWrapper packetWrapper = new PacketWrapper(19, (ByteBuf)null, this.connection);
        packetWrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, (Object)new int[] { this.entityId });
        PacketUtil.sendPacket(packetWrapper, Protocol1_8TO1_9.class, true, true);
    }
    
    public void setPlayerLocation(double posX, double posY, double posZ, final float yaw, final float pitch) {
        final double yawR = Math.toRadians(yaw);
        final double pitchR = Math.toRadians(pitch);
        posX -= Math.cos(pitchR) * Math.sin(yawR) * 48.0;
        posY -= Math.sin(pitchR) * 48.0;
        posZ += Math.cos(pitchR) * Math.cos(yawR) * 48.0;
        this.setLocation(posX, posY, posZ);
    }
    
    static {
        WitherBossBar.highestId = 2147473647;
    }
}
