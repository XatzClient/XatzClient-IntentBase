// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.data;

import us.myles.ViaVersion.api.data.Mappings;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.viaversion.libs.gson.JsonObject;
import com.google.common.base.Preconditions;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectMap;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.data.MappingData;

public class BackwardsMappings extends MappingData
{
    private final Class<? extends Protocol> vvProtocolClass;
    private Int2ObjectMap<MappedItem> backwardsItemMappings;
    private Map<String, String> backwardsSoundMappings;
    
    public BackwardsMappings(final String oldVersion, final String newVersion, @Nullable final Class<? extends Protocol> vvProtocolClass) {
        this(oldVersion, newVersion, vvProtocolClass, false);
    }
    
    public BackwardsMappings(final String oldVersion, final String newVersion, @Nullable final Class<? extends Protocol> vvProtocolClass, final boolean hasDiffFile) {
        super(oldVersion, newVersion, hasDiffFile);
        Preconditions.checkArgument(!vvProtocolClass.isAssignableFrom(BackwardsProtocol.class));
        this.vvProtocolClass = vvProtocolClass;
        this.loadItems = false;
    }
    
    protected void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, @Nullable final JsonObject diffMappings) {
        if (diffMappings != null) {
            final JsonObject diffItems = diffMappings.getAsJsonObject("items");
            if (diffItems != null) {
                this.backwardsItemMappings = VBMappingDataLoader.loadItemMappings(oldMappings.getAsJsonObject("items"), newMappings.getAsJsonObject("items"), diffItems);
            }
            final JsonObject diffSounds = diffMappings.getAsJsonObject("sounds");
            if (diffSounds != null) {
                this.backwardsSoundMappings = VBMappingDataLoader.objectToMap(diffSounds);
            }
        }
        if (this.vvProtocolClass != null) {
            this.itemMappings = ProtocolRegistry.getProtocol((Class)this.vvProtocolClass).getMappingData().getItemMappings().inverse();
        }
        this.loadVBExtras(oldMappings, newMappings);
    }
    
    @Nullable
    protected Mappings loadFromArray(final JsonObject oldMappings, final JsonObject newMappings, @Nullable final JsonObject diffMappings, final String key) {
        if (!oldMappings.has(key) || !newMappings.has(key)) {
            return null;
        }
        final JsonObject diff = (diffMappings != null) ? diffMappings.getAsJsonObject(key) : null;
        return new VBMappings(oldMappings.getAsJsonArray(key), newMappings.getAsJsonArray(key), diff, this.shouldWarnOnMissing(key));
    }
    
    @Nullable
    protected Mappings loadFromObject(final JsonObject oldMappings, final JsonObject newMappings, @Nullable final JsonObject diffMappings, final String key) {
        if (!oldMappings.has(key) || !newMappings.has(key)) {
            return null;
        }
        final JsonObject diff = (diffMappings != null) ? diffMappings.getAsJsonObject(key) : null;
        return new VBMappings(oldMappings.getAsJsonObject(key), newMappings.getAsJsonObject(key), diff, this.shouldWarnOnMissing(key));
    }
    
    protected JsonObject loadDiffFile() {
        return VBMappingDataLoader.loadFromDataDir("mapping-" + this.newVersion + "to" + this.oldVersion + ".json");
    }
    
    protected void loadVBExtras(final JsonObject oldMappings, final JsonObject newMappings) {
    }
    
    protected boolean shouldWarnOnMissing(final String key) {
        return !key.equals("blocks") && !key.equals("statistics");
    }
    
    public int getNewItemId(final int id) {
        return this.itemMappings.get(id);
    }
    
    public int getNewBlockId(final int id) {
        return this.blockMappings.getNewId(id);
    }
    
    public int getOldItemId(final int id) {
        return this.checkValidity(id, this.itemMappings.inverse().get(id), "item");
    }
    
    @Nullable
    public MappedItem getMappedItem(final int id) {
        return (this.backwardsItemMappings != null) ? ((MappedItem)this.backwardsItemMappings.get(id)) : null;
    }
    
    @Nullable
    public String getMappedNamedSound(final String id) {
        return (this.backwardsSoundMappings != null) ? this.backwardsSoundMappings.get(id) : null;
    }
    
    @Nullable
    public Int2ObjectMap<MappedItem> getBackwardsItemMappings() {
        return this.backwardsItemMappings;
    }
    
    @Nullable
    public Map<String, String> getBackwardsSoundMappings() {
        return this.backwardsSoundMappings;
    }
}
