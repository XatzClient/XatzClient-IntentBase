// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import us.myles.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.Pair;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectMap;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;

public class FlowerPotHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler
{
    private static final Int2ObjectMap<Pair<String, Byte>> FLOWERS;
    private static final Pair<String, Byte> AIR;
    
    private static void register(final int id, final String identifier, final byte data) {
        FlowerPotHandler.FLOWERS.put(id, (Object)new Pair((Object)identifier, (Object)data));
    }
    
    public static boolean isFlowah(final int id) {
        return id >= 5265 && id <= 5286;
    }
    
    public Pair<String, Byte> getOrDefault(final int blockId) {
        final Pair<String, Byte> pair = (Pair<String, Byte>)FlowerPotHandler.FLOWERS.get(blockId);
        return (pair != null) ? pair : FlowerPotHandler.AIR;
    }
    
    @Override
    public CompoundTag transform(final UserConnection user, final int blockId, final CompoundTag tag) {
        final Pair<String, Byte> item = this.getOrDefault(blockId);
        tag.put((Tag)new StringTag("Item", (String)item.getKey()));
        tag.put((Tag)new IntTag("Data", (int)(byte)item.getValue()));
        return tag;
    }
    
    static {
        FLOWERS = (Int2ObjectMap)new Int2ObjectOpenHashMap(22, 1.0f);
        AIR = new Pair((Object)"minecraft:air", (Object)0);
        FlowerPotHandler.FLOWERS.put(5265, (Object)FlowerPotHandler.AIR);
        register(5266, "minecraft:sapling", (byte)0);
        register(5267, "minecraft:sapling", (byte)1);
        register(5268, "minecraft:sapling", (byte)2);
        register(5269, "minecraft:sapling", (byte)3);
        register(5270, "minecraft:sapling", (byte)4);
        register(5271, "minecraft:sapling", (byte)5);
        register(5272, "minecraft:tallgrass", (byte)2);
        register(5273, "minecraft:yellow_flower", (byte)0);
        register(5274, "minecraft:red_flower", (byte)0);
        register(5275, "minecraft:red_flower", (byte)1);
        register(5276, "minecraft:red_flower", (byte)2);
        register(5277, "minecraft:red_flower", (byte)3);
        register(5278, "minecraft:red_flower", (byte)4);
        register(5279, "minecraft:red_flower", (byte)5);
        register(5280, "minecraft:red_flower", (byte)6);
        register(5281, "minecraft:red_flower", (byte)7);
        register(5282, "minecraft:red_flower", (byte)8);
        register(5283, "minecraft:red_mushroom", (byte)0);
        register(5284, "minecraft:brown_mushroom", (byte)0);
        register(5285, "minecraft:deadbush", (byte)0);
        register(5286, "minecraft:cactus", (byte)0);
    }
}
