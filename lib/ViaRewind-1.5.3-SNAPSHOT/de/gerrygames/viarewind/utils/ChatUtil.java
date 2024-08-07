// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.utils;

import us.myles.viaversion.libs.gson.JsonElement;
import java.util.logging.Level;
import de.gerrygames.viarewind.ViaRewind;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import us.myles.viaversion.libs.bungeecordchat.chat.ComponentSerializer;
import java.util.regex.Pattern;

public class ChatUtil
{
    private static final Pattern UNUSED_COLOR_PATTERN;
    
    public static String jsonToLegacy(final String json) {
        if (json == null || json.equals("null") || json.equals("")) {
            return "";
        }
        try {
            String legacy;
            for (legacy = TextComponent.toLegacyText(ComponentSerializer.parse(json)); legacy.startsWith("§f"); legacy = legacy.substring(2)) {}
            return legacy;
        }
        catch (Exception ex) {
            ViaRewind.getPlatform().getLogger().log(Level.WARNING, "Could not convert component to legacy text: " + json, ex);
            return "";
        }
    }
    
    public static String jsonToLegacy(final JsonElement component) {
        if (component.isJsonNull() || (component.isJsonArray() && component.getAsJsonArray().size() == 0) || (component.isJsonObject() && component.getAsJsonObject().size() == 0)) {
            return "";
        }
        if (component.isJsonPrimitive()) {
            return component.getAsString();
        }
        return jsonToLegacy(component.toString());
    }
    
    public static String legacyToJson(final String legacy) {
        if (legacy == null) {
            return "";
        }
        return ComponentSerializer.toString(TextComponent.fromLegacyText(legacy));
    }
    
    public static String removeUnusedColor(String legacy, char last) {
        if (legacy == null) {
            return null;
        }
        legacy = ChatUtil.UNUSED_COLOR_PATTERN.matcher(legacy).replaceAll("$1$2");
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < legacy.length(); ++i) {
            char current = legacy.charAt(i);
            if (current != '§' || i == legacy.length() - 1) {
                builder.append(current);
            }
            else {
                current = legacy.charAt(++i);
                if (current != last) {
                    builder.append('§').append(current);
                    last = current;
                }
            }
        }
        return builder.toString();
    }
    
    static {
        UNUSED_COLOR_PATTERN = Pattern.compile("(?>(?>§[0-fk-or])*(§r|\\Z))|(?>(?>§[0-f])*(§[0-f]))");
    }
}
