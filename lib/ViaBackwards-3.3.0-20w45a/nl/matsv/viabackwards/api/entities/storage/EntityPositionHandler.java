// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.entities.storage;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.function.Supplier;
import nl.matsv.viabackwards.api.rewriters.EntityRewriterBase;

public class EntityPositionHandler
{
    public static final double RELATIVE_MOVE_FACTOR = 4096.0;
    private final EntityRewriterBase<?> entityRewriter;
    private final Class<? extends EntityPositionStorage> storageClass;
    private final Supplier<? extends EntityPositionStorage> storageSupplier;
    private boolean warnedForMissingEntity;
    
    public EntityPositionHandler(final EntityRewriterBase<?> entityRewriter, final Class<? extends EntityPositionStorage> storageClass, final Supplier<? extends EntityPositionStorage> storageSupplier) {
        this.entityRewriter = entityRewriter;
        this.storageClass = storageClass;
        this.storageSupplier = storageSupplier;
    }
    
    public void cacheEntityPosition(final PacketWrapper wrapper, final boolean create, final boolean relative) throws Exception {
        this.cacheEntityPosition(wrapper, (double)wrapper.get(Type.DOUBLE, 0), (double)wrapper.get(Type.DOUBLE, 1), (double)wrapper.get(Type.DOUBLE, 2), create, relative);
    }
    
    public void cacheEntityPosition(final PacketWrapper wrapper, final double x, final double y, final double z, final boolean create, final boolean relative) throws Exception {
        final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
        final EntityTracker.StoredEntity storedEntity = this.entityRewriter.getEntityTracker(wrapper.user()).getEntity(entityId);
        if (storedEntity == null) {
            if (Via.getManager().isDebug()) {
                ViaBackwards.getPlatform().getLogger().warning("Stored entity with id " + entityId + " missing at position: " + x + " - " + y + " - " + z + " in " + this.storageClass.getSimpleName());
                if (entityId == -1 && x == 0.0 && y == 0.0 && z == 0.0) {
                    ViaBackwards.getPlatform().getLogger().warning("DO NOT REPORT THIS TO VIA, THIS IS A PLUGIN ISSUE");
                }
                else if (!this.warnedForMissingEntity) {
                    this.warnedForMissingEntity = true;
                    ViaBackwards.getPlatform().getLogger().warning("This is very likely caused by a plugin sending a teleport packet for an entity outside of the player's range.");
                }
            }
            return;
        }
        final EntityPositionStorage positionStorage = create ? this.storageSupplier.get() : ((EntityPositionStorage)storedEntity.get(this.storageClass));
        if (positionStorage == null) {
            ViaBackwards.getPlatform().getLogger().warning("Stored entity with id " + entityId + " missing " + this.storageClass.getSimpleName());
            return;
        }
        positionStorage.setCoordinates(x, y, z, relative);
        storedEntity.put(positionStorage);
    }
    
    public EntityPositionStorage getStorage(final UserConnection user, final int entityId) {
        final EntityTracker.StoredEntity storedEntity = ((EntityTracker)user.get((Class)EntityTracker.class)).get(this.entityRewriter.getProtocol()).getEntity(entityId);
        final EntityPositionStorage entityStorage;
        if (storedEntity == null || (entityStorage = storedEntity.get(EntityPositionStorage.class)) == null) {
            ViaBackwards.getPlatform().getLogger().warning("Untracked entity with id " + entityId + " in " + this.storageClass.getSimpleName());
            return null;
        }
        return entityStorage;
    }
    
    public static void writeFacingAngles(final PacketWrapper wrapper, final double x, final double y, final double z, final double targetX, final double targetY, final double targetZ) {
        final double dX = targetX - x;
        final double dY = targetY - y;
        final double dZ = targetZ - z;
        final double r = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        double yaw = -Math.atan2(dX, dZ) / 3.141592653589793 * 180.0;
        if (yaw < 0.0) {
            yaw += 360.0;
        }
        final double pitch = -Math.asin(dY / r) / 3.141592653589793 * 180.0;
        wrapper.write(Type.BYTE, (Object)(byte)(yaw * 256.0 / 360.0));
        wrapper.write(Type.BYTE, (Object)(byte)(pitch * 256.0 / 360.0));
    }
    
    public static void writeFacingDegrees(final PacketWrapper wrapper, final double x, final double y, final double z, final double targetX, final double targetY, final double targetZ) {
        final double dX = targetX - x;
        final double dY = targetY - y;
        final double dZ = targetZ - z;
        final double r = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        double yaw = -Math.atan2(dX, dZ) / 3.141592653589793 * 180.0;
        if (yaw < 0.0) {
            yaw += 360.0;
        }
        final double pitch = -Math.asin(dY / r) / 3.141592653589793 * 180.0;
        wrapper.write((Type)Type.FLOAT, (Object)(float)yaw);
        wrapper.write((Type)Type.FLOAT, (Object)(float)pitch);
    }
}
