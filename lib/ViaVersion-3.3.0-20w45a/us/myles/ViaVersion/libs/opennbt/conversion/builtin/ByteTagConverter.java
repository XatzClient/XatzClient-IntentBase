// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class ByteTagConverter implements TagConverter<ByteTag, Byte>
{
    @Override
    public Byte convert(final ByteTag tag) {
        return tag.getValue();
    }
    
    @Override
    public ByteTag convert(final String name, final Byte value) {
        return new ByteTag(name, value);
    }
}
