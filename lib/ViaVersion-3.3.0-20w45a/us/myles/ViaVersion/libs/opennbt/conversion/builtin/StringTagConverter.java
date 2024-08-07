// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class StringTagConverter implements TagConverter<StringTag, String>
{
    @Override
    public String convert(final StringTag tag) {
        return tag.getValue();
    }
    
    @Override
    public StringTag convert(final String name, final String value) {
        return new StringTag(name, value);
    }
}
