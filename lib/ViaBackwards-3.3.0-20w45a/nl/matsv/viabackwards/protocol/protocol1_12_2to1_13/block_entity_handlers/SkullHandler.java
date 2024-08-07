// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;

public class SkullHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler
{
    private static final int SKULL_START = 5447;
    
    @Override
    public CompoundTag transform(final UserConnection user, final int blockId, final CompoundTag tag) {
        final int diff = blockId - 5447;
        final int pos = diff % 20;
        final byte type = (byte)Math.floor(diff / 20.0f);
        tag.put((Tag)new ByteTag("SkullType", type));
        if (pos < 4) {
            return tag;
        }
        tag.put((Tag)new ByteTag("Rot", (byte)(pos - 4 & 0xFF)));
        return tag;
    }
}
