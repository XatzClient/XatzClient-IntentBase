// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.data;

import us.myles.viaversion.libs.gson.JsonArray;
import java.util.Arrays;
import org.jetbrains.annotations.Nullable;
import us.myles.viaversion.libs.gson.JsonObject;

public class Mappings
{
    protected final short[] oldToNew;
    
    public Mappings(final short[] oldToNew) {
        this.oldToNew = oldToNew;
    }
    
    public Mappings(final int size, final JsonObject oldMapping, final JsonObject newMapping, @Nullable final JsonObject diffMapping) {
        Arrays.fill(this.oldToNew = new short[size], (short)(-1));
        MappingDataLoader.mapIdentifiers(this.oldToNew, oldMapping, newMapping, diffMapping);
    }
    
    public Mappings(final JsonObject oldMapping, final JsonObject newMapping, @Nullable final JsonObject diffMapping) {
        this(oldMapping.entrySet().size(), oldMapping, newMapping, diffMapping);
    }
    
    public Mappings(final int size, final JsonObject oldMapping, final JsonObject newMapping) {
        Arrays.fill(this.oldToNew = new short[size], (short)(-1));
        MappingDataLoader.mapIdentifiers(this.oldToNew, oldMapping, newMapping);
    }
    
    public Mappings(final JsonObject oldMapping, final JsonObject newMapping) {
        this(oldMapping.entrySet().size(), oldMapping, newMapping);
    }
    
    public Mappings(final int size, final JsonArray oldMapping, final JsonArray newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        Arrays.fill(this.oldToNew = new short[size], (short)(-1));
        MappingDataLoader.mapIdentifiers(this.oldToNew, oldMapping, newMapping, diffMapping, warnOnMissing);
    }
    
    public Mappings(final int size, final JsonArray oldMapping, final JsonArray newMapping, final boolean warnOnMissing) {
        this(size, oldMapping, newMapping, null, warnOnMissing);
    }
    
    public Mappings(final JsonArray oldMapping, final JsonArray newMapping, final boolean warnOnMissing) {
        this(oldMapping.size(), oldMapping, newMapping, warnOnMissing);
    }
    
    public Mappings(final int size, final JsonArray oldMapping, final JsonArray newMapping) {
        this(size, oldMapping, newMapping, true);
    }
    
    public Mappings(final JsonArray oldMapping, final JsonArray newMapping, final JsonObject diffMapping) {
        this(oldMapping.size(), oldMapping, newMapping, diffMapping, true);
    }
    
    public Mappings(final JsonArray oldMapping, final JsonArray newMapping) {
        this(oldMapping.size(), oldMapping, newMapping, true);
    }
    
    public int getNewId(final int old) {
        return (old >= 0 && old < this.oldToNew.length) ? this.oldToNew[old] : -1;
    }
    
    public short[] getOldToNew() {
        return this.oldToNew;
    }
}
