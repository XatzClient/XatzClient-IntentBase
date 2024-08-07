// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2;

import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.ComponentRewriter1_13;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.bungeecordchat.chat.ComponentSerializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.BaseComponent;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import us.myles.ViaVersion.api.rewriters.ComponentRewriter;

public class ChatRewriter
{
    private static final ComponentRewriter COMPONENT_REWRITER;
    
    public static String fromLegacyTextAsString(final String message, final ChatColor defaultColor, final boolean itemData) {
        final TextComponent headComponent = new TextComponent();
        TextComponent component = new TextComponent();
        StringBuilder builder = new StringBuilder();
        if (itemData) {
            headComponent.setItalic(false);
        }
        for (int i = 0; i < message.length(); ++i) {
            char c = message.charAt(i);
            if (c == '§') {
                if (++i >= message.length()) {
                    break;
                }
                c = message.charAt(i);
                if (c >= 'A' && c <= 'Z') {
                    c += ' ';
                }
                ChatColor format = ChatColor.getByChar(c);
                if (format != null) {
                    if (builder.length() > 0) {
                        final TextComponent old = component;
                        component = new TextComponent(old);
                        old.setText(builder.toString());
                        builder = new StringBuilder();
                        headComponent.addExtra(old);
                    }
                    if (ChatColor.BOLD.equals(format)) {
                        component.setBold(true);
                    }
                    else if (ChatColor.ITALIC.equals(format)) {
                        component.setItalic(true);
                    }
                    else if (ChatColor.UNDERLINE.equals(format)) {
                        component.setUnderlined(true);
                    }
                    else if (ChatColor.STRIKETHROUGH.equals(format)) {
                        component.setStrikethrough(true);
                    }
                    else if (ChatColor.MAGIC.equals(format)) {
                        component.setObfuscated(true);
                    }
                    else if (ChatColor.RESET.equals(format)) {
                        format = defaultColor;
                        component = new TextComponent();
                        component.setColor(format);
                    }
                    else {
                        component = new TextComponent();
                        component.setColor(format);
                    }
                }
            }
            else {
                builder.append(c);
            }
        }
        component.setText(builder.toString());
        headComponent.addExtra(component);
        return ComponentSerializer.toString(headComponent);
    }
    
    public static JsonElement fromLegacyText(final String message, final ChatColor defaultColor) {
        return GsonUtil.getJsonParser().parse(fromLegacyTextAsString(message, defaultColor, false));
    }
    
    public static JsonElement legacyTextToJson(final String legacyText) {
        return fromLegacyText(legacyText, ChatColor.WHITE);
    }
    
    public static String legacyTextToJsonString(final String legacyText) {
        return fromLegacyTextAsString(legacyText, ChatColor.WHITE, false);
    }
    
    public static String jsonTextToLegacy(final String value) {
        return BaseComponent.toLegacyText(ComponentSerializer.parse(value));
    }
    
    public static void processTranslate(final JsonElement value) {
        ChatRewriter.COMPONENT_REWRITER.processText(value);
    }
    
    static {
        COMPONENT_REWRITER = new ComponentRewriter1_13();
    }
}
