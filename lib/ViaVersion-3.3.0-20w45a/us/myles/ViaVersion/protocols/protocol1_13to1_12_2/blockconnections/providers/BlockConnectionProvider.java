// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.blockconnections.providers;

import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.platform.providers.Provider;

public class BlockConnectionProvider implements Provider
{
    public int getBlockData(final UserConnection connection, final int x, final int y, final int z) {
        final int oldId = this.getWorldBlockData(connection, x, y, z);
        return Protocol1_13To1_12_2.MAPPINGS.getBlockMappings().getNewId(oldId);
    }
    
    public int getWorldBlockData(final UserConnection connection, final int x, final int y, final int z) {
        return -1;
    }
    
    public void storeBlock(final UserConnection connection, final int x, final int y, final int z, final int blockState) {
    }
    
    public void removeBlock(final UserConnection connection, final int x, final int y, final int z) {
    }
    
    public void clearStorage(final UserConnection connection) {
    }
    
    public void unloadChunk(final UserConnection connection, final int x, final int z) {
    }
    
    public boolean storesBlocks() {
        return false;
    }
}
