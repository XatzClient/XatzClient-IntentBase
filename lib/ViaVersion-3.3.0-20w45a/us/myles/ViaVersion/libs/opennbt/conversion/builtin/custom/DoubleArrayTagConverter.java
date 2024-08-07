// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin.custom;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.custom.DoubleArrayTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class DoubleArrayTagConverter implements TagConverter<DoubleArrayTag, double[]>
{
    @Override
    public double[] convert(final DoubleArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public DoubleArrayTag convert(final String name, final double[] value) {
        return new DoubleArrayTag(name, value);
    }
}
