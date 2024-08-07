// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.storage;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BulkChunkTranslatorProvider;
import java.util.List;
import com.google.common.collect.Sets;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.Set;
import us.myles.ViaVersion.api.data.StoredObject;

public class ClientChunks extends StoredObject
{
    private final Set<Long> loadedChunks;
    private final Set<Long> bulkChunks;
    
    public ClientChunks(final UserConnection user) {
        super(user);
        this.loadedChunks = (Set<Long>)Sets.newConcurrentHashSet();
        this.bulkChunks = (Set<Long>)Sets.newConcurrentHashSet();
    }
    
    public static long toLong(final int msw, final int lsw) {
        return ((long)msw << 32) + lsw + 2147483648L;
    }
    
    public List<Object> transformMapChunkBulk(final Object packet) throws Exception {
        return Via.getManager().getProviders().get(BulkChunkTranslatorProvider.class).transformMapChunkBulk(packet, this);
    }
    
    public Set<Long> getLoadedChunks() {
        return this.loadedChunks;
    }
    
    public Set<Long> getBulkChunks() {
        return this.bulkChunks;
    }
}
