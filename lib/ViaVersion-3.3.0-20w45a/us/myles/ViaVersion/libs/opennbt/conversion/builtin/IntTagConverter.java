// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class IntTagConverter implements TagConverter<IntTag, Integer>
{
    @Override
    public Integer convert(final IntTag tag) {
        return tag.getValue();
    }
    
    @Override
    public IntTag convert(final String name, final Integer value) {
        return new IntTag(name, value);
    }
}
