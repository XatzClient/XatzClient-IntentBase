// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import java.util.Iterator;
import us.myles.ViaVersion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.item.Item;
import java.util.UUID;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import java.util.Map;
import us.myles.ViaVersion.api.data.ExternalJoinGameListener;
import us.myles.ViaVersion.api.data.StoredObject;

public class EntityTracker extends StoredObject implements ExternalJoinGameListener
{
    private final Map<Integer, Entity1_10Types.EntityType> clientEntityTypes;
    private final Map<Integer, List<Metadata>> metadataBuffer;
    private final Map<Integer, Integer> vehicles;
    private final Map<Integer, EntityReplacement> entityReplacements;
    private final Map<Integer, UUID> playersByEntityId;
    private final Map<UUID, Integer> playersByUniqueId;
    private final Map<UUID, Item[]> playerEquipment;
    private int gamemode;
    private int playerId;
    private int spectating;
    private int dimension;
    
    public EntityTracker(final UserConnection user) {
        super(user);
        this.clientEntityTypes = new ConcurrentHashMap<Integer, Entity1_10Types.EntityType>();
        this.metadataBuffer = new ConcurrentHashMap<Integer, List<Metadata>>();
        this.vehicles = new ConcurrentHashMap<Integer, Integer>();
        this.entityReplacements = new ConcurrentHashMap<Integer, EntityReplacement>();
        this.playersByEntityId = new HashMap<Integer, UUID>();
        this.playersByUniqueId = new HashMap<UUID, Integer>();
        this.playerEquipment = new HashMap<UUID, Item[]>();
        this.gamemode = 0;
        this.playerId = -1;
        this.spectating = -1;
        this.dimension = 0;
    }
    
    public void removeEntity(final int entityId) {
        this.clientEntityTypes.remove(entityId);
        if (this.entityReplacements.containsKey(entityId)) {
            this.entityReplacements.remove(entityId).despawn();
        }
        if (this.playersByEntityId.containsKey(entityId)) {
            this.playersByUniqueId.remove(this.playersByEntityId.remove(entityId));
        }
    }
    
    public void addPlayer(final Integer entityId, final UUID uuid) {
        this.playersByUniqueId.put(uuid, entityId);
        this.playersByEntityId.put(entityId, uuid);
    }
    
    public UUID getPlayerUUID(final int entityId) {
        return this.playersByEntityId.get(entityId);
    }
    
    public int getPlayerEntityId(final UUID uuid) {
        return this.playersByUniqueId.getOrDefault(uuid, -1);
    }
    
    public Item[] getPlayerEquipment(final UUID uuid) {
        return this.playerEquipment.get(uuid);
    }
    
    public void setPlayerEquipment(final UUID uuid, final Item[] equipment) {
        this.playerEquipment.put(uuid, equipment);
    }
    
    public Map<Integer, Entity1_10Types.EntityType> getClientEntityTypes() {
        return this.clientEntityTypes;
    }
    
    public void addMetadataToBuffer(final int entityID, final List<Metadata> metadataList) {
        if (this.metadataBuffer.containsKey(entityID)) {
            this.metadataBuffer.get(entityID).addAll(metadataList);
        }
        else if (!metadataList.isEmpty()) {
            this.metadataBuffer.put(entityID, metadataList);
        }
    }
    
    public void addEntityReplacement(final EntityReplacement entityReplacement) {
        this.entityReplacements.put(entityReplacement.getEntityId(), entityReplacement);
    }
    
    public EntityReplacement getEntityReplacement(final int entityId) {
        return this.entityReplacements.get(entityId);
    }
    
    public List<Metadata> getBufferedMetadata(final int entityId) {
        return this.metadataBuffer.get(entityId);
    }
    
    public void sendMetadataBuffer(final int entityId) {
        if (!this.metadataBuffer.containsKey(entityId)) {
            return;
        }
        if (this.entityReplacements.containsKey(entityId)) {
            this.entityReplacements.get(entityId).updateMetadata(this.metadataBuffer.remove(entityId));
        }
        else {
            final Entity1_10Types.EntityType type = this.getClientEntityTypes().get(entityId);
            final PacketWrapper wrapper = new PacketWrapper(28, (ByteBuf)null, this.getUser());
            wrapper.write((Type)Type.VAR_INT, (Object)entityId);
            wrapper.write(Types1_8.METADATA_LIST, (Object)this.metadataBuffer.get(entityId));
            MetadataRewriter.transform(type, this.metadataBuffer.get(entityId));
            if (!this.metadataBuffer.get(entityId).isEmpty()) {
                PacketUtil.sendPacket(wrapper, Protocol1_7_6_10TO1_8.class);
            }
            this.metadataBuffer.remove(entityId);
        }
    }
    
