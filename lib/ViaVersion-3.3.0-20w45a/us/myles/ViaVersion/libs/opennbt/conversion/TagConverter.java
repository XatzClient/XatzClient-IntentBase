// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.conversion;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public interface TagConverter<T extends Tag, V>
{
    V convert(final T p0);
    
    T convert(final String p0, final V p1);
}
