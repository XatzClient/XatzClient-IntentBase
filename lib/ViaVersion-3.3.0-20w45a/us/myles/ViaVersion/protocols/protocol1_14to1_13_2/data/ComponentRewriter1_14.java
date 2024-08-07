// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_14to1_13_2.data;

import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.packets.InventoryPackets;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.ComponentRewriter1_13;

public class ComponentRewriter1_14 extends ComponentRewriter1_13
{
    public ComponentRewriter1_14(final Protocol protocol) {
        super(protocol);
    }
    
    @Override
    protected void handleItem(final Item item) {
        InventoryPackets.toClient(item);
    }
    
    @Override
    protected void handleTranslate(final JsonObject object, final String translate) {
    }
}
