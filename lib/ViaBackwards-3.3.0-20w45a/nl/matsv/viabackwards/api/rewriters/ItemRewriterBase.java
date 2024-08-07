// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.rewriters;

import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.viaversion.libs.opennbt.conversion.builtin.CompoundTagConverter;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public abstract class ItemRewriterBase<T extends BackwardsProtocol> extends Rewriter<T>
{
    protected static final CompoundTagConverter CONVERTER;
    protected final String nbtTagName;
    protected final boolean jsonNameFormat;
    
    protected ItemRewriterBase(final T protocol, final boolean jsonNameFormat) {
        super(protocol);
        this.jsonNameFormat = jsonNameFormat;
        this.nbtTagName = "VB|" + protocol.getClass().getSimpleName();
    }
    
    @Nullable
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        if (this.protocol.getMappingData() != null) {
            item.setIdentifier(this.protocol.getMappingData().getNewItemId(item.getIdentifier()));
        }
        return item;
    }
    
    @Nullable
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        if (this.protocol.getMappingData() != null) {
            item.setIdentifier(this.protocol.getMappingData().getOldItemId(item.getIdentifier()));
        }
        this.restoreDisplayTag(item);
        return item;
    }
    
    protected void saveNameTag(final CompoundTag displayTag, final StringTag original) {
        displayTag.put((Tag)new StringTag(this.nbtTagName + "|o" + original.getName(), original.getValue()));
    }
    
    protected void saveLoreTag(final CompoundTag displayTag, final ListTag original) {
        displayTag.put((Tag)new ListTag(this.nbtTagName + "|o" + original.getName(), original.getValue()));
    }
    
    protected void restoreDisplayTag(final Item item) {
        if (item.getTag() == null) {
            return;
        }
        final CompoundTag display = (CompoundTag)item.getTag().get("display");
        if (display != null) {
            if (display.remove(this.nbtTagName + "|customName") != null) {
                display.remove("Name");
            }
            else {
                this.restoreDisplayTag(display, "Name");
            }
            this.restoreDisplayTag(display, "Lore");
        }
    }
    
    protected void restoreDisplayTag(final CompoundTag displayTag, final String tagName) {
        final Tag original = displayTag.remove(this.nbtTagName + "|o" + tagName);
        if (original != null) {
            displayTag.put(original);
        }
    }
    
    static {
        CONVERTER = new CompoundTagConverter();
    }
}
