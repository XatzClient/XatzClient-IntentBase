// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.storage;

import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.Pair;
import java.util.Map;
import us.myles.ViaVersion.api.data.StoredObject;

public class CommandBlockStorage extends StoredObject
{
    private final Map<Pair<Integer, Integer>, Map<Position, CompoundTag>> storedCommandBlocks;
    private boolean permissions;
    
    public CommandBlockStorage(final UserConnection user) {
        super(user);
        this.storedCommandBlocks = new ConcurrentHashMap<Pair<Integer, Integer>, Map<Position, CompoundTag>>();
        this.permissions = false;
    }
    
    public void unloadChunk(final int x, final int z) {
        final Pair<Integer, Integer> chunkPos = new Pair<Integer, Integer>(x, z);
        this.storedCommandBlocks.remove(chunkPos);
    }
    
    public void addOrUpdateBlock(final Position position, final CompoundTag tag) {
        final Pair<Integer, Integer> chunkPos = this.getChunkCoords(position);
        if (!this.storedCommandBlocks.containsKey(chunkPos)) {
            this.storedCommandBlocks.put(chunkPos, new ConcurrentHashMap<Position, CompoundTag>());
        }
        final Map<Position, CompoundTag> blocks = this.storedCommandBlocks.get(chunkPos);
        if (blocks.containsKey(position) && blocks.get(position).equals(tag)) {
            return;
        }
        blocks.put(position, tag);
    }
    
    private Pair<Integer, Integer> getChunkCoords(final Position position) {
        final int chunkX = Math.floorDiv(position.getX(), 16);
        final int chunkZ = Math.floorDiv(position.getZ(), 16);
        return new Pair<Integer, Integer>(chunkX, chunkZ);
    }
    
    public Optional<CompoundTag> getCommandBlock(final Position position) {
        final Pair<Integer, Integer> chunkCoords = this.getChunkCoords(position);
        final Map<Position, CompoundTag> blocks = this.storedCommandBlocks.get(chunkCoords);
        if (blocks == null) {
            return Optional.empty();
        }
        CompoundTag tag = blocks.get(position);
        if (tag == null) {
            return Optional.empty();
        }
        tag = tag.clone();
        tag.put(new ByteTag("powered", (byte)0));
        tag.put(new ByteTag("auto", (byte)0));
        tag.put(new ByteTag("conditionMet", (byte)0));
        return Optional.of(tag);
    }
    
    public void unloadChunks() {
        this.storedCommandBlocks.clear();
    }
    
    public boolean isPermissions() {
        return this.permissions;
    }
    
    public void setPermissions(final boolean permissions) {
        this.permissions = permissions;
    }
}
