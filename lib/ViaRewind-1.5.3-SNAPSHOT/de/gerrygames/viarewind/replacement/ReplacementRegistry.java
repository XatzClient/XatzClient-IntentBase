// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.replacement;

import de.gerrygames.viarewind.storage.BlockState;
import us.myles.ViaVersion.api.minecraft.item.Item;
import java.util.HashMap;

public class ReplacementRegistry
{
    private HashMap<Integer, Replacement> itemReplacements;
    private HashMap<Integer, Replacement> blockReplacements;
    
    public ReplacementRegistry() {
        this.itemReplacements = new HashMap<Integer, Replacement>();
        this.blockReplacements = new HashMap<Integer, Replacement>();
    }
    
    public void registerItem(final int id, final Replacement replacement) {
        this.registerItem(id, -1, replacement);
    }
    
    public void registerBlock(final int id, final Replacement replacement) {
        this.registerBlock(id, -1, replacement);
    }
    
    public void registerItemBlock(final int id, final Replacement replacement) {
        this.registerItemBlock(id, -1, replacement);
    }
    
    public void registerItem(final int id, final int data, final Replacement replacement) {
        this.itemReplacements.put(combine(id, data), replacement);
    }
    
    public void registerBlock(final int id, final int data, final Replacement replacement) {
        this.blockReplacements.put(combine(id, data), replacement);
    }
    
    public void registerItemBlock(final int id, final int data, final Replacement replacement) {
        this.registerItem(id, data, replacement);
        this.registerBlock(id, data, replacement);
    }
    
    public Item replace(final Item item) {
        Replacement replacement = this.itemReplacements.get(combine(item.getIdentifier(), item.getData()));
        if (replacement == null) {
            replacement = this.itemReplacements.get(combine(item.getIdentifier(), -1));
        }
        return (replacement == null) ? item : replacement.replace(item);
    }
    
    public BlockState replace(final BlockState block) {
        Replacement replacement = this.blockReplacements.get(combine(block.getId(), block.getData()));
        if (replacement == null) {
            replacement = this.blockReplacements.get(combine(block.getId(), -1));
        }
        return (replacement == null) ? block : replacement.replace(block);
    }
    
    private static int combine(final int id, final int data) {
        return id << 16 | (data & 0xFFFF);
    }
}
