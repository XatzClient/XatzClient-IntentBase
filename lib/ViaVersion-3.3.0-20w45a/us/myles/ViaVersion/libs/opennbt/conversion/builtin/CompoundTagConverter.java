// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import java.util.Iterator;
import us.myles.viaversion.libs.opennbt.conversion.ConverterRegistry;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.HashMap;
import java.util.Map;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class CompoundTagConverter implements TagConverter<CompoundTag, Map>
{
    @Override
    public Map convert(final CompoundTag tag) {
        final Map<String, Object> ret = new HashMap<String, Object>();
        final Map<String, Tag> tags = tag.getValue();
        for (final String name : tags.keySet()) {
            final Tag t = tags.get(name);
            ret.put(t.getName(), ConverterRegistry.convertToValue(t));
        }
        return ret;
    }
    
    @Override
    public CompoundTag convert(final String name, final Map value) {
        final Map<String, Tag> tags = new HashMap<String, Tag>();
        for (final Object na : value.keySet()) {
            final String n = (String)na;
            tags.put(n, ConverterRegistry.convertToTag(n, value.get(n)));
        }
        return new CompoundTag(name, tags);
    }
}
