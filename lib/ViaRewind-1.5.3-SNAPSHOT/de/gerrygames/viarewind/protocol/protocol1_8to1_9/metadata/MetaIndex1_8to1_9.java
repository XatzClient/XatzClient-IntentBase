// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata;

import java.util.Optional;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.metadata.MetaIndex;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.Pair;
import java.util.HashMap;

public class MetaIndex1_8to1_9
{
    private static final HashMap<Pair<Entity1_10Types.EntityType, Integer>, MetaIndex> metadataRewrites;
    
    private static Optional<MetaIndex> getIndex(final Entity1_10Types.EntityType type, final int index) {
        final Pair pair = new Pair((Object)type, (Object)index);
        if (MetaIndex1_8to1_9.metadataRewrites.containsKey(pair)) {
            return Optional.of(MetaIndex1_8to1_9.metadataRewrites.get(pair));
        }
        return Optional.empty();
    }
    
    public static MetaIndex searchIndex(final Entity1_10Types.EntityType type, final int index) {
        Entity1_10Types.EntityType currentType = type;
        do {
            final Optional<MetaIndex> optMeta = getIndex(currentType, index);
            if (optMeta.isPresent()) {
                return optMeta.get();
            }
            currentType = currentType.getParent();
        } while (currentType != null);
        return null;
    }
    
    static {
        metadataRewrites = new HashMap<Pair<Entity1_10Types.EntityType, Integer>, MetaIndex>();
        for (final MetaIndex index : MetaIndex.values()) {
            MetaIndex1_8to1_9.metadataRewrites.put((Pair<Entity1_10Types.EntityType, Integer>)new Pair((Object)index.getClazz(), (Object)index.getNewIndex()), index);
        }
    }
}
