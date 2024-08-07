// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_14to1_13_2.data;

import us.myles.viaversion.libs.gson.JsonArray;
import java.util.Iterator;
import us.myles.ViaVersion.api.Via;
import us.myles.viaversion.libs.fastutil.ints.IntOpenHashSet;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import us.myles.viaversion.libs.gson.JsonElement;
import java.util.Map;
import java.util.HashMap;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.fastutil.ints.IntSet;

public class MappingData extends us.myles.ViaVersion.api.data.MappingData
{
    private IntSet motionBlocking;
    private IntSet nonFullBlocks;
    
    public MappingData() {
        super("1.13.2", "1.14");
    }
    
    public void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings) {
        final JsonObject blockStates = newMappings.getAsJsonObject("blockstates");
        final Map<String, Integer> blockStateMap = new HashMap<String, Integer>(blockStates.entrySet().size());
        for (final Map.Entry<String, JsonElement> entry : blockStates.entrySet()) {
            blockStateMap.put(entry.getValue().getAsString(), Integer.parseInt(entry.getKey()));
        }
        final JsonObject heightMapData = MappingDataLoader.loadData("heightMapData-1.14.json");
        final JsonArray motionBlocking = heightMapData.getAsJsonArray("MOTION_BLOCKING");
        this.motionBlocking = new IntOpenHashSet(motionBlocking.size(), 1.0f);
        for (final JsonElement blockState : motionBlocking) {
            final String key = blockState.getAsString();
            final Integer id = blockStateMap.get(key);
            if (id == null) {
                Via.getPlatform().getLogger().warning("Unknown blockstate " + key + " :(");
            }
            else {
                this.motionBlocking.add((int)id);
            }
        }
        if (Via.getConfig().isNonFullBlockLightFix()) {
            this.nonFullBlocks = new IntOpenHashSet(1611, 1.0f);
            for (final Map.Entry<String, JsonElement> blockstates : oldMappings.getAsJsonObject("blockstates").entrySet()) {
                final String state = blockstates.getValue().getAsString();
                if (state.contains("_slab") || state.contains("_stairs") || state.contains("_wall[")) {
                    this.nonFullBlocks.add(this.blockStateMappings.getNewId(Integer.parseInt(blockstates.getKey())));
                }
            }
            this.nonFullBlocks.add(this.blockStateMappings.getNewId(8163));
            for (int i = 3060; i <= 3067; ++i) {
                this.nonFullBlocks.add(this.blockStateMappings.getNewId(i));
            }
        }
    }
    
    public IntSet getMotionBlocking() {
        return this.motionBlocking;
    }
    
    public IntSet getNonFullBlocks() {
        return this.nonFullBlocks;
    }
}
