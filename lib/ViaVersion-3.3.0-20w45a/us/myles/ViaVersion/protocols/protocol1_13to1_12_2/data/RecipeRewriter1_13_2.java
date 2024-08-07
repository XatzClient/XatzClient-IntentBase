// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data;

import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.RecipeRewriter;

public class RecipeRewriter1_13_2 extends RecipeRewriter
{
    public RecipeRewriter1_13_2(final Protocol protocol, final ItemRewriter.RewriteFunction rewriter) {
        super(protocol, rewriter);
        this.recipeHandlers.put("crafting_shapeless", this::handleCraftingShapeless);
        this.recipeHandlers.put("crafting_shaped", this::handleCraftingShaped);
        this.recipeHandlers.put("smelting", this::handleSmelting);
    }
    
    public void handleSmelting(final PacketWrapper wrapper) throws Exception {
        wrapper.passthrough(Type.STRING);
        final Item[] array;
        final Item[] items = array = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
        for (final Item item : array) {
            this.rewriter.rewrite(item);
        }
        this.rewriter.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
        wrapper.passthrough((Type<Object>)Type.FLOAT);
        wrapper.passthrough((Type<Object>)Type.VAR_INT);
    }
    
    public void handleCraftingShaped(final PacketWrapper wrapper) throws Exception {
        final int ingredientsNo = wrapper.passthrough((Type<Integer>)Type.VAR_INT) * wrapper.passthrough((Type<Integer>)Type.VAR_INT);
        wrapper.passthrough(Type.STRING);
        for (int j = 0; j < ingredientsNo; ++j) {
            final Item[] array;
            final Item[] items = array = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
            for (final Item item : array) {
                this.rewriter.rewrite(item);
            }
        }
        this.rewriter.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
    
    public void handleCraftingShapeless(final PacketWrapper wrapper) throws Exception {
        wrapper.passthrough(Type.STRING);
        for (int ingredientsNo = wrapper.passthrough((Type<Integer>)Type.VAR_INT), j = 0; j < ingredientsNo; ++j) {
            final Item[] array;
            final Item[] items = array = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
            for (final Item item : array) {
                this.rewriter.rewrite(item);
            }
        }
        this.rewriter.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
}
