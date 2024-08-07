// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.Iterator;
import java.util.ArrayList;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import de.gerrygames.viarewind.replacement.EntityReplacement;

public class EndermiteReplacement implements EntityReplacement
{
    private int entityId;
    private List<Metadata> datawatcher;
    private double locX;
    private double locY;
    private double locZ;
    private float yaw;
    private float pitch;
    private float headYaw;
    private UserConnection user;
    
    public EndermiteReplacement(final int entityId, final UserConnection user) {
        this.datawatcher = new ArrayList<Metadata>();
        this.entityId = entityId;
        this.user = user;
        this.spawn();
    }
    
    @Override
    public void setLocation(final double x, final double y, final double z) {
        this.locX = x;
        this.locY = y;
        this.locZ = z;
        this.updateLocation();
    }
    
    @Override
    public void relMove(final double x, final double y, final double z) {
        this.locX += x;
        this.locY += y;
        this.locZ += z;
        this.updateLocation();
    }
    
    @Override
    public void setYawPitch(final float yaw, final float pitch) {
        if (this.yaw != yaw || this.pitch != pitch) {
            this.yaw = yaw;
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
        this.updateMetadata();
    }
    
    public void updateLocation() {
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
    
    public void updateMetadata() {
        final PacketWrapper metadataPacket = new PacketWrapper(28, (ByteBuf)null, this.user);
        metadataPacket.write(Type.INT, (Object)this.entityId);
        final List<Metadata> metadataList = new ArrayList<Metadata>();
        for (final Metadata metadata : this.datawatcher) {
            metadataList.add(new Metadata(metadata.getId(), metadata.getMetaType(), metadata.getValue()));
        }
        MetadataRewriter.transform(Entity1_10Types.EntityType.SQUID, metadataList);
        metadataPacket.write((Type)Types1_7_6_10.METADATA_LIST, (Object)metadataList);
        PacketUtil.sendPacket(metadataPacket, Protocol1_7_6_10TO1_8.class);
    }
    
    @Override
    public void spawn() {
        final PacketWrapper spawn = new PacketWrapper(15, (ByteBuf)null, this.user);
        spawn.write((Type)Type.VAR_INT, (Object)this.entityId);
        spawn.write(Type.UNSIGNED_BYTE, (Object)60);
        spawn.write(Type.INT, (Object)0);
        spawn.write(Type.INT, (Object)0);
        spawn.write(Type.INT, (Object)0);
        spawn.write(Type.BYTE, (Object)0);
        spawn.write(Type.BYTE, (Object)0);
        spawn.write(Type.BYTE, (Object)0);
        spawn.write((Type)Type.SHORT, (Object)0);
        spawn.write((Type)Type.SHORT, (Object)0);
        spawn.write((Type)Type.SHORT, (Object)0);
        spawn.write((Type)Types1_7_6_10.METADATA_LIST, (Object)new ArrayList());
        PacketUtil.sendPacket(spawn, Protocol1_7_6_10TO1_8.class, true, true);
    }
    
    @Override
    public void despawn() {
        final PacketWrapper despawn = new PacketWrapper(19, (ByteBuf)null, this.user);
        despawn.write((Type)Types1_7_6_10.INT_ARRAY, (Object)new int[] { this.entityId });
        PacketUtil.sendPacket(despawn, Protocol1_7_6_10TO1_8.class, true, true);
    }
    
    @Override
    public int getEntityId() {
        return this.entityId;
    }
}
