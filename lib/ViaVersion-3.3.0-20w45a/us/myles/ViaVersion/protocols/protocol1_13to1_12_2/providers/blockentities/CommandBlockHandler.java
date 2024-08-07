// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.providers.blockentities;

import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;

public class CommandBlockHandler implements BlockEntityProvider.BlockEntityHandler
{
    @Override
    public int transform(final UserConnection user, final CompoundTag tag) {
        final Tag name = tag.get("CustomName");
        if (name instanceof StringTag) {
            ((StringTag)name).setValue(ChatRewriter.legacyTextToJsonString(((StringTag)name).getValue()));
        }
        final Tag out = tag.get("LastOutput");
        if (out instanceof StringTag) {
            final JsonElement value = GsonUtil.getJsonParser().parse(((StringTag)out).getValue());
            ChatRewriter.processTranslate(value);
            ((StringTag)out).setValue(value.toString());
        }
        return -1;
    }
}
