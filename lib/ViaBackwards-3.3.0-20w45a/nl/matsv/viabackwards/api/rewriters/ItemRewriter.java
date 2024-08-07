// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.rewriters;

import nl.matsv.viabackwards.api.data.MappedItem;
import java.util.Iterator;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public abstract class ItemRewriter<T extends BackwardsProtocol> extends ItemRewriterBase<T>
{
    private final TranslatableRewriter translatableRewriter;
    
    protected ItemRewriter(final T protocol, @Nullable final TranslatableRewriter translatableRewriter) {
        super(protocol, true);
        this.translatableRewriter = translatableRewriter;
    }
    
    @Nullable
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        CompoundTag display = (item.getTag() != null) ? ((CompoundTag)item.getTag().get("display")) : null;
        if (this.translatableRewriter != null && display != null) {
            final StringTag name = (StringTag)display.get("Name");
            if (name != null) {
                final String newValue = this.translatableRewriter.processText(name.getValue()).toString();
                if (!newValue.equals(name.getValue())) {
                    this.saveNameTag(display, name);
                }
                name.setValue(newValue);
            }
            final ListTag lore = (ListTag)display.get("Lore");
            if (lore != null) {
                ListTag original = null;
                boolean changed = false;
                for (final Tag loreEntryTag : lore) {
                    if (!(loreEntryTag instanceof StringTag)) {
                        continue;
                    }
                    final StringTag loreEntry = (StringTag)loreEntryTag;
                    final String newValue2 = this.translatableRewriter.processText(loreEntry.getValue()).toString();
                    if (!changed && !newValue2.equals(loreEntry.getValue())) {
                        changed = true;
                        original = lore.clone();
                    }
                    loreEntry.setValue(newValue2);
                }
                if (changed) {
                    this.saveLoreTag(display, original);
                }
            }
        }
        final MappedItem data = this.protocol.getMappingData().getMappedItem(item.getIdentifier());
        if (data == null) {
            return super.handleItemToClient(item);
        }
        if (item.getTag() == null) {
            item.setTag(new CompoundTag(""));
        }
        item.getTag().put((Tag)new IntTag(this.nbtTagName + "|id", item.getIdentifier()));
        item.setIdentifier(data.getId());
        if (display == null) {
            item.getTag().put((Tag)(display = new CompoundTag("display")));
        }
        if (!display.contains("Name")) {
            display.put((Tag)new StringTag("Name", data.getJsonName()));
            display.put((Tag)new ByteTag(this.nbtTagName + "|customName"));
        }
        return item;
    }
    
    @Nullable
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToServer(item);
        if (item.getTag() != null) {
            final IntTag originalId = (IntTag)item.getTag().remove(this.nbtTagName + "|id");
            if (originalId != null) {
                item.setIdentifier((int)originalId.getValue());
            }
        }
        return item;
    }
}
