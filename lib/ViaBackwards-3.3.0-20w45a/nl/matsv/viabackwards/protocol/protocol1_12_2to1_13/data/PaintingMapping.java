// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data;

import us.myles.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectMap;

public class PaintingMapping
{
    private static final Int2ObjectMap<String> PAINTINGS;
    
    public static void init() {
        add("Kebab");
        add("Aztec");
        add("Alban");
        add("Aztec2");
        add("Bomb");
        add("Plant");
        add("Wasteland");
        add("Pool");
        add("Courbet");
        add("Sea");
        add("Sunset");
        add("Creebet");
        add("Wanderer");
        add("Graham");
        add("Match");
        add("Bust");
        add("Stage");
        add("Void");
        add("SkullAndRoses");
        add("Wither");
        add("Fighters");
        add("Pointer");
        add("Pigscene");
        add("BurningSkull");
        add("Skeleton");
        add("DonkeyKong");
    }
    
    private static void add(final String motive) {
        PaintingMapping.PAINTINGS.put(PaintingMapping.PAINTINGS.size(), (Object)motive);
    }
    
    public static String getStringId(final int id) {
        return (String)PaintingMapping.PAINTINGS.getOrDefault(id, (Object)"kebab");
    }
    
    static {
        PAINTINGS = (Int2ObjectMap)new Int2ObjectOpenHashMap(26, 1.0f);
    }
}
