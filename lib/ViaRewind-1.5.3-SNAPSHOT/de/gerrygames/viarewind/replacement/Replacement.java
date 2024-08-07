// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.replacement;

import de.gerrygames.viarewind.storage.BlockState;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.item.Item;

public class Replacement
{
    private int id;
    private int data;
    private String name;
    private String resetName;
    private String bracketName;
    
    public Replacement(final int id) {
        this(id, -1);
    }
    
    public Replacement(final int id, final int data) {
        this(id, data, null);
    }
    
    public Replacement(final int id, final String name) {
        this(id, -1, name);
    }
    
    public Replacement(final int id, final int data, final String name) {
        this.id = id;
        this.data = data;
        this.name = name;
        if (name != null) {
            this.resetName = "�r" + name;
            this.bracketName = " �r�7(" + name + "�r�7)";
        }
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getData() {
        return this.data;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Item replace(final Item item) {
        item.setIdentifier(this.id);
        if (this.data != -1) {
            item.setData((short)this.data);
        }
        if (this.name != null) {
            final CompoundTag compoundTag = (item.getTag() == null) ? new CompoundTag("") : item.getTag();
            if (!compoundTag.contains("display")) {
                compoundTag.put((Tag)new CompoundTag("display"));
            }
            final CompoundTag display = (CompoundTag)compoundTag.get("display");
            if (display.contains("Name")) {
                final StringTag name = (StringTag)display.get("Name");
                if (!name.getValue().equals(this.resetName) && !name.getValue().endsWith(this.bracketName)) {
                    name.setValue(name.getValue() + this.bracketName);
                }
            }
            else {
                display.put((Tag)new StringTag("Name", this.resetName));
            }
            item.setTag(compoundTag);
        }
        return item;
    }
    
    public BlockState replace(final BlockState block) {
        return new BlockState(this.id, (this.data == -1) ? block.getData() : this.data);
    }
}
