// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag;

import us.myles.viaversion.libs.opennbt.tag.builtin.custom.ShortArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.custom.FloatArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.custom.DoubleArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.FloatTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.LongTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import java.util.HashMap;
import java.lang.reflect.Constructor;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Map;

public class TagRegistry
{
    private static final Map<Integer, Class<? extends Tag>> idToTag;
    private static final Map<Class<? extends Tag>, Integer> tagToId;
    
    public static void register(final int id, final Class<? extends Tag> tag) throws TagRegisterException {
        if (TagRegistry.idToTag.containsKey(id)) {
            throw new TagRegisterException("Tag ID \"" + id + "\" is already in use.");
        }
        if (TagRegistry.tagToId.containsKey(tag)) {
            throw new TagRegisterException("Tag \"" + tag.getSimpleName() + "\" is already registered.");
        }
        TagRegistry.idToTag.put(id, tag);
        TagRegistry.tagToId.put(tag, id);
    }
    
    public static void unregister(final int id) {
        TagRegistry.tagToId.remove(getClassFor(id));
        TagRegistry.idToTag.remove(id);
    }
    
    public static Class<? extends Tag> getClassFor(final int id) {
        if (!TagRegistry.idToTag.containsKey(id)) {
            return null;
        }
        return TagRegistry.idToTag.get(id);
    }
    
    public static int getIdFor(final Class<? extends Tag> clazz) {
        if (!TagRegistry.tagToId.containsKey(clazz)) {
            return -1;
        }
        return TagRegistry.tagToId.get(clazz);
    }
    
    public static Tag createInstance(final int id, final String tagName) throws TagCreateException {
        final Class<? extends Tag> clazz = TagRegistry.idToTag.get(id);
        if (clazz == null) {
            throw new TagCreateException("Could not find tag with ID \"" + id + "\".");
        }
        try {
            final Constructor<? extends Tag> constructor = clazz.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            return (Tag)constructor.newInstance(tagName);
        }
        catch (Exception e) {
            throw new TagCreateException("Failed to create instance of tag \"" + clazz.getSimpleName() + "\".", e);
        }
    }
    
    static {
        idToTag = new HashMap<Integer, Class<? extends Tag>>();
        tagToId = new HashMap<Class<? extends Tag>, Integer>();
        register(1, ByteTag.class);
        register(2, ShortTag.class);
        register(3, IntTag.class);
        register(4, LongTag.class);
        register(5, FloatTag.class);
        register(6, DoubleTag.class);
        register(7, ByteArrayTag.class);
        register(8, StringTag.class);
        register(9, ListTag.class);
        register(10, CompoundTag.class);
        register(11, IntArrayTag.class);
        register(12, LongArrayTag.class);
        register(60, DoubleArrayTag.class);
        register(61, FloatArrayTag.class);
        register(65, ShortArrayTag.class);
    }
}
