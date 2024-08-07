// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.rewriters;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.ArrayList;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public class LegacyEnchantmentRewriter
{
    private final Map<Short, String> enchantmentMappings;
    private final String nbtTagName;
    private Set<Short> hideLevelForEnchants;
    
    public LegacyEnchantmentRewriter(final String nbtTagName) {
        this.enchantmentMappings = new HashMap<Short, String>();
        this.nbtTagName = nbtTagName;
    }
    
    public void registerEnchantment(final int id, final String replacementLore) {
        this.enchantmentMappings.put((short)id, replacementLore);
    }
    
    public void rewriteEnchantmentsToClient(final CompoundTag tag, final boolean storedEnchant) {
        final String key = storedEnchant ? "StoredEnchantments" : "ench";
        final ListTag enchantments = (ListTag)tag.get(key);
        final ListTag remappedEnchantments = new ListTag(this.nbtTagName + "|" + key, (Class)CompoundTag.class);
        final List<Tag> lore = new ArrayList<Tag>();
        for (final Tag enchantmentEntry : enchantments.clone()) {
            final Short newId = (Short)((CompoundTag)enchantmentEntry).get("id").getValue();
            final String enchantmentName = this.enchantmentMappings.get(newId);
            if (enchantmentName != null) {
                enchantments.remove(enchantmentEntry);
                final Number level = (Number)((CompoundTag)enchantmentEntry).get("lvl").getValue();
                if (this.hideLevelForEnchants != null && this.hideLevelForEnchants.contains(newId)) {
                    lore.add((Tag)new StringTag("", enchantmentName));
                }
                else {
                    lore.add((Tag)new StringTag("", enchantmentName + " " + EnchantmentRewriter.getRomanNumber(level.shortValue())));
                }
                remappedEnchantments.add(enchantmentEntry);
            }
        }
        if (!lore.isEmpty()) {
            if (!storedEnchant && enchantments.size() == 0) {
                final CompoundTag dummyEnchantment = new CompoundTag("");
                dummyEnchantment.put((Tag)new ShortTag("id", (short)0));
                dummyEnchantment.put((Tag)new ShortTag("lvl", (short)0));
                enchantments.add((Tag)dummyEnchantment);
                tag.put((Tag)new ByteTag(this.nbtTagName + "|dummyEnchant"));
                IntTag hideFlags = (IntTag)tag.get("HideFlags");
                if (hideFlags == null) {
                    hideFlags = new IntTag("HideFlags");
                }
                else {
                    tag.put((Tag)new IntTag(this.nbtTagName + "|oldHideFlags", (int)hideFlags.getValue()));
                }
                final int flags = hideFlags.getValue() | 0x1;
                hideFlags.setValue(flags);
                tag.put((Tag)hideFlags);
            }
            tag.put((Tag)remappedEnchantments);
            CompoundTag display = (CompoundTag)tag.get("display");
            if (display == null) {
                tag.put((Tag)(display = new CompoundTag("display")));
            }
            ListTag loreTag = (ListTag)display.get("Lore");
            if (loreTag == null) {
                display.put((Tag)(loreTag = new ListTag("Lore", (Class)StringTag.class)));
            }
            lore.addAll(loreTag.getValue());
            loreTag.setValue((List)lore);
        }
    }
    
    public void rewriteEnchantmentsToServer(final CompoundTag tag, final boolean storedEnchant) {
        final String key = storedEnchant ? "StoredEnchantments" : "ench";
        final ListTag remappedEnchantments = (ListTag)tag.get(this.nbtTagName + "|" + key);
        ListTag enchantments = (ListTag)tag.get(key);
        if (enchantments == null) {
            enchantments = new ListTag(key, (Class)CompoundTag.class);
        }
        if (!storedEnchant && tag.remove(this.nbtTagName + "|dummyEnchant") != null) {
            for (final Tag enchantment : enchantments.clone()) {
                final Short id = (Short)((CompoundTag)enchantment).get("id").getValue();
                final Short level = (Short)((CompoundTag)enchantment).get("lvl").getValue();
                if (id == 0 && level == 0) {
                    enchantments.remove(enchantment);
                }
            }
            final IntTag hideFlags = (IntTag)tag.remove(this.nbtTagName + "|oldHideFlags");
            if (hideFlags != null) {
                tag.put((Tag)new IntTag("HideFlags", (int)hideFlags.getValue()));
            }
            else {
                tag.remove("HideFlags");
            }
        }
        final CompoundTag display = (CompoundTag)tag.get("display");
        final ListTag lore = (display != null) ? ((ListTag)display.get("Lore")) : null;
        for (final Tag enchantment2 : remappedEnchantments.clone()) {
            enchantments.add(enchantment2);
            if (lore != null && lore.size() != 0) {
                lore.remove(lore.get(0));
            }
        }
        if (lore != null && lore.size() == 0) {
            display.remove("Lore");
            if (display.isEmpty()) {
                tag.remove("display");
            }
        }
        tag.put((Tag)enchantments);
        tag.remove(remappedEnchantments.getName());
    }
    
    public void setHideLevelForEnchants(final int... enchants) {
        this.hideLevelForEnchants = new HashSet<Short>();
        for (final int enchant : enchants) {
            this.hideLevelForEnchants.add((short)enchant);
        }
    }
}
