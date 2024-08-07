// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_12to1_11_1;

import us.myles.ViaVersion.api.minecraft.item.Item;

public class BedRewriter
{
    public static void toClientItem(final Item item) {
        if (item == null) {
            return;
        }
        if (item.getIdentifier() == 355 && item.getData() == 0) {
            item.setData((short)14);
        }
    }
    
    public static void toServerItem(final Item item) {
        if (item == null) {
            return;
        }
        if (item.getIdentifier() == 355 && item.getData() == 14) {
            item.setData((short)0);
        }
    }
}
