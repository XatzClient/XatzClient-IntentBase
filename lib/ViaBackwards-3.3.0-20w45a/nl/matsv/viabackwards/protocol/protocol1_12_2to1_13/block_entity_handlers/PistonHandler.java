// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import java.util.StringJoiner;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import java.util.Iterator;
import java.lang.reflect.Field;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
import us.myles.ViaVersion.api.Via;
import java.util.HashMap;
import java.util.Map;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;

public class PistonHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler
{
    private final Map<String, Integer> pistonIds;
    
    public PistonHandler() {
        this.pistonIds = new HashMap<String, Integer>();
        if (Via.getConfig().isServersideBlockConnections()) {
            Map<String, Integer> keyToId;
            try {
                final Field field = ConnectionData.class.getDeclaredField("keyToId");
                field.setAccessible(true);
                keyToId = (Map<String, Integer>)field.get(null);
            }
            catch (IllegalAccessException | NoSuchFieldException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                e.printStackTrace();
                return;
            }
            for (final Map.Entry<String, Integer> entry : keyToId.entrySet()) {
                if (!entry.getKey().contains("piston")) {
                    continue;
                }
                this.addEntries(entry.getKey(), entry.getValue());
            }
        }
        else {
            final JsonObject mappings = MappingDataLoader.getMappingsCache().get("mapping-1.13.json").getAsJsonObject("blockstates");
            for (final Map.Entry<String, JsonElement> blockState : mappings.entrySet()) {
                final String key = blockState.getValue().getAsString();
                if (!key.contains("piston")) {
                    continue;
                }
                this.addEntries(key, Integer.parseInt(blockState.getKey()));
            }
        }
    }
    
    private void addEntries(String data, int id) {
        id = Protocol1_12_2To1_13.MAPPINGS.getNewBlockStateId(id);
        this.pistonIds.put(data, id);
        final String substring = data.substring(10);
        if (!substring.startsWith("piston") && !substring.startsWith("sticky_piston")) {
            return;
        }
        final String[] split = data.substring(0, data.length() - 1).split("\\[");
        final String[] properties = split[1].split(",");
        data = split[0] + "[" + properties[1] + "," + properties[0] + "]";
        this.pistonIds.put(data, id);
    }
    
    @Override
    public CompoundTag transform(final UserConnection user, final int blockId, final CompoundTag tag) {
        final CompoundTag blockState = (CompoundTag)tag.get("blockState");
        if (blockState == null) {
            return tag;
        }
        final String dataFromTag = this.getDataFromTag(blockState);
        if (dataFromTag == null) {
            return tag;
        }
        final Integer id = this.pistonIds.get(dataFromTag);
        if (id == null) {
            return tag;
        }
        tag.put((Tag)new IntTag("blockId", id >> 4));
        tag.put((Tag)new IntTag("blockData", id & 0xF));
        return tag;
    }
    
    private String getDataFromTag(final CompoundTag tag) {
        final StringTag name = (StringTag)tag.get("Name");
        if (name == null) {
            return null;
        }
        final CompoundTag properties = (CompoundTag)tag.get("Properties");
        if (properties == null) {
            return name.getValue();
        }
        final StringJoiner joiner = new StringJoiner(",", name.getValue() + "[", "]");
        for (final Tag property : properties) {
            if (!(property instanceof StringTag)) {
                continue;
            }
            joiner.add(property.getName() + "=" + ((StringTag)property).getValue());
        }
        return joiner.toString();
    }
}
