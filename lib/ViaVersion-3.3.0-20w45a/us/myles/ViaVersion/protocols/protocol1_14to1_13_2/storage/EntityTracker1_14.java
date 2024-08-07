// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_14to1_13_2.storage;

import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.concurrent.ConcurrentHashMap;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_14Types;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.Map;
import us.myles.ViaVersion.api.storage.EntityTracker;

public class EntityTracker1_14 extends EntityTracker
{
    private final Map<Integer, Byte> insentientData;
    private final Map<Integer, Byte> sleepingAndRiptideData;
    private final Map<Integer, Byte> playerEntityFlags;
    private int latestTradeWindowId;
    private boolean forceSendCenterChunk;
    private int chunkCenterX;
    private int chunkCenterZ;
    
    public EntityTracker1_14(final UserConnection user) {
        super(user, Entity1_14Types.EntityType.PLAYER);
        this.insentientData = new ConcurrentHashMap<Integer, Byte>();
        this.sleepingAndRiptideData = new ConcurrentHashMap<Integer, Byte>();
        this.playerEntityFlags = new ConcurrentHashMap<Integer, Byte>();
        this.forceSendCenterChunk = true;
    }
    
    @Override
    public void removeEntity(final int entityId) {
        super.removeEntity(entityId);
        this.insentientData.remove(entityId);
        this.sleepingAndRiptideData.remove(entityId);
        this.playerEntityFlags.remove(entityId);
    }
    
    public byte getInsentientData(final int entity) {
        final Byte val = this.insentientData.get(entity);
        return (byte)((val == null) ? 0 : ((byte)val));
    }
    
    public void setInsentientData(final int entity, final byte value) {
        this.insentientData.put(entity, value);
    }
    
    private static byte zeroIfNull(final Byte val) {
        if (val == null) {
            return 0;
        }
        return val;
    }
    
    public boolean isSleeping(final int player) {
        return (zeroIfNull(this.sleepingAndRiptideData.get(player)) & 0x1) != 0x0;
    }
    
    public void setSleeping(final int player, final boolean value) {
        final byte newValue = (byte)((zeroIfNull(this.sleepingAndRiptideData.get(player)) & 0xFFFFFFFE) | (value ? 1 : 0));
        if (newValue == 0) {
            this.sleepingAndRiptideData.remove(player);
        }
        else {
            this.sleepingAndRiptideData.put(player, newValue);
        }
    }
    
    public boolean isRiptide(final int player) {
        return (zeroIfNull(this.sleepingAndRiptideData.get(player)) & 0x2) != 0x0;
    }
    
    public void setRiptide(final int player, final boolean value) {
        final byte newValue = (byte)((zeroIfNull(this.sleepingAndRiptideData.get(player)) & 0xFFFFFFFD) | (value ? 2 : 0));
        if (newValue == 0) {
            this.sleepingAndRiptideData.remove(player);
        }
        else {
            this.sleepingAndRiptideData.put(player, newValue);
        }
    }
    
    @Override
    public void onExternalJoinGame(final int playerEntityId) {
        super.onExternalJoinGame(playerEntityId);
        final PacketWrapper setViewDistance = new PacketWrapper(65, null, this.getUser());
        setViewDistance.write(Type.VAR_INT, 64);
        try {
            setViewDistance.send(Protocol1_14To1_13_2.class, true, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public byte getEntityFlags(final int player) {
        return zeroIfNull(this.playerEntityFlags.get(player));
    }
    
    public void setEntityFlags(final int player, final byte data) {
        this.playerEntityFlags.put(player, data);
    }
    
    public int getLatestTradeWindowId() {
        return this.latestTradeWindowId;
    }
    
    public void setLatestTradeWindowId(final int latestTradeWindowId) {
        this.latestTradeWindowId = latestTradeWindowId;
    }
    
    public boolean isForceSendCenterChunk() {
        return this.forceSendCenterChunk;
    }
    
    public void setForceSendCenterChunk(final boolean forceSendCenterChunk) {
        this.forceSendCenterChunk = forceSendCenterChunk;
    }
    
    public int getChunkCenterX() {
        return this.chunkCenterX;
    }
    
    public void setChunkCenterX(final int chunkCenterX) {
        this.chunkCenterX = chunkCenterX;
    }
    
    public int getChunkCenterZ() {
        return this.chunkCenterZ;
    }
    
    public void setChunkCenterZ(final int chunkCenterZ) {
        this.chunkCenterZ = chunkCenterZ;
    }
}
