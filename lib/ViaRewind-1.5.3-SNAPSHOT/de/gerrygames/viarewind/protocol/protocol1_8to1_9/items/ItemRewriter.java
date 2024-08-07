// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.items;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
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
import java.util.Map;

public class ItemRewriter
{
    private static Map<String, Integer> ENTTIY_NAME_TO_ID;
    private static Map<Integer, String> ENTTIY_ID_TO_NAME;
    private static Map<String, Integer> POTION_NAME_TO_ID;
    private static Map<Integer, String> POTION_ID_TO_NAME;
    private static Map<String, String> POTION_NAME_INDEX;
    
    public static Item toClient(final Item item) {
        if (item == null) {
            return null;
        }
        CompoundTag tag = item.getTag();
        if (tag == null) {
            item.setTag(tag = new CompoundTag(""));
        }
        final CompoundTag viaVersionTag = new CompoundTag("ViaRewind1_8to1_9");
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
                String s;
                if (id == 70) {
                    s = "§r§7Mending ";
                }
                else {
                    if (id != 9) {
                        continue;
                    }
                    s = "§r§7Frost Walker ";
                }
                enchTag.remove(ench);
                s += Enchantments.ENCHANTMENTS.getOrDefault(lvl, "enchantment.level." + lvl);
                lore.add((Tag)new StringTag("", s));
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
        if (item.getData() != 0 && tag.contains("Unbreakable")) {
            final ByteTag unbreakable = (ByteTag)tag.get("Unbreakable");
            if (unbreakable.getValue() != 0) {
                viaVersionTag.put((Tag)new ByteTag("Unbreakable", (byte)unbreakable.getValue()));
                tag.remove("Unbreakable");
                if (display == null) {
                    tag.put((Tag)(display = new CompoundTag("display")));
                    viaVersionTag.put((Tag)new ByteTag("noDisplay"));
                }
                ListTag loreTag2 = (ListTag)display.get("Lore");
                if (loreTag2 == null) {
                    display.put((Tag)(loreTag2 = new ListTag("Lore", (Class)StringTag.class)));
                }
                loreTag2.add((Tag)new StringTag("", "§9Unbreakable"));
            }
        }
        if (tag.contains("AttributeModifiers")) {
            viaVersionTag.put(tag.get("AttributeModifiers").clone());
        }
        if (item.getIdentifier() == 383 && item.getData() == 0) {
            int data = 0;
            if (tag.contains("EntityTag")) {
                final CompoundTag entityTag = (CompoundTag)tag.get("EntityTag");
                if (entityTag.contains("id")) {
                    final StringTag id2 = (StringTag)entityTag.get("id");
                    if (ItemRewriter.ENTTIY_NAME_TO_ID.containsKey(id2.getValue())) {
                        data = ItemRewriter.ENTTIY_NAME_TO_ID.get(id2.getValue());
                    }
                    else if (display == null) {
                        tag.put((Tag)(display = new CompoundTag("display")));
                        viaVersionTag.put((Tag)new ByteTag("noDisplay"));
                        display.put((Tag)new StringTag("Name", "§rSpawn " + id2.getValue()));
                    }
                }
            }
            item.setData((short)data);
        }
        ReplacementRegistry1_8to1_9.replace(item);
        if (item.getIdentifier() == 373 || item.getIdentifier() == 438 || item.getIdentifier() == 441) {
            int data = 0;
            if (tag.contains("Potion")) {
                final StringTag potion = (StringTag)tag.get("Potion");
                String potionName = potion.getValue().replace("minecraft:", "");
                if (ItemRewriter.POTION_NAME_TO_ID.containsKey(potionName)) {
                    data = ItemRewriter.POTION_NAME_TO_ID.get(potionName);
                }
                if (item.getIdentifier() == 438) {
                    potionName += "_splash";
                }
                else if (item.getIdentifier() == 441) {
                    potionName += "_lingering";
                }
                if ((display == null || !display.contains("Name")) && ItemRewriter.POTION_NAME_INDEX.containsKey(potionName)) {
                    if (display == null) {
                        tag.put((Tag)(display = new CompoundTag("display")));
                        viaVersionTag.put((Tag)new ByteTag("noDisplay"));
                    }
                    display.put((Tag)new StringTag("Name", (String)ItemRewriter.POTION_NAME_INDEX.get(potionName)));
                }
            }
            if (item.getIdentifier() == 438 || item.getIdentifier() == 441) {
                item.setIdentifier(373);
                data += 8192;
            }
            item.setData((short)data);
        }
        if (tag.contains("AttributeModifiers")) {
            final ListTag attributes = (ListTag)tag.get("AttributeModifiers");
            for (int i = 0; i < attributes.size(); ++i) {
                final CompoundTag attribute = (CompoundTag)attributes.get(i);
                final String name = (String)attribute.get("AttributeName").getValue();
                if (!Protocol1_8TO1_9.VALID_ATTRIBUTES.contains((Object)attribute)) {
                    attributes.remove((Tag)attribute);
                    --i;
                }
            }
        }
        if (viaVersionTag.size() == 2 && (short)viaVersionTag.get("id").getValue() == item.getIdentifier() && (short)viaVersionTag.get("data").getValue() == item.getData()) {
            item.getTag().remove("ViaRewind1_8to1_9");
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
        CompoundTag tag = item.getTag();
        if (item.getIdentifier() == 383 && item.getData() != 0) {
            if (tag == null) {
                item.setTag(tag = new CompoundTag(""));
            }
            if (!tag.contains("EntityTag") && ItemRewriter.ENTTIY_ID_TO_NAME.containsKey((int)item.getData())) {
                final CompoundTag entityTag = new CompoundTag("EntityTag");
                entityTag.put((Tag)new StringTag("id", (String)ItemRewriter.ENTTIY_ID_TO_NAME.get((int)item.getData())));
                tag.put((Tag)entityTag);
            }
            item.setData((short)0);
        }
        if (item.getIdentifier() == 373 && (tag == null || !tag.contains("Potion"))) {
            if (tag == null) {
                item.setTag(tag = new CompoundTag(""));
            }
            if (item.getData() >= 16384) {
                item.setIdentifier(438);
                item.setData((short)(item.getData() - 8192));
            }
            final String name = (item.getData() == 8192) ? "water" : us.myles.ViaVersion.protocols.protocol1_9to1_8.ItemRewriter.potionNameFromDamage(item.getData());
            tag.put((Tag)new StringTag("Potion", "minecraft:" + name));
            item.setData((short)0);
        }
        if (tag == null || !item.getTag().contains("ViaRewind1_8to1_9")) {
            return item;
        }
        final CompoundTag viaVersionTag = (CompoundTag)tag.remove("ViaRewind1_8to1_9");
        item.setIdentifier((int)(short)viaVersionTag.get("id").getValue());
        item.setData((short)viaVersionTag.get("data").getValue());
        if (viaVersionTag.contains("noDisplay")) {
            tag.remove("display");
        }
        if (viaVersionTag.contains("Unbreakable")) {
            tag.put(viaVersionTag.get("Unbreakable").clone());
        }
        if (viaVersionTag.contains("displayName")) {
            CompoundTag display = (CompoundTag)tag.get("display");
            if (display == null) {
                tag.put((Tag)(display = new CompoundTag("display")));
            }
            final StringTag name2 = (StringTag)display.get("Name");
            if (name2 == null) {
                display.put((Tag)new StringTag("Name", (String)viaVersionTag.get("displayName").getValue()));
            }
            else {
                name2.setValue((String)viaVersionTag.get("displayName").getValue());
            }
        }
        else if (tag.contains("display")) {
            ((CompoundTag)tag.get("display")).remove("Name");
        }
        if (viaVersionTag.contains("lore")) {
            CompoundTag display = (CompoundTag)tag.get("display");
            if (display == null) {
                tag.put((Tag)(display = new CompoundTag("display")));
            }
            final ListTag lore = (ListTag)display.get("Lore");
            if (lore == null) {
                display.put((Tag)new ListTag("Lore", (List)viaVersionTag.get("lore").getValue()));
            }
            else {
                lore.setValue((List)viaVersionTag.get("lore").getValue());
            }
        }
        else if (tag.contains("display")) {
            ((CompoundTag)tag.get("display")).remove("Lore");
        }
        tag.remove("AttributeModifiers");
        if (viaVersionTag.contains("AttributeModifiers")) {
            tag.put(viaVersionTag.get("AttributeModifiers"));
        }
        return item;
    }
    
    static {
        ItemRewriter.POTION_NAME_INDEX = new HashMap<String, String>();
        for (final Field field : ItemRewriter.class.getDeclaredFields()) {
            try {
                final Field other = us.myles.ViaVersion.protocols.protocol1_9to1_8.ItemRewriter.class.getDeclaredField(field.getName());
                other.setAccessible(true);
                field.setAccessible(true);
                field.set(null, other.get(null));
            }
            catch (Exception ex) {}
        }
        ItemRewriter.POTION_NAME_TO_ID.put("luck", 8203);
        ItemRewriter.POTION_NAME_INDEX.put("water", "§rWater Bottle");
        ItemRewriter.POTION_NAME_INDEX.put("mundane", "§rMundane Potion");
        ItemRewriter.POTION_NAME_INDEX.put("thick", "§rThick Potion");
        ItemRewriter.POTION_NAME_INDEX.put("awkward", "§rAwkward Potion");
        ItemRewriter.POTION_NAME_INDEX.put("water_splash", "§rSplash Water Bottle");
        ItemRewriter.POTION_NAME_INDEX.put("mundane_splash", "§rMundane Splash Potion");
        ItemRewriter.POTION_NAME_INDEX.put("thick_splash", "§rThick Splash Potion");
        ItemRewriter.POTION_NAME_INDEX.put("awkward_splash", "§rAwkward Splash Potion");
        ItemRewriter.POTION_NAME_INDEX.put("water_lingering", "§rLingering Water Bottle");
        ItemRewriter.POTION_NAME_INDEX.put("mundane_lingering", "§rMundane Lingering Potion");
        ItemRewriter.POTION_NAME_INDEX.put("thick_lingering", "§rThick Lingering Potion");
        ItemRewriter.POTION_NAME_INDEX.put("awkward_lingering", "§rAwkward Lingering Potion");
        ItemRewriter.POTION_NAME_INDEX.put("night_vision_lingering", "§rLingering Potion of Night Vision");
        ItemRewriter.POTION_NAME_INDEX.put("long_night_vision_lingering", "§rLingering Potion of Night Vision");
        ItemRewriter.POTION_NAME_INDEX.put("invisibility_lingering", "§rLingering Potion of Invisibility");
        ItemRewriter.POTION_NAME_INDEX.put("long_invisibility_lingering", "§rLingering Potion of Invisibility");
        ItemRewriter.POTION_NAME_INDEX.put("leaping_lingering", "§rLingering Potion of Leaping");
        ItemRewriter.POTION_NAME_INDEX.put("long_leaping_lingering", "§rLingering Potion of Leaping");
        ItemRewriter.POTION_NAME_INDEX.put("strong_leaping_lingering", "§rLingering Potion of Leaping");
        ItemRewriter.POTION_NAME_INDEX.put("fire_resistance_lingering", "§rLingering Potion of Fire Resistance");
        ItemRewriter.POTION_NAME_INDEX.put("long_fire_resistance_lingering", "§rLingering Potion of Fire Resistance");
        ItemRewriter.POTION_NAME_INDEX.put("swiftness_lingering", "§rLingering Potion of Swiftness");
        ItemRewriter.POTION_NAME_INDEX.put("long_swiftness_lingering", "§rLingering Potion of Swiftness");
        ItemRewriter.POTION_NAME_INDEX.put("strong_swiftness_lingering", "§rLingering Potion of Swiftness");
        ItemRewriter.POTION_NAME_INDEX.put("slowness_lingering", "§rLingering Potion of Slowness");
        ItemRewriter.POTION_NAME_INDEX.put("long_slowness_lingering", "§rLingering Potion of Slowness");
        ItemRewriter.POTION_NAME_INDEX.put("water_breathing_lingering", "§rLingering Potion of Water Breathing");
        ItemRewriter.POTION_NAME_INDEX.put("long_water_breathing_lingering", "§rLingering Potion of Water Breathing");
        ItemRewriter.POTION_NAME_INDEX.put("healing_lingering", "§rLingering Potion of Healing");
        ItemRewriter.POTION_NAME_INDEX.put("strong_healing_lingering", "§rLingering Potion of Healing");
        ItemRewriter.POTION_NAME_INDEX.put("harming_lingering", "§rLingering Potion of Harming");
        ItemRewriter.POTION_NAME_INDEX.put("strong_harming_lingering", "§rLingering Potion of Harming");
        ItemRewriter.POTION_NAME_INDEX.put("poison_lingering", "§rLingering Potion of Poisen");
        ItemRewriter.POTION_NAME_INDEX.put("long_poison_lingering", "§rLingering Potion of Poisen");
        ItemRewriter.POTION_NAME_INDEX.put("strong_poison_lingering", "§rLingering Potion of Poisen");
        ItemRewriter.POTION_NAME_INDEX.put("regeneration_lingering", "§rLingering Potion of Regeneration");
        ItemRewriter.POTION_NAME_INDEX.put("long_regeneration_lingering", "§rLingering Potion of Regeneration");
        ItemRewriter.POTION_NAME_INDEX.put("strong_regeneration_lingering", "§rLingering Potion of Regeneration");
        ItemRewriter.POTION_NAME_INDEX.put("strength_lingering", "§rLingering Potion of Strength");
        ItemRewriter.POTION_NAME_INDEX.put("long_strength_lingering", "§rLingering Potion of Strength");
        ItemRewriter.POTION_NAME_INDEX.put("strong_strength_lingering", "§rLingering Potion of Strength");
        ItemRewriter.POTION_NAME_INDEX.put("weakness_lingering", "§rLingering Potion of Weakness");
        ItemRewriter.POTION_NAME_INDEX.put("long_weakness_lingering", "§rLingering Potion of Weakness");
        ItemRewriter.POTION_NAME_INDEX.put("luck_lingering", "§rLingering Potion of Luck");
        ItemRewriter.POTION_NAME_INDEX.put("luck", "§rPotion of Luck");
        ItemRewriter.POTION_NAME_INDEX.put("luck_splash", "§rSplash Potion of Luck");
    }
}
