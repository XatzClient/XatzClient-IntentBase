// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.remapper;

import us.myles.ViaVersion.api.PacketWrapper;

@FunctionalInterface
public interface ValueReader<T>
{
    T read(final PacketWrapper p0) throws Exception;
}
