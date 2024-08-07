// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets;

import java.util.Iterator;
import us.myles.viaversion.libs.gson.JsonPrimitive;
import us.myles.viaversion.libs.gson.JsonArray;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.viaversion.libs.gson.JsonElement;

public class ChatItemRewriter
{
    public static void toClient(final JsonElement element, final UserConnection user) {
        if (element instanceof JsonObject) {
            final JsonObject obj = (JsonObject)element;
            if (obj.has("hoverEvent")) {
                if (obj.get("hoverEvent") instanceof JsonObject) {
                    final JsonObject hoverEvent = (JsonObject)obj.get("hoverEvent");
                    if (hoverEvent.has("action") && hoverEvent.has("value")) {
                        final String type = hoverEvent.get("action").getAsString();
                        if (type.equals("show_item") || type.equals("show_entity")) {
                            final JsonElement value = hoverEvent.get("value");
                            if (value.isJsonArray()) {
                                final JsonArray newArray = new JsonArray();
                                final int index = 0;
                                for (final JsonElement valueElement : value.getAsJsonArray()) {
                                    if (valueElement.isJsonPrimitive() && valueElement.getAsJsonPrimitive().isString()) {
                                        final String newValue = index + ":" + valueElement.getAsString();
                                        newArray.add((JsonElement)new JsonPrimitive(newValue));
                                    }
                                }
                                hoverEvent.add("value", (JsonElement)newArray);
                            }
                        }
                    }
                }
            }
            else if (obj.has("extra")) {
                toClient(obj.get("extra"), user);
            }
        }
        else if (element instanceof JsonArray) {
            final JsonArray array = (JsonArray)element;
            for (final JsonElement value2 : array) {
                toClient(value2, user);
            }
        }
    }
}
