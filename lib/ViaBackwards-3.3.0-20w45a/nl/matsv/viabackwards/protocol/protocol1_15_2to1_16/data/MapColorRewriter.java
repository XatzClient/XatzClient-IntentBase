// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.data;

import us.myles.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;

public class MapColorRewriter
{
    private static final Int2IntMap MAPPINGS;
    
    public static int getMappedColor(final int color) {
        return MapColorRewriter.MAPPINGS.getOrDefault(color, -1);
    }
    
    static {
        (MAPPINGS = (Int2IntMap)new Int2IntOpenHashMap()).put(208, 113);
        MapColorRewriter.MAPPINGS.put(209, 114);
        MapColorRewriter.MAPPINGS.put(210, 114);
        MapColorRewriter.MAPPINGS.put(211, 112);
        MapColorRewriter.MAPPINGS.put(212, 152);
        MapColorRewriter.MAPPINGS.put(213, 83);
        MapColorRewriter.MAPPINGS.put(214, 83);
        MapColorRewriter.MAPPINGS.put(215, 155);
        MapColorRewriter.MAPPINGS.put(216, 143);
        MapColorRewriter.MAPPINGS.put(217, 115);
        MapColorRewriter.MAPPINGS.put(218, 115);
        MapColorRewriter.MAPPINGS.put(219, 143);
        MapColorRewriter.MAPPINGS.put(220, 127);
        MapColorRewriter.MAPPINGS.put(221, 127);
        MapColorRewriter.MAPPINGS.put(222, 127);
        MapColorRewriter.MAPPINGS.put(223, 95);
        MapColorRewriter.MAPPINGS.put(224, 127);
        MapColorRewriter.MAPPINGS.put(225, 127);
        MapColorRewriter.MAPPINGS.put(226, 124);
        MapColorRewriter.MAPPINGS.put(227, 95);
        MapColorRewriter.MAPPINGS.put(228, 187);
        MapColorRewriter.MAPPINGS.put(229, 155);
        MapColorRewriter.MAPPINGS.put(230, 184);
        MapColorRewriter.MAPPINGS.put(231, 187);
        MapColorRewriter.MAPPINGS.put(232, 127);
        MapColorRewriter.MAPPINGS.put(233, 124);
        MapColorRewriter.MAPPINGS.put(234, 125);
        MapColorRewriter.MAPPINGS.put(235, 127);
    }
}
