// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin.custom;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.custom.ShortArrayTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class ShortArrayTagConverter implements TagConverter<ShortArrayTag, short[]>
{
    @Override
    public short[] convert(final ShortArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public ShortArrayTag convert(final String name, final short[] value) {
        return new ShortArrayTag(name, value);
    }
}
