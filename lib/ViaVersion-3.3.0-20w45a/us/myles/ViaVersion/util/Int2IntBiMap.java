// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.util;

import java.util.Set;
import java.util.Collection;
import us.myles.viaversion.libs.fastutil.ints.IntCollection;
import us.myles.viaversion.libs.fastutil.ints.IntSet;
import us.myles.viaversion.libs.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import com.google.common.base.Preconditions;
import us.myles.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;

public class Int2IntBiMap implements Int2IntMap
{
    private final Int2IntMap map;
    private final Int2IntBiMap inverse;
    
    public Int2IntBiMap() {
        this.map = new Int2IntOpenHashMap();
        this.inverse = new Int2IntBiMap(this);
    }
    
    private Int2IntBiMap(final Int2IntBiMap inverse) {
        this.map = new Int2IntOpenHashMap();
        this.inverse = inverse;
    }
    
    public Int2IntBiMap inverse() {
        return this.inverse;
    }
    
    @Override
    public int put(final int key, final int value) {
        if (this.containsKey(key) && value == this.get(key)) {
            return value;
        }
        Preconditions.checkArgument(!this.containsValue(value), "value already present: %s", new Object[] { value });
        this.map.put(key, value);
        this.inverse.map.put(value, key);
        return this.defaultReturnValue();
    }
    
    @Override
    public boolean remove(final int key, final int value) {
        this.map.remove(key, value);
        return this.inverse.map.remove(key, value);
    }
    
    @Override
    public int get(final int key) {
        return this.map.get(key);
    }
    
    @Override
    public void clear() {
        this.map.clear();
        this.inverse.map.clear();
    }
    
    @Override
    public int size() {
        return this.map.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    @Deprecated
    @Override
    public void putAll(@NotNull final Map<? extends Integer, ? extends Integer> m) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void defaultReturnValue(final int rv) {
        this.map.defaultReturnValue(rv);
        this.inverse.map.defaultReturnValue(rv);
    }
    
    @Override
    public int defaultReturnValue() {
        return this.map.defaultReturnValue();
    }
    
    @Override
    public ObjectSet<Entry> int2IntEntrySet() {
        return this.map.int2IntEntrySet();
    }
    
    @NotNull
    @Override
    public IntSet keySet() {
        return this.map.keySet();
    }
    
    @NotNull
    @Override
    public IntSet values() {
        return this.inverse.map.keySet();
    }
    
    @Override
    public boolean containsKey(final int key) {
        return this.map.containsKey(key);
    }
    
    @Override
    public boolean containsValue(final int value) {
        return this.inverse.map.containsKey(value);
    }
}
