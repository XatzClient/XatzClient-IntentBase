// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items;

import java.util.Iterator;
import de.gerrygames.viarewind.utils.ChatUtil;
import java.util.List;
import java.util.Collection;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import de.gerrygames.viarewind.utils.Enchantments;
import java.util.ArrayList;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.item.Item;

public class ItemRewriter
{
    public static Item toClient(final Item item) {
        if (item == null) {
            return null;
        }
        CompoundTag tag = item.getTag();
        if (tag == null) {
            item.setTag(tag = new CompoundTag(""));
        }
        final CompoundTag viaVersionTag = new CompoundTag("ViaRewind1_7_6_10to1_8");
        tag.put((Tag)viaVersionTag);
        viaVersionTag.put((Tag)new ShortTag("id", (short)item.getIdentifier()));
        viaVersionTag.put((Tag)new ShortTag("data", item.getData()));
        CompoundTag display = (CompoundTag)tag.get("display");
        if (display != null && display.contains("Name")) {
            viaVersionTag.put((Tag)new StringTag("displayName", (String)display.get("Name").getValue()));
        }
        if (display != null && display.contains("Lore")) {
            viaVersionTag.put((Tag)new ListTag("lore", ((ListTag)display.get("Lore")).getValue()));
        }
        if (tag.contains("ench") || tag.contains("StoredEnchantments")) {
            final ListTag enchTag = (ListTag)(tag.contains("ench") ? tag.get("ench") : ((ListTag)tag.get("StoredEnchantments")));
            final List<Tag> enchants = (List<Tag>)enchTag.getValue();
            final List<Tag> lore = new ArrayList<Tag>();
            for (final Tag ench : enchants) {
                final short id = (short)((CompoundTag)ench).get("id").getValue();
                final short lvl = (short)((CompoundTag)ench).get("lvl").getValue();
                if (id == 8) {
                    String s = "§r§7Depth Strider ";
                    enchTag.remove(ench);
                    s += Enchantments.ENCHANTMENTS.getOrDefault(lvl, "enchantment.level." + lvl);
                    lore.add((Tag)new StringTag("", s));
                }
            }
            if (!lore.isEmpty()) {
                if (display == null) {
                    tag.put((Tag)(display = new CompoundTag("display")));
                    viaVersionTag.put((Tag)new ByteTag("noDisplay"));
                }
                ListTag loreTag = (ListTag)display.get("Lore");
                if (loreTag == null) {
                    display.put((Tag)(loreTag = new ListTag("Lore", (Class)StringTag.class)));
                }
                lore.addAll(loreTag.getValue());
                loreTag.setValue((List)lore);
            }
        }
        if (item.getIdentifier() == 387 && tag.contains("pages")) {
            final ListTag pages = (ListTag)tag.get("pages");
            final ListTag oldPages = new ListTag("pages", (Class)StringTag.class);
            viaVersionTag.put((Tag)oldPages);
            for (int i = 0; i < pages.size(); ++i) {
                final StringTag page = (StringTag)pages.get(i);
                String value = page.getValue();
                oldPages.add((Tag)new StringTag(page.getName(), value));
                value = ChatUtil.jsonToLegacy(value);
                page.setValue(value);
            }
        }
        ReplacementRegistry1_7_6_10to1_8.replace(item);
        if (viaVersionTag.size() == 2 && (short)viaVersionTag.get("id").getValue() == item.getIdentifier() && (short)viaVersionTag.get("data").getValue() == item.getData()) {
            item.getTag().remove("ViaRewind1_7_6_10to1_8");
            if (item.getTag().isEmpty()) {
                item.setTag((CompoundTag)null);
            }
        }
        return item;
    }
    
    public static Item toServer(final Item item) {
        if (item == null) {
            return null;
        }
        final CompoundTag tag = item.getTag();
        if (tag == null || !item.getTag().contains("ViaRewind1_7_6_10to1_8")) {
            return item;
        }
        final CompoundTag viaVersionTag = (CompoundTag)tag.remove("ViaRewind1_7_6_10to1_8");
        item.setIdentifier((int)(short)viaVersionTag.get("id").getValue());
        item.setData((short)viaVersionTag.get("data").getValue());
        if (viaVersionTag.contains("noDisplay")) {
            tag.remove("display");
        }
        if (viaVersionTag.contains("displayName")) {
            CompoundTag display = (CompoundTag)tag.get("display");
            if (display == null) {
                tag.put((Tag)(display = new CompoundTag("display")));
            }
            final StringTag name = (StringTag)display.get("Name");
            if (name == null) {
                display.put((Tag)new StringTag("Name", (String)viaVersionTag.get("displayName").getValue()));
            }
            else {
                name.setValue((String)viaVersionTag.get("displayName").getValue());
            }
        }
        else if (tag.contains("display")) {
            ((CompoundTag)tag.get("display")).remove("Name");
        }
        if (item.getIdentifier() == 387) {
            final ListTag oldPages = (ListTag)viaVersionTag.get("pages");
            tag.remove("pages");
            tag.put((Tag)oldPages);
        }
        return item;
    }
}
