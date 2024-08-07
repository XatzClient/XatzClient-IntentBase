// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements;

import de.gerrygames.viarewind.utils.math.Vector3d;
import de.gerrygames.viarewind.utils.math.AABB;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.MetaType1_7_6_10;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_8;
import java.util.Iterator;
import java.util.ArrayList;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import de.gerrygames.viarewind.replacement.EntityReplacement;

public class ArmorStandReplacement implements EntityReplacement
{
    private int entityId;
    private List<Metadata> datawatcher;
    private int[] entityIds;
    private double locX;
    private double locY;
    private double locZ;
    private State currentState;
    private boolean invisible;
    private boolean nameTagVisible;
    private String name;
    private UserConnection user;
    private float yaw;
    private float pitch;
    private float headYaw;
    private boolean small;
    private boolean marker;
    private static int ENTITY_ID;
    
    public ArmorStandReplacement(final int entityId, final UserConnection user) {
        this.datawatcher = new ArrayList<Metadata>();
        this.entityIds = null;
        this.currentState = null;
        this.invisible = false;
        this.nameTagVisible = false;
        this.name = null;
        this.small = false;
        this.marker = false;
        this.entityId = entityId;
        this.user = user;
    }
    
    @Override
    public void setLocation(final double x, final double y, final double z) {
        if (x != this.locX || y != this.locY || z != this.locZ) {
            this.locX = x;
            this.locY = y;
            this.locZ = z;
            this.updateLocation();
        }
    }
    
    @Override
    public void relMove(final double x, final double y, final double z) {
        if (x == 0.0 && y == 0.0 && z == 0.0) {
            return;
        }
        this.locX += x;
        this.locY += y;
        this.locZ += z;
        this.updateLocation();
    }
    
    @Override
    public void setYawPitch(final float yaw, final float pitch) {
        if ((this.yaw != yaw && this.pitch != pitch) || this.headYaw != yaw) {
            this.yaw = yaw;
            this.headYaw = yaw;
            this.pitch = pitch;
            this.updateLocation();
        }
    }
    
    @Override
    public void setHeadYaw(final float yaw) {
        if (this.headYaw != yaw) {
            this.headYaw = yaw;
            this.updateLocation();
        }
    }
    
    @Override
    public void updateMetadata(final List<Metadata> metadataList) {
        for (final Metadata metadata : metadataList) {
            this.datawatcher.removeIf(m -> m.getId() == metadata.getId());
            this.datawatcher.add(metadata);
        }
        this.updateState();
    }
    
    public void updateState() {
        byte flags = 0;
        byte armorStandFlags = 0;
        for (final Metadata metadata : this.datawatcher) {
            if (metadata.getId() == 0 && metadata.getMetaType() == MetaType1_8.Byte) {
                flags = (byte)metadata.getValue();
            }
            else if (metadata.getId() == 2 && metadata.getMetaType() == MetaType1_8.String) {
                this.name = (String)metadata.getValue();
                if (this.name == null || !this.name.equals("")) {
                    continue;
                }
                this.name = null;
            }
            else if (metadata.getId() == 10 && metadata.getMetaType() == MetaType1_8.Byte) {
                armorStandFlags = (byte)metadata.getValue();
            }
            else {
                if (metadata.getId() != 3 || metadata.getMetaType() != MetaType1_8.Byte) {
                    continue;
                }
                this.nameTagVisible = ((byte)metadata.getId() != 0);
            }
        }
        this.invisible = ((flags & 0x20) != 0x0);
        this.small = ((armorStandFlags & 0x1) != 0x0);
        this.marker = ((armorStandFlags & 0x10) != 0x0);
        final State prevState = this.currentState;
        if (this.invisible && this.name != null) {
            this.currentState = State.HOLOGRAM;
        }
        else {
            this.currentState = State.ZOMBIE;
        }
        if (this.currentState != prevState) {
            this.despawn();
            this.spawn();
        }
        else {
            this.updateMetadata();
            this.updateLocation();
        }
    }
    
