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

public class SkullHandler implements BlockEntityProvider.BlockEntityHandler
{
    private static final int SKULL_WALL_START = 5447;
    private static final int SKULL_END = 5566;
    
    @Override
    public int transform(final UserConnection user, final CompoundTag tag) {
        final BlockStorage storage = user.get(BlockStorage.class);
        final Position position = new Position((int)this.getLong(tag.get("x")), (short)this.getLong(tag.get("y")), (int)this.getLong(tag.get("z")));
        if (!storage.contains(position)) {
            Via.getPlatform().getLogger().warning("Received an head update packet, but there is no head! O_o " + tag);
            return -1;
        }
        int id = storage.get(position).getOriginal();
        if (id >= 5447 && id <= 5566) {
            final Tag skullType = tag.get("SkullType");
            if (skullType != null) {
                id += ((Number)tag.get("SkullType").getValue()).intValue() * 20;
            }
            if (tag.contains("Rot")) {
                id += ((Number)tag.get("Rot").getValue()).intValue();
            }
            return id;
        }
        Via.getPlatform().getLogger().warning("Why does this block have the skull block entity? " + tag);
        return -1;
    }
    
    private long getLong(final Tag tag) {
        return (long)tag.getValue();
    }
}
