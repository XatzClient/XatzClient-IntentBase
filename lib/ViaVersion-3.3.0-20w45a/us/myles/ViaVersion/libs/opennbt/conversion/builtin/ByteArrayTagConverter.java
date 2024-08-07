// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion.builtin;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteArrayTag;
import us.myles.viaversion.libs.opennbt.conversion.TagConverter;

public class ByteArrayTagConverter implements TagConverter<ByteArrayTag, byte[]>
{
    @Override
    public byte[] convert(final ByteArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public ByteArrayTag convert(final String name, final byte[] value) {
        return new ByteArrayTag(name, value);
    }
}
