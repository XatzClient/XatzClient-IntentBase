// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class DoubleTagConverter implements TagConverter<DoubleTag, Double>
{
    @Override
    public Double convert(final DoubleTag tag) {
        return tag.getValue();
    }
    
    @Override
    public DoubleTag convert(final String name, final Double value) {
        return new DoubleTag(name, value);
    }
}
