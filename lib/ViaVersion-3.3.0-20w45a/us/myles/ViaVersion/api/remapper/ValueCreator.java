// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.remapper;

import us.myles.ViaVersion.exception.InformativeException;
import us.myles.ViaVersion.api.PacketWrapper;

@FunctionalInterface
public interface ValueCreator extends ValueWriter
{
    void write(final PacketWrapper p0) throws Exception;
    
    default void write(final PacketWrapper writer, final Object inputValue) throws Exception {
        try {
            this.write(writer);
        }
        catch (InformativeException e) {
            e.addSource(this.getClass());
            throw e;
        }
    }
}
