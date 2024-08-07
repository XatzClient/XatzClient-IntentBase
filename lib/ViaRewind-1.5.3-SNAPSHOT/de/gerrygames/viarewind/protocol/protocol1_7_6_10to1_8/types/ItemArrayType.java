// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.type.Type;

public class ItemArrayType extends Type<Item[]>
{
    private final boolean compressed;
    
    public ItemArrayType(final boolean compressed) {
        super((Class)Item[].class);
        this.compressed = compressed;
    }
    
    public Item[] read(final ByteBuf buffer) throws Exception {
        final int amount = Type.SHORT.read(buffer);
        final Item[] items = new Item[amount];
        for (int i = 0; i < amount; ++i) {
            items[i] = (Item)(this.compressed ? Types1_7_6_10.COMPRESSED_NBT_ITEM : Types1_7_6_10.ITEM).read(buffer);
        }
        return items;
    }
    
    public void write(final ByteBuf buffer, final Item[] items) throws Exception {
        Type.SHORT.write(buffer, Short.valueOf((short)items.length));
        for (final Item item : items) {
            (this.compressed ? Types1_7_6_10.COMPRESSED_NBT_ITEM : Types1_7_6_10.ITEM).write(buffer, (Object)item);
        }
    }
}
