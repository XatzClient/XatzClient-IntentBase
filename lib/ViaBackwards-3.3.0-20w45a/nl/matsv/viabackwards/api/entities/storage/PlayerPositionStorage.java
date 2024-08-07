// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.entities.storage;

import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public abstract class PlayerPositionStorage extends StoredObject
{
    private double x;
    private double y;
    private double z;
    
    protected PlayerPositionStorage(final UserConnection user) {
        super(user);
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public void setCoordinates(final PacketWrapper wrapper, final boolean relative) throws Exception {
        this.setCoordinates((double)wrapper.get(Type.DOUBLE, 0), (double)wrapper.get(Type.DOUBLE, 1), (double)wrapper.get(Type.DOUBLE, 2), relative);
    }
    
    public void setCoordinates(final double x, final double y, final double z, final boolean relative) {
        if (relative) {
            this.x += x;
            this.y += y;
            this.z += z;
        }
        else {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
