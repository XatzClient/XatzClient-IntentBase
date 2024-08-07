// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class ShortTagConverter implements TagConverter<ShortTag, Short>
{
    @Override
    public Short convert(final ShortTag tag) {
        return tag.getValue();
    }
    
    @Override
    public ShortTag convert(final String name, final Short value) {
        return new ShortTag(name, value);
    }
}
