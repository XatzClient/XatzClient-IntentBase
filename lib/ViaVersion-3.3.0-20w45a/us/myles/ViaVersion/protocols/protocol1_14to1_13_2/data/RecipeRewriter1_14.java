// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_14to1_13_2.data;

import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.RecipeRewriter1_13_2;

public class RecipeRewriter1_14 extends RecipeRewriter1_13_2
{
    public RecipeRewriter1_14(final Protocol protocol, final ItemRewriter.RewriteFunction rewriter) {
        super(protocol, rewriter);
        this.recipeHandlers.put("stonecutting", this::handleStonecutting);
        this.recipeHandlers.put("blasting", this::handleSmelting);
        this.recipeHandlers.put("smoking", this::handleSmelting);
        this.recipeHandlers.put("campfire_cooking", this::handleSmelting);
    }
    
    public void handleStonecutting(final PacketWrapper wrapper) throws Exception {
        wrapper.passthrough(Type.STRING);
        final Item[] array;
        final Item[] items = array = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
        for (final Item item : array) {
            this.rewriter.rewrite(item);
        }
        this.rewriter.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
}
