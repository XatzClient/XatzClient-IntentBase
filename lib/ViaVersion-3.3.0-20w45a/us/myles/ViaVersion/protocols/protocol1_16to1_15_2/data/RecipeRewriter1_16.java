// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16to1_15_2.data;

import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.data.RecipeRewriter1_14;

public class RecipeRewriter1_16 extends RecipeRewriter1_14
{
    public RecipeRewriter1_16(final Protocol protocol, final ItemRewriter.RewriteFunction rewriter) {
        super(protocol, rewriter);
        this.recipeHandlers.put("smithing", this::handleSmithing);
    }
    
    public void handleSmithing(final PacketWrapper wrapper) throws Exception {
        final Item[] array;
        final Item[] baseIngredients = array = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
        for (final Item item : array) {
            this.rewriter.rewrite(item);
        }
        final Item[] array2;
        final Item[] ingredients = array2 = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
        for (final Item item2 : array2) {
            this.rewriter.rewrite(item2);
        }
        this.rewriter.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
}
