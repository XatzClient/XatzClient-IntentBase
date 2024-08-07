// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_15to1_14_4.data;

import us.myles.ViaVersion.api.data.Mappings;
import org.jetbrains.annotations.Nullable;
import us.myles.viaversion.libs.gson.JsonObject;

public class MappingData extends us.myles.ViaVersion.api.data.MappingData
{
    public MappingData() {
        super("1.14", "1.15", true);
    }
    
    @Override
    protected Mappings loadFromArray(final JsonObject oldMappings, final JsonObject newMappings, @Nullable final JsonObject diffMappings, final String key) {
        if (!key.equals("sounds")) {
            return super.loadFromArray(oldMappings, newMappings, diffMappings, key);
        }
        return new Mappings(oldMappings.getAsJsonArray(key), newMappings.getAsJsonArray(key), false);
    }
}
