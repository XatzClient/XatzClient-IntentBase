// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class LongArrayTagConverter implements TagConverter<LongArrayTag, long[]>
{
    @Override
    public long[] convert(final LongArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public LongArrayTag convert(final String name, final long[] value) {
        return new LongArrayTag(name, value);
    }
}
