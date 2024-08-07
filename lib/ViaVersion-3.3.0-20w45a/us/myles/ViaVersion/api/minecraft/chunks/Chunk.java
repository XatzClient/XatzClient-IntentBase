// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.minecraft.chunks;

import java.util.List;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public interface Chunk
{
    int getX();
    
    int getZ();
    
    boolean isBiomeData();
    
    boolean isFullChunk();
    
    @Deprecated
    default boolean isGroundUp() {
        return this.isFullChunk();
    }
    
    boolean isIgnoreOldLightData();
    
    void setIgnoreOldLightData(final boolean p0);
    
    int getBitmask();
    
    ChunkSection[] getSections();
    
    int[] getBiomeData();
    
    void setBiomeData(final int[] p0);
    
    CompoundTag getHeightMap();
    
    void setHeightMap(final CompoundTag p0);
    
    List<CompoundTag> getBlockEntities();
}
