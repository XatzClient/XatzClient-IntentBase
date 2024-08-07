// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.type.Type;

public class ItemType extends Type<Item>
{
    private final boolean compressed;
    
    public ItemType(final boolean compressed) {
        super((Class)Item.class);
        this.compressed = compressed;
    }
    
    public Item read(final ByteBuf buffer) throws Exception {
        final int readerIndex = buffer.readerIndex();
        final short id = buffer.readShort();
        if (id < 0) {
            return null;
        }
        final Item item = new Item();
        item.setIdentifier((int)id);
        item.setAmount(buffer.readByte());
        item.setData(buffer.readShort());
        item.setTag((CompoundTag)(this.compressed ? Types1_7_6_10.COMPRESSED_NBT : Types1_7_6_10.NBT).read(buffer));
        return item;
    }
    
    public void write(final ByteBuf buffer, final Item item) throws Exception {
        if (item == null) {
            buffer.writeShort(-1);
        }
        else {
            buffer.writeShort(item.getIdentifier());
            buffer.writeByte((int)item.getAmount());
            buffer.writeShort((int)item.getData());
            (this.compressed ? Types1_7_6_10.COMPRESSED_NBT : Types1_7_6_10.NBT).write(buffer, (Object)item.getTag());
        }
    }
}
