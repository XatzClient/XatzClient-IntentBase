// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.minecraft;

import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.minecraft.item.Item;
import io.netty.buffer.ByteBuf;

public class ItemArrayType extends BaseItemArrayType
{
    public ItemArrayType() {
        super("Item Array");
    }
    
    @Override
    public Item[] read(final ByteBuf buffer) throws Exception {
        final int amount = Type.SHORT.readPrimitive(buffer);
        final Item[] array = new Item[amount];
        for (int i = 0; i < amount; ++i) {
            array[i] = Type.ITEM.read(buffer);
        }
        return array;
    }
    
    @Override
    public void write(final ByteBuf buffer, final Item[] object) throws Exception {
        Type.SHORT.writePrimitive(buffer, (short)object.length);
        for (final Item o : object) {
            Type.ITEM.write(buffer, o);
        }
    }
}
