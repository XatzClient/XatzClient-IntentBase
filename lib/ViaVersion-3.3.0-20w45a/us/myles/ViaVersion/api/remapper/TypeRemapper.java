// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.remapper;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;

public class TypeRemapper<T> implements ValueReader<T>, ValueWriter<T>
{
    private final Type<T> type;
    
    public TypeRemapper(final Type<T> type) {
        this.type = type;
    }
    
    @Override
    public T read(final PacketWrapper wrapper) throws Exception {
        return wrapper.read(this.type);
    }
    
    @Override
    public void write(final PacketWrapper output, final T inputValue) {
        output.write(this.type, inputValue);
    }
}
