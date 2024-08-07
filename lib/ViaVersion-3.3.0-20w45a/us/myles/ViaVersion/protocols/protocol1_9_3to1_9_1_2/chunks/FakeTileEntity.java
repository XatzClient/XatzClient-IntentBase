// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.chunks;

import java.util.Arrays;
import java.util.HashMap;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import java.util.Iterator;
import java.util.List;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.Map;

public class FakeTileEntity
{
    private static final Map<Integer, CompoundTag> tileEntities;
    
    private static void register(final Integer material, final String name) {
        final CompoundTag comp = new CompoundTag("");
        comp.put(new StringTag(name));
        FakeTileEntity.tileEntities.put(material, comp);
    }
    
    private static void register(final List<Integer> materials, final String name) {
        for (final int m : materials) {
            register(m, name);
        }
    }
    
    public static boolean hasBlock(final int block) {
        return FakeTileEntity.tileEntities.containsKey(block);
    }
    
    public static CompoundTag getFromBlock(final int x, final int y, final int z, final int block) {
        final CompoundTag originalTag = FakeTileEntity.tileEntities.get(block);
        if (originalTag != null) {
            final CompoundTag tag = originalTag.clone();
            tag.put(new IntTag("x", x));
            tag.put(new IntTag("y", y));
            tag.put(new IntTag("z", z));
            return tag;
        }
        return null;
    }
    
    static {
        tileEntities = new HashMap<Integer, CompoundTag>();
        register(Arrays.asList(61, 62), "Furnace");
        register(Arrays.asList(54, 146), "Chest");
        register(130, "EnderChest");
        register(84, "RecordPlayer");
        register(23, "Trap");
        register(158, "Dropper");
        register(Arrays.asList(63, 68), "Sign");
        register(52, "MobSpawner");
        register(25, "Music");
        register(Arrays.asList(33, 34, 29, 36), "Piston");
        register(117, "Cauldron");
        register(116, "EnchantTable");
        register(Arrays.asList(119, 120), "Airportal");
        register(138, "Beacon");
        register(144, "Skull");
        register(Arrays.asList(178, 151), "DLDetector");
        register(154, "Hopper");
        register(Arrays.asList(149, 150), "Comparator");
        register(140, "FlowerPot");
        register(Arrays.asList(176, 177), "Banner");
        register(209, "EndGateway");
        register(137, "Control");
    }
}
