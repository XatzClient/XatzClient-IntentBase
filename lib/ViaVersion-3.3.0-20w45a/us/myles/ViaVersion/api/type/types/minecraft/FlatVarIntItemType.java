// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.minecraft;

import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.minecraft.item.Item;
import io.netty.buffer.ByteBuf;

public class FlatVarIntItemType extends BaseItemType
{
    public FlatVarIntItemType() {
        super("FlatVarIntItem");
    }
    
    @Override
    public Item read(final ByteBuf buffer) throws Exception {
        final boolean present = buffer.readBoolean();
        if (!present) {
            return null;
        }
        final Item item = new Item();
        item.setIdentifier(Type.VAR_INT.readPrimitive(buffer));
        item.setAmount(buffer.readByte());
        item.setTag(Type.NBT.read(buffer));
        return item;
    }
    
    @Override
    public void write(final ByteBuf buffer, final Item object) throws Exception {
        if (object == null) {
            buffer.writeBoolean(false);
        }
        else {
            buffer.writeBoolean(true);
            Type.VAR_INT.writePrimitive(buffer, object.getIdentifier());
            buffer.writeByte((int)object.getAmount());
            Type.NBT.write(buffer, object.getTag());
        }
    }
}
