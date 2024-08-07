// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data;

import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import us.myles.viaversion.libs.gson.JsonPrimitive;
import java.util.Iterator;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
import java.io.IOException;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonArray;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.minecraft.nbt.BinaryTagIO;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.ComponentRewriter;

public class ComponentRewriter1_13 extends ComponentRewriter
{
    public ComponentRewriter1_13(final Protocol protocol) {
        super(protocol);
    }
    
    public ComponentRewriter1_13() {
    }
    
    @Override
    protected void handleHoverEvent(final JsonObject hoverEvent) {
        super.handleHoverEvent(hoverEvent);
        final String action = hoverEvent.getAsJsonPrimitive("action").getAsString();
        if (!action.equals("show_item")) {
            return;
        }
        final JsonElement value = hoverEvent.get("value");
        if (value == null) {
            return;
        }
        final String text = this.findItemNBT(value);
        if (text == null) {
            return;
        }
        CompoundTag tag;
        try {
            tag = BinaryTagIO.readString(text);
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().warning("Error reading NBT in show_item:" + text);
            throw new RuntimeException(e);
        }
        final CompoundTag itemTag = tag.get("tag");
        final ShortTag damageTag = tag.get("Damage");
        final short damage = (short)((damageTag != null) ? ((short)damageTag.getValue()) : 0);
        final Item item = new Item();
        item.setData(damage);
        item.setTag(itemTag);
        this.handleItem(item);
        if (damage != item.getData()) {
            tag.put(new ShortTag("Damage", item.getData()));
        }
        if (itemTag != null) {
            tag.put(itemTag);
        }
        final JsonArray array = new JsonArray();
        final JsonObject object = new JsonObject();
        array.add(object);
        try {
            final String serializedNBT = BinaryTagIO.writeString(tag);
            object.addProperty("text", serializedNBT);
            hoverEvent.add("value", array);
        }
        catch (IOException e2) {
            Via.getPlatform().getLogger().warning("Error writing NBT in show_item:" + text);
            e2.printStackTrace();
        }
    }
    
    protected void handleItem(final Item item) {
        InventoryPackets.toClient(item);
    }
    
    protected String findItemNBT(final JsonElement element) {
        if (element.isJsonArray()) {
            for (final JsonElement jsonElement : element.getAsJsonArray()) {
                final String value = this.findItemNBT(jsonElement);
                if (value != null) {
                    return value;
                }
            }
        }
        else if (element.isJsonObject()) {
            final JsonPrimitive text = element.getAsJsonObject().getAsJsonPrimitive("text");
            if (text != null) {
                return text.getAsString();
            }
        }
        else if (element.isJsonPrimitive()) {
            return element.getAsJsonPrimitive().getAsString();
        }
        return null;
    }
    
    @Override
    protected void handleTranslate(final JsonObject object, final String translate) {
        super.handleTranslate(object, translate);
        String newTranslate = Protocol1_13To1_12_2.MAPPINGS.getTranslateMapping().get(translate);
        if (newTranslate == null) {
            newTranslate = Protocol1_13To1_12_2.MAPPINGS.getMojangTranslation().get(translate);
        }
        if (newTranslate != null) {
            object.addProperty("translate", newTranslate);
        }
    }
}
