// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import java.util.Iterator;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;

public class BannerHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler
{
    private static final int WALL_BANNER_START = 7110;
    private static final int WALL_BANNER_STOP = 7173;
    private static final int BANNER_START = 6854;
    private static final int BANNER_STOP = 7109;
    
    @Override
    public CompoundTag transform(final UserConnection user, final int blockId, final CompoundTag tag) {
        if (blockId >= 6854 && blockId <= 7109) {
            final int color = blockId - 6854 >> 4;
            tag.put((Tag)new IntTag("Base", 15 - color));
        }
        else if (blockId >= 7110 && blockId <= 7173) {
            final int color = blockId - 7110 >> 2;
            tag.put((Tag)new IntTag("Base", 15 - color));
        }
        else {
            ViaBackwards.getPlatform().getLogger().warning("Why does this block have the banner block entity? :(" + tag);
        }
        final Tag patternsTag = tag.get("Patterns");
        if (patternsTag instanceof ListTag) {
            for (final Tag pattern : (ListTag)patternsTag) {
                if (!(pattern instanceof CompoundTag)) {
                    continue;
                }
                final IntTag c = (IntTag)((CompoundTag)pattern).get("Color");
                c.setValue(15 - c.getValue());
            }
        }
        return tag;
    }
}
