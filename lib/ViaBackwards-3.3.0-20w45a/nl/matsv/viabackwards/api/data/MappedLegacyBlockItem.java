// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.data;

import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import org.jetbrains.annotations.Nullable;
import nl.matsv.viabackwards.utils.Block;

public class MappedLegacyBlockItem
{
    private final int id;
    private final short data;
    private final String name;
    private final Block block;
    private BlockEntityHandler blockEntityHandler;
    
    public MappedLegacyBlockItem(final int id, final short data, @Nullable final String name, final boolean block) {
        this.id = id;
        this.data = data;
        this.name = ((name != null) ? (ChatColor.RESET + name) : null);
        this.block = (block ? new Block(id, data) : null);
    }
    
    public int getId() {
        return this.id;
    }
    
    public short getData() {
        return this.data;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isBlock() {
        return this.block != null;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public boolean hasBlockEntityHandler() {
        return this.blockEntityHandler != null;
    }
    
    @Nullable
    public BlockEntityHandler getBlockEntityHandler() {
        return this.blockEntityHandler;
    }
    
    public void setBlockEntityHandler(@Nullable final BlockEntityHandler blockEntityHandler) {
        this.blockEntityHandler = blockEntityHandler;
    }
    
    @FunctionalInterface
    public interface BlockEntityHandler
    {
        CompoundTag handleOrNewCompoundTag(final int p0, final CompoundTag p1);
    }
}
