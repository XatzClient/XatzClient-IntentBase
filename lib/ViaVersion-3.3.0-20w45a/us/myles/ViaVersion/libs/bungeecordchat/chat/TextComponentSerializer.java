// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.chat;

import java.util.List;
import us.myles.viaversion.libs.gson.JsonSerializationContext;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.bungeecordchat.api.chat.BaseComponent;
import us.myles.viaversion.libs.gson.JsonParseException;
import us.myles.viaversion.libs.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonDeserializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import us.myles.viaversion.libs.gson.JsonSerializer;

public class TextComponentSerializer extends BaseComponentSerializer implements JsonSerializer<TextComponent>, JsonDeserializer<TextComponent>
{
    @Override
    public TextComponent deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final TextComponent component = new TextComponent();
        final JsonObject object = json.getAsJsonObject();
        if (!object.has("text")) {
            throw new JsonParseException("Could not parse JSON: missing 'text' property");
        }
        component.setText(object.get("text").getAsString());
        this.deserialize(object, component, context);
        return component;
    }
    
    @Override
    public JsonElement serialize(final TextComponent src, final Type typeOfSrc, final JsonSerializationContext context) {
        final List<BaseComponent> extra = src.getExtra();
        final JsonObject object = new JsonObject();
        object.addProperty("text", src.getText());
        if (src.hasFormatting() || (extra != null && !extra.isEmpty())) {
            this.serialize(object, src, context);
        }
        return object;
    }
}
