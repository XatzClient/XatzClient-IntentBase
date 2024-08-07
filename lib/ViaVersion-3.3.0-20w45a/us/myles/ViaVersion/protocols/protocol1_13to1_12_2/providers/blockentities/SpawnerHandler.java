// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.providers.blockentities;

import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.EntityNameRewriter;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;

public class SpawnerHandler implements BlockEntityProvider.BlockEntityHandler
{
    @Override
    public int transform(final UserConnection user, final CompoundTag tag) {
        if (tag.contains("SpawnData") && tag.get("SpawnData") instanceof CompoundTag) {
            final CompoundTag data = tag.get("SpawnData");
            if (data.contains("id") && data.get("id") instanceof StringTag) {
                final StringTag s = data.get("id");
                s.setValue(EntityNameRewriter.rewrite(s.getValue()));
            }
        }
        return -1;
    }
}
