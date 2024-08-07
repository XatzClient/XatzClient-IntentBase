// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.FloatTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class FloatTagConverter implements TagConverter<FloatTag, Float>
{
    @Override
    public Float convert(final FloatTag tag) {
        return tag.getValue();
    }
    
    @Override
    public FloatTag convert(final String name, final Float value) {
        return new FloatTag(name, value);
    }
}
