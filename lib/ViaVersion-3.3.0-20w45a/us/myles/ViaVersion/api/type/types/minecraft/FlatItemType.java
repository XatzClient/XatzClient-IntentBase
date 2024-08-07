// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.minecraft;

import us.myles.ViaVersion.api.type.Type;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.item.Item;
import io.netty.buffer.ByteBuf;

public class FlatItemType extends BaseItemType
{
    public FlatItemType() {
        super("FlatItem");
    }
    
    @Override
    public Item read(final ByteBuf buffer) throws Exception {
        final short id = buffer.readShort();
        if (id < 0) {
            return null;
        }
        final Item item = new Item();
        item.setIdentifier(id);
        item.setAmount(buffer.readByte());
        item.setTag(Type.NBT.read(buffer));
        return item;
    }
    
    @Override
    public void write(final ByteBuf buffer, final Item object) throws Exception {
        if (object == null) {
            buffer.writeShort(-1);
        }
        else {
            buffer.writeShort(object.getIdentifier());
            buffer.writeByte((int)object.getAmount());
            Type.NBT.write(buffer, object.getTag());
        }
    }
}
