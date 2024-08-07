// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.remapper;

import java.util.Iterator;
import us.myles.ViaVersion.exception.CancelException;
import us.myles.ViaVersion.exception.InformativeException;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.function.Function;
import us.myles.ViaVersion.api.type.Type;
import java.util.ArrayList;
import us.myles.ViaVersion.api.Pair;
import java.util.List;

public abstract class PacketRemapper
{
    private final List<Pair<ValueReader, ValueWriter>> valueRemappers;
    
    public PacketRemapper() {
        this.valueRemappers = new ArrayList<Pair<ValueReader, ValueWriter>>();
        this.registerMap();
    }
    
    public void map(final Type type) {
        final TypeRemapper remapper = new TypeRemapper(type);
        this.map(remapper, (ValueWriter<Object>)remapper);
    }
    
    public void map(final Type oldType, final Type newType) {
        this.map(new TypeRemapper<Object>(oldType), new TypeRemapper<Object>(newType));
    }
    
    public <T1, T2> void map(final Type<T1> oldType, final Type<T2> newType, final Function<T1, T2> transformer) {
        this.map(new TypeRemapper<T1>(oldType), new ValueTransformer<T1, T2>(newType) {
            @Override
            public T2 transform(final PacketWrapper wrapper, final T1 inputValue) throws Exception {
                return transformer.apply(inputValue);
            }
        });
    }
    
    public <T1, T2> void map(final ValueTransformer<T1, T2> transformer) {
        if (transformer.getInputType() == null) {
            throw new IllegalArgumentException("Use map(Type<T1>, ValueTransformer<T1, T2>) for value transformers without specified input type!");
        }
        this.map(transformer.getInputType(), transformer);
    }
    
    public <T1, T2> void map(final Type<T1> oldType, final ValueTransformer<T1, T2> transformer) {
        this.map(new TypeRemapper<T1>(oldType), transformer);
    }
    
    public <T> void map(final ValueReader<T> inputReader, final ValueWriter<T> outputWriter) {
        this.valueRemappers.add(new Pair<ValueReader, ValueWriter>(inputReader, outputWriter));
    }
    
    public void create(final ValueCreator creator) {
        this.map(new TypeRemapper<Object>((Type<Object>)Type.NOTHING), creator);
    }
    
    public void handler(final PacketHandler handler) {
        this.map(new TypeRemapper<Object>((Type<Object>)Type.NOTHING), handler);
    }
    
    public abstract void registerMap();
    
    public void remap(final PacketWrapper packetWrapper) throws Exception {
        try {
            for (final Pair<ValueReader, ValueWriter> valueRemapper : this.valueRemappers) {
                final Object object = valueRemapper.getKey().read(packetWrapper);
                valueRemapper.getValue().write(packetWrapper, object);
            }
        }
        catch (InformativeException e) {
            e.addSource(this.getClass());
            throw e;
        }
        catch (CancelException e2) {
            throw e2;
        }
        catch (Exception e3) {
            final InformativeException ex = new InformativeException(e3);
            ex.addSource(this.getClass());
            throw ex;
        }
    }
}
