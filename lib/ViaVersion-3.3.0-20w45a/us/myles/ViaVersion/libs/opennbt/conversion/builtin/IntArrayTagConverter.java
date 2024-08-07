// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class IntArrayTagConverter implements TagConverter<IntArrayTag, int[]>
{
    @Override
    public int[] convert(final IntArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public IntArrayTag convert(final String name, final int[] value) {
        return new IntArrayTag(name, value);
    }
}
