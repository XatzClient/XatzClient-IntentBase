// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin.custom;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.custom.FloatArrayTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class FloatArrayTagConverter implements TagConverter<FloatArrayTag, float[]>
{
    @Override
    public float[] convert(final FloatArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public FloatArrayTag convert(final String name, final float[] value) {
        return new FloatArrayTag(name, value);
    }
}
