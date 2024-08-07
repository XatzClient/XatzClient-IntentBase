// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.data;

import java.util.Arrays;
import us.myles.viaversion.libs.gson.JsonArray;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.api.data.Mappings;

public class VBMappings extends Mappings
{
    public VBMappings(final int size, final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        super(create(size, oldMapping, newMapping, diffMapping, warnOnMissing));
    }
    
    public VBMappings(final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        super(create(oldMapping.entrySet().size(), oldMapping, newMapping, diffMapping, warnOnMissing));
    }
    
    public VBMappings(final JsonObject oldMapping, final JsonObject newMapping, final boolean warnOnMissing) {
        this(oldMapping, newMapping, null, warnOnMissing);
    }
    
    public VBMappings(final JsonArray oldMapping, final JsonArray newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        super(oldMapping.size(), oldMapping, newMapping, diffMapping, warnOnMissing);
    }
    
    private static short[] create(final int size, final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        final short[] oldToNew = new short[size];
        Arrays.fill(oldToNew, (short)(-1));
        VBMappingDataLoader.mapIdentifiers(oldToNew, oldMapping, newMapping, diffMapping, warnOnMissing);
        return oldToNew;
    }
}
