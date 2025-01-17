// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage;

import us.myles.viaversion.libs.fastutil.ints.IntOpenHashSet;
import java.util.concurrent.ConcurrentHashMap;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.Position;
import java.util.Map;
import us.myles.viaversion.libs.fastutil.ints.IntSet;
import us.myles.ViaVersion.api.data.StoredObject;

public class BackwardsBlockStorage extends StoredObject
{
    private static final IntSet WHITELIST;
    private final Map<Position, Integer> blocks;
    
    public BackwardsBlockStorage(final UserConnection user) {
        super(user);
        this.blocks = new ConcurrentHashMap<Position, Integer>();
    }
    
    public void checkAndStore(final Position position, final int block) {
        if (!BackwardsBlockStorage.WHITELIST.contains(block)) {
            this.blocks.remove(position);
            return;
        }
        this.blocks.put(position, block);
    }
    
    public boolean isWelcome(final int block) {
        return BackwardsBlockStorage.WHITELIST.contains(block);
    }
    
    public Integer get(final Position position) {
        return this.blocks.get(position);
    }
    
    public int remove(final Position position) {
        return this.blocks.remove(position);
    }
    
    public void clear() {
        this.blocks.clear();
    }
    
    public Map<Position, Integer> getBlocks() {
        return this.blocks;
    }
    
    static {
        WHITELIST = (IntSet)new IntOpenHashSet(779);
        for (int i = 5265; i <= 5286; ++i) {
            BackwardsBlockStorage.WHITELIST.add(i);
        }
        for (int i = 0; i < 256; ++i) {
            BackwardsBlockStorage.WHITELIST.add(748 + i);
        }
        for (int i = 6854; i <= 7173; ++i) {
            BackwardsBlockStorage.WHITELIST.add(i);
        }
        BackwardsBlockStorage.WHITELIST.add(1647);
        for (int i = 5447; i <= 5566; ++i) {
            BackwardsBlockStorage.WHITELIST.add(i);
        }
        for (int i = 1028; i <= 1039; ++i) {
            BackwardsBlockStorage.WHITELIST.add(i);
        }
        for (int i = 1047; i <= 1082; ++i) {
            BackwardsBlockStorage.WHITELIST.add(i);
        }
        for (int i = 1099; i <= 1110; ++i) {
            BackwardsBlockStorage.WHITELIST.add(i);
        }
    }
}
