// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.remapper;

import us.myles.ViaVersion.api.PacketWrapper;

@FunctionalInterface
public interface ValueWriter<T>
{
    void write(final PacketWrapper p0, final T p1) throws Exception;
}
