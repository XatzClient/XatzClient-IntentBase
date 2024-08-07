// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement;

import us.myles.ViaVersion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.ArrayList;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import de.gerrygames.viarewind.replacement.EntityReplacement;

public class ShulkerBulletReplacement implements EntityReplacement
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
    
    public ShulkerBulletReplacement(final int entityId, final UserConnection user) {
        this.datawatcher = new ArrayList<Metadata>();
        this.entityId = entityId;
        this.user = user;
        this.spawn();
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
        if (this.yaw != yaw && this.pitch != pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.updateLocation();
        }
    }
    
    @Override
    public void setHeadYaw(final float yaw) {
        this.headYaw = yaw;
    }
    
    @Override
    public void updateMetadata(final List<Metadata> metadataList) {
    }
    
    public void updateLocation() {
        final PacketWrapper teleport = new PacketWrapper(24, (ByteBuf)null, this.user);
        teleport.write((Type)Type.VAR_INT, (Object)this.entityId);
        teleport.write(Type.INT, (Object)(int)(this.locX * 32.0));
        teleport.write(Type.INT, (Object)(int)(this.locY * 32.0));
        teleport.write(Type.INT, (Object)(int)(this.locZ * 32.0));
        teleport.write(Type.BYTE, (Object)(byte)(this.yaw / 360.0f * 256.0f));
        teleport.write(Type.BYTE, (Object)(byte)(this.pitch / 360.0f * 256.0f));
        teleport.write(Type.BOOLEAN, (Object)true);
        final PacketWrapper head = new PacketWrapper(25, (ByteBuf)null, this.user);
        head.write((Type)Type.VAR_INT, (Object)this.entityId);
        head.write(Type.BYTE, (Object)(byte)(this.headYaw / 360.0f * 256.0f));
        PacketUtil.sendPacket(teleport, Protocol1_8TO1_9.class, true, true);
        PacketUtil.sendPacket(head, Protocol1_8TO1_9.class, true, true);
    }
    
    @Override
    public void spawn() {
        final PacketWrapper spawn = new PacketWrapper(14, (ByteBuf)null, this.user);
        spawn.write((Type)Type.VAR_INT, (Object)this.entityId);
        spawn.write(Type.BYTE, (Object)66);
        spawn.write(Type.INT, (Object)0);
        spawn.write(Type.INT, (Object)0);
        spawn.write(Type.INT, (Object)0);
        spawn.write(Type.BYTE, (Object)0);
        spawn.write(Type.BYTE, (Object)0);
        spawn.write(Type.INT, (Object)0);
        PacketUtil.sendPacket(spawn, Protocol1_8TO1_9.class, true, true);
    }
    
    @Override
    public void despawn() {
        final PacketWrapper despawn = new PacketWrapper(19, (ByteBuf)null, this.user);
        despawn.write(Type.VAR_INT_ARRAY_PRIMITIVE, (Object)new int[] { this.entityId });
        PacketUtil.sendPacket(despawn, Protocol1_8TO1_9.class, true, true);
    }
    
    @Override
    public int getEntityId() {
        return this.entityId;
    }
}