    public void updateLocation() {
        if (this.entityIds == null) {
            return;
        }
        if (this.currentState == State.ZOMBIE) {
            final PacketWrapper teleport = new PacketWrapper(24, (ByteBuf)null, this.user);
            teleport.write(Type.INT, (Object)this.entityId);
            teleport.write(Type.INT, (Object)(int)(this.locX * 32.0));
            teleport.write(Type.INT, (Object)(int)(this.locY * 32.0));
            teleport.write(Type.INT, (Object)(int)(this.locZ * 32.0));
            teleport.write(Type.BYTE, (Object)(byte)(this.yaw / 360.0f * 256.0f));
            teleport.write(Type.BYTE, (Object)(byte)(this.pitch / 360.0f * 256.0f));
            final PacketWrapper head = new PacketWrapper(25, (ByteBuf)null, this.user);
            head.write(Type.INT, (Object)this.entityId);
            head.write(Type.BYTE, (Object)(byte)(this.headYaw / 360.0f * 256.0f));
            PacketUtil.sendPacket(teleport, Protocol1_7_6_10TO1_8.class, true, true);
            PacketUtil.sendPacket(head, Protocol1_7_6_10TO1_8.class, true, true);
        }
        else if (this.currentState == State.HOLOGRAM) {
            final PacketWrapper detach = new PacketWrapper(27, (ByteBuf)null, this.user);
            detach.write(Type.INT, (Object)this.entityIds[1]);
            detach.write(Type.INT, (Object)(-1));
            detach.write(Type.BOOLEAN, (Object)false);
            final PacketWrapper teleportSkull = new PacketWrapper(24, (ByteBuf)null, this.user);
            teleportSkull.write(Type.INT, (Object)this.entityIds[0]);
            teleportSkull.write(Type.INT, (Object)(int)(this.locX * 32.0));
            teleportSkull.write(Type.INT, (Object)(int)((this.locY + (this.marker ? 54.85 : (this.small ? 56.0 : 57.0))) * 32.0));
            teleportSkull.write(Type.INT, (Object)(int)(this.locZ * 32.0));
            teleportSkull.write(Type.BYTE, (Object)0);
            teleportSkull.write(Type.BYTE, (Object)0);
            final PacketWrapper teleportHorse = new PacketWrapper(24, (ByteBuf)null, this.user);
            teleportHorse.write(Type.INT, (Object)this.entityIds[1]);
            teleportHorse.write(Type.INT, (Object)(int)(this.locX * 32.0));
            teleportHorse.write(Type.INT, (Object)(int)((this.locY + 56.75) * 32.0));
            teleportHorse.write(Type.INT, (Object)(int)(this.locZ * 32.0));
            teleportHorse.write(Type.BYTE, (Object)0);
            teleportHorse.write(Type.BYTE, (Object)0);
            final PacketWrapper attach = new PacketWrapper(27, (ByteBuf)null, this.user);
            attach.write(Type.INT, (Object)this.entityIds[1]);
            attach.write(Type.INT, (Object)this.entityIds[0]);
            attach.write(Type.BOOLEAN, (Object)false);
            PacketUtil.sendPacket(detach, Protocol1_7_6_10TO1_8.class, true, true);
            PacketUtil.sendPacket(teleportSkull, Protocol1_7_6_10TO1_8.class, true, true);
            PacketUtil.sendPacket(teleportHorse, Protocol1_7_6_10TO1_8.class, true, true);
            PacketUtil.sendPacket(attach, Protocol1_7_6_10TO1_8.class, true, true);
        }
    }
    
    public void updateMetadata() {
        if (this.entityIds == null) {
            return;
        }
        final PacketWrapper metadataPacket = new PacketWrapper(28, (ByteBuf)null, this.user);
        if (this.currentState == State.ZOMBIE) {
            metadataPacket.write(Type.INT, (Object)this.entityIds[0]);
            final List<Metadata> metadataList = new ArrayList<Metadata>();
            for (final Metadata metadata : this.datawatcher) {
                if (metadata.getId() >= 0) {
                    if (metadata.getId() > 9) {
                        continue;
                    }
                    metadataList.add(new Metadata(metadata.getId(), metadata.getMetaType(), metadata.getValue()));
                }
            }
            if (this.small) {
                metadataList.add(new Metadata(12, (MetaType)MetaType1_8.Byte, (Object)1));
            }
            MetadataRewriter.transform(Entity1_10Types.EntityType.ZOMBIE, metadataList);
            metadataPacket.write((Type)Types1_7_6_10.METADATA_LIST, (Object)metadataList);
        }
        else {
            if (this.currentState != State.HOLOGRAM) {
                return;
            }
            metadataPacket.write(Type.INT, (Object)this.entityIds[1]);
            final List<Metadata> metadataList = new ArrayList<Metadata>();
            metadataList.add(new Metadata(12, (MetaType)MetaType1_7_6_10.Int, (Object)(-1700000)));
            metadataList.add(new Metadata(10, (MetaType)MetaType1_7_6_10.String, (Object)this.name));
            metadataList.add(new Metadata(11, (MetaType)MetaType1_7_6_10.Byte, (Object)1));
            metadataPacket.write((Type)Types1_7_6_10.METADATA_LIST, (Object)metadataList);
        }
        PacketUtil.sendPacket(metadataPacket, Protocol1_7_6_10TO1_8.class);
    }
    
