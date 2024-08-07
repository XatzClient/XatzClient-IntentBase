// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.providers.blockentities;

import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage.BlockStorage;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;

public class BedHandler implements BlockEntityProvider.BlockEntityHandler
{
    @Override
    public int transform(final UserConnection user, final CompoundTag tag) {
        final BlockStorage storage = user.get(BlockStorage.class);
        final Position position = new Position((int)this.getLong(tag.get("x")), (short)this.getLong(tag.get("y")), (int)this.getLong(tag.get("z")));
        if (!storage.contains(position)) {
            Via.getPlatform().getLogger().warning("Received an bed color update packet, but there is no bed! O_o " + tag);
            return -1;
        }
        int blockId = storage.get(position).getOriginal() - 972 + 748;
        final Tag color = tag.get("color");
        if (color != null) {
            blockId += ((Number)color.getValue()).intValue() * 16;
        }
        return blockId;
    }
    
    private long getLong(final Tag tag) {
        return (long)tag.getValue();
    }
}
