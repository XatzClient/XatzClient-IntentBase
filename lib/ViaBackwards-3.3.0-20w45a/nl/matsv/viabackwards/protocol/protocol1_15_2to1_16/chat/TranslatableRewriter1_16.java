// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.chat;

import us.myles.viaversion.libs.gson.JsonPrimitive;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.gson.JsonElement;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;

public class TranslatableRewriter1_16 extends TranslatableRewriter
{
    private static final ChatColor[] COLORS;
    
    public TranslatableRewriter1_16(final BackwardsProtocol protocol) {
        super(protocol);
    }
    
    public void processText(final JsonElement value) {
        super.processText(value);
        if (!value.isJsonObject()) {
            return;
        }
        final JsonObject object = value.getAsJsonObject();
        final JsonPrimitive color = object.getAsJsonPrimitive("color");
        if (color != null) {
            final String colorName = color.getAsString();
            if (!colorName.isEmpty() && colorName.charAt(0) == '#') {
                final int rgb = Integer.parseInt(colorName.substring(1), 16);
                final String closestChatColor = this.getClosestChatColor(rgb);
                object.addProperty("color", closestChatColor);
            }
        }
    }
    
    protected void handleHoverEvent(final JsonObject hoverEvent) {
        final JsonElement contentsElement = hoverEvent.remove("contents");
        if (contentsElement == null) {
            return;
        }
        final String asString;
        final String action = asString = hoverEvent.getAsJsonPrimitive("action").getAsString();
        switch (asString) {
            case "show_text": {
                this.processText(contentsElement);
                hoverEvent.add("value", contentsElement);
                break;
            }
            case "show_item": {
                final JsonObject item = contentsElement.getAsJsonObject();
                final JsonElement count = item.remove("count");
                item.addProperty("Count", (Number)(byte)((count != null) ? count.getAsByte() : 1));
                hoverEvent.addProperty("value", TagSerializer.toString(item));
                break;
            }
            case "show_entity": {
                final JsonObject entity = contentsElement.getAsJsonObject();
                final JsonObject name = entity.getAsJsonObject("name");
                if (name != null) {
                    this.processText((JsonElement)name);
                    entity.addProperty("name", name.toString());
                }
                final JsonObject hoverObject = new JsonObject();
                hoverObject.addProperty("text", TagSerializer.toString(entity));
                hoverEvent.add("value", (JsonElement)hoverObject);
                break;
            }
        }
    }
    
    private String getClosestChatColor(final int rgb) {
        final int r = rgb >> 16 & 0xFF;
        final int g = rgb >> 8 & 0xFF;
        final int b = rgb & 0xFF;
        ChatColor closest = null;
        int smallestDiff = 0;
        for (final ChatColor color : TranslatableRewriter1_16.COLORS) {
            if (color.rgb == rgb) {
                return color.colorName;
            }
            final int rAverage = (color.r + r) / 2;
            final int rDiff = color.r - r;
            final int gDiff = color.g - g;
            final int bDiff = color.b - b;
            final int diff = (2 + (rAverage >> 8)) * rDiff * rDiff + 4 * gDiff * gDiff + (2 + (255 - rAverage >> 8)) * bDiff * bDiff;
            if (closest == null || diff < smallestDiff) {
                closest = color;
                smallestDiff = diff;
            }
        }
        return closest.colorName;
    }
    
    static {
        COLORS = new ChatColor[] { new ChatColor("black", 0), new ChatColor("dark_blue", 170), new ChatColor("dark_green", 43520), new ChatColor("dark_aqua", 43690), new ChatColor("dark_red", 11141120), new ChatColor("dark_purple", 11141290), new ChatColor("gold", 16755200), new ChatColor("gray", 11184810), new ChatColor("dark_gray", 5592405), new ChatColor("blue", 5592575), new ChatColor("green", 5635925), new ChatColor("aqua", 5636095), new ChatColor("red", 16733525), new ChatColor("light_purple", 16733695), new ChatColor("yellow", 16777045), new ChatColor("white", 16777215) };
    }
    
    private static final class ChatColor
    {
        private final String colorName;
        private final int rgb;
        private final int r;
        private final int g;
        private final int b;
        
        ChatColor(final String colorName, final int rgb) {
            this.colorName = colorName;
            this.rgb = rgb;
            this.r = (rgb >> 16 & 0xFF);
            this.g = (rgb >> 8 & 0xFF);
            this.b = (rgb & 0xFF);
        }
    }
}
