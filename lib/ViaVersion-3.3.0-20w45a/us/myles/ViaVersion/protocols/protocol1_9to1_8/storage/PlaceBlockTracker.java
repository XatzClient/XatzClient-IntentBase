// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.data.StoredObject;

public class PlaceBlockTracker extends StoredObject
{
    private long lastPlaceTimestamp;
    private Position lastPlacedPosition;
    
    public PlaceBlockTracker(final UserConnection user) {
        super(user);
        this.lastPlaceTimestamp = 0L;
        this.lastPlacedPosition = null;
    }
    
    public boolean isExpired(final int ms) {
        return System.currentTimeMillis() > this.lastPlaceTimestamp + ms;
    }
    
    public void updateTime() {
        this.lastPlaceTimestamp = System.currentTimeMillis();
    }
    
    public long getLastPlaceTimestamp() {
        return this.lastPlaceTimestamp;
    }
    
    public Position getLastPlacedPosition() {
        return this.lastPlacedPosition;
    }
    
    public void setLastPlacedPosition(final Position lastPlacedPosition) {
        this.lastPlacedPosition = lastPlacedPosition;
    }
}
