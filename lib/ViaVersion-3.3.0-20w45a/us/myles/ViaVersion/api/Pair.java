// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;

public class Pair<X, Y>
{
    private final X key;
    private Y value;
    
    public Pair(@Nullable final X key, @Nullable final Y value) {
        this.key = key;
        this.value = value;
    }
    
    public X getKey() {
        return this.key;
    }
    
    public Y getValue() {
        return this.value;
    }
    
    public void setValue(@Nullable final Y value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "Pair{" + this.key + ", " + this.value + '}';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Pair<?, ?> pair = (Pair<?, ?>)o;
        return Objects.equals(this.key, pair.key) && Objects.equals(this.value, pair.value);
    }
    
    @Override
    public int hashCode() {
        int result = (this.key != null) ? this.key.hashCode() : 0;
        result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
        return result;
    }
}
