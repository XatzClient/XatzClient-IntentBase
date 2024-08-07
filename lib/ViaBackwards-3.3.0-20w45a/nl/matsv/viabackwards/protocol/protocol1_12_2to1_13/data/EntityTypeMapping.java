// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data;

import us.myles.viaversion.libs.fastutil.objects.ObjectIterator;
import java.lang.reflect.Field;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.EntityTypeRewriter;
import us.myles.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;

public class EntityTypeMapping
{
    private static final Int2IntMap TYPES;
    
    public static int getOldId(final int type1_13) {
        return EntityTypeMapping.TYPES.get(type1_13);
    }
    
    static {
        (TYPES = (Int2IntMap)new Int2IntOpenHashMap()).defaultReturnValue(-1);
        try {
            final Field field = EntityTypeRewriter.class.getDeclaredField("ENTITY_TYPES");
            field.setAccessible(true);
            final Int2IntMap entityTypes = (Int2IntMap)field.get(null);
            for (final Int2IntMap.Entry entry : entityTypes.int2IntEntrySet()) {
                EntityTypeMapping.TYPES.put(entry.getIntValue(), entry.getIntKey());
            }
        }
        catch (NoSuchFieldException | IllegalAccessException ex3) {
            final ReflectiveOperationException ex2;
            final ReflectiveOperationException ex = ex2;
            ex.printStackTrace();
        }
    }
}
