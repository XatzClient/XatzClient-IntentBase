// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_17to1_16_4.storage;

import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.Map;
import us.myles.ViaVersion.api.data.StoredObject;

public class BiomeStorage extends StoredObject
{
    private final Map<Long, int[]> chunkBiomes;
    private String world;
    
    public BiomeStorage(final UserConnection user) {
        super(user);
        this.chunkBiomes = new HashMap<Long, int[]>();
    }
    
    @Nullable
    public String getWorld() {
        return this.world;
    }
    
    public void setWorld(final String world) {
        this.world = world;
    }
    
    @Nullable
    public int[] getBiomes(final int x, final int z) {
        return this.chunkBiomes.get(this.getChunkSectionIndex(x, z));
    }
    
    public void setBiomes(final int x, final int z, final int[] biomes) {
        this.chunkBiomes.put(this.getChunkSectionIndex(x, z), biomes);
    }
    
    public void clearBiomes(final int x, final int z) {
        this.chunkBiomes.remove(this.getChunkSectionIndex(x, z));
    }
    
    public void clearBiomes() {
        this.chunkBiomes.clear();
    }
    
    private long getChunkSectionIndex(final int x, final int z) {
        return ((long)x & 0x3FFFFFFL) << 38 | ((long)z & 0x3FFFFFFL);
    }
}
