// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.remapper;

import us.myles.ViaVersion.exception.InformativeException;
import us.myles.ViaVersion.api.PacketWrapper;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.type.Type;

public abstract class ValueTransformer<T1, T2> implements ValueWriter<T1>
{
    private final Type<T1> inputType;
    private final Type<T2> outputType;
    
    public ValueTransformer(@Nullable final Type<T1> inputType, final Type<T2> outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
    }
    
    public ValueTransformer(final Type<T2> outputType) {
        this(null, outputType);
    }
    
    public abstract T2 transform(final PacketWrapper p0, final T1 p1) throws Exception;
    
    @Override
    public void write(final PacketWrapper writer, final T1 inputValue) throws Exception {
        try {
            writer.write(this.outputType, this.transform(writer, inputValue));
        }
        catch (InformativeException e) {
            e.addSource(this.getClass());
            throw e;
        }
    }
    
    @Nullable
    public Type<T1> getInputType() {
        return this.inputType;
    }
    
    public Type<T2> getOutputType() {
        return this.outputType;
    }
}
