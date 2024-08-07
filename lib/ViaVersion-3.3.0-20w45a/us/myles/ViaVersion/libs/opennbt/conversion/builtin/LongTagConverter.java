// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.LongTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class LongTagConverter implements TagConverter<LongTag, Long>
{
    @Override
    public Long convert(final LongTag tag) {
        return tag.getValue();
    }
    
    @Override
    public LongTag convert(final String name, final Long value) {
        return new LongTag(name, value);
    }
}