    @Override
    public void spawn() {
        if (this.entityIds != null) {
            this.despawn();
        }
        if (this.currentState == State.ZOMBIE) {
            final PacketWrapper spawn = new PacketWrapper(15, (ByteBuf)null, this.user);
            spawn.write((Type)Type.VAR_INT, (Object)this.entityId);
            spawn.write(Type.UNSIGNED_BYTE, (Object)54);
            spawn.write(Type.INT, (Object)(int)(this.locX * 32.0));
            spawn.write(Type.INT, (Object)(int)(this.locY * 32.0));
            spawn.write(Type.INT, (Object)(int)(this.locZ * 32.0));
            spawn.write(Type.BYTE, (Object)0);
            spawn.write(Type.BYTE, (Object)0);
            spawn.write(Type.BYTE, (Object)0);
            spawn.write((Type)Type.SHORT, (Object)0);
            spawn.write((Type)Type.SHORT, (Object)0);
            spawn.write((Type)Type.SHORT, (Object)0);
            spawn.write((Type)Types1_7_6_10.METADATA_LIST, (Object)new ArrayList());
            PacketUtil.sendPacket(spawn, Protocol1_7_6_10TO1_8.class, true, true);
            this.entityIds = new int[] { this.entityId };
        }
        else if (this.currentState == State.HOLOGRAM) {
            final int[] entityIds = { this.entityId, ArmorStandReplacement.ENTITY_ID-- };
            final PacketWrapper spawnSkull = new PacketWrapper(14, (ByteBuf)null, this.user);
            spawnSkull.write((Type)Type.VAR_INT, (Object)entityIds[0]);
            spawnSkull.write(Type.BYTE, (Object)66);
            spawnSkull.write(Type.INT, (Object)(int)(this.locX * 32.0));
            spawnSkull.write(Type.INT, (Object)(int)(this.locY * 32.0));
            spawnSkull.write(Type.INT, (Object)(int)(this.locZ * 32.0));
            spawnSkull.write(Type.BYTE, (Object)0);
            spawnSkull.write(Type.BYTE, (Object)0);
            spawnSkull.write(Type.INT, (Object)0);
            final PacketWrapper spawnHorse = new PacketWrapper(15, (ByteBuf)null, this.user);
            spawnHorse.write((Type)Type.VAR_INT, (Object)entityIds[1]);
            spawnHorse.write(Type.UNSIGNED_BYTE, (Object)100);
            spawnHorse.write(Type.INT, (Object)(int)(this.locX * 32.0));
            spawnHorse.write(Type.INT, (Object)(int)(this.locY * 32.0));
            spawnHorse.write(Type.INT, (Object)(int)(this.locZ * 32.0));
            spawnHorse.write(Type.BYTE, (Object)0);
            spawnHorse.write(Type.BYTE, (Object)0);
            spawnHorse.write(Type.BYTE, (Object)0);
            spawnHorse.write((Type)Type.SHORT, (Object)0);
            spawnHorse.write((Type)Type.SHORT, (Object)0);
            spawnHorse.write((Type)Type.SHORT, (Object)0);
            spawnHorse.write((Type)Types1_7_6_10.METADATA_LIST, (Object)new ArrayList());
            PacketUtil.sendPacket(spawnSkull, Protocol1_7_6_10TO1_8.class, true, true);
            PacketUtil.sendPacket(spawnHorse, Protocol1_7_6_10TO1_8.class, true, true);
            this.entityIds = entityIds;
        }
        this.updateMetadata();
        this.updateLocation();
    }
    
    public AABB getBoundingBox() {
        final double w = this.small ? 0.25 : 0.5;
        final double h = this.small ? 0.9875 : 1.975;
        final Vector3d min = new Vector3d(this.locX - w / 2.0, this.locY, this.locZ - w / 2.0);
        final Vector3d max = new Vector3d(this.locX + w / 2.0, this.locY + h, this.locZ + w / 2.0);
        return new AABB(min, max);
    }
    
    @Override
    public void despawn() {
        if (this.entityIds == null) {
            return;
        }
        final PacketWrapper despawn = new PacketWrapper(19, (ByteBuf)null, this.user);
        despawn.write(Type.BYTE, (Object)(byte)this.entityIds.length);
        for (final int id : this.entityIds) {
            despawn.write(Type.INT, (Object)id);
        }
        this.entityIds = null;
        PacketUtil.sendPacket(despawn, Protocol1_7_6_10TO1_8.class, true, true);
    }
    
    @Override
    public int getEntityId() {
        return this.entityId;
    }
    
    static {
        ArmorStandReplacement.ENTITY_ID = 2147467647;
    }
    
    private enum State
    {
        HOLOGRAM, 
        ZOMBIE;
    }
}