    public int getVehicle(final int passengerId) {
        for (final Map.Entry<Integer, Integer> vehicle : this.vehicles.entrySet()) {
            if (vehicle.getValue() == passengerId) {
                return vehicle.getValue();
            }
        }
        return -1;
    }
    
    public int getPassenger(final int vehicleId) {
        return this.vehicles.getOrDefault(vehicleId, -1);
    }
    
    public void setPassenger(final int vehicleId, final int passengerId) {
        if (vehicleId == this.spectating && this.spectating != this.playerId) {
            try {
                final PacketWrapper sneakPacket = new PacketWrapper(11, (ByteBuf)null, this.getUser());
                sneakPacket.write((Type)Type.VAR_INT, (Object)this.playerId);
                sneakPacket.write((Type)Type.VAR_INT, (Object)0);
                sneakPacket.write((Type)Type.VAR_INT, (Object)0);
                final PacketWrapper unsneakPacket = new PacketWrapper(11, (ByteBuf)null, this.getUser());
                unsneakPacket.write((Type)Type.VAR_INT, (Object)this.playerId);
                unsneakPacket.write((Type)Type.VAR_INT, (Object)1);
                unsneakPacket.write((Type)Type.VAR_INT, (Object)0);
                PacketUtil.sendToServer(sneakPacket, Protocol1_7_6_10TO1_8.class, true, true);
                this.setSpectating(this.playerId);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (vehicleId == -1) {
            final int oldVehicleId = this.getVehicle(passengerId);
            this.vehicles.remove(oldVehicleId);
        }
        else if (passengerId == -1) {
            this.vehicles.remove(vehicleId);
        }
        else {
            this.vehicles.put(vehicleId, passengerId);
        }
    }
    
    public int getSpectating() {
        return this.spectating;
    }
    
    public boolean setSpectating(final int spectating) {
        if (spectating != this.playerId && this.getPassenger(spectating) != -1) {
            final PacketWrapper sneakPacket = new PacketWrapper(11, (ByteBuf)null, this.getUser());
            sneakPacket.write((Type)Type.VAR_INT, (Object)this.playerId);
            sneakPacket.write((Type)Type.VAR_INT, (Object)0);
            sneakPacket.write((Type)Type.VAR_INT, (Object)0);
            final PacketWrapper unsneakPacket = new PacketWrapper(11, (ByteBuf)null, this.getUser());
            unsneakPacket.write((Type)Type.VAR_INT, (Object)this.playerId);
            unsneakPacket.write((Type)Type.VAR_INT, (Object)1);
            unsneakPacket.write((Type)Type.VAR_INT, (Object)0);
            PacketUtil.sendToServer(sneakPacket, Protocol1_7_6_10TO1_8.class, true, true);
            this.setSpectating(this.playerId);
            return false;
        }
        if (this.spectating != spectating && this.spectating != this.playerId) {
            final PacketWrapper unmount = new PacketWrapper(27, (ByteBuf)null, this.getUser());
            unmount.write(Type.INT, (Object)this.playerId);
            unmount.write(Type.INT, (Object)(-1));
            unmount.write(Type.BOOLEAN, (Object)false);
            PacketUtil.sendPacket(unmount, Protocol1_7_6_10TO1_8.class);
        }
        if ((this.spectating = spectating) != this.playerId) {
            final PacketWrapper mount = new PacketWrapper(27, (ByteBuf)null, this.getUser());
            mount.write(Type.INT, (Object)this.playerId);
            mount.write(Type.INT, (Object)this.spectating);
            mount.write(Type.BOOLEAN, (Object)false);
            PacketUtil.sendPacket(mount, Protocol1_7_6_10TO1_8.class);
        }
        return true;
    }
    
    public int getGamemode() {
        return this.gamemode;
    }
    
    public void setGamemode(final int gamemode) {
        this.gamemode = gamemode;
    }
    
    public int getPlayerId() {
        return this.playerId;
    }
    
    public void setPlayerId(final int playerId) {
        if (this.playerId != -1) {
            throw new IllegalStateException("playerId was already set!");
        }
        this.spectating = playerId;
        this.playerId = playerId;
    }
    
    public void clearEntities() {
        this.clientEntityTypes.clear();
        this.entityReplacements.clear();
        this.vehicles.clear();
        this.metadataBuffer.clear();
    }
    
    public int getDimension() {
        return this.dimension;
    }
    
    public void setDimension(final int dimension) {
        this.dimension = dimension;
    }
    
    public void onExternalJoinGame(final int playerEntityId) {
        if (this.spectating == this.playerId) {
            this.spectating = playerEntityId;
        }
        this.clientEntityTypes.remove(this.playerId);
        this.playerId = playerEntityId;
        this.clientEntityTypes.put(this.playerId, Entity1_10Types.EntityType.ENTITY_HUMAN);
    }
}
