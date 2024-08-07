// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data;

import java.util.Arrays;
import org.jetbrains.annotations.Nullable;
import us.myles.viaversion.libs.gson.JsonPrimitive;
import us.myles.viaversion.libs.fastutil.objects.Object2IntMap;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.Via;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import java.util.Iterator;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.StatisticMappings;
import nl.matsv.viabackwards.api.data.VBMappings;
import us.myles.viaversion.libs.gson.JsonObject;
import java.util.HashMap;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import us.myles.ViaVersion.api.data.Mappings;
import java.util.Map;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectMap;

public class BackwardsMappings extends nl.matsv.viabackwards.api.data.BackwardsMappings
{
    private final Int2ObjectMap<String> statisticMappings;
    private final Map<String, String> translateMappings;
    private Mappings enchantmentMappings;
    
    public BackwardsMappings() {
        super("1.13", "1.12", (Class<? extends Protocol>)Protocol1_13To1_12_2.class, true);
        this.statisticMappings = (Int2ObjectMap<String>)new Int2ObjectOpenHashMap();
        this.translateMappings = new HashMap<String, String>();
    }
    
    public void loadVBExtras(final JsonObject oldMappings, final JsonObject newMappings) {
        this.enchantmentMappings = new VBMappings(oldMappings.getAsJsonObject("enchantments"), newMappings.getAsJsonObject("enchantments"), false);
        for (final Map.Entry<String, Integer> entry : StatisticMappings.CUSTOM_STATS.entrySet()) {
            this.statisticMappings.put((int)entry.getValue(), (Object)entry.getKey());
        }
        for (final Map.Entry<String, String> entry2 : Protocol1_13To1_12_2.MAPPINGS.getTranslateMapping().entrySet()) {
            this.translateMappings.put(entry2.getValue(), entry2.getKey());
        }
    }
    
    private static void mapIdentifiers(final short[] output, final JsonObject newIdentifiers, final JsonObject oldIdentifiers, final JsonObject mapping) {
        final Object2IntMap newIdentifierMap = MappingDataLoader.indexedObjectToMap(oldIdentifiers);
        for (final Map.Entry<String, JsonElement> entry : newIdentifiers.entrySet()) {
            final String key = entry.getValue().getAsString();
            int value = newIdentifierMap.getInt((Object)key);
            short hardId = -1;
            if (value == -1) {
                JsonPrimitive replacement = mapping.getAsJsonPrimitive(key);
                final int propertyIndex;
                if (replacement == null && (propertyIndex = key.indexOf(91)) != -1) {
                    replacement = mapping.getAsJsonPrimitive(key.substring(0, propertyIndex));
                }
                if (replacement != null) {
                    if (replacement.getAsString().startsWith("id:")) {
                        final String id = replacement.getAsString().replace("id:", "");
                        hardId = Short.parseShort(id);
                        value = newIdentifierMap.getInt((Object)oldIdentifiers.getAsJsonPrimitive(id).getAsString());
                    }
                    else {
                        value = newIdentifierMap.getInt((Object)replacement.getAsString());
                    }
                }
                if (value == -1) {
                    if (Via.getConfig().isSuppressConversionWarnings() && !Via.getManager().isDebug()) {
                        continue;
                    }
                    if (replacement != null) {
                        ViaBackwards.getPlatform().getLogger().warning("No key for " + entry.getValue() + "/" + replacement.getAsString() + " :( ");
                        continue;
                    }
                    ViaBackwards.getPlatform().getLogger().warning("No key for " + entry.getValue() + " :( ");
                    continue;
                }
            }
            output[Integer.parseInt(entry.getKey())] = ((hardId != -1) ? hardId : ((short)value));
        }
    }
    
    @Nullable
    @Override
    protected Mappings loadFromObject(final JsonObject oldMappings, final JsonObject newMappings, @Nullable final JsonObject diffMappings, final String key) {
        if (key.equals("blockstates")) {
            final short[] oldToNew = new short[8582];
            Arrays.fill(oldToNew, (short)(-1));
            mapIdentifiers(oldToNew, oldMappings.getAsJsonObject("blockstates"), newMappings.getAsJsonObject("blocks"), diffMappings.getAsJsonObject("blockstates"));
            return new Mappings(oldToNew);
        }
        return super.loadFromObject(oldMappings, newMappings, diffMappings, key);
    }
    
    protected int checkValidity(final int id, final int mappedId, final String type) {
        return mappedId;
    }
    
    public Int2ObjectMap<String> getStatisticMappings() {
        return this.statisticMappings;
    }
    
    public Map<String, String> getTranslateMappings() {
        return this.translateMappings;
    }
    
    public Mappings getEnchantmentMappings() {
        return this.enchantmentMappings;
    }
}
