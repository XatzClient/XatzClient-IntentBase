// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.remapper;

import us.myles.ViaVersion.exception.InformativeException;
import us.myles.ViaVersion.api.PacketWrapper;

@FunctionalInterface
public interface PacketHandler extends ValueWriter
{
    void handle(final PacketWrapper p0) throws Exception;
    
    default void write(final PacketWrapper writer, final Object inputValue) throws Exception {
        try {
            this.handle(writer);
        }
        catch (InformativeException e) {
            e.addSource(this.getClass());
            throw e;
        }
    }
}
