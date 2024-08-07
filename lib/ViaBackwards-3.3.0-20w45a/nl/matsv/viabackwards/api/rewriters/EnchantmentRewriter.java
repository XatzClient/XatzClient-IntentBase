// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.rewriters;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.ArrayList;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.ViaVersion.api.minecraft.item.Item;
import java.util.HashMap;
import java.util.Map;

public class EnchantmentRewriter
{
    private final Map<String, String> enchantmentMappings;
    private final String nbtTagName;
    private final boolean jsonFormat;
    
    public EnchantmentRewriter(final String nbtTagName, final boolean jsonFormat) {
        this.enchantmentMappings = new HashMap<String, String>();
        this.nbtTagName = nbtTagName;
        this.jsonFormat = jsonFormat;
    }
    
    public EnchantmentRewriter(final String nbtTagName) {
        this(nbtTagName, true);
    }
    
    public void registerEnchantment(final String key, final String replacementLore) {
        this.enchantmentMappings.put(key, replacementLore);
    }
    
    public void handleToClient(final Item item) {
        final CompoundTag tag = item.getTag();
        if (tag == null) {
            return;
        }
        if (tag.get("Enchantments") instanceof ListTag) {
            this.rewriteEnchantmentsToClient(tag, false);
        }
        if (tag.get("StoredEnchantments") instanceof ListTag) {
            this.rewriteEnchantmentsToClient(tag, true);
        }
    }
    
    public void handleToServer(final Item item) {
        final CompoundTag tag = item.getTag();
        if (tag == null) {
            return;
        }
        if (tag.contains(this.nbtTagName + "|Enchantments")) {
            this.rewriteEnchantmentsToServer(tag, false);
        }
        if (tag.contains(this.nbtTagName + "|StoredEnchantments")) {
            this.rewriteEnchantmentsToServer(tag, true);
        }
    }
    
    public void rewriteEnchantmentsToClient(final CompoundTag tag, final boolean storedEnchant) {
        final String key = storedEnchant ? "StoredEnchantments" : "Enchantments";
        final ListTag enchantments = (ListTag)tag.get(key);
        final ListTag remappedEnchantments = new ListTag(this.nbtTagName + "|" + key, (Class)CompoundTag.class);
        final List<Tag> lore = new ArrayList<Tag>();
        for (final Tag enchantmentEntry : enchantments.clone()) {
            final String newId = (String)((CompoundTag)enchantmentEntry).get("id").getValue();
            final String enchantmentName = this.enchantmentMappings.get(newId);
            if (enchantmentName != null) {
                enchantments.remove(enchantmentEntry);
                final Number level = (Number)((CompoundTag)enchantmentEntry).get("lvl").getValue();
                String loreValue = enchantmentName + " " + getRomanNumber(level.intValue());
                if (this.jsonFormat) {
                    loreValue = ChatRewriter.legacyTextToJson(loreValue).toString();
                }
                lore.add((Tag)new StringTag("", loreValue));
                remappedEnchantments.add(enchantmentEntry);
            }
        }
        if (!lore.isEmpty()) {
            if (!storedEnchant && enchantments.size() == 0) {
                final CompoundTag dummyEnchantment = new CompoundTag("");
                dummyEnchantment.put((Tag)new StringTag("id", ""));
                dummyEnchantment.put((Tag)new ShortTag("lvl", (short)0));
                enchantments.add((Tag)dummyEnchantment);
                tag.put((Tag)new ByteTag(this.nbtTagName + "|dummyEnchant"));
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
        final String key = storedEnchant ? "StoredEnchantments" : "Enchantments";
        final ListTag remappedEnchantments = (ListTag)tag.get(this.nbtTagName + "|" + key);
        ListTag enchantments = (ListTag)tag.get(key);
        if (enchantments == null) {
            enchantments = new ListTag(key, (Class)CompoundTag.class);
        }
        if (!storedEnchant && tag.remove(this.nbtTagName + "|dummyEnchant") != null) {
            for (final Tag enchantment : enchantments.clone()) {
                final String id = (String)((CompoundTag)enchantment).get("id").getValue();
                if (id.isEmpty()) {
                    enchantments.remove(enchantment);
                }
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
    
    public static String getRomanNumber(final int number) {
        switch (number) {
            case 1: {
                return "I";
            }
            case 2: {
                return "II";
            }
            case 3: {
                return "III";
            }
            case 4: {
                return "IV";
            }
            case 5: {
                return "V";
            }
            case 6: {
                return "VI";
            }
            case 7: {
                return "VII";
            }
            case 8: {
                return "VIII";
            }
            case 9: {
                return "IX";
            }
            case 10: {
                return "X";
            }
            default: {
                return Integer.toString(number);
            }
        }
    }
}
