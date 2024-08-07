// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.protocol;

import com.google.common.base.Preconditions;

public class VersionRange
{
    private final String baseVersion;
    private final int rangeFrom;
    private final int rangeTo;
    
    public VersionRange(final String baseVersion, final int rangeFrom, final int rangeTo) {
        Preconditions.checkNotNull((Object)baseVersion);
        Preconditions.checkArgument(rangeFrom >= 0);
        Preconditions.checkArgument(rangeTo > rangeFrom);
        this.baseVersion = baseVersion;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
    }
    
    public String getBaseVersion() {
        return this.baseVersion;
    }
    
    public int getRangeFrom() {
        return this.rangeFrom;
    }
    
    public int getRangeTo() {
        return this.rangeTo;
    }
}
