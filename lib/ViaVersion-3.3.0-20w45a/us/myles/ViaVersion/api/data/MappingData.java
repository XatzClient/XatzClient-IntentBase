// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.data;

import org.jetbrains.annotations.Nullable;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.util.Int2IntBiMap;

public class MappingData
{
    protected final String oldVersion;
    protected final String newVersion;
    protected final boolean hasDiffFile;
    protected Int2IntBiMap itemMappings;
    protected ParticleMappings particleMappings;
    protected Mappings blockMappings;
    protected Mappings blockStateMappings;
    protected Mappings soundMappings;
    protected Mappings statisticsMappings;
    protected boolean loadItems;
    
    public MappingData(final String oldVersion, final String newVersion) {
        this(oldVersion, newVersion, false);
    }
    
    public MappingData(final String oldVersion, final String newVersion, final boolean hasDiffFile) {
        this.loadItems = true;
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
        this.hasDiffFile = hasDiffFile;
    }
    
    public void load() {
        Via.getPlatform().getLogger().info("Loading " + this.oldVersion + " -> " + this.newVersion + " mappings...");
        final JsonObject diffmapping = this.hasDiffFile ? this.loadDiffFile() : null;
        final JsonObject oldMappings = MappingDataLoader.loadData("mapping-" + this.oldVersion + ".json", true);
        final JsonObject newMappings = MappingDataLoader.loadData("mapping-" + this.newVersion + ".json", true);
        this.blockMappings = this.loadFromObject(oldMappings, newMappings, diffmapping, "blocks");
        this.blockStateMappings = this.loadFromObject(oldMappings, newMappings, diffmapping, "blockstates");
        this.soundMappings = this.loadFromArray(oldMappings, newMappings, diffmapping, "sounds");
        this.statisticsMappings = this.loadFromArray(oldMappings, newMappings, diffmapping, "statistics");
        final Mappings particles = this.loadFromArray(oldMappings, newMappings, diffmapping, "particles");
        if (particles != null) {
            this.particleMappings = new ParticleMappings(oldMappings.getAsJsonArray("particles"), particles);
        }
        if (this.loadItems && newMappings.has("items")) {
            (this.itemMappings = new Int2IntBiMap()).defaultReturnValue(-1);
            MappingDataLoader.mapIdentifiers(this.itemMappings, oldMappings.getAsJsonObject("items"), newMappings.getAsJsonObject("items"), (diffmapping != null) ? diffmapping.getAsJsonObject("items") : null);
        }
        this.loadExtras(oldMappings, newMappings, diffmapping);
    }
    
    public int getNewBlockStateId(final int id) {
        return this.checkValidity(id, this.blockStateMappings.getNewId(id), "blockstate");
    }
    
    public int getNewBlockId(final int id) {
        return this.checkValidity(id, this.blockMappings.getNewId(id), "block");
    }
    
    public int getNewItemId(final int id) {
        return this.checkValidity(id, this.itemMappings.get(id), "item");
    }
    
    public int getOldItemId(final int id) {
        final int oldId = this.itemMappings.inverse().get(id);
        return (oldId != -1) ? oldId : 1;
    }
    
    public int getNewParticleId(final int id) {
        return this.checkValidity(id, this.particleMappings.getMappings().getNewId(id), "particles");
    }
    
    @Nullable
    public Int2IntBiMap getItemMappings() {
        return this.itemMappings;
    }
    
    @Nullable
    public ParticleMappings getParticleMappings() {
        return this.particleMappings;
    }
    
    @Nullable
    public Mappings getBlockMappings() {
        return this.blockMappings;
    }
    
    @Nullable
    public Mappings getBlockStateMappings() {
        return this.blockStateMappings;
    }
    
    @Nullable
    public Mappings getSoundMappings() {
        return this.soundMappings;
    }
    
    @Nullable
    public Mappings getStatisticsMappings() {
        return this.statisticsMappings;
    }
    
    @Nullable
    protected Mappings loadFromArray(final JsonObject oldMappings, final JsonObject newMappings, @Nullable final JsonObject diffMappings, final String key) {
        if (!oldMappings.has(key) || !newMappings.has(key)) {
            return null;
        }
        final JsonObject diff = (diffMappings != null) ? diffMappings.getAsJsonObject(key) : null;
        return new Mappings(oldMappings.getAsJsonArray(key), newMappings.getAsJsonArray(key), diff);
    }
    
    @Nullable
    protected Mappings loadFromObject(final JsonObject oldMappings, final JsonObject newMappings, @Nullable final JsonObject diffMappings, final String key) {
        if (!oldMappings.has(key) || !newMappings.has(key)) {
            return null;
        }
        final JsonObject diff = (diffMappings != null) ? diffMappings.getAsJsonObject(key) : null;
        return new Mappings(oldMappings.getAsJsonObject(key), newMappings.getAsJsonObject(key), diff);
    }
    
    protected JsonObject loadDiffFile() {
        return MappingDataLoader.loadData("mappingdiff-" + this.oldVersion + "to" + this.newVersion + ".json");
    }
    
    protected int checkValidity(final int id, final int mappedId, final String type) {
        if (mappedId == -1) {
            Via.getPlatform().getLogger().warning(String.format("Missing %s %s for %s %s %d", this.newVersion, type, this.oldVersion, type, id));
            return 0;
        }
        return mappedId;
    }
    
    protected void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, @Nullable final JsonObject diffMappings) {
    }
}
